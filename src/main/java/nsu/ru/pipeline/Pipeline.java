package nsu.ru.pipeline;

import de.jplag.JPlagResult;
import nsu.ru.gui.GUI;
import nsu.ru.models.Config;
import nsu.ru.models.RatedTask;
import nsu.ru.models.Student;
import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.IOException;
import java.util.*;

public class Pipeline {
    public static void runPipeline(Config config) throws IOException, GitAPIException{
        GUI gui = new GUI();
        gui.setStatus("setup students repos");
        Set<String> studentsReposLocalPaths = Downloader.setupStudentsRepositories(config.getStudents().stream().map(
                student -> student.getRepoUrl()
        ).toList());
        gui.setStatus("download additional");
        Set<String> additionalReposLocalPaths = Downloader.downloadAdditional(config.getAdditionalRepositories());
        studentsReposLocalPaths.addAll(additionalReposLocalPaths);
        gui.setStatus("check plagiarism");
        PlagiarismChecker pgCheck = new PlagiarismChecker(studentsReposLocalPaths);
        JPlagResult result = pgCheck.setupJPlag();
        gui.setStatus("evaluate students works");
        Map<Student, List<RatedTask>> rated = Evaluator.rate(config, result);
        ReportBuilder rb = new ReportBuilder();
        gui.showResults(
                rb.show(
                        config.getStudents(),
                        rated,
                        config.getExtraScore(),
                        config.getExcellentCriteria(),
                        config.getGoodCriteria(),
                        config.getSatisfactoryCriteria()
                )
        );
    }
}
