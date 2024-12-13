package tests;

import managers.FileBackedTaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {
    FileBackedTaskManager manager = new FileBackedTaskManager("test");

    @Test
    public void saveTaskTest() {
        Task task = new Task("Test addNewTask", TaskStatus.NEW);
        manager.addTask(task);

        assertNotNull(manager.getTasks(), "Задача найдена.");
        assertEquals(1, manager.getTasks().size(), "Верное количество задач.");
    }

    @Test
    public void saveEpicTest() {
        Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
        manager.addEpic(epic);

        Epic epic1 = new Epic("Test addNewEpic1", TaskStatus.NEW);
        manager.addEpic(epic1);

        assertNotNull(manager.getEpics(), "Эпики найдены.");
        assertEquals(2, manager.getEpics().size(), "Верное количество эпиков.");
    }

    @Test
    public void saveSubtaskTest() {
        Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask);

        assertNotNull(manager.getSubtasks(), "Подзадача найдена.");
        assertEquals(1, manager.getEpics().size(), "Верное количество подзадач.");
    }

    @Test
    public void loadFromFileTest() {
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
    }

    @Test
    public void saveFileTest() {
        manager.removeFileContent();
        assertEquals(0, manager.getTasks().size(), "Хеш-таблица пустая.");
        assertEquals(0, manager.getEpics().size(), "Хеш-таблица пустая.");
        assertEquals(0, manager.getSubtasks().size(), "Хеш-таблица пустая.");

        assertNotNull(manager.getFile(), "Файл существует.");

        FileBackedTaskManager manager1 = FileBackedTaskManager.loadFromFile(manager.getFile());

        assertEquals(0, manager1.getTasks().size(), "Хеш-таблица пустая.");
        assertEquals(0, manager1.getEpics().size(), "Хеш-таблица пустая.");
        assertEquals(0, manager1.getSubtasks().size(), "Хеш-таблица пустая.");

        assertNotNull(manager1.getFile(), "Файл существует.");
    }

    @Test
    public void subtaskAndEpicShouldBeConnected() {
        Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
        manager.addEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        manager.addSubtask(subtask);

        assertNotNull(manager.getEpics(), "Эпики найдены.");
        assertEquals(1, manager.getEpics().size(), "Верное количество эпиков.");

        assertNotNull(manager.getSubtasks(), "Подзадача найдена.");
        assertEquals(1, manager.getEpics().size(), "Верное количество подзадач.");

        assertEquals(1, epic.getSubtaskIds().size(), "Эпик и подзадача связаны");
    }

    @Test
    void testRemoveFileContentMethodTrowException(){
        assertNotNull(manager.getFile());
        manager.getFile().setReadOnly();
        Exception exception = assertThrows(Exception.class, () -> {manager.removeFileContent();});
        manager.getFile().setWritable(true);
    }

    @Test
    void testSaveMethodTrowException(){
        assertNotNull(manager.getFile());
        manager.getFile().setReadOnly();
        Exception exception1 = assertThrows(Exception.class, () -> {manager.save();});
        manager.getFile().setWritable(true);
    }

    @Test
    void testLoadFromFileMethodTrowException() {
        File file = null;
        Exception exception = assertThrows(RuntimeException.class, () -> {manager.loadFromFile(file);});
        manager.getFile().setWritable(true);
    }

}