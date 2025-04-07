package nsu.ru.pipeline;

import de.jplag.JPlagResult;
import nsu.ru.models.Config;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.Set;

public class Pipeline {
    Config config;

    public Pipeline(Config config) {
        this.config = config;
    }

    public void startPipeline() throws IOException, GitAPIException, NoSuchFieldException, IllegalAccessException {
        Set<String> studentsReposLocalPaths = Downloader.setupStudentsRepositories(config.getStudents().stream().map(
                student -> student.getRepoUrl()
        ).toList());

        Set<String> additionalReposLocalPaths = Downloader.downloadAdditional(config.getAdditionalRepositories());
        studentsReposLocalPaths.addAll(additionalReposLocalPaths);
        PlagiarismChecker pgCheck = new PlagiarismChecker(studentsReposLocalPaths);
        JPlagResult result = pgCheck.setupJPlag();

        ReportBuilder reportBuilder = new ReportBuilder(config, result);
        reportBuilder.rate();
        reportBuilder.show();
    }
}
