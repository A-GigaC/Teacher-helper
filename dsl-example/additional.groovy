extraScore "A-GigaC", 123

markAsResolved "A-GigaC", "Task_1_1_1"
markAsResolved "A-GigaC", "Task_1_1_1", "2024-10-22"

alternativeScoreStrategy {rt -> {
    Double ratedScores = rt.task.getMaxScore()
    if (rt.getFirstCommitDate().after(rt.task.getSoftDeadline())) {
        ratedScores -= 0.5
    }
    if (rt.getLastCommitDate().after(rt.task.getHardDeadline())) {
        ratedScores -= 0.5
    }

    if (rt.getPlagiarism() > 25) {
        ratedScores -= 0.1111
    }

    return ratedScores
}}

alternativeScoreStrategy "Task_1_1_1", {rt -> {
    Double totalScore = 0.0
    if (rt.plagiarism < 10) {
        totalScore = 0.1
    } else {
        totalScore = 19.0
    }

    return totalScore
}}