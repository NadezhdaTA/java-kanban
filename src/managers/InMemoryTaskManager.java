package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private int nextId = 1;
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    private final HistoryManager memory = Managers.getDefaultHistory();

    public HashMap<Integer, Task> getTasks() {return tasks;}

    public HashMap<Integer, Epic> getEpics() {return epics;}

    public HashMap<Integer, Subtask> getSubtasks() {return subtasks;}

    public int getNextId() {return nextId;}

    public void setNextId(int nextId) {this.nextId = nextId;}

    public TreeSet<Task> getPrioritizedTasks() { return prioritizedTasks; }

    @Override
    public void addTask(Task task) {
        task.setId(nextId++);
        if (task.getStartTime() != null) {

            boolean validation = taskTimeOverlap(task);

            if (validation) {
                System.out.println("Невозможно добавить задачу, т.к. время её выполнения накладывается на уже существующие." + "\n" +
                        "Время начала первой задачи - " + prioritizedTasks.first().getStartTime() + "." + "\n" +
                        "Время окончания последней задачи - " + prioritizedTasks.last().getStartTime() + ".");
                return;
            }
            prioritizedTasks.add(task);
        }
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
        Task task1 = tasks.get(taskId);
        prioritizedTasks.remove(task1);
        task1 = task;
        task1.setId(taskId);
        tasks.put(taskId, task1);
        if (task1.getStartTime() != null) {
            prioritizedTasks.add(task1);
        }
    }

    @Override
    public void removeTaskById(int id) {
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void removeAllTasks() {
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.clear();

    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
        updateEpicDuration(epic);
        updateEpicStartTime(epic);
        updateEpicEndTime(epic);
    }

    @Override
    public ArrayList<Epic> getEpicList() {return new ArrayList<>(epics.values());}

    public ArrayList<Subtask> getSubtasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        if (!epic.getSubtaskIds().isEmpty()) {
            subtasksOfEpic = (ArrayList<Subtask>) epic.getSubtaskIds().stream()
                    .map(this::getSubtaskById)
                    .collect(Collectors.toList());
        }
        return subtasksOfEpic;
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        epics.put(epicId, epic);
        updateEpicStatus(epic);
        updateEpicDuration(epic);
        updateEpicStartTime(epic);
        updateEpicEndTime(epic);
    }

    public void updateEpicStatus(Epic epic) {
        int countNew = 0;
        int countInProgress = 0;
        int countDone = 0;

        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = subtasks.get(subtaskId);

            switch (subtask.getStatus()) {
                case NEW:
                    countNew++;
                    break;
                case IN_PROGRESS:
                    countInProgress++;
                    break;
                case DONE:
                    countDone++;
                    break;
            }
        }

        if (countNew == epic.getSubtaskIds().size()) {
            epic.setStatus(TaskStatus.NEW);
        } else if (countDone == epic.getSubtaskIds().size()) {
            epic.setStatus(TaskStatus.DONE);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

    public void updateEpicDuration(Epic epic) {
        Duration duration = epic.getDuration();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = getSubtaskById(subtaskId);
            if (subtask != null) {
                if (subtask.getDuration() != null) {
                    if (duration == null) {
                        epic.setDuration(subtask.getDuration());
                    } else {
                        epic.setDuration(duration.plus(subtask.getDuration()));
                    }
                }
            }
        }
    }

    public void updateEpicStartTime(Epic epic) {
        LocalDateTime startTime = epic.getStartTime();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = getSubtaskById(subtaskId);
            if (subtask != null) {
                if (subtask.getStartTime() != null) {
                    if (startTime == null) {
                        epic.setStartTime(subtask.getStartTime(), subtask.getDuration());
                    } else {
                        if (subtask.getStartTime().isBefore(startTime))
                            epic.setStartTime(subtask.getStartTime(), subtask.getDuration());
                    }
                }
            }
        }
    }

    public void updateEpicEndTime(Epic epic) {
        LocalDateTime endTime = epic.getEndTime();
        for (Integer subtaskId : epic.getSubtaskIds()) {
            Subtask subtask = getSubtaskById(subtaskId);
            if (subtask != null) {
                if (subtask.getEndTime() != null) {
                    if (endTime == null) {
                        epic.setEndTime(subtask.getEndTime());
                    } else {
                        if (subtask.getEndTime().isAfter(endTime)) {
                            epic.setEndTime(subtask.getEndTime());
                        }
                    }
                }
            }
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
        Epic epic = epics.get(id);
        epic.getSubtaskIds().stream().map(subtasks::get).forEach(prioritizedTasks::remove);
        epic.getSubtaskIds().forEach(subtasks::remove);
        epics.remove(id);
    }

    @Override
    public void removeAllEpics() {
        subtasks.values().forEach(prioritizedTasks::remove);
        subtasks.clear();
        epics.clear();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null) {
            return;
        }

        if (subtask.getStartTime() != null) {

            boolean validation = taskTimeOverlap(subtask);

            if (validation) {
                System.out.println("Невозможно добавить задачу, т.к. время её выполнения накладывается на уже существующие." + "\n" +
                        "Время начала первой задачи - " + prioritizedTasks.first().getStartTime()+ "." + "\n"  +
                        "Время окончания последней задачи - " + prioritizedTasks.last().getStartTime() + ".");
                return;
            }
            prioritizedTasks.add(subtask);
        }

        if (epic.getId() == subtask.getId()) {
            System.out.println("Подзадача " + "'" + subtask.getTitle() + "'" + " не может быть добавлена в эпик, т.к. у них одинаковый id.");
            return;
        }

        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);

        if (!epic.getSubtaskIds().contains(subtask.getId())) {
            epic.setSubtaskId(subtask.getId());
        }

        updateEpicStatus(epic);
        updateEpicDuration(epic);
        updateEpicStartTime(epic);
        updateEpicEndTime(epic);
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void updateSubtask(Subtask subtask, int subtaskId) {
        Subtask subtask1 = subtasks.get(subtaskId);
        prioritizedTasks.remove(subtask1);
        subtask1 = subtask;
        subtask1.setId(subtaskId);
        subtasks.put(subtaskId, subtask1);

        if (subtask1.getStartTime() != null) {
            prioritizedTasks.add(subtask1);
        }

        Epic epic = epics.get(subtask1.getEpicId());
        updateEpicStatus(epic);
        updateEpicDuration(epic);
        updateEpicStartTime(epic);
        updateEpicEndTime(epic);
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
        prioritizedTasks.remove(subtask);
        Epic epic = epics.get(subtask.getEpicId());
        subtasks.remove(id);
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        subtaskIds.removeIf(subtaskId -> subtaskId == id);
        updateEpicStatus(epic);
        updateEpicDuration(epic);
        updateEpicStartTime(epic);
        updateEpicEndTime(epic);
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.values().forEach(prioritizedTasks::remove);
        subtasks.values().forEach(subtask -> {epics.get(subtask.getEpicId()).getSubtaskIds().clear();});
        subtasks.clear();
        for (Epic epic : epics.values()) {
            updateEpicStatus(epic);
            updateEpicDuration(epic);
            updateEpicStartTime(epic);
            updateEpicEndTime(epic);
        }
    }

    @Override
    public List<Task> getHistory() {
        return memory.getHistory();
    }

    public boolean taskTimeOverlap(Task task) {
        boolean validation = false;
        if (!prioritizedTasks.isEmpty()) {
            for (Task task1 : prioritizedTasks) {
                if (task.getStartTime().isAfter(task1.getStartTime()) && task.getStartTime().isBefore(task1.getEndTime())) {
                    validation = true;
                    break;
                } else if (task1.getStartTime().isAfter(task.getStartTime()) && task1.getStartTime().isBefore(task.getEndTime())) {
                    validation = true;
                    break;
                } else if (task.getStartTime() == task1.getStartTime()) {
                    validation = true;
                    break;
                }
            }
        }
        return validation;
    }

}
