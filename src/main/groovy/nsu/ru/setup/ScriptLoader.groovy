package nsu.ru.setup

import nsu.ru.exceptions.DSLException
import nsu.ru.models.Config

class ScriptLoader {
    static Config loadScript(String configuration, String additional) throws IOException, DSLException {
        Config config = new Config();
        Binding binding = new Binding([
                'config': config
        ])

        if (!new File(configuration).exists() || ! new File(additional).exists()) {
            throw new DSLException("Not all script files given correctly.")
        }

        //println "eval"

        new GroovyShell(binding).evaluate(
                new File("./src/main/groovy/nsu/ru/setup/dslMethods.groovy").text
                        + "\n"
                        + new File(configuration).text
                        + "\n"
                        + new File(additional).text
        )

        return config
    }
}