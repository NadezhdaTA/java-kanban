package tests;

import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.Managers;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;


import static org.junit.jupiter.api.Assertions.*;

public class HistoryManagerTest {
    InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    Task task = new Task("Test addNewTask", TaskStatus.NEW);
    Task task1 = new Task("Test addNewTask1", TaskStatus.IN_PROGRESS);
    Task task2 = new Task("Test addNewTask2", TaskStatus.DONE);
    Task task3 = new Task("Test addNewTask3", TaskStatus.NEW);
    Task task4 = new Task("Test addNewTask4", TaskStatus.NEW);
    Task task5 = new Task("Test addNewTask5", TaskStatus.NEW);
    Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
    Epic epic1 = new Epic("Test addNewEpic1", TaskStatus.NEW);
    Epic epic2 = new Epic("Test addNewEpic2", TaskStatus.NEW);

    @Test
    void addHistoryTest() {
        historyManager.addHistory(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());

        historyManager.addHistory(task);
        historyManager.addHistory(subtask);

        assertEquals(3, historyManager.getHistory().size(), "Задачи добавлены.");
    }

    @Test
    void getHistoryTest() {
        assertEquals(0, historyManager.getHistory().size(), "Задач нет.");

        taskManager.addTask(task);
        historyManager.addHistory(task);
        taskManager.addTask(task1);
        historyManager.addHistory(task1);
        taskManager.addTask(task2);
        historyManager.addHistory(task2);
        taskManager.addTask(task3);
        historyManager.addHistory(task3);

        assertEquals(4, historyManager.getHistory().size(), "Добавлено 4 задачи.");

        historyManager.addHistory(task3);

        assertEquals(4, historyManager.getHistory().size(), "Та же задача не может быть добавлена повторно.");

        taskManager.addEpic(epic);
        historyManager.addHistory(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        Subtask subtask1 = new Subtask("Test addNewSubtask1", TaskStatus.DONE, epic.getId());
        Subtask subtask2 = new Subtask("Test addNewSubtask2", TaskStatus.IN_PROGRESS, epic.getId());

        taskManager.addTask(task4);
        historyManager.addHistory(task4);
        taskManager.addEpic(epic1);
        historyManager.addHistory(epic1);
        taskManager.addEpic(epic2);
        historyManager.addHistory(epic2);
        taskManager.addSubtask(subtask);
        historyManager.addHistory(subtask);
        taskManager.addSubtask(subtask1);
        historyManager.addHistory(subtask1);

        historyManager.addHistory(task5);
        assertEquals(10, historyManager.getHistory().size(), "Задач 10.");

        taskManager.addSubtask(subtask2);
        historyManager.addHistory(subtask2);
        assertEquals(10, historyManager.getHistory().size(), "Задач может быть только 10.");
        assertTrue(historyManager.getHistory().contains(subtask2), "Последняя задача добавлена.");
        assertFalse(historyManager.getHistory().contains(task), "Первая задача удалена.");
    }

}

