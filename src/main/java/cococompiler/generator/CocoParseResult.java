package cococompiler.generator;

import java.util.Optional;

public class CocoParseResult 
{
    private String parserText = null;
    private String scannerText = null;

    public boolean isSuccess()
    {
        return this.parserText != null && scannerText != null;
    }
    
    public Optional<String> getParserText()
    {
        return Optional.ofNullable(this.parserText);
    }

    public Optional<String> getScannerText()
    {
        return Optional.ofNullable(this.scannerText);
    }

    public void setParserText(String text)
    {
        this.parserText = text;
    }

    public void setScannerText(String text)
    {
        this.scannerText = text;
    }
}
