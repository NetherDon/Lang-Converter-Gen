package cococompiler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import cococompiler.frames.FramePath;
import cococompiler.generator.Coco;
import cococompiler.generator.CocoParseResult;

public class ConverterFilesBuilder 
{
    private final Settings settings;

    public ConverterFilesBuilder(Settings settings)
    {
        this.settings = settings;
    }

    public boolean generateFiles()
    {
        Map<FramePath, String> texts = new HashMap<>();

        FramePath scannerPath = this.settings.getFrames().getScanner();
        FramePath parserPath = this.settings.getFrames().getParser();
        Optional<String> sourceName = this.settings.getGrammarPath();
        if (!sourceName.isPresent())
        {
            System.out.println("The path to the grammar file is not specified");
            return false;
        }

        CocoParseResult cocoResult = Coco.generateParser(
            sourceName.get(), 
            settings.getPackage(), 
            null,
            null,
            scannerPath,
            parserPath
        );
        if (!cocoResult.isSuccess())
        {
            return false;
        }

        texts.put(scannerPath, cocoResult.getScannerText().get());
        texts.put(parserPath, cocoResult.getParserText().get());

        FramePath mainFrame = this.settings.getFrames().getMain();
        Optional<String> mainText = readFrame(mainFrame);
        if (!mainText.isPresent())
        {
            System.out.println("Failed to read frame " + mainFrame.getPath());
            return false;
        }

        Optional<String> writersClass = this.settings.getWritersClassName();
        if (!writersClass.isPresent())
        {
            System.out.println("Unknown writers class");
            return false;
        }
        
        texts.put(mainFrame, addPackage(mainText.get().replace("-----> writers", writersClass.get())));

        FramePath coreFrame = this.settings.getFrames().getCore();
        Optional<String> coreText = readFrame(coreFrame);
        if (!mainText.isPresent())
        {
            System.out.println("Failed to read frame " + coreFrame.getPath());
            return false;
        }

        texts.put(coreFrame, addPackage(coreText.get()));

        for (FramePath frame : this.settings.getFrames().getAdditional())
        {
            Optional<String> text = readFrame(frame);
            if (!mainText.isPresent())
            {
                System.out.println("Failed to read frame " + frame.getPath());
                return false;
            }

            texts.put(frame, addPackage(text.get()));
        }

        Path temp = this.getTempDir();
        File tempFile = temp.toFile();
        deleteFile(tempFile);
        try
        {
            Files.createDirectories(temp);
        }
        catch (IOException e) {}
        
        for (Entry<FramePath, String> entry : texts.entrySet())
        {
            File file = temp.resolve(entry.getKey().getFileName() + ".java").toFile();

            try 
            {
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                String text = entry.getValue().replace("-----> package", this.settings.getPackage());
                out.write(text.getBytes());
                out.close();
            } 
            catch (IOException e) 
            {
                return false;
            }
        }

        return true;
    }

    public void deleteTemp()
    {
        if (!this.settings.isSaveTempFiles())
        {
            Path temp = this.getTempDir();
            deleteFile(temp.toFile());

            File parent = temp.getParent().toFile();
            if (parent.listFiles().length == 0)
            {
                parent.delete();
            }
        }
    }

    private Path getTempDir()
    {
        return Path.of("./temp/" + settings.getJarName());
    }

    private Optional<String> readFrame(FramePath path)
    {
        try(InputStream stream = path.createInputStream())
        {
            String text = new String(stream.readAllBytes());
            return Optional.of(text);
        } 
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    private String addPackage(String text)
    {
        return String.format("package %s;\n\n%s", this.settings.getPackage(), text);
    }

    private static void deleteFile(File file)
    {
        File[] list = file.listFiles();
        if (list != null) 
        {
            for (File temp : list) 
            {
                deleteFile(temp);
            }
        }

        file.delete();
    }
}
