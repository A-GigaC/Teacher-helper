package nsu.ru.pipeline;

import de.jplag.JPlagResult;
import nsu.ru.models.Config;
import nsu.ru.models.RatedTask;
import nsu.ru.models.Student;
import org.eclipse.jgit.api.errors.GitAPIException;
import java.io.IOException;
import java.util.*;

public class Pipeline {
    public static void runPipeline(Config config) throws IOException, GitAPIException{
        Set<String> studentsReposLocalPaths = Downloader.setupStudentsRepositories(config.getStudents().stream().map(
                student -> student.getRepoUrl()
        ).toList());

        Set<String> additionalReposLocalPaths = Downloader.downloadAdditional(config.getAdditionalRepositories());
        studentsReposLocalPaths.addAll(additionalReposLocalPaths);
        PlagiarismChecker pgCheck = new PlagiarismChecker(studentsReposLocalPaths);
        JPlagResult result = pgCheck.setupJPlag();

        Map<Student, List<RatedTask>> rated = Evaluator.rate(config, result);
        ReportBuilder.show(config.getStudents(), rated, config.getExtraScore());
        System.out.println("pipeline");
    }
}
