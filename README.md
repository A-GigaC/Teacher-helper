# Dependencies
1. java 21
2. Gradle 8.8
# Deployment
1. > gradle jar
2. > java -jar ./build/libs/dsl-1.0-SNAPSHOT.jar {config.path} {additional.path}

./dsl-example folder contains config.groovy and additional.groovy, that u can use for test runs.

# DSL
Give config and additional to the program in this order.
Keep in mind: the order of dsl-methods execution directly affects the result.

## Config
There are described dsl methods used in config.
### Tasks
List semester tasks inside `tasks {}` closure.
Task has structure:
```groovy
task TaskName, maxScore, softDeadline, hardDeadline, maxPlagiarism
```
There are example of `task` using:
```groovy
task "Task_1_5_1", 4, "2024-12-10", "2024-12-24", 50
```
### Students
List students, that results u want to see, inside `students {}` closure.
Student has structure:
```groovy
student GroupName, GHNick, FullName, GHRepoLink
```
There are example of `student` using:
```groovy
student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP.git"
```
If repo has private access rights or repository does not exist, there should be thrown DSL exception.
### additionalPlagSources
There u give path to file, where listed additional resources for antiPlag check.
It can be gh-repo links or paths on ur pc.
In this file u can use comments.
```txt
https://github.com/user/repo.git 
# this is comment 
./some/local/link/
```

Usage example:
```groovy
additionalPlagSources "./dsl-example/addSources.txt"
```
### SetScoreCriteria
Set criterias for semester assessment.
Usage example:
```groovy
setScoreCriteria {
excellent 13
good 11
satisfactory 8
}
```
## Additional
There are additional dsl functionality. 
It means, that u can run ur program without using it and have report created by default parameters.
### extraScore
Set extra scores to student by GHNick.
Repeated using for one student will summed up.
Example:
```groovy
extraScore "A-GigaC", 123
```
### markAsResolved
Marks some task for some student as resolved.
U can set the date when task was approved, but if u want to not set it, there will be default value Date(0).
If task A marked as resolved for student B, there will be plagiarism equals to -10%.
Usage example without lastCommitDate:
```groovy
markAsResolved "A-GigaC", "Task_1_1_1"
```
Usage example with lastCommitDate:
```groovy
markAsResolved "A-GigaC", "Task_1_1_1", "2024-10-22"
```
### alternativeScoreStrategy
Set alternative strategy for counting scores for tasks.
U can set strategy for one task by giving its name, or set for all tasks.
Strategy is lambda function, that is implementation of method `evaluate`:
```java
@FunctionalInterface
interface TaskEvaluation {
    Double evaluate(RatedTask ratedTask);
}
```
RatedTask is groovy-class with given properties with default values:
```java
    public final Task task
    Double score = 0
    Date firstCommitDate = new Date(0)
    Date lastCommitDate = new Date(0)
    Double plagiarism = -0.1
```
`firstCommitDate` and `lastCommitDate` will be filled by real values, after jplagRunning as well as plagiarism value.

This lamda should return Double value.
```groovy
alternativeScoreStrategy {rt -> { /*some lambda*/}}
alternativeScoreStrategy "Task_1_1_1", {rt -> { /*some lambda*/}}
```