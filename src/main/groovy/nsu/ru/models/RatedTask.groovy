package nsu.ru.models

class RatedTask {
    public final Task task
    Double score = 0
    Date firstCommitDate = new Date(0)
    Date lastCommitDate = new Date(0)
    Double plagiarism = -0.1

    RatedTask(Task task, Date firstCommitDate, Date lastCommitDate, Double plagiarism) {
        this.task = task
        this.plagiarism = plagiarism
        this.firstCommitDate = firstCommitDate
        this.lastCommitDate = lastCommitDate
    }

    RatedTask(Task task, Double score) {
        this.task = task
        this.score = score
    }

    RatedTask(Task task, Double score, Date firstCommitDate, Date lastCommitDate) {
        this.task = task
        this.score = score
        this.firstCommitDate = firstCommitDate
        this.lastCommitDate = lastCommitDate
    }
}

