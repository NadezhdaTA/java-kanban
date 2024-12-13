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

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


class InMemoryTaskManagerTest {
    Task task = new Task("Test addNewTask", TaskStatus.NEW);

    Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);

    Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());


    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

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
        historyManager.addHistory(task1);

        Epic epic1 = new Epic("Test addNewEpic1", TaskStatus.IN_PROGRESS);
        taskManager.updateEpic(epic1, epic.getId());
        historyManager.addHistory(epic1);

        Subtask subtask1 = new Subtask("Test addNewSubtask1", TaskStatus.DONE, epic1.getId());
        taskManager.updateSubtask(subtask1, subtask.getId());
        historyManager.addHistory(subtask1);

        List<Task> history1 = historyManager.getHistory();

        assertEquals(3, history1.size(), "Количество элементов прежнее.");

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

    @Test
    void EpicStatusTest() {
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("SubtaskTitleTest1", TaskStatus.NEW, epic.getId());
        Subtask subtask2 = new Subtask("SubtaskTitleTest2", TaskStatus.NEW, epic.getId());
        Subtask subtask3 = new Subtask("SubtaskTitleTest3", TaskStatus.IN_PROGRESS, epic.getId());
        Subtask subtask4 = new Subtask("SubtaskTitleTest4", TaskStatus.DONE, epic.getId());
        Subtask subtask5 = new Subtask("SubtaskTitleTest5", TaskStatus.DONE, epic.getId());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        assertEquals(TaskStatus.NEW, epic.getStatus(), "Статус - NEW.");

        taskManager.addSubtask(subtask3);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус - IN_PROGRESS.");

        taskManager.addSubtask(subtask4);
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус - IN_PROGRESS.");

        taskManager.removeSubtaskById(subtask1.getId());
        taskManager.removeSubtaskById(subtask2.getId());
        assertEquals(TaskStatus.IN_PROGRESS, epic.getStatus(), "Статус - IN_PROGRESS.");

        taskManager.removeSubtaskById(subtask3.getId());
        taskManager.addSubtask(subtask5);
        assertEquals(TaskStatus.DONE, epic.getStatus(), "Статус - DONE.");
    }

    @Test
    void SubtaskShouldHaveEpicTest() {
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("SubtaskTitleTest1", TaskStatus.NEW, epic.getId());
        Subtask subtask2 = new Subtask("SubtaskTitleTest2", TaskStatus.IN_PROGRESS, epic.getId());

        taskManager.addSubtask(subtask1);
        assertNotNull(taskManager.getEpicById(subtask1.getEpicId()));

        taskManager.addSubtask(subtask2);
        assertNotNull(taskManager.getEpicById(subtask1.getEpicId()));
        assertNotNull(taskManager.getEpicById(subtask2.getEpicId()));

        List<Subtask> subtasks = taskManager.getSubtaskList();
        assertEquals(2, subtasks.size(), "Добавлено 3 подзадачи.");

        Subtask subtask3 = new Subtask("SubtaskTitleTest3", TaskStatus.NEW, 26);
        taskManager.addSubtask(subtask3);
        assertEquals(2, subtasks.size(), "Подзадача не добавлена.");
    }

    @Test
    void taskTimeOverlapTest() {
        Task task1 = new Task("Test addNewTask1", TaskStatus.NEW);
        Duration duration1 = Duration.ofMinutes(12586);
        LocalDateTime time1 = LocalDateTime.of(2025, 2, 2, 14, 53);
        task1.setStartTime(time1, duration1);

        taskManager.addTask(task);
        taskManager.addTask(task1);
        assertEquals(2, taskManager.getTaskList().size(), "Добавлены 2 задачи.");

        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("SubtaskTitleTest1", TaskStatus.NEW, epic.getId());
        LocalDateTime time2 = LocalDateTime.of(2024, 11, 2, 6, 0);
        Duration duration2 = Duration.ofMinutes(76);
        subtask1.setStartTime(time2, duration2);
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("SubtaskTitleTest2", TaskStatus.IN_PROGRESS, epic.getId());
        LocalDateTime time3 = LocalDateTime.of(2025, 1, 2, 8, 12);
        Duration duration3 = Duration.ofMinutes(186);
        subtask2.setStartTime(time3, duration3);
        taskManager.addSubtask(subtask2);

        assertEquals(epic.getStartTime(), subtask1.getStartTime(), "Расчетное время старта эпика совпадает со стартом самой ранней подзадачи эпика.");
        assertEquals(epic.getEndTime(), subtask2.getEndTime(), "Расчетное время окончания эпика совпадает со временем окончания самой поздней подзадачи эпика.");

        Task task3 = new Task("Test addNewTask2", TaskStatus.NEW);
        LocalDateTime time = LocalDateTime.of(2025, 1, 12, 15, 25);
        Duration duration = Duration.ofMinutes(459);
        task3.setStartTime(time, duration);
        taskManager.addTask(task3);
        assertEquals(3, taskManager.getTaskList().size(), "Задача с наложением временных отрезков не добавлена.");
    }

}