package nsu.ru.models

class Config {
    List<Task> tasks = []
    List<Group> groups = []
    List<Student> students = []
    //List<Assignment> assignments = []
    JPlagSettings jplagSettings = new JPlagSettings()
    HashMap<Student, Double> extraScore = new HashMap<>()
    Integer excellentCriteria = null
    Integer goodCriteria = null
    Integer satisfactoryCriteria = null
    //Map<String, List<Student>> plagiarismCheck = new HashMap<>()
    List<String> additionalRepositories = []
    Map<Student, List<RatedTask>> resolvedTasks = new HashMap<>()
    List<String> reviewers = []
}
