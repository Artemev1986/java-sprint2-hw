import java.util.Objects;

public class Task {
    private int id;
    private String title;
    private String description;
    private String state;

    public Task(String title, String description) {
        this.id = 0;
        this.title = title;
        this.description = description;
        state = "NEW";
    }

    public Task(String title) {
        this.id = 0;
        this.title = title;
        state = "NEW";
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

    public String getState() {
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

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
        String result = "Task{" +
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
