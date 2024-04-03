package cococompiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import cococompiler.frames.FramePath;
import cococompiler.frames.FrameType;

public class Settings 
{
    private final RawSettings raw;
    private final Path relativePath;
    private final Frames frames;
    private Optional<String> grammarPath = null;
    private Optional<String> writersClassName = null;

    private Settings(RawSettings raw, Path relativePath)
    {
        this.raw = raw;
        this.relativePath = relativePath;
        this.frames = new Frames(this);
    }

    public boolean isSaveTempFiles() { return this.raw.saveTempFiles; }
    public String getPackage() { return this.raw.packageName; }
    public String getJarName() { return this.raw.jarFileName; }
    public Frames getFrames() { return this.frames; }
    public Optional<String> getGrammarPath() { return this.grammarPath; }
    public Optional<String> getWritersClassName() { return this.writersClassName; }

    private void init()
    {
        if (this.raw.grammarFileName == null)
        {
            this.grammarPath = Optional.empty();
        }
        else
        {
            this.grammarPath = Optional.of(this.relativePath.resolve(this.raw.grammarFileName).toString());
        }

        this.writersClassName = Optional.ofNullable(this.raw.writersClassName);
        
        this.frames.init();
    }

    public static Settings loadFromJson(File file) throws IOException
    {
        FileInputStream fileStream = new FileInputStream(file);
        ObjectMapper mapper = new ObjectMapper();
        RawSettings rawSettings = mapper.readValue(fileStream, RawSettings.class);
        Settings settings = new Settings(rawSettings, Path.of(file.getParent()));
        settings.init();
        return settings;
    }

    public static final class Frames
    {
        private Settings settings;

        private FramePath scannerFrame = null;
        private FramePath parserFrame = null;
        private FramePath mainFrame = null;
        private FramePath coreFrame = null;
        private FramePath[] additionalFrames = null;

        private Frames(Settings settings) { this.settings = settings; }
        
        public FramePath getScanner() { return this.scannerFrame; }
        public FramePath getParser() { return this.parserFrame; }
        public FramePath getMain() { return this.mainFrame; }
        public FramePath getCore() { return this.coreFrame; }
        public FramePath[] getAdditional() { return this.additionalFrames; }

        private RawFrames raw() { return this.settings.raw.frames; }
        private String resolve(String str) { return this.settings.relativePath.resolve(str).toString(); }

        private void init()
        {
            this.scannerFrame = createPath(this.raw().scannerPath, FrameType.SCANNER.defaultFileName);
            this.parserFrame = createPath(this.raw().parserPath, FrameType.PARSER.defaultFileName);
            this.mainFrame = createPath(this.raw().mainPath, FrameType.MAIN.defaultFileName);
            this.coreFrame = createPath(this.raw().mainPath, FrameType.CORE.defaultFileName);
            this.additionalFrames = Arrays.stream(this.raw().additional)
                .map(this::resolve)
                .map(FramePath::of)
                .toArray((i) -> new FramePath[i]);
        }

        private FramePath createPath(String path, String defaultFileName)
        {
            if (path != null)
            {
                return FramePath.of(this.resolve(path));
            }
            
            if (this.raw().directory != null)
            {
                return FramePath.of(this.resolve(this.raw().directory), defaultFileName);
            }

            return FramePath.resource(defaultFileName);
        }
    }
}

class RawSettings
{
    @JsonProperty("saveTempFiles")
    public boolean saveTempFiles = false;
    @JsonProperty("package")
    public String packageName = "project";
    @JsonProperty("jarName")
    public String jarFileName = "project";
    @JsonProperty(value = "grammar", required = true)
    public String grammarFileName = null;
    @JsonProperty("writers")
    public String writersClassName = null;
    @JsonProperty("frames")
    public RawFrames frames = new RawFrames(); 
}

class RawFrames
{
    @JsonProperty("directory")
    public String directory = null;
    @JsonProperty("scanner")
    public String scannerPath = null;
    @JsonProperty("parser")
    public String parserPath = null;
    @JsonProperty("main")
    public String mainPath = null;
    @JsonProperty("core")
    public String corePath = null;
    @JsonProperty("other")
    public String[] additional = new String[0];
}
