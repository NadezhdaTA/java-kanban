package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    List<Task> getTaskList();

    Task getTaskById(int id);

    void updateTask(Task task, int taskId);

    void removeTaskById(int id);

    void removeAllTasks();

    void addEpic(Epic epic);

    List<Epic> getEpicList();

    void updateEpic(Epic epic, int epicId);

    Epic getEpicById(int id);

    void removeEpicById(int id);

    void removeAllEpics();

    void addSubtask(Subtask subtask);

    List<Subtask> getSubtaskList();

    void updateSubtask(Subtask subtask, int subtaskId);

    Subtask getSubtaskById(int id);

    void removeSubtaskById(int id);

    void removeAllSubtasks();

    List<Task> getHistory();


}
