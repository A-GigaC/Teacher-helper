package nsu.ru.models

import nsu.ru.pipeline.ReportBuilder
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.revwalk.RevCommit;

class Task {
    String name
    Double maxScore
    Date softDeadline
    Date hardDeadline
    Integer maxPlagiarism
    TaskEvaluation evaluate = (RatedTask ratedTask) -> {
        double ratedScores = 0
        if (ratedTask.getCommitDate().before(ratedTask.task.getHardDeadline())) {
            //ratedTask.rated.get(student).add(new ReportBuilder.RatedTask(task, task.getMaxScore(), creationDate))
            ratedScores += maxScore
        } else if (ratedTask.getCommitDate().before(ratedTask.task.getSoftDeadline())) {
            //rated.get(student).add(new ReportBuilder.RatedTask(task, task.getMaxScore() - 0.5, creationDate))
            ratedScores += maxScore - 0.5
        } else {
            //rated.get(student).add(new ReportBuilder.RatedTask(task, task.getMaxScore() - 1, creationDate))
            ratedScores += maxScore - 1
        }
        if (ratedTask.getPlagiarism() > maxPlagiarism) {
            ratedScores -= 0.123
        }

        ratedTask.setProperty("score", ratedScores)
    }
}