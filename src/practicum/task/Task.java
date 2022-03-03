package practicum.task;

import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private State state;

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

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setState(State state) {
        this.state = state;
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
                Objects.equals(state, task.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, state);
    }

    @Override
    public String toString() {
        String result = "practicum.task.Task{" +
                "id=" + id +
                ", title='" + title + '\'';
        if (description != null) {
            result +=  ", description.length=" + description.length();
        } else  {
            result += ", description.length=" + null;
        }
        return result + ", state='" + state + '\'' + '}';
    }
}
