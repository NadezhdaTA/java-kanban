public class Subtask extends Task {
    private int epicId;

    Subtask(String title, TaskStatus taskStatus, int epicId) {
        super(title, taskStatus);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                " id=" + getId() +
                ", epicId=" + epicId +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", subtaskStatus=" + getStatus() +
                '}';
    }
}
