package nsu.ru;

import nsu.ru.exceptions.DSLException;
import nsu.ru.models.Config;
import nsu.ru.pipeline.Pipeline;
import nsu.ru.setup.ScriptLoader;
import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.IOException;
import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;


class Main {
    private static class Args {
        @Parameter(names = {"-configuration", "-c"}, description = "Path to configuration script", arity = 1)
        private String configPath = "./dsl-example/configuration.groovy";

        @Parameter(names = {"-additional", "-a"}, description = "Path to additional script", arity = 1)
        private String additionalPath = "./dsl-example/additional.groovy";

        @Parameter(names = {"-showConsole", "-sc"}, description = "Use console output or not", arity = 1)
        private boolean showConsole = false;
    }

    public static void main(String[] args) throws IOException, DSLException, GitAPIException {
        Args arguments = new Args();
        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        System.out.println("Using configuration: " + arguments.configPath);
        System.out.println("Using additional: " + arguments.additionalPath);

        Config config = ScriptLoader.loadScript(arguments.configPath, arguments.additionalPath);
        Pipeline.runPipeline(config, arguments.showConsole);
    }
}