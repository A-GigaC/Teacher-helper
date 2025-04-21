package nsu.ru;

import nsu.ru.exceptions.DSLException;
import nsu.ru.models.Config;
import nsu.ru.pipeline.Pipeline;
import nsu.ru.setup.ScriptLoader;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException, NoSuchFieldException, IllegalAccessException, DSLException, GitAPIException {
        if (args.length < 2) {
            throw new DSLException("Not enough arguments. Give path to configuration and additional.");
        }

        Config config = ScriptLoader.loadScript(args[0], args[1]);
//        Config config = ScriptLoader.loadScript(
//                "./dsl-example/configuration.groovy"
//                ,"./dsl-example/additional.groovy"
//        );
        Pipeline.runPipeline(config);
    }
}