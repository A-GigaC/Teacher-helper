package nsu.ru.pipeline;

import de.jplag.JPlagComparison;
import de.jplag.JPlagResult;
import nsu.ru.models.Config;
import nsu.ru.models.RatedTask;
import nsu.ru.models.Student;
import nsu.ru.models.Task;
import org.eclipse.jgit.api.Git;
//import org.kohsuke.github.*;
//import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
//import java.net.URL;
import java.util.*;

import static java.lang.Double.sum;
//import static java.lang.Math.*;
import static java.lang.Math.round;

public class ReportBuilder {

    public static void show(
            List<Student> students,
            Map<Student, List<RatedTask>> ratedTasks,
            HashMap<Student, Double> extraScore
    ) {
        System.out.println("SCORES");
        System.out.println("-------------------------------------------------------------------------------------");
        for (Student student : students) {
            System.out.println(student.getGithubNick() + " : " );
            Double totalScore = 0.0;
            if (extraScore.get(student) != null) {
                totalScore += extraScore.get(student);
            }
            for (RatedTask ratedTask : ratedTasks.get(student)) {
                totalScore += ratedTask.getScore();
                String output = ratedTask.task.getName()
                        + "  |  "
                        + ratedTask.getCommitDate()
                        + "  |  "
                        + ratedTask.getScore()
//                        + jPlagResult.getAllComparisons()..maximalSimilarity();
                        + "  |  "
                        + ratedTask.getPlagiarism()
                        + " | "
                        + (
                                (ratedTask.getPlagiarism() > ratedTask.task.getMaxPlagiarism())
                                        ? "🤬"
                                        : "✅"
                );

                System.out.println(output);
            }
            System.out.println("Total score : " + totalScore);
            System.out.println("-------------------------------------------------------------------------------------");
        }
    }
//        jPlagResult.getAllComparisons().get(0).maximalSimilarity();
        //List<Submission> submissions = jPlagResult.getSubmissions().getSubmissions();
        //List<JPlagComparison> comparisons = jPlagResult.getAllComparisons();
//        for (JPlagComparison comparison : comparisons) {
//            System.out.println(comparison.firstSubmission().getName() + " _ " + comparison.secondSubmission().getName());
//        }
        //submissions.get(0).get
//        for (Student student : extraScore.keySet()) {
//            System.out.println("-------------------------------------------------------------------------------------");
//            System.out.println(student.getGithubNick() + " total score: " + totalScore.get(student));
//            for (RatedTask ratedTask : rated.get(student)) {
//                List<JPlagComparison> allComparisons = new ArrayList<>();
//                List<JPlagComparison> withFirstSubMatch = comparisons.stream().filter(it ->
//                        it.secondSubmission().getName().equals(
//                                student.getGithubNick()
//                                        + "\\" + ratedTask.task.getName())).toList();
//
//                List<JPlagComparison> withSecondSubMatch = comparisons.stream().filter(it ->
//                        it.secondSubmission().getName().equals(
//                                student.getGithubNick()
//                                        + "\\" + ratedTask.task.getName())).toList();
//
//                allComparisons.addAll(withFirstSubMatch);
//                allComparisons.addAll(withSecondSubMatch);
//                double maximalSimilarity = 0.0;
//                for (JPlagComparison comparison: allComparisons) {
//                    if (comparison.maximalSimilarity() > maximalSimilarity) {
//                        maximalSimilarity = comparison.maximalSimilarity();
//                    }
//                }
////                System.out.println(withFirstSubMatch.size() + " " + withSecondSubMatch.size() + " " +
////                        student.getGithubNick()
////                                + "\\"
////                                + ratedTask.task.getName()
////                                + " "
////                                + allComparisons.size()
////                );
//
////                double similarity = allComparisons.size() > 0
////                        ? allComparisons.get(0).maximalSimilarity()
////                        : 0.0;
////                ArrayList<String> formatedDate = Arrays.stream(ratedTask.commitDate.toString().split(" ")).toList();
////                formatedDate.remove(4);
//                //JPlagComparison comparison = comparisons.stream().filter(it -> it.firstSubmission().getName() == student.getGithubNick()
//                //                        + "/" + ratedTask.task.getName());
//                String output = ratedTask.task.getName()
//                        + "  |  "
//                        + ratedTask.commitDate
//                        + "  |  "
//                        + ratedTask.score
////                        + jPlagResult.getAllComparisons()..maximalSimilarity();
//                        + "  |  "
//                        + (maximalSimilarity == 0 ? '-' : (((double)round(maximalSimilarity * 10000)) / 10\0.0))
//                + " %";
//                //);
//                System.out.println(output);
//            }
//        }
//        System.out.println("-------------------------------------------------------------------------------------");
//    }

//    private final Map<Student, List<RatedTask>> rated;
//    private final Map<Student, Double> extraScore;
//    private final List<Task> allTasks;
//    private final Map<Student, Double> totalScore;
//    private final JPlagResult jPlagResult;
//    //private final List<String> reviewers;
//
//    public ReportBuilder(Config config, JPlagResult jPlagResult) {
//        this.rated = new HashMap<>();
//        this.extraScore = new HashMap<>();
//        this.allTasks = config.getTasks();
//        this.totalScore = new HashMap<>();
//        this.jPlagResult = jPlagResult;
//
//
//        Map<String, List<Task>> resolvedTasks = config.getResolvedTasks();
//
//        for (Student student : config.getStudents()) {
//            List<RatedTask> ratedTasks = new ArrayList<>();
//
//            List<Task> studentTasks = resolvedTasks.get(student.getGithubNick());
//
//            if (studentTasks != null) {
//                for (Task task : studentTasks) {
//                    if (task != null) {
//                        ratedTasks.add(new RatedTask(task, task.getMaxScore()));
//                    }
//                }
//            }
//            extraScore.put(
//                    student,
//                    config.getExtraScore().get(student.getGithubNick()) != null
//                        ? config.getExtraScore().get(student.getGithubNick())
//                        : 0.0
//            );
//            rated.put(student, ratedTasks);
//            totalScore.put(student, 0.0);
//        }
//        //reviewers = config.getReviewers();
//    }

//    public void rate() throws IOException, GitAPIException {
//        for (Student student : rated.keySet()) {
//            rateTasks(student);
//        }
//    }
//
//    private void rateTasks(Student student) throws IOException, GitAPIException {
//        String repoPath = "./repos/" + student.getGithubNick();
//        Git git = Git.open(new File(repoPath));
//        Double ratedScores = 0.0;
//
//        for (Task task : allTasks) {
//            if (rated.get(student).stream().map( rt -> rt.task).toList().contains(task)) {
//                continue;
//            }
//
////            String taskPath = repoPath + "/" + task.getName();
//            String taskPath = task.getName();
//
//            Iterable<RevCommit> commits = git.log().addPath(taskPath).call();
//
//            RevCommit oldestCommit = null;
//            for (RevCommit commit : commits) {
//                if (oldestCommit == null || commit.getCommitTime() < oldestCommit.getCommitTime()) {
//                    oldestCommit = commit;
//                }
//            }
//            if (oldestCommit == null) {
//                System.out.println(
//                        "Cannot find oldest commit. Maybe " + task.getName()
//                        + " does not exsist on " + student.getGithubNick() + " repo?"
//                );
//                continue;
//            }
//
//            Date creationDate = new Date(oldestCommit.getCommitTime() * 1000L);
//
//            if (creationDate.before(task.getHardDeadline())) {
//                rated.get(student).add(new RatedTask(task, task.getMaxScore(), creationDate));
//                ratedScores += task.getMaxScore();
//            } else if (creationDate.before(task.getSoftDeadline())) {
//                rated.get(student).add(new RatedTask(task, task.getMaxScore() - 0.5, creationDate));
//                ratedScores += task.getMaxScore() - 0.5;
//            } else {
//                rated.get(student).add(new RatedTask(task, task.getMaxScore() - 1, creationDate));
//                ratedScores += task.getMaxScore() - 1;
//            }
//        }
//
//        totalScore.put(student, sum(sum(totalScore.get(student), extraScore.get(student)), ratedScores));
//        git.close();

