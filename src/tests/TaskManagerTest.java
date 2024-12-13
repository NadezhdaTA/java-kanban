package tests;

import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskManagerTest <T extends TaskManager> {
    Task task = new Task("Test addNewTask", TaskStatus.NEW);
    Task task1 = new Task("Test addNewTask1", TaskStatus.NEW);
    Task task2 = new Task("Test addNewTask2", TaskStatus.NEW);

    Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
    Epic epic1 = new Epic("Test addNewEpic1", TaskStatus.NEW);
    Epic epic2 = new Epic("Test addNewEpic2", TaskStatus.NEW);

    Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
    Subtask subtask1 = new Subtask("Test addNewSubtask1", TaskStatus.DONE, epic.getId());
    Subtask subtask2 = new Subtask("Test addNewSubtask2", TaskStatus.IN_PROGRESS, epic.getId());

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addTaskTest() {
        assertEquals(0, taskManager.getTaskList().size(), "Задача отсутствует.");
        Task task = new Task("Test addNewTask", TaskStatus.NEW);
        taskManager.addTask(task);
        assertEquals(1, taskManager.getTaskList().size(), "Задача добавлена.");
    }

    @Test
    void getTaskListTest() {
        ArrayList<Task> tasks = taskManager.getTaskList();
        assertEquals(0, tasks.size(), "Верное количество задач.");

        taskManager.addTask(task);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Список не пуст.");
        assertEquals(3, tasks.size(), "Верное количество задач.");
    }

    @Test
    void getTaskByIdTest() {
        taskManager.addTask(task);
        Task task3 = taskManager.getTaskById(task.getId());
        assertEquals(task3, task, "Задачи совпадают.");
    }

    @Test
    void updateTaskTest() {
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getTaskList().size());
        Task task5 = new Task("Test addNewTask", TaskStatus.NEW);
        taskManager.addTask(task5);

        Task task3 = new Task("TaskTestTitle3", TaskStatus.IN_PROGRESS);

        taskManager.updateTask(task3, task5.getId());
        assertEquals(task3, taskManager.getTaskById(task5.getId()), "Задачи совпадают.");
    }

    @Test
    void removeTaskByIdTest() {
        taskManager.addTask(task);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(3, taskManager.getTaskList().size(), "Верное количество задач.");
        taskManager.removeTaskById(task1.getId());
        assertEquals(2, taskManager.getTaskList().size(), "Верное количество задач.");
    }

    @Test
    void removeAllTasks() {
        taskManager.addTask(task);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        assertEquals(3, taskManager.getTaskList().size(), "Верное количество задач.");
        taskManager.removeAllTasks();
        assertEquals(0, taskManager.getTaskList().size(), "Список пуст.");
    }

    @Test
    void addEpicTest() {
        assertEquals(0, taskManager.getEpicList().size(), "Эпик отсутствует.");
        taskManager.addEpic(epic);
        assertEquals(1, taskManager.getEpicList().size(), "Эпик добавлен.");
    }

    @Test
    void getEpicListTest() {
        ArrayList<Epic> epics = taskManager.getEpicList();
        assertEquals(0, epics.size(), "Верное количество задач.");

        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        epics = taskManager.getEpicList();

        assertNotNull(epics, "Список не пуст.");
        assertEquals(3, epics.size(), "Верное количество задач.");
    }

    @Test
    void updateEpicTest() {
        taskManager.addEpic(epic);

        Epic epic3 = new Epic("Test addNewEpic", TaskStatus.NEW);

        taskManager.updateEpic(epic3, epic.getId());
        assertEquals("Test addNewEpic", epic.getTitle(), "Заголовки совпадают.");
    }

    @Test
    void getEpicByIdTest() {
        taskManager.addEpic(epic);
        Epic epic3 = taskManager.getEpicById(epic.getId());
        assertEquals(epic3, epic, "Задачи совпадают.");
    }

    @Test
    void removeEpicByIdTest() {
        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        assertEquals(3, taskManager.getEpicList().size(), "Верное количество задач.");
        taskManager.removeEpicById(epic1.getId());
        assertEquals(2, taskManager.getEpicList().size(), "Верное количество задач.");
    }

    @Test
    void removeAllEpicsTest() {
        taskManager.addEpic(epic);
        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        assertEquals(3, taskManager.getEpicList().size(), "Верное количество задач.");
        taskManager.removeAllEpics();
        assertEquals(0, taskManager.getEpicList().size(), "Список пуст.");
    }

    @Test
    void addSubtaskTest() {
        assertEquals(0, taskManager.getSubtaskList().size(), "Подзадача отсутствует.");
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        taskManager.addSubtask(subtask);
        assertEquals(1, taskManager.getSubtaskList().size(), "Подзадача добавлена.");
    }

    @Test
    void getSubtaskListTest() {
        ArrayList<Subtask> subtasks = taskManager.getSubtaskList();
        assertEquals(0, subtasks.size(), "Верное количество задач.");

        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        Subtask subtask1 = new Subtask("Test addNewSubtask1", TaskStatus.DONE, epic.getId());
        Subtask subtask2 = new Subtask("Test addNewSubtask2", TaskStatus.IN_PROGRESS, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        subtasks = taskManager.getSubtaskList();

        assertNotNull(subtasks, "Список не пуст.");
        assertEquals(3, subtasks.size(), "Верное количество задач.");
    }

    @Test
    void updateSubtaskTest() {
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        taskManager.addSubtask(subtask);
        Subtask subtask3 = new Subtask("Test addNewSubtask3", TaskStatus.IN_PROGRESS, epic.getId());

        taskManager.updateSubtask(subtask3, subtask.getId());

        assertEquals(subtask3, taskManager.getSubtaskById(subtask.getId()), "Задачи совпадают.");
    }

    @Test
    void getSubtaskByIdTest() {
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        taskManager.addSubtask(subtask);
        Subtask subtask3 = taskManager.getSubtaskById(subtask.getId());
        assertEquals(subtask, subtask3, "Подзадачи совпадают.");
    }

    @Test
    void removeSubtaskByIdTest() {
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        Subtask subtask1 = new Subtask("Test addNewSubtask1", TaskStatus.DONE, epic.getId());
        Subtask subtask2 = new Subtask("Test addNewSubtask2", TaskStatus.IN_PROGRESS, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(3, taskManager.getSubtaskList().size(), "Подзадачи добавлены в список.");
        taskManager.removeSubtaskById(subtask2.getId());
        assertEquals(2, taskManager.getSubtaskList().size(), "Подзадач стало на 1 меньше.");
    }

    @Test
    void removeAllSubtasksTest() {
        taskManager.addEpic(epic);
        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        Subtask subtask1 = new Subtask("Test addNewSubtask1", TaskStatus.DONE, epic.getId());
        Subtask subtask2 = new Subtask("Test addNewSubtask2", TaskStatus.IN_PROGRESS, epic.getId());
        taskManager.addSubtask(subtask);
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(3,taskManager.getSubtaskList().size(), "Подзадачи добавлены в список.");
        taskManager.removeAllSubtasks();
        assertEquals(0,taskManager.getSubtaskList().size(), "Список пуст.");
    }

    @Test
    void getHistoryTest() {
        Task task = new Task("Test addNewTask", TaskStatus.NEW);
        taskManager.addTask(task);
        Epic epic2 = new Epic("Test addNewEpic2", TaskStatus.NEW);
        taskManager.addEpic(epic2);
        Subtask subtask1 = new Subtask("Test addNewSubtask1", TaskStatus.DONE, epic2.getId());
        taskManager.addSubtask(subtask1);


        taskManager.getEpicById(epic2.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getTaskById(task.getId());

        List<Task> history = taskManager.getHistory();

        assertNotNull(history, "История просмотров не пустая.");
        assertEquals(3, history.size(), "В историю просмотров добавлены просмотренные задачи.");
    }

}
