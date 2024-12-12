package managers;

import tasks.*;

import java.io.*;
import java.io.IOException;
import java.io.FileWriter;
import java.time.Duration;
import java.time.LocalDateTime;


import static java.lang.Integer.parseInt;
import static tasks.TaskType.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File file;


import static java.lang.Integer.parseInt;
import static tasks.TaskType.*;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    public FileBackedTaskManager(String fileName) {
        file = new File(fileName);
    }

    public File getFile() {
        return file;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        task.setType(TASK);
        save();
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        epic.setType(TaskType.EPIC);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        subtask.setType(TaskType.SUBTASK);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    public void save() {
        try {
            removeFileContent();
            FileWriter bw = new FileWriter(file, true);
            bw.write("id,type,name,status,description,startTime,duration,epicIdForSubtask" + "\n");
            for (Task task : getTasks().values()) {
                String line = this.toString(task);
                bw.write(line + "\n");
            }
            for (Task task : getEpics().values()) {
                String line = this.toString(task);
                bw.write(line + "\n");
            }
            for (Task task : getSubtasks().values()) {
                String line = this.toString(task);
                bw.write(line + "\n");
            }
            bw.close();
        } catch (ManagerSaveException | IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    public void removeFileContent() {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void printFile() {
        String line;
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            while (br.ready()) {
                line = br.readLine();
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fbtManager = new FileBackedTaskManager("StateFile.csv");

        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader br = new BufferedReader(fileReader);
            for (int i = 1; i < 2; i++) {
                br.readLine();
            }
            while (br.ready()) {

                String line = br.readLine();
                String[] lineForTask = line.split(",");

                int id = parseInt(lineForTask[0]);
                TaskType type = TaskType.valueOf(lineForTask[1]);
                String title = lineForTask[2];
                TaskStatus status = TaskStatus.valueOf(lineForTask[3]);
                String description = lineForTask[4];
                LocalDateTime startTime = null;
                if (!lineForTask[5].equals("null")) {
                    startTime = LocalDateTime.parse(lineForTask[5]);
                }
                Duration duration = null;
                if (!lineForTask[6].equals("null")){
                    duration = Duration.parse(lineForTask[6]);
                }

                if (type.equals(TASK)) {
                    Task task = new Task(title, status);
                    task.setType(type);
                    task.setDescription(description);
                    task.setId(id);
                    task.setStartTime(startTime, duration);
                    fbtManager.getTasks().put(task.getId(), task);

                } else if (type.equals(EPIC)) {
                    Epic epic = new Epic(title, status);
                    epic.setType(type);
                    epic.setDescription(description);
                    epic.setId(id);
                    epic.setStartTime(startTime, duration);
                    fbtManager.getEpics().put(epic.getId(), epic);

                } else if (type.equals(SUBTASK)) {
                    int epicId = parseInt(lineForTask[7]);
                    Epic epic1 = fbtManager.getEpicById(epicId);
                    Subtask subtask = new Subtask(title, status, epicId);
                    subtask.setType(type);
                    subtask.setDescription(description);
                    subtask.setId(id);
                    subtask.setStartTime(startTime, duration);
                    fbtManager.getSubtasks().put(subtask.getId(), subtask);
                    epic1.setSubtaskId(subtask.getId());
                }

                if (fbtManager.getNextId() < id) {
                    fbtManager.setNextId(id + 1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fbtManager;
    }

    public String toString(Task task) {
        String toString;
        if (task.getType().equals(TaskType.SUBTASK)) {
            Subtask task1 = (Subtask) task;
            toString = "%d,%s,%s,%s,%s,%s,%s,%s".formatted(task1.getId(),
                    task1.getType(),
                    task1.getTitle(),
                    task1.getStatus(),
                    task1.getDescription(),
                    task1.getStartTime(),
                    task1.getDuration(),
                    task1.getEpicId());
        } else {
            toString = "%d,%s,%s,%s,%s,%s,%s".formatted(task.getId(),
                    task.getType(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getDescription(),
                    task.getStartTime(),
                    task.getDuration());
        }
        return toString;
    }
}