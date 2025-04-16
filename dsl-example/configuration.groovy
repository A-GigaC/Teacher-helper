//println "conf groovy end"

tasks {
    task "Task_1_1_1", 2, "2025-02-11", "2025-02-18"
    task "Task_1_1_2", 3, "2025-05-21", "2025-05-29"
    task "Task_1_1_3", 2, "2024-09-25", "2024-10-02"
    task "Task_1_2_1", 2, "2024-10-10", "2024-10-20"
    task "Task_1_2_2", 2, "2024-10-30", "2024-11-09"
}

groups {
    group "23216"
    group "23214"
}

students {
    student "23214", "dromankin", "Ганс Ромашкин", "https://github.com/dromankin/OOP.git"
    student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP.git"
    student "23216", "nkrainov", "Никита Сергеевич Крайнов", "https://github.com/nkrainov/OOP.git"
}

additional ([
        "https://github.com/PavelBun/OOP",
        "./src/"
])

//assignments {
//    assignment "Task_1_1_1", ["A-GigaC", "nkrainov"]
//    assignment "Task_1_1_2", ["dromankin", "nkrainov"]
//}

setScoreCriteria {
    excellent 13
    good 11
    satisfactory 8
}

//setReviewer "BIGNIKi"
//
//println "conf groovy start"