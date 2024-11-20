import java.io.*;
import java.io.IOException;
import java.io.FileWriter;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private String fileName = "StateFile.csv";

    public FileBackedTaskManager(String fileName) {
        this.fileName = fileName;
    }

    private File file = new File(fileName);

    public File getFile() {
        return file;
    }

    public void addTask(Task task) {
        super.addTask(task);
        task.setType(TaskType.TASK);
        save();
    }

    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    public void addEpic(Epic epic) {
        super.addEpic(epic);
        epic.setType(TaskType.EPIC);
        save();
    }

    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        subtask.setType(TaskType.SUBTASK);
        save();
    }

    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    private void save() {

        try {
            removeFileContent();
            FileWriter bw = new FileWriter(file, true);
            bw.write("id,type,name,status,description,epic" + "\n");
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

    private void removeFileContent() {
        try {
            PrintWriter writer = new PrintWriter(file);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printFile() {
        String line = null;
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

                int id = Integer.parseInt(lineForTask[0]);
                TaskType type = TaskType.valueOf(lineForTask[1]);
                String title = lineForTask[2];
                TaskStatus status = TaskStatus.valueOf(lineForTask[3]);
                String description = lineForTask[4];

                if (type.equals(TaskType.TASK)) {
                    Task task = new Task(title, status);
                    task.setType(type);
                    task.setDescription(description);
                    task.setId(id);
                    fbtManager.getTasks().put(task.getId(), task);

                } else if (type.equals(TaskType.EPIC)) {
                    Epic epic = new Epic(title, status);
                    epic.setType(type);
                    epic.setDescription(description);
                    epic.setId(id);
                    fbtManager.getEpics().put(epic.getId(), epic);

                } else if (type.equals(TaskType.SUBTASK)) {
                    int epicId = Integer.parseInt(lineForTask[5]);
                    Epic epic = fbtManager.getEpicById(epicId);
                    Subtask subtask = new Subtask(title, status, epicId);
                    subtask.setType(type);
                    subtask.setDescription(description);
                    subtask.setId(id);
                    fbtManager.getSubtasks().put(subtask.getId(), subtask);
                }

                for (Epic epic : fbtManager.getEpics().values()) {
                    int epicId = epic.getId();
                    for (Subtask subtask : fbtManager.getSubtasks().values()) {
                        if (subtask.getEpicId() == epicId) {
                            epic.setSubtaskId(subtask.getId());
                        }
                    }
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
        String toString = null;
        if (task.getType().equals(TaskType.SUBTASK)) {
            Subtask task1 = (Subtask) task;
            toString = "%d,%s,%s,%s,%s,%s".formatted(task1.getId(),
                    task1.getType(),
                    task1.getTitle(),
                    task1.getStatus(),
                    task1.getDescription(),
                    task1.getEpicId());
        } else {
            toString = "%d,%s,%s,%s,%s".formatted(task.getId(),
                    task.getType(),
                    task.getTitle(),
                    task.getStatus(),
                    task.getDescription());
        }
        return toString;
    }
}

