import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;

    private HistoryManager memory = Managers.getDefaultHistory();

    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        memory.addHistory(task);
        return task;
    }

    @Override
    public void updateTask(Task task, int taskId) {
        task.setId(taskId);
        tasks.put(taskId, task);
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }


    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (Integer i : epics.keySet()) {
            Epic epic = epics.get(i);
            epicList.add(epic);
        }
        return epicList;
    }

    public ArrayList<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        if (!epic.getSubtaskIds().isEmpty()) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
                Subtask subtask = subtasks.get(subtaskId);
                subtasksOfEpic.add(subtask);
            }
        }
        return subtasksOfEpic;
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        epic.setId(epicId);
        epics.put(epicId, epic);
        updateEpicStatus(epic);
    }

    public void updateEpicStatus(Epic epic) {
        int count_NEW = 0;
        int count_IN_PROGRESS = 0;
        int count_DONE = 0;

        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);

            switch (subtask.getStatus()) {
                case NEW:
                    count_NEW++;
                    break;
                case IN_PROGRESS:
                    count_IN_PROGRESS++;
                    break;
                case DONE:
                    count_DONE++;
                    break;
            }
        }

        if (count_NEW == epic.getSubtaskIds().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (count_DONE == epic.getSubtaskIds().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        memory.addHistory(epic);
        return epic;
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.remove(id);

        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(id);
    }

    @Override
    public void removeAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return;
        }
        if (epic.getId() == subtask.getId()) {
            System.out.println("Подзадача " + "'" + subtask.getTitle() + "'" + " не может быть добавлена в эпик, т.к. у них одинаковый id.");
            return;
        } else {
            subtask.setId(nextId++);
            subtasks.put(subtask.getId(), subtask);
            epic.setSubtaskId(subtask.getId());
            updateEpicStatus(epic);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Integer i : subtasks.keySet()) {
            Subtask subtask = subtasks.get(i);
            subtasksList.add(subtask);
        }
        return subtasksList;
    }

    @Override
    public void updateSubtask(Subtask subtask, int subtaskId) {
        subtask.setId(subtaskId);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        updateEpicStatus(epic);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        memory.addHistory(subtask);
        return subtask;
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        subtasks.remove(id);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        for (Integer subtaskId : subtaskIds) {
            if (subtaskId == id) {
                subtaskIds.remove(subtaskId);
            }
        }
        updateEpicStatus(epic);
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            updateEpicStatus(epic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return memory.getHistory();
    }

}
