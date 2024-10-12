import java.util.ArrayList;

public final class Managers implements TaskManager {

    public static TaskManager getDefault(){
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory(){
        return new InMemoryHistoryManager();
    }

    @Override
    public void addTask(Task task) {

    }

    @Override
    public ArrayList<Task> getTaskList() {
        return null;
    }

    @Override
    public Task getTaskById(int id) {
        return null;
    }

    @Override
    public void updateTask(Task task, int tskId) {

    }

    @Override
    public void removeTaskById(int id) {

    }

    @Override
    public void removeAllTasks() {

    }

    @Override
    public void addEpic(Epic epic) {

    }


    @Override
    public ArrayList<Epic> getEpicList() {
        return null;
    }


    public ArrayList<Subtask> getSubtasksOfEpic(int epicId) {
        return null;
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {

    }


    public void updateEpicStatus(Epic epic) {

    }

    @Override
    public Epic getEpicById(int id) {
        return null;
    }

    @Override
    public void removeEpicById(int id) {

    }

    @Override
    public void removeAllEpics() {

    }

    @Override
    public void addSubtask(Subtask subtask) {

    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        return null;
    }

    @Override
    public void updateSubtask(Subtask subtask, int subtaskId) {

    }

    @Override
    public Subtask getSubtaskById(int id) {
        return null;
    }

    @Override
    public void removeSubtaskById(int id) {

    }

    @Override
    public void removeAllSubtasks() {

    }
    @Override
    public ArrayList<Task> getHistory() {
        return null;
    }

}
