import nsu.ru.exceptions.DSLException
import nsu.ru.models.Config

class TestScriptLoader {
    static Config loadScript(String scriptText, String configuration=null, String additional=null)
            throws IOException, DSLException
    {
        Config config = new Config()
        Binding binding = new Binding([
                'config': config
        ])

        new GroovyShell(binding).evaluate(
                configuration == null ? "" : new File(configuration).text
                        + "\n"
                        + additional == null ? "" : new File(additional).text
                        + "\n"
                        + scriptText
        )

        return config
    }
}
