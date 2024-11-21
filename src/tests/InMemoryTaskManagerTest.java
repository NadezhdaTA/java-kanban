package tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.util.ArrayList;
import java.util.List;


class InMemoryTaskManagerTest {
    Task task = new Task("Test addNewTask", TaskStatus.NEW);
    Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
    Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());

    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addNewTask() {
        taskManager.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача найдена.");
        assertEquals(task, savedTask, "Задачи совпадают.");

        final ArrayList<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задача найдена.");
        assertEquals(1, tasks.size(), "Верное количество задач.");
        assertEquals(task, tasks.getFirst(), "Задачи совпадают.");
    }

    @Test
     void taskEqualsTaskById() {
        taskManager.addTask(task);

        Task task1 = new Task("Test addNewTask", TaskStatus.NEW);
        taskManager.addTask(task);
        task1.setId(task.getId());

        assertEquals(task1, task, "Задачи совпадают.");
    }

    @Test
     void epicEqualsEpicById() {
        taskManager.addEpic(epic);

        Epic epic1 = new Epic("Test addNewEpic", TaskStatus.NEW);
        taskManager.addEpic(epic);
        epic1.setId(epic.getId());

        assertEquals(epic1, epic, "Эпики совпадают.");
    }

    @Test
    void subtaskEqualsSubtaskById() {
        taskManager.addSubtask(subtask);

        Subtask subtask1 = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        taskManager.addSubtask(subtask);
        subtask1.setId(subtask.getId());

        assertEquals(subtask1, subtask, "Подзадачи совпадают.");
    }

    @Test
    void historyManagerNotNull() {
        assertNotNull(historyManager, "Объект класса существует.");
    }

    @Test
    void taskManagerNotNull() {
        TaskManager taskManager1 = Managers.getDefault();

        List<Task> list = taskManager1.getTaskList();

        assertNotNull(taskManager1, "Объект класса существует.");

        assertNotNull(list, "Вызываемые методы работают.");
    }

    @Test
    void epicShouldNotBeAddedAsSubtaskInTheSameEpic() {
        taskManager.addEpic(epic);

        subtask = new Subtask("Test addNewEpic", TaskStatus.NEW, epic.getId());
        subtask.setId(epic.getId());

        ArrayList<Subtask> subtasks = taskManager.getSubtasksOfEpic(epic.getId());

        int arrayListSize = subtasks.size();

        taskManager.addSubtask(subtask);

        ArrayList<Subtask> subtasks1 = taskManager.getSubtasksOfEpic(epic.getId());
        int arrayListSize1 = subtasks1.size();

        assertEquals(arrayListSize, arrayListSize1);
    }

    @Test
    void addHistory() {
        taskManager.addTask(task);
        historyManager.addHistory(task);

        List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void taskShouldBetheSameInHistoryManager() {
        historyManager.addHistory(task);
        historyManager.addHistory(epic);
        historyManager.addHistory(subtask);

        List<Task> history = historyManager.getHistory();

        assertEquals(3, history.size(), "История не пустая, содержит 3 элемента.");

        Task task1 = new Task("Test addNewTask1", TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1, task.getId());
        historyManager.addHistory(task);

        Epic epic1 = new Epic("Test addNewEpic1", TaskStatus.IN_PROGRESS);
        taskManager.updateEpic(epic1, epic.getId());
        historyManager.addHistory(epic);

        Subtask subtask1 = new Subtask("Test addNewSubtask1", TaskStatus.DONE, epic.getId());
        taskManager.updateSubtask(subtask1, subtask.getId());
        historyManager.addHistory(subtask);

        List<Task> history1 = historyManager.getHistory();

        assertEquals(6, history1.size(), "История пополнилась.");

        assertEquals(history.get(0), history1.get(0));
        assertEquals(history.get(1), history1.get(1));
        assertEquals(history.get(2), history1.get(2));

    }

    @Test
    void taskShouldBeAsGiven() {
        Task task = new Task("Test addNewTask", TaskStatus.NEW);
        task.setDescription("TaskDescription");
        taskManager.addTask(task);
        int taskId = task.getId();

        assertEquals(1, taskManager.getTaskList().size(), "История не пустая.");

        Task task1 = taskManager.getTaskById(taskId);

        assertEquals("Test addNewTask", task1.getTitle());
        assertEquals("TaskDescription", task1.getDescription());
        assertEquals(taskId, task1.getId());

    }

    @Test
    void tasksWithSameIdShouldNotConflictInManager() {
        taskManager.addTask(task);

        Task task1 = new Task("Test addNewTask1", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task1);
        task1.setId(task.getId());

        ArrayList<Task> list = taskManager.getTaskList();
        Task task2 = list.get(0);
        Task task3 = list.get(1);

        assertEquals(2, taskManager.getTaskList().size(), "Оба элемента добавлены в таблицу.");

        assertEquals(task.getId(), task1.getId(), "id одинаковые.");

        assertEquals(task, task2, "Задачи одинаковые.");

        assertEquals(task1, task3, "Задачи одинаковые.");

    }

}