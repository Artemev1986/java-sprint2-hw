import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> listOfEndTasks;

    public InMemoryHistoryManager() {
        listOfEndTasks = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (listOfEndTasks.size() >= 10) {
            listOfEndTasks.remove(0);
        }
        listOfEndTasks.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return listOfEndTasks;
    }
}