        /* old solution */
//        for (Task task : allTasks) {
//            boolean alreadyRated = rated.get(student).stream()
//                    .anyMatch(ratedTask -> ratedTask.task.equals(task));
//        }
//
//        System.out.println(student.getGithubNick());
//        GHRepository repo = new GitHubBuilder().build().getRepository(student.getGithubNick() + "/OOP");
//        for (GHPullRequest pr : repo.getPullRequests(GHIssueState.ALL)) {
//            System.out.println(pr);
////            System.out.println("Body: " + pr.getBody() + " closedBy: " + pr.getClosedBy()
////                    + " title: " + pr.getTitle() + " merB: " + pr.getMergedBy() + " merDate: " + pr.getMergedAt()
////                    + " isMerged: " + pr.isMerged() + " mergaebleState: " + pr.getMergeableState());
//
//            if (!pr.getRequestedReviewers().isEmpty()) {
//                System.out.println("REWIEVER: " + pr.getRequestedReviewers().getFirst().getLogin());
//            }
//
//        }
    }



//    public class RatedTask {
//        final Task task;
//        final Double score;
//        Date commitDate;
//        Double plagiarism;
//
//        RatedTask(Task task, Double score, Double plagiarism) {
//            this.task = task;
//            this.score = score;
//            this.commitDate = new Date(0);
//            this.plagiarism = plagiarism;
//        }
//
//        RatedTask(Task task, Double score, Date commitDate, Double plagiarism) {
//            this.task = task;
//            this.score = score;
//            this.commitDate = commitDate;
//            this.plagiarism = plagiarism;
//        }
//
//        RatedTask(Task task, Double score) {
//            this.task = task;
//            this.score = score;
//            this.commitDate = new Date(0);;
//            this.plagiarism = -0.1;
//        }
//    }
//}