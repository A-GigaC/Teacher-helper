package nsu.ru.models

class Task {
    String name
    Double maxScore
    Date softDeadline
    Date hardDeadline
    Integer maxPlagiarism
    TaskEvaluation evaluate = (RatedTask ratedTask) -> {
        double ratedScores = maxScore
        if (ratedTask.getFirstCommitDate().after(ratedTask.task.getSoftDeadline())) {
            //ratedTask.rated.get(student).add(new ReportBuilder.RatedTask(task, task.getMaxScore(), creationDate))
            ratedScores -= 0.5
        }
        if (ratedTask.getLastCommitDate().after(ratedTask.task.getHardDeadline())) {
            //rated.get(student).add(new ReportBuilder.RatedTask(task, task.getMaxScore() - 0.5, creationDate))
            ratedScores -= 0.5
        }

        if (ratedTask.getPlagiarism() > maxPlagiarism) {
            ratedScores -= 0.1
        }

        ratedTask.setProperty("score", ratedScores)
    }
}