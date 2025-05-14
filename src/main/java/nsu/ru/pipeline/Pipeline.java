package nsu.ru.pipeline;

import de.jplag.JPlagResult;
import nsu.ru.output.ConsoleReport;
import nsu.ru.output.GraphicalReport;
import nsu.ru.models.Config;
import nsu.ru.models.RatedTask;
import nsu.ru.models.Student;
import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.IOException;
import java.util.*;

public class Pipeline {
    public static void runPipeline(Config config, boolean useConsole) throws IOException, GitAPIException{
        GraphicalReport graphicalReport = new GraphicalReport();
        graphicalReport.setStatus("setup students repos");
        Set<String> studentsReposLocalPaths = Downloader.setupStudentsRepositories(config.getStudents().stream().map(
                student -> student.getRepoUrl()
        ).toList());
        graphicalReport.setStatus("download additional");
        Set<String> additionalReposLocalPaths = Downloader.downloadAdditional(config.getAdditionalRepositories());
        studentsReposLocalPaths.addAll(additionalReposLocalPaths);
        graphicalReport.setStatus("check plagiarism");
        PlagiarismChecker pgCheck = new PlagiarismChecker(studentsReposLocalPaths);
        JPlagResult result = pgCheck.setupJPlag();
        graphicalReport.setStatus("evaluate students works");
        Map<Student, List<RatedTask>> rated = Evaluator.rate(config, result);
        ReportBuilder rb = new ReportBuilder();

        if (useConsole) {
            ConsoleReport.showReport(config.getStudents(), rated, config.getExtraScore());
        }

        graphicalReport.showReport(
                rb.formDataForGraphicalReport(
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
