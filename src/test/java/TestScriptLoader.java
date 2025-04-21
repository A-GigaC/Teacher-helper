import nsu.ru.exceptions.DSLException;
import nsu.ru.models.Config;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestScriptLoader {

    public static Config loadScript(String scriptText, String configuration, String additional)
            throws IOException, DSLException {
        Config config = new Config();
        Binding binding = new Binding();
        binding.setVariable("config", config);

        GroovyShell shell = new GroovyShell(binding);

        StringBuilder scriptBuilder = new StringBuilder();
        if (configuration != null) {
            scriptBuilder.append(readFile(configuration)).append("\n");
        }
        if (additional != null) {
            scriptBuilder.append(readFile(additional)).append("\n");
        }
        scriptBuilder.append(scriptText);

        shell.evaluate(scriptBuilder.toString());

        return config;
    }

    public static Config loadScript(String scriptText) throws IOException, DSLException {
        return loadScript(scriptText, null, null);
    }

    private static String readFile(String filePath) throws IOException {
        return Files.readString(Paths.get(filePath));
    }
}