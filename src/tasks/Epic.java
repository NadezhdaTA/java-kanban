package tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String title, TaskStatus taskStatus) {
        super(title, taskStatus);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);
    }

    @Override
    public LocalDateTime getEndTime() { return endTime; }

    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                " id=" + getId() +
                ", title='" + getTitle() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", subtaskIds=" + subtaskIds + '\'' +
                ", epicStatus=" + getStatus() + '\'' +
                ", startTime=" + startTime + '\'' +
                ", duration=" + duration + '\'' +
                ", endTime=" + endTime + '\'' +
                '}';

    }

}
