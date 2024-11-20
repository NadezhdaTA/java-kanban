import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static java.io.File.createTempFile;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    FileBackedTaskManager manager = new FileBackedTaskManager("test");

    @Test
    public void saveTaskTest() {
        try {
            File file = createTempFile("test", null);
            Task task = new Task("Test addNewTask", TaskStatus.NEW);
            manager.addTask(task);

            assertNotNull(manager.getTasks(), "Задача найдена.");
            assertEquals(1, manager.getTasks().size(), "Верное количество задач.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void saveEpicTest() {
        try {
            File file = createTempFile("test", null);
            Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
            manager.addEpic(epic);

            Epic epic1 = new Epic("Test addNewEpic1", TaskStatus.NEW);
            manager.addEpic(epic1);

            assertNotNull(manager.getEpics(), "Эпики найдены.");
            assertEquals(2, manager.getEpics().size(), "Верное количество эпиков.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void saveSubtaskTest() {
        try {
            File file = createTempFile("test", null);
            Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
            manager.addEpic(epic);

            Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
            manager.addSubtask(subtask);

            assertNotNull(manager.getSubtasks(), "Подзадача найдена.");
            assertEquals(1, manager.getEpics().size(), "Верное количество подзадач.");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void loadFromFileTest() {
        try {
            File file = createTempFile("test", null);
            Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
            manager.addEpic(epic);

            Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
            manager.addSubtask(subtask);

            assertNotNull(manager.getEpics(), "Эпики найдены.");
            assertEquals(1, manager.getEpics().size(), "Верное количество эпиков.");

            assertNotNull(manager.getSubtasks(), "Подзадача найдена.");
            assertEquals(1, manager.getEpics().size(), "Верное количество подзадач.");

            FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(manager.getFile());

            assertNotNull(manager1.getEpics(), "Эпики найдены.");
            assertEquals(1, manager1.getEpics().size(), "Верное количество эпиков.");

            assertNotNull(manager1.getSubtasks(), "Эпики найдены.");
            assertEquals(1, manager1.getSubtasks().size(), "Верное количество эпиков.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void saveFileTest() {
        try {
            File file = createTempFile("test", null);

            assertEquals(0, manager.getTasks().size(), "Хеш-таблица пустая.");
            assertEquals(0, manager.getEpics().size(), "Хеш-таблица пустая.");
            assertEquals(0, manager.getSubtasks().size(), "Хеш-таблица пустая.");

            assertNotNull(file, "Файл существует.");

            FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(file);

            assertEquals(0, manager1.getTasks().size(), "Хеш-таблица пустая.");
            assertEquals(0, manager1.getEpics().size(), "Хеш-таблица пустая.");
            assertEquals(0, manager1.getSubtasks().size(), "Хеш-таблица пустая.");

            assertNotNull(manager1.getFile(), "Файл существует.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void subtaskAndEpicShouldBeConnected() {
        try {
        File file = createTempFile("test", null);
        Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask);

        assertNotNull(manager.getEpics(), "Эпики найдены.");
        assertEquals(1, manager.getEpics().size(), "Верное количество эпиков.");

        assertNotNull(manager.getSubtasks(), "Подзадача найдена.");
        assertEquals(1, manager.getEpics().size(), "Верное количество подзадач.");

        assertEquals(1, epic.getSubtaskIds().size(), "Эпик и подзадача связаны");

        } catch (IOException e) {
        throw new RuntimeException(e);
        }
    }
}