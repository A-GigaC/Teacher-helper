package nsu.ru.pipeline;

import de.jplag.*;
import de.jplag.exceptions.ExitException;
import de.jplag.java.JavaLanguage;
import de.jplag.options.JPlagOptions;
import de.jplag.reporting.reportobject.ReportObjectFactory;
import nsu.ru.exceptions.DSLException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class PlagiarismChecker {
    Set<String> repos;

    public PlagiarismChecker(Set<String> paths) {
        repos = paths;
    }

    public JPlagResult setupJPlag() throws FileNotFoundException {
        System.setProperty("file.encoding","UTF-8");

        Language language = new JavaLanguage();

        Set<File> reposFiles = new HashSet<>();
        for (String path : repos) {
            File file = new File(path);
            if (file.exists() && file.isDirectory()) {
                System.out.println(path);
                reposFiles.add(new File(path));
            }
        }

        JPlagOptions options = new JPlagOptions(
                language,
                reposFiles,
                Set.of()
        );

        try {
            JPlagResult result = JPlag.run(options);
            System.out.println("Forming report");
            ReportObjectFactory reportObjectFactory = new ReportObjectFactory(new File("./report.zip"));
            System.out.println("Saved to zip");
            reportObjectFactory.createAndSaveReport(result);

            return result;
//            result.getAllComparisons().forEach(jPlagComparison -> jPlagComparison.
//            result.get
//                    );
        } catch (ExitException e) {
            System.out.println("Ploho");
            throw new DSLException("Problems with JPlag");
        } //catch (FileNotFoundException e) {
            // handle IO exception here
        //}
    }
}
