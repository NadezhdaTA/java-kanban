package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String title;
    private String description;
    private int id;
    private TaskStatus status;
    private TaskType type;
    protected Duration duration;
    protected LocalDateTime startTime;


    public Task(String title, TaskStatus status) {
        this.title = title;
        this.status = status;
    }

    public LocalDateTime getEndTime() {
        if (duration == null || startTime == null) {
            return null;
        } else {
            return startTime.plus(duration);
        }
    }

    public void setTitle(String title) { this.title = title; }

    public Duration getDuration() { return duration; }

    public void setDuration(Duration duration) { this.duration = duration; }

    public LocalDateTime getStartTime() { return startTime; }

    public void setStartTime(LocalDateTime startTime, Duration duration) {
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.status = taskStatus;
    }

    public TaskType getType() { return type; }

    public void setType(TaskType type) { this.type = type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "tasks.Task{" +
                " id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + status + '\'' +
                ", startTime=" + startTime + '\'' +
                ", duration=" + duration +
                "}";
    }
}




