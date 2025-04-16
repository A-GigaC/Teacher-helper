//println "additional groovy start"

plagiarismSettings {
    reportPath "jplag/report"
    //additionalRepos "repositories.txt"
    //baseCode "./baseCode"
}

extraScore "A-GigaC", 123
markAsResolved "A-GigaC", "Task_1_1_1"

checkPlagiarism "Task_1_1_1"
checkPlagiarism "Task_1_1_2", ["dromankin", "A-GigaC"]

students.findAll {
    student -> resolvedTasks.get(student.githubNick).size() == 1
} collect {
    student -> student.githubNick
} each {
    nick -> extraScore nick, 1
}

//println "additional groovy end"