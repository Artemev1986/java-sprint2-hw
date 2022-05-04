package practicum.task;

import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String title, String description) {
        super(title, description);
    }

    public Subtask(String title) {
        super(title);
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask)) return false;
        if (!super.equals(o)) return false;

        Subtask subtask = (Subtask) o;

        return getEpicId() == subtask.getEpicId();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getEpicId();
        return result;
    }

    @Override
    public String toString() {
        String result = "Subtask{" +
                "id=" + getId() +
                ", epicId=" + epicId +
                ", title='" + getTitle() + '\'';
        if (getDescription() != null) {
            result +=  ", description.length=" + getDescription().length();
        } else  {
            result += ", description.length=" + null;
        }
        return result + ", state='" + getState() + '\'' + '}';
    }
}
