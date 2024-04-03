package cococompiler.frames;

public enum FrameType 
{
    SCANNER("Scanner.frame"),
    PARSER("Parser.frame"),
    MAIN("Main.frame"),
    CORE("Core.frame");

    public String defaultFileName;

    private FrameType(String defaultName)
    {
        this.defaultFileName = defaultName;
    }
}
