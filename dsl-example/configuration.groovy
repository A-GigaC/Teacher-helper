//println "conf groovy end"

tasks {
    task "Task_1_1_1", 1, "2024-09-10", "2024-09-10", 30 //"Сортировка"
    task "Task_1_1_2", 2, "2024-09-17", "2024-09-24", 8
    task "Task_1_1_3", 2, "2024-10-21", "2024-10-08", 50
    task "Task_1_2_1", 2, "2024-10-15", "2024-10-22", 50
    task "Task_1_2_2", 2, "2024-10-29", "2024-11-05", 70
    task "Task_1_3_1", 2, "2024-11-12", "2024-11-19", 50
    task "Task_1_4_1", 1, "2024-11-26", "2024-11-26", 50
    task "Task_1_5_1", 4, "2024-12-10", "2024-12-24", 50
}

students {
    student "23214", "dromankin", "Ганс Ромашкин", "https://github.com/dromankin/OOP.git"
    student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP.git"
    student "23216", "nkrainov", "Никита Сергеевич Крайнов", "https://github.com/nkrainov/OOP.git"
    student "23213", "ArtemChepenkov", "Чепеньков Артем Игоревич","https://github.com/ArtemChepenkov/OOP.git"
}

additionalPlagSources "./dsl-example/addSources.txt"

setScoreCriteria {
    excellent 13
    good 11
    satisfactory 8
}