
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import org.junit.jupiter.api.Test;

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
    void TaskEqualsTaskById() {
        taskManager.addTask(task);

        Task task1 = new Task("Test addNewTask", TaskStatus.NEW);
        taskManager.addTask(task);
        task1.setId(task.getId());

        assertEquals(task1, task, "Задачи совпадают.");
    }

    @Test
    void EpicEqualsEpicById() {
        taskManager.addEpic(epic);

        Epic epic1 = new Epic("Test addNewEpic", TaskStatus.NEW);
        taskManager.addEpic(epic);
        epic1.setId(epic.getId());

        assertEquals(epic1, epic, "Эпики совпадают.");
    }

    @Test
    void SubtaskEqualsSubtaskById() {
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

        final ArrayList<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void taskShouldBeAddedInHistoryAsTheyGet() {
        taskManager.addTask(task);
        historyManager.addHistory(task);

        taskManager.addEpic(epic);
        historyManager.addHistory(epic);

        taskManager.addSubtask(subtask);
        historyManager.addHistory(subtask);

        historyManager.addHistory(epic);

        final ArrayList<Task> history = historyManager.getHistory();

        assertNotNull(history, "История не пустая.");

        assertEquals(3, history.size(), "История насчитывает 3 элемента.");

        assertEquals(history.getFirst(), task, "На первом месте стоит объект task.");

        assertEquals(history.get(1), subtask, "На втором месте стоит объект subtask.");

        assertEquals(history.get(2), epic, "На третьем месте стоит объект epic.");
    }

    @Test
    void TaskShouldBeAsGiven() {
        Task task = new Task("Test addNewTask", TaskStatus.NEW);
        task.setDescription("TaskDescription");
        taskManager.addTask(task);

        assertEquals(1, taskManager.getTaskList().size(), "История не пустая.");

        Task task1 = taskManager.getTaskById(task.getId());

        assertEquals("Test addNewTask", task1.getTitle(), "Заголовки совпадают.");
        assertEquals("TaskDescription", task1.getDescription(), "Описание совпадают.");
        assertEquals(task.getId(), task1.getId(), "Id совпадают.");
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
    void epicShouldNotHaveSubtaskId() {
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("Test addNewSubtask1", TaskStatus.NEW, epic.getId());
        taskManager.addSubtask(subtask1);

        Subtask subtask2 = new Subtask("Test addNewSubtask2", TaskStatus.NEW, epic.getId());
        taskManager.addSubtask(subtask2);

        Subtask subtask3 = new Subtask("Test addNewSubtask3", TaskStatus.NEW, epic.getId());
        taskManager.addSubtask(subtask3);

        int count = epic.getSubtaskIds().size();

        assertEquals(3, count);

        taskManager.removeSubtaskById(subtask2.getId());

        int count1 = epic.getSubtaskIds().size();

        assertEquals(2, count1);

    }


    @Test
    void taskGetByIdTest() {
        taskManager.addTask(task);

        Task task1 = new Task("Test addNewTask1", TaskStatus.NEW);
        Task task2 = new Task("Test addNewTask2", TaskStatus.NEW);

        taskManager.addTask(task1);
        taskManager.addTask(task2);

        Task testTask = taskManager.getTaskById(task1.getId());

        assertEquals(task1, testTask, "Задачи одинаковые.");

        assertEquals(task1.getId(), testTask.getId(), "Id задач одинаковые.");

    }

    @Test
    void updateTaskTest() {
        Task task1 = new Task("Test addNewTask1", TaskStatus.NEW);

        taskManager.addTask(task1);

        assertEquals(1, taskManager.getTaskList().size());

        Task task2 = new Task("Test addNewTask2", TaskStatus.IN_PROGRESS);

        taskManager.updateTask(task2, task1.getId());

        assertEquals(task1.getId(), task2.getId(), "Id задач одинаковые.");

        assertNotEquals(task1.getTitle(), task2.getTitle(), "Название задач разное.");

        assertNotEquals(task1.getStatus(), task2.getStatus(), "Статус задач разный.");

        assertEquals(1, taskManager.getTaskList().size(), "Размер списка прежний.");

    }

    @Test
    void updateSubtaskTest() {
        taskManager.addEpic(epic);

        Subtask subtask1 = new Subtask("Test addNewSubtask1", TaskStatus.NEW, epic.getId());
        Subtask subtask2 = new Subtask("Test addNewSubtask2", TaskStatus.NEW, epic.getId());

        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);

        assertEquals(2, taskManager.getSubtaskList().size());

        Subtask subtask3 = new Subtask("Test addNewSubtask3", TaskStatus.IN_PROGRESS, epic.getId());

        taskManager.updateSubtask(subtask3, subtask1.getId());

        assertEquals(2, taskManager.getSubtaskList().size());

        assertEquals(subtask1.getId(), subtask3.getId());

        assertNotEquals(subtask1.getTitle(), subtask3.getTitle());

        assertNotEquals(subtask1.getStatus(), subtask3.getStatus());
    }

    @Test
    void updateEpicTest() {
        taskManager.addEpic(epic);

        Epic epic1 = new Epic("Test addNewEpic1", TaskStatus.NEW);
        Epic epic2 = new Epic("Test addNewEpic2", TaskStatus.NEW);

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);

        assertEquals(3, taskManager.getEpicList().size());

        taskManager.updateEpic(epic2, epic.getId());

        assertEquals(3, taskManager.getEpicList().size());

        assertEquals(epic.getId(), epic2.getId());

        assertNotEquals(epic.getTitle(), epic2.getTitle());

    }

    @Test
    void setTitleTest() {
        taskManager.addTask(task);

        task.setTitle("NewTaskTitle");

        assertNotEquals("Test addNewTask", "NewTaskTitle");

        assertEquals("NewTaskTitle", task.getTitle());

    }

    @Test
    void setDescriptionTest() {
        assertNull(epic.getDescription());

        epic.setDescription("SetDescriptionTest");

        assertNotNull(epic.getDescription());

    }


}