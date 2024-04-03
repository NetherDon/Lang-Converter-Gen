package cococompiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class CocoCompiler 
{
    private static final CommandRunner COMMANDS = new CommandRunner();

    public static void main(String[] args) throws Exception
    {
        if (args.length == 0)
        {
            System.out.println("You must enter the path to the json file with the source data");
            System.exit(1);
            return;
        }
        
        Path dataPath = Path.of(args[0]);
        if (dataPath.toFile().isDirectory())
        {
            dataPath = dataPath.resolve("args.json");
        }

        File dataFile = dataPath.toFile();
        if (!dataFile.exists())
        {
            System.out.println("File " + dataPath.toString() + " not found");
            System.exit(1);
            return;
        }

        Settings settings;
        try
        {
            settings = Settings.loadFromJson(dataFile);
        }
        catch (IOException e)
        {
            System.out.println("Failed to read file " + dataPath);
            System.out.println(e.getMessage());
            System.exit(1);
            return;
        }

        ConverterFilesBuilder filesBuilder = new ConverterFilesBuilder(settings);
        if (!filesBuilder.generateFiles())
        {
            System.exit(1);
            return;
        }
        
        if (!COMMANDS.compileProject(settings.getPackage(), settings.getJarName()))
        {
            filesBuilder.deleteTemp();
            System.exit(1);
            return;
        }
        
        if (!COMMANDS.createJar(settings.getPackage(), settings.getJarName()))
        {
            filesBuilder.deleteTemp();
            System.exit(1);
            return;
        }

        filesBuilder.deleteTemp();
    }
}
