package nsu.ru.models

class Config {
    List<Task> tasks = []
    List<Group> groups = []
    List<Student> students = []
    //List<Assignment> assignments = []
    JPlagSettings jplagSettings = new JPlagSettings()
    HashMap<String, Double> extraScore = new HashMap<>()
    Integer excellentCriteria = null
    Integer goodCriteria = null
    Integer satisfactoryCriteria = null
    List<String> studentsToCheckPlagiarism = []
    Map<String, List<Student>> plagiarismCheck = new HashMap<>()
    List<String> additionalRepositories = []
    Map<String, List<Task>> resolvedTasks = new HashMap<>()
    List<String> reviewers = []
}
