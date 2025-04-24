package nsu.ru.setup

import nsu.ru.exceptions.DSLException
//import nsu.ru.models.Assignment
import nsu.ru.models.Group
import nsu.ru.models.RatedTask
import nsu.ru.models.Student
import nsu.ru.models.Task
import nsu.ru.models.TaskEvaluation

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

def task(String name, int maxScore, String softDeadline, String hardDeadline, int maxPlagiarism) {
    config.tasks << new Task(
            name: name,
            maxScore: maxScore,
            softDeadline: new SimpleDateFormat(timeParsingPattern).parse(softDeadline),
            hardDeadline: new SimpleDateFormat(timeParsingPattern).parse(hardDeadline),
            maxPlagiarism: maxPlagiarism
    )
}

def students(Closure closure) {
    closure.delegate = this
    closure()
}

def student(String groupName, String githubNick, String fullName, String repoUrl) {
    def group = config.groups.find { it -> it.name == groupName}
    if (group == null) {
        group = new Group(name: groupName, students: [])
        config.groups.add(group)
    }
    if (!urlValidator.isValid(repoUrl)) {
        throw new DSLException("Invalid URL")
    }
    def newStudent = new Student(githubNick: githubNick, fullName: fullName, repoUrl: repoUrl)
    group.students << newStudent
    config.students << newStudent
    config.resolvedTasks.put(githubNick, [])
}

def reportPath(String path) {
    config.jplagSettings.reportPath = path
}

def additionalPlagSources(String path) {
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

def setReviewer(String reviewerNick) {
    config.reviewers.add(reviewerNick)
}

def markAsResolved(String githubNick, String taskName) {
    Student student = config.students.find({it -> it.githubNick == githubNick})
    if (student == null) {
        throw new DSLException("Student $githubNick does not exist")
    }
    Task task = config.tasks.find({it -> it.name == taskName});
    if (task == null) {
        throw new DSLException("Task $taskName does not exist")
    }
    if (config.resolvedTasks.get(student) == null) {
        config.resolvedTasks.put(student, [])
    }
    if (config.resolvedTasks.get(student).find{it -> it.task.name == taskName} != null) {
        return
    }
    RatedTask ratedTask = new RatedTask(task, task.getMaxScore())
    config.resolvedTasks.get(student).add(ratedTask)
}

def markAsResolved(String githubNick, String taskName, String completionDate) {
    Student student = config.students.find({it -> it.githubNick == githubNick})
    if (student == null) {
        throw new DSLException("Student $githubNick does not exist")
    }
    Task task = config.tasks.find({it -> it.name == taskName});
    if (task == null) {
        throw new DSLException("Task $taskName does not exist")
    }
    if (config.resolvedTasks.get(student) == null) {
        config.resolvedTasks.put(student, [])
    }
    RatedTask foundedRatedTask = config.resolvedTasks.get(student).find{{rt -> rt.task.name == taskName}}
    if (foundedRatedTask != null) {
        foundedRatedTask.setLastCommitDate(new SimpleDateFormat(timeParsingPattern).parse(completionDate))
    }
    RatedTask ratedTask = new RatedTask(
            task,
            task.getMaxScore(),
            new Date(0),
            new SimpleDateFormat(timeParsingPattern).parse(completionDate)
    )
    config.resolvedTasks.get(student).add(ratedTask)
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
    Student student = config.students.find({it -> it.githubNick == githubNick})
    if (student == null) {
        throw new DSLException("Student $githubNick does not exist")
    }
    if (config.extraScore.get(student) == null) {
        config.extraScore.put(student, 0)
    }
    config.extraScore.put(student, config.extraScore.get(student) + score)
}

def alternativeScoreStrategy(TaskEvaluation evaluation) {
    for (Task task : config.tasks) {
        task.setEvaluate(evaluation)
    }
}

def alternativeScoreStrategy(String taskName, TaskEvaluation evaluation) {
    Task task = config.tasks.find({it -> it.name == taskName})
    if (task == null) {
        throw new DSLException("Task $taskName does not exists")
    }
    task.setProperty("evaluate", evaluation)
}

this.groups = config.groups
this.students = config.students
this.tasks = config.tasks
this.resolvedTasks = config.resolvedTasks