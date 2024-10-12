import java.util.ArrayList;

public interface TaskManager {
    void addTask(Task task);

    ArrayList<Task> getTaskList();

    Task getTaskById(int id);

    void updateTask(Task task, int taskId);

    void removeTaskById(int id);

    void removeAllTasks();

    void addEpic(Epic epic);

    ArrayList<Epic> getEpicList();

    void updateEpic(Epic epic, int epicId);

    Epic getEpicById(int id);

    void removeEpicById(int id);

    void removeAllEpics();

    void addSubtask(Subtask subtask);

    ArrayList<Subtask> getSubtaskList();

    void updateSubtask(Subtask subtask, int subtaskId);

    Subtask getSubtaskById(int id);

    void removeSubtaskById(int id);

    void removeAllSubtasks();

    ArrayList<Task> getHistory();


}
