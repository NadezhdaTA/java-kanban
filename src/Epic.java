import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    Epic(String title, TaskStatus taskStatus) {
        super(title, taskStatus);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                " id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", subtaskIds=" + subtaskIds +
                ", epicStatus=" + getStatus() +
                '}';

    }

}
