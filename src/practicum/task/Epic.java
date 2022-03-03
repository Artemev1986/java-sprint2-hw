package practicum.task;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds;

    public Epic(String title, String description) {
        super(title, description);
        subtaskIds = new ArrayList<>();
    }

    public Epic(String title) {
        super(title);
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        Epic epic = (Epic) o;
        return Objects.equals(subtaskIds, epic.subtaskIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds);
    }

    @Override
    public String toString() {

        String result = "practicum.task.Epic{" + "id=" + getId() + ", title='" + getTitle() + '\'';
        if (getDescription() != null) {
            result +=  ", description.length=" + getDescription().length();
        } else  {
            result += ", description.length=" + null;
        }
        return result + ", state='" + getState() + '\'' + ", subtaskIds = " + subtaskIds + '}';
    }
}
