extraScore "A-GigaC", 123

markAsResolved "A-GigaC", "Task_1_1_1"
markAsResolved "A-GigaC", "Task_1_1_1", "2024-10-22"

alternativeScoreStrategy "Task_1_1_1", {rt -> {
    if (rt.plagiarism < 10) {
        rt.setScore(0.99)
    } else {
        rt.setScore(2.1)
    }
    println "Hi from altScStr for T111"
}}