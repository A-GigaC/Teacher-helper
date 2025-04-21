package nsu.ru.pipeline;

import de.jplag.JPlagComparison;
import de.jplag.JPlagResult;
import nsu.ru.exceptions.DSLException;
import nsu.ru.models.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import nsu.ru.models.Student;
import nsu.ru.models.Task;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;
//import org.kohsuke.github.*;
//import org.eclipse.jgit.api.Git;

//import java.net.URL;

//import static java.lang.Math.*;
import static java.lang.Math.round;

public class Evaluator {
    public static Map<Student, List<RatedTask>> rate(Config config, JPlagResult jPlagResult) throws IOException, GitAPIException {
        Map<Student, List<RatedTask>> rated = new HashMap<>();
        for (Student student : config.getStudents()) {
            rated.put(student, config.getResolvedTasks().get(student));

            String repoPath = "./repos/" + student.getGithubNick();
            Git git = Git.open(new File(repoPath));

            if (rated.get(student) == null) {
                rated.put(student, new ArrayList());
            }

            for (Task task : config.getTasks()) {
                if (!rated.get(student).stream().filter(
                        rt -> rt.task == task
                ).toList().isEmpty()) {
                    continue;
                }
                long creationDateAsLong = getCreationDate(task, git);
                if (creationDateAsLong == -1) {
                    continue;
                }

                double maximalSimmilarity = getPlagiarism(task, student, jPlagResult);
                maximalSimmilarity = (((double)round(maximalSimmilarity * 10000)) / 100.0);

                RatedTask ratedTask = new RatedTask(task, new Date(creationDateAsLong), maximalSimmilarity);
                rated.get(student).add(ratedTask);
            }
            //rated.put(student, config.getResolvedTasks().get(student));
            git.close();

            for (RatedTask ratedTask : rated.get(student)) {
                ratedTask.task.getEvaluate().evaluate(ratedTask);
            }
        }

        return rated;
    }

    private static long getCreationDate(Task task, Git git) throws GitAPIException {
        String taskPath = task.getName();

        Iterable<RevCommit> commits = git.log().addPath(taskPath).call();

        RevCommit oldestCommit = null;
        for (RevCommit commit : commits) {
            if (oldestCommit == null || commit.getCommitTime() < oldestCommit.getCommitTime()) {
                oldestCommit = commit;
            }
        }
        if (oldestCommit == null) {
            System.out.println(
                    "Cannot find oldest commit. Maybe " + task.getName()
                            + " does not exsist on " + git.getRepository().getIdentifier() + " repo?"
            );
            return -1;
        }

        Date creationDate = new Date(oldestCommit.getCommitTime() * 1000L);
        return creationDate.getTime();
    }

    private static double getPlagiarism(Task task, Student student, JPlagResult jplagResult) {
        List<JPlagComparison> comparisons = jplagResult.getAllComparisons();
        List<JPlagComparison> allComparisons = new ArrayList<>();
        List<JPlagComparison> withFirstSubMatch = comparisons.stream().filter(it ->
                it.secondSubmission().getName().equals(
                        student.getGithubNick()
                                + "\\" + task.getName())).toList();

        List<JPlagComparison> withSecondSubMatch = comparisons.stream().filter(it ->
                it.secondSubmission().getName().equals(
                        student.getGithubNick()
                                + "\\" + task.getName())).toList();

        allComparisons.addAll(withFirstSubMatch);
        allComparisons.addAll(withSecondSubMatch);
        double maximalSimilarity = 0.0;
        for (JPlagComparison comparison: allComparisons) {
            if (comparison.maximalSimilarity() > maximalSimilarity) {
                maximalSimilarity = comparison.maximalSimilarity();
            }
        }

        return maximalSimilarity;
    }
}