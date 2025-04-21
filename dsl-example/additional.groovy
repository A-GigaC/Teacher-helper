//println "additional groovy start"

//plagiarismSettings {
    //reportPath "jplag/report"
    //additionalRepos "repositories.txt"
    //baseCode "./baseCode"
//}


//TasksAccepted["ArtemChepenkov"]["Task_1_1_1"].date_close="2024-12-24"


extraScore "A-GigaC", 123
//extraScore "A-GigaC", "Task_1_1_1", 2

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

//Plagiarism "A-GigaC","Task_1_1_1", 100.0
//Plagiarism "A-GigaC","Task_1_1_1", 50.0

//checkPlagiarism "Task_1_1_1"
//checkPlagiarism "Task_1_1_2", ["dromankin", "A-GigaC"]

//students.findAll {
//    student -> resolvedTasks.get(student.githubNick).size() == 1
//} collect {
//    student -> student.githubNick
//} each {
//    nick -> extraScore nick, 1
//}

//println "additional groovy end"