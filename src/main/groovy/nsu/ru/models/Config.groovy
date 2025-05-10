package nsu.ru.models

class Config {
    List<Task> tasks = []
    List<Group> groups = []
    List<Student> students = []
    JPlagSettings jplagSettings = new JPlagSettings()
    HashMap<Student, Double> extraScore = new HashMap<>()
    Integer excellentCriteria = 13
    Integer goodCriteria = 11
    Integer satisfactoryCriteria = 8
    List<String> additionalRepositories = []
    Map<Student, List<RatedTask>> resolvedTasks = new HashMap<>()
    List<String> reviewers = []
}
