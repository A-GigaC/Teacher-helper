package nsu.ru.pipeline;

import nsu.ru.models.RatedTask;
import nsu.ru.models.Student;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
            System.out.println(student.getFullName() + " '" + student.getGithubNick() + "' ");
            Double totalScore = 0.0;
            if (extraScore.get(student) != null) {
                totalScore += extraScore.get(student);
            }
            if (ratedTasks.get(student) == null) {
                System.err.println("There is no rated tasks for student: " + student.getGithubNick());
                continue;
            }
            for (RatedTask ratedTask : ratedTasks.get(student)) {
                totalScore += ratedTask.getScore();
                String output = ratedTask.task.getName()
                        + "  |  "
                        + format.format(ratedTask.getFirstCommitDate())
                        + "  |  "
                        + format.format(ratedTask.getLastCommitDate())
                        + " | "
                        + TimeUnit.MILLISECONDS.toDays(
                                ratedTask.getLastCommitDate().getTime() - ratedTask.getFirstCommitDate().getTime()
                            )
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
//            System.out.println("Folders : " +
//                    new HashSet(
//                            Arrays.stream(new File("./repos/" + student.getGithubNick())
//                                    .listFiles()).toList()
//                ).stream().filter(
//                        folderName -> ratedTasks.get(student).stream().findFirst(
//                                rrt -> rrt.task.getName() == folderName
//                        ).isEmpty()
//                    )
//            );
            System.out.println("Unknown folders: ");
            for (String string : new HashSet<String>(
                    Arrays.stream(new File("./repos/" + student.getGithubNick())
                            .listFiles())
                            .filter(
                                    File::isDirectory
                            )
                            .map(
                                    File::getName
                            ).filter(
                                    fileName ->
                                        fileName.charAt(0) != '.'
                                                && (ratedTasks.get(student) == null
                                                || ratedTasks.get(student).stream().map(
                                                rt -> rt.task.getName()).filter(
                                                name -> name.equals(fileName)
                                        ).toList().isEmpty())
                            ).toList()
                    )
            ) {
                System.out.println(string);
            }

            System.out.println("-------------------------------------------------------------------------------------");
        }
    }
}