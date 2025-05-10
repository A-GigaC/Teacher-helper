package nsu.ru.pipeline;

import nsu.ru.models.RatedTask;
import nsu.ru.models.Student;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ReportBuilder {

    public List<StudentResults> show(
            List<Student> students,
            Map<Student, List<RatedTask>> ratedTasks,
            HashMap<Student, Double> extraScore,
            Integer excellentCriteria,
            Integer goodCriteria,
            Integer satisfactoryCriteria
    ) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        List studentsResults = new ArrayList();
        for (Student student : students) {
            String studentLabel = student.getFullName()
                    + " '"
                    + student.getGithubNick()
                    + "' "
                    + student.getGroupName();
            Double totalScore = 0.0;
            if (extraScore.get(student) != null) {
                totalScore += extraScore.get(student);
            }
            if (ratedTasks.get(student) == null) {
                System.err.println("There is no rated tasks for this student");
                continue;
            }

            List<List<Object>> data = new ArrayList();
            for (RatedTask ratedTask : ratedTasks.get(student)) {
                totalScore += ratedTask.getScore();
                List<Object> taskData = new ArrayList();
                taskData.add(ratedTask.task.getName());
                taskData.add(format.format(ratedTask.getFirstCommitDate()));
                taskData.add(format.format(ratedTask.getLastCommitDate()));
                taskData.add(
                        TimeUnit.MILLISECONDS.toDays(
                        ratedTask.getLastCommitDate().getTime() - ratedTask.getFirstCommitDate().getTime()
                ));
                taskData.add(ratedTask.getScore());
                taskData.add(ratedTask.getPlagiarism());
                taskData.add(
                        (ratedTask.getPlagiarism() > ratedTask.task.getMaxPlagiarism())
                        ? "-"
                        : "+"
                );

                data.add(taskData);
            }

            List<String> unknownFolders = new ArrayList();
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
                unknownFolders.add(string);
            }
            List<Object[]> arrayedData = new ArrayList<>();
            for (List<Object> line : data) {
                arrayedData.add(line.toArray(new Object[7]));
            }
            String courseGrade = "VeryVeryBad";
            if (totalScore >= excellentCriteria) {
                courseGrade = "Excellent";
            } else if (totalScore >= goodCriteria) {
                courseGrade = "Good";
            } else if (totalScore >= satisfactoryCriteria) {
                courseGrade = "Satisfactory";
            }

            studentsResults.add(
                    new StudentResults(
                        studentLabel,
                        arrayedData.toArray(new Object[arrayedData.size()][]),
                        totalScore,
                        unknownFolders.toString(),
                        courseGrade
                )
            );
        }

        return studentsResults;
    }

    public class StudentResults {
        public String studentLabel;
        public Object[][] data;
        public Double totalScore;
        public String unknownFolders;
        public String courseGrade;

        public StudentResults(
                String studentLabel,
                Object[][] data,
                double totalScore,
                String unknownFolders,
                String courseGrade
        ) {
            this.studentLabel = studentLabel;
            this.data = data;
            this.totalScore = totalScore;
            this.unknownFolders = unknownFolders;
            this.courseGrade = courseGrade;
        }
    }
}