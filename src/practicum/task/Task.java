package practicum.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private int id;
    private final String title;
    private String description;
    private State state;
    private Duration duration;
    private LocalDateTime startTime;


    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        state = State.NEW;
    }

    public Task(String title) {
        this.title = title;
        state = State.NEW;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public State getState() {
        return state;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (id != task.id) return false;
        if (!title.equals(task.title)) return false;
        if (!Objects.equals(description, task.description)) return false;
        if (state != task.state) return false;
        if (!duration.equals(task.duration)) return false;
        return startTime.equals(task.startTime);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + state.hashCode();
        result = 31 * result + duration.hashCode();
        result = 31 * result + startTime.hashCode();
        return result;
    }

    @Override
    public String toString() {
        String result = "Task{" +
                "id=" + id +
                ", title='" + title +
                ", duration=" + duration +
                ", startTime" + startTime + '\'';
        if (description != null) {
            result +=  ", description.length=" + description.length();
        } else  {
            result += ", description.length=" + null;
        }
        return result + ", state='" + state + '\'' + '}';
    }
}
