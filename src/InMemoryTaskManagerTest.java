
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class InMemoryTaskManagerTest {
    Task task = new Task("Test addNewTask", TaskStatus.NEW);
    Epic epic = new Epic("Test addNewEpic", TaskStatus.NEW);
    Subtask subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());

    HistoryManager historyManager = Managers.getDefaultHistory();
    InMemoryTaskManager taskManager = new InMemoryTaskManager();

    @Test
    void addNewTask() {
        taskManager.addTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача найдена.");
        assertEquals(task, savedTask, "Задачи совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задача найдена.");
        assertEquals(1, tasks.size(), "Верное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи совпадают.");
    }

    @Test
    void TaskEqualsTaskById() {
        taskManager.addTask(task);
        final int taskId = task.getId();

        Task task1 = new Task("Test addNewTask", TaskStatus.NEW);
        taskManager.addTask(task);
        task1.setId(task.getId());

        assertEquals(task1, task, "Задачи совпадают.");
    }

    @Test
    void EpicEqualsEpicById() {
        taskManager.addEpic(epic);
        final int epicId = epic.getId();

        Epic epic1 = new Epic("Test addNewEpic", TaskStatus.NEW);
        taskManager.addEpic(epic);
        epic1.setId(epic.getId());

        assertEquals(epic1, epic, "Эпики совпадают.");
    }

    @Test
    void SubtaskEqualsSubtaskById() {
        taskManager.addSubtask(subtask);
        final int subtaskId = subtask.getId();

        Subtask subtask1 = new Subtask("Test addNewSubtask", TaskStatus.NEW, epic.getId());
        taskManager.addSubtask(subtask);
        subtask1.setId(subtask.getId());

        assertEquals(subtask1, subtask, "Подзадачи совпадают.");
    }

    @Test
    void historyManagerNotNull(){
        assertNotNull(historyManager, "Объект класса существует.");
    }

    @Test
    void taskManagerNotNull(){
        TaskManager taskManager1 = Managers.getDefault();

        ArrayList<Task> list = taskManager1.getTaskList();

        assertNotNull(taskManager1, "Объект класса существует.");

        assertNotNull(list, "Вызываемые методы работают.");
    }

    @Test
    void epicShouldNotBeAddedAsSubtaskInTheSameEpic() {
        taskManager.addEpic(epic);

        subtask = new Subtask("Test addNewEpic", TaskStatus.NEW, epic.getId());

        ArrayList<Subtask> subtasks = taskManager.getSubtasksOfEpic(epic.getId());

        int arrayListSize = subtasks.size();
        taskManager.addSubtask(subtask);

        ArrayList<Subtask> subtasks1 = taskManager.getSubtasksOfEpic(epic.getId());
        int arrayListSize1 = subtasks1.size();

        assertEquals(arrayListSize, arrayListSize1);
    }

    @Test
    void SubtaskShouldNotBeAddedAsEpicInTheSameEpic() {
        subtask = new Subtask("Test addNewSubtask", TaskStatus.NEW, 10);
        taskManager.addSubtask(subtask);

        ArrayList<Epic> epics = taskManager.getEpicList();
        int arrayListSize = epics.size();

        Epic epic1 = new Epic("Test addNewSubtask", TaskStatus.NEW);

        taskManager.addEpic(epic1);
        ArrayList<Epic> epics1 = taskManager.getEpicList();

        int arrayListSize1 = epics1.size();

        assertEquals(arrayListSize, arrayListSize1);
    }

    @Test
    void addHistory() {
        taskManager.addTask(task);
        historyManager.addHistory(task);

        final List<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void TaskShouldBetheSameInHistoryManager(){
        historyManager.addHistory(task);
        historyManager.addHistory(epic);
        historyManager.addHistory(subtask);

        ArrayList<Task> history = historyManager.getHistory();

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

        ArrayList<Task> history1 = historyManager.getHistory();

        assertEquals(6, history1.size(), "История пополнилась.");

        assertEquals(history.get(0), history1.get(0));
        assertEquals(history.get(1), history1.get(1));
        assertEquals(history.get(2), history1.get(2));
    }

    @Test
    void TaskShouldBeAsGiven() {
        Task task = new Task("Test addNewTask", TaskStatus.NEW);
        task.setDescription("TaskDescription");
        taskManager.addTask(task);
        task.setId(9);

        assertEquals(1, taskManager.getTaskList().size(), "История не пустая.");

        Task task1 = taskManager.getTaskById(9);

        assertEquals("Test addNewTask", task1.getTitle());
        assertEquals("TaskDescription", task1.getDescription());
        assertEquals(9, task1.getId());

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