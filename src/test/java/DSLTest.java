import nsu.ru.exceptions.DSLException;
import nsu.ru.models.Config;
import nsu.ru.models.Task;
import nsu.ru.models.Group;
import nsu.ru.models.Student;
import nsu.ru.models.Assignment;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DSLTest {

    @Test
    public void testTaskCreation() throws IOException, DSLException {
        String script = """
            tasks {
                task "Task_1_1_1", 2, "2025-02-11", "2025-02-18"
            }
        """;
        Config config = TestScriptLoader.loadScript(script);
        Task task = config.getTasks().stream().filter(t -> t.getName().equals("Task_1_1_1")).findFirst().orElse(null);
        assertNotNull(task);
        assertEquals(2, task.getMaxScore());
    }

    @Test
    public void testGroupCreation() throws IOException, DSLException {
        String script = """
            groups {
                group "23216"
            }
        """;
        Config config = TestScriptLoader.loadScript(script);
        Group group = config.getGroups().stream().filter(g -> g.getName().equals("23216")).findFirst().orElse(null);
        assertNotNull(group);
    }

    @Test
    public void testStudentCreation() throws IOException, DSLException {
        String script = """
            groups {
                group "23216"
            }
            students {
                student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP"
            }
        """;
        Config config = TestScriptLoader.loadScript(script);
        Student student = config.getStudents().stream().filter(s -> s.getGithubNick().equals("A-GigaC")).findFirst().orElse(null);
        assertNotNull(student);
        assertEquals("Пивоваров Фёдор Евгеньевич", student.getFullName());
    }

    @Test
    public void testAssignmentCreation() throws IOException, DSLException {
        String script = """
            tasks {
                task "Task_1_1_1", 2, "2025-02-11", "2025-02-18"
            }
            groups {
                group "23216"
            }
            students {
                student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP"
            }
            assignments {
                assignment "Task_1_1_1", ["A-GigaC"]
            }
        """;
        Config config = TestScriptLoader.loadScript(script);
        Assignment assignment = config.getAssignments().stream().filter(
                a -> a.getTask().getName().equals("Task_1_1_1")
            ).findFirst().orElse(null);
        assertNotNull(assignment);
        assertEquals(1, assignment.getStudents().size());
    }

    @Test(expected = DSLException.class)
    public void testAssignmentWithNonExistentTask() throws IOException, DSLException {
        String script = """
            groups {
                group "23216"
            }
            students {
                student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP"
            }
            assignments {
                assignment "NonExistentTask", ["A-GigaC"]
            }
        """;
        TestScriptLoader.loadScript(script);
    }

    @Test(expected = DSLException.class)
    public void testAssignmentWithNonExistentStudent() throws IOException, DSLException {
        String script = """
            tasks {
                task "Task_1_1_1", 2, "2025-02-11", "2025-02-18"
            }
            groups {
                group "23216"
            }
            assignments {
                assignment "Task_1_1_1", ["NonExistentStudent"]
            }
        """;
        TestScriptLoader.loadScript(script);
    }

    @Test
    public void testMarkAsResolved() throws IOException, DSLException {
        String script = """
            tasks {
                task "Task_1_1_1", 2, "2025-02-11", "2025-02-18"
            }
            groups {
                group "23216"
            }
            students {
                student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP"
            }
            markAsResolved "A-GigaC", "Task_1_1_1"
        """;
        Config config = TestScriptLoader.loadScript(script);
        var resolvedTasks = config.getResolvedTasks().get("A-GigaC");
        assertNotNull(resolvedTasks);
        assertTrue(resolvedTasks.contains("Task_1_1_1"));
    }

    @Test(expected = DSLException.class)
    public void testMarkAsResolvedWithNonExistentStudent() throws IOException, DSLException {
        String script = """
            tasks {
                task "Task_1_1_1", 2, "2025-02-11", "2025-02-18"
            }
            markAsResolved "NonExistentStudent", "Task_1_1_1"
        """;
        TestScriptLoader.loadScript(script);
    }

    @Test(expected = DSLException.class)
    public void testMarkAsResolvedWithNonExistentTask() throws IOException, DSLException {
        String script = """
            groups {
                group "23216"
            }
            students {
                student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP"
            }
            markAsResolved "A-GigaC", "NonExistentTask"
        """;
        TestScriptLoader.loadScript(script);
    }

    @Test
    public void testExtraScore() throws IOException, DSLException {
        String script = """
            groups {
                group "23216"
            }
            students {
                student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP"
            }
            extraScore "A-GigaC", 123
        """;
        Config config = TestScriptLoader.loadScript(script);
        var extraScore = config.getExtraScore().get("A-GigaC");
        assertNotNull(extraScore);
        assertEquals(123, extraScore.intValue());
    }

    @Test(expected = DSLException.class)
    public void testExtraScoreWithNonExistentStudent() throws IOException, DSLException {
        String script = """
            extraScore "NonExistentStudent", 123
        """;
        TestScriptLoader.loadScript(script);
    }

    @Test
    public void testCheckPlagiarism() throws IOException, DSLException {
        String script = """
            tasks {
                task "Task_1_1_1", 2, "2025-02-11", "2025-02-18"
            }
            groups {
                group "23216"
            }
            students {
                student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP"
            }
            checkPlagiarism "Task_1_1_1"
        """;
        Config config = TestScriptLoader.loadScript(script);
        var plagiarismCheck = config.getPlagiarismCheck().get("Task_1_1_1");
        assertNotNull(plagiarismCheck);
        assertTrue(plagiarismCheck.isEmpty());
    }

    @Test(expected = DSLException.class)
    public void testCheckPlagiarismWithNonExistentTask() throws IOException, DSLException {
        String script = """
            checkPlagiarism "NonExistentTask"
        """;
        TestScriptLoader.loadScript(script);
    }

    @Test
    public void testCheckPlagiarismWithSpecificStudents() throws IOException, DSLException {
        String script = """
            tasks {
                task "Task_1_1_1", 2, "2025-02-11", "2025-02-18"
            }
            groups {
                group "23216"
            }
            students {
                student "23216", "A-GigaC", "Пивоваров Фёдор Евгеньевич", "https://github.com/A-GigaC/OOP"
            }
            checkPlagiarism "Task_1_1_1", ["A-GigaC"]
        """;
        Config config = TestScriptLoader.loadScript(script);
        var plagiarismCheck = config.getPlagiarismCheck().get("Task_1_1_1");
        assertNotNull(plagiarismCheck);
        assertEquals(1, plagiarismCheck.size());
    }

    @Test(expected = DSLException.class)
    public void testCheckPlagiarismWithNonExistentStudents() throws IOException, DSLException {
        String script = """
            tasks {
                task "Task_1_1_1", 2, "2025-02-11", "2025-02-18"
            }
            checkPlagiarism "Task_1_1_1", ["NonExistentStudent"]
        """;
        TestScriptLoader.loadScript(script);
    }
}