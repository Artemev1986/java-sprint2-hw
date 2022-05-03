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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return id == task.id &&
                Objects.equals(title, task.title) &&
                Objects.equals(description, task.description) &&
                Objects.equals(state, task.state) &&
                Objects.equals(duration, task.duration) &&
                Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, state, duration, startTime);
    }

    @Override
    public String toString() {
        String result = "Task{" +
                "id=" + id +
                ", title='" + title +
                ", duration=" +
                ", startTime" + startTime + '\'';
        if (description != null) {
            result +=  ", description.length=" + description.length();
        } else  {
            result += ", description.length=" + null;
        }
        return result + ", state='" + state + '\'' + '}';
    }
}
