package nsu.ru.setup

import nsu.ru.exceptions.DSLException
//import nsu.ru.models.Assignment
import nsu.ru.models.Group
import nsu.ru.models.Student
import nsu.ru.models.Task

import java.text.SimpleDateFormat
import org.apache.commons.validator.routines.UrlValidator

//println "dslMethods start"

this.timeParsingPattern = "yyyy-MM-dd"

this.schemes = (String[]) ["http", "https"]
urlValidator = new UrlValidator(schemes)

def tasks(Closure closure) {
    closure.delegate = this
    closure()
}

def groups(Closure closure) {
    closure.delegate = this
    closure()
}

//def assignments(Closure closure) {
//    closure.delegate = this
//    closure()
//}

def plagiarismSettings(Closure closure) {
    closure.delegate = this
    closure()
}

def task(String name, int maxScore, String softDeadline, String hardDeadline) {
    config.tasks << new Task(
            name: name,
            maxScore: maxScore,
            softDeadline: new SimpleDateFormat(timeParsingPattern).parse(softDeadline),
            hardDeadline: new SimpleDateFormat(timeParsingPattern).parse(hardDeadline)
    )
}

def group(String name) {
    def newGroup = new Group(name: name)
    config.groups << newGroup
}

def students(Closure closure) {
    closure.delegate = this
    closure()
}

def student(String groupName, String githubNick, String fullName, String repoUrl) {
    def group = config.groups.find { it -> it.name == groupName}
    if (group == null) {
        throw new DSLException("Group does not exists")
    }
    if (!urlValidator.isValid(repoUrl)) {
        throw new DSLException("Invalid URL")
    }
    def newStudent = new Student(githubNick: githubNick, fullName: fullName, repoUrl: repoUrl)
    group.students << newStudent
    config.students << newStudent
    config.resolvedTasks.put(githubNick, [])
}

//def assignment(String taskName, List<String> studentNicks) {
//    def task = config.tasks.find { it -> it.name == taskName }
//    if (task == null) {
//        throw new DSLException("Task $taskName does not exists")
//    }
//
//    def studentsList = config.students.findAll {
//        it -> studentNicks.contains(it.githubNick)
//    }
//
//    if (studentsList.isEmpty()) {
//        throw new DSLException("These students all doesn't exist")
//    }
//    config.assignments << new Assignment(task: task, students: studentsList)
//}

def reportPath(String path) {
    config.jplagSettings.reportPath = path
}

def additionalPlagSources(String filename) {
    File file = new File(path)
    if(!file.exists() || file.isDirectory()) {
        throw new DSLException("There is no file with name $path")
    }
    addReposList = []
    for (String line : file.readLines()) {
        if (line[0] != '#') {
            addReposList.add(line)
        }
    }
    config.additionalRepositories = addReposList
}

//def additional(List<String> paths) {
//    if (config.additionalRepositories.isEmpty()) {
//        config.additionalRepositories = paths
//    } else {
//        config.additionalRepositories.addALl(0, paths)
//    }
//}

def setReviewer(String reviewerNick) {
    config.reviewers.add(reviewerNick)
}

def markAsResolved(String githubNick, String taskName) {
    if (config.students.find({it -> it.githubNick == githubNick}) == null) {
        throw new DSLException("Student $githubNick does not exist")
    }
    Task task = config.tasks.find({it -> it.name == taskName});
    if (task == null) {
        throw new DSLException("Task $taskName does not exist")
    }
    config.resolvedTasks.get(githubNick).add(task)
    //println config.resolvedTasks.get(githubNick).size()
}

def setScoreCriteria(Closure closure) {
    closure.delegate = this
    closure()
}

def excellent(double score) {
    config.excellentCriteria = score
}

def good(double score) {
    config.goodCriteria = score
}

def satisfactory(double score) {
    config.satisfactoryCriteria = score
}

def extraScore(String githubNick, Double score) {
    if (config.students.find({it -> it.githubNick == githubNick}) == null) {
        throw new DSLException("Student $githubNick does not exist")
    }
    if (config.extraScore.get(githubNick) == null) {
        config.extraScore.put(githubNick, 0)
    }
    config.extraScore.put(githubNick, config.extraScore.get(githubNick) + score)
}

def checkPlagiarism(String taskName) {
    if (config.tasks.find({it -> it.name == taskName}) == null) {
        throw new DSLException("Task $taskName does not exist")
    }
    config.plagiarismCheck.put(taskName, [])
}

def checkPlagiarism(String taskName, List<String> students) {
    if (config.tasks.find({it -> it.name == taskName}) == null) {
        throw new DSLException("Task $taskName does not exist")
    }
    def studentsList = config.students.findAll {
        it -> students.contains(it.githubNick)
    }

    if (studentsList.isEmpty()) {
        throw new DSLException("These students all doesn't exist")
    }
    config.plagiarismCheck.put(taskName, studentsList)
}

this.groups = config.groups
this.students = config.students
this.tasks = config.tasks
this.resolvedTasks = config.resolvedTasks

//println "dslMethods end"