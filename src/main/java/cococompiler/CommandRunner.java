package cococompiler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class CommandRunner
{
    public boolean compileProject(String packageName, String jarName) throws IOException
    {
        String command = String.format(
            "javac -d ./temp/%s/compiled ./temp/%s/*.java -cp %s.Main",
            jarName,
            jarName,
            packageName
        );

        ProcessBuilder builder = new ProcessBuilder(command.split(" "));
        return run(builder, false);
    }

    public boolean createJar(String packageName, String jarFileName) throws IOException
    {
        File parsersDir = new File("parsers");
        if (!parsersDir.exists())
        {
            Files.createDirectory(parsersDir.toPath());
        }

        String compiled = String.format("./temp/%s/compiled", jarFileName);
        String outFile = String.format("../../../parsers/%s.jar", jarFileName);
        String mainClass = String.format("%s.Main", packageName);

        ProcessBuilder builder = new ProcessBuilder("jar", "cfe", outFile, mainClass, "./" + packageName);
        builder.directory(new File(compiled));
        return run(builder, false);
    }

    private boolean run(ProcessBuilder processBuilder, boolean throwException) throws IOException
    {
        System.out.println("[\u001B[33mRUN COMMAND\u001B[0m] " + String.join(" ", processBuilder.command()));

        Process process = processBuilder.start();
        while (process.isAlive()) {}

        int exitValue = process.exitValue();
        String exitStr = "[\u001B[36mEXIT VALUE\u001B[0m] " + exitValue;
        System.out.println(exitStr);

        String result = process.inputReader().lines().collect(Collectors.joining("\n"));
        String errors = process.errorReader().lines().collect(Collectors.joining("\n"));

        if (!result.isEmpty())
        {
            System.out.println("[\u001B[32mCOMMAND EXECUTION RESULT\u001B[0m]");
            System.out.println(result);
        }
        
        if (!errors.isEmpty())
        {
            System.out.println("[\u001B[31mCOMMAND EXECUTION ERRORS AND WARNINGS\u001B[0m]");
            System.out.println(errors);
            if (throwException)
            {
                throw new RuntimeException("Command execution error");
            }
        }
        
        return exitValue == 0;
    }    
}
