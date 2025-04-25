package nsu.ru.pipeline;

import nsu.ru.models.RatedTask;
import nsu.ru.models.Student;

import java.text.SimpleDateFormat;
import java.util.*;

public class ReportBuilder {

    public static void show(
            List<Student> students,
            Map<Student, List<RatedTask>> ratedTasks,
            HashMap<Student, Double> extraScore
    ) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("SCORES");
        System.out.println("-------------------------------------------------------------------------------------");
        for (Student student : students) {
            System.out.println(student.getGithubNick() + " : ");
            Double totalScore = 0.0;
            if (extraScore.get(student) != null) {
                totalScore += extraScore.get(student);
            }
            for (RatedTask ratedTask : ratedTasks.get(student)) {
                totalScore += ratedTask.getScore();
                String output = ratedTask.task.getName()
                        + "  |  "
                        + format.format(ratedTask.getFirstCommitDate())
                        + "  |  "
                        + format.format(ratedTask.getLastCommitDate())
                        + " | "
                        + ratedTask.getScore()
                        + "  |  "
                        + ratedTask.getPlagiarism()
                        + " | "
                        + (
                        (ratedTask.getPlagiarism() > ratedTask.task.getMaxPlagiarism())
                                ? "-"
                                : "+"
                );

                System.out.println(output);
            }
            System.out.println("Total score : " + totalScore);
            System.out.println("-------------------------------------------------------------------------------------");
        }
    }
}