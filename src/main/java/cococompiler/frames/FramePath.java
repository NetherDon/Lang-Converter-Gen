package cococompiler.frames;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class FramePath 
{
    private final String path;
    private final boolean isResource;

    private FramePath(String path, boolean isResource)
    {
        this.path = path;
        this.isResource = isResource;
    }

    public String getPath()
    {
        return this.path;
    }

    public boolean isResource()
    {
        return this.isResource;
    }

    public String getFileName()
    {
        String name = new File(this.path).getName();
        int dotIndex = name.lastIndexOf('.');
        return dotIndex == -1 ? name : name.substring(0, dotIndex);
    }

    public InputStream createInputStream() throws IOException
    {
        if (this.isResource)
        {
            InputStream frameStream = FramePath.class.getClassLoader().getResourceAsStream(this.path);
            if (frameStream == null)
            {
                throw new IOException("Can't open frame file: " + this.path);
            }

            return frameStream;
        }

        Path path = Path.of(this.path);
        return new FileInputStream(path.toFile());
    } 

    public static FramePath of(String path)
    {
        return new FramePath(path, false);
    }

    public static FramePath resource(String name)
    {
        return new FramePath(name, true);
    }

    public static FramePath of(String directory, String name)
    {
        return new FramePath(Path.of(directory).resolve(name).toString(), false);
    }
}
