package practicum.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds;
    private LocalDateTime endTime;

    public Epic(String title, String description) {
        super(title, description);
        this.setStartTime(LocalDateTime.now());
        this.setDuration(Duration.ZERO);
        this.setEndTime(LocalDateTime.now());
        subtaskIds = new ArrayList<>();
    }

    public Epic(String title) {
        super(title);
        this.setStartTime(LocalDateTime.now());
        this.setDuration(Duration.ZERO);
        this.setEndTime(LocalDateTime.now());
        subtaskIds = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
        return Objects.equals(subtaskIds, epic.subtaskIds) &&
                Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskIds, endTime);
    }

    @Override
    public String toString() {

        String result = "Epic{" + "id=" + getId() + ", title='" + getTitle() + '\'';
        if (getDescription() != null) {
            result +=  ", description.length=" + getDescription().length();
        } else  {
            result += ", description.length=" + null;
        }
        return result + ", state='" + getState() + '\'' + ", subtaskIds = " + subtaskIds + '}';
    }
}
