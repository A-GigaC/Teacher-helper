package nsu.ru.models

class RatedTask {
    public final Task task
    Double score = 0
    Date commitDate = new Date(0)
    Double plagiarism = -0.1

    RatedTask(Task task, Date commitDate, Double plagiarism) {
        this.task = task
        this.commitDate = commitDate
        this.plagiarism = plagiarism
    }

    RatedTask(Task task, Double score) {
        this.task = task
        this.score = score
    }

    RatedTask(Task task, Double score, Date commitDate) {
        this.task = task
        this.score = score
        this.commitDate = commitDate
    }
}

