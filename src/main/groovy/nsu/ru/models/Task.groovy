package nsu.ru.models

class Task {
    String name
    Double maxScore
    Date softDeadline
    Date hardDeadline
    Integer maxPlagiarism
    TaskEvaluation evaluate = (RatedTask ratedTask) -> {
        Double ratedScores = maxScore
        if (ratedTask.getFirstCommitDate().after(ratedTask.task.getSoftDeadline())) {
            ratedScores -= 0.5
        }
        if (ratedTask.getLastCommitDate().after(ratedTask.task.getHardDeadline())) {
            ratedScores -= 0.5
        }

        if (ratedTask.getPlagiarism() > maxPlagiarism) {
            ratedScores -= 0.1
        }

        return ratedScores
    }
}