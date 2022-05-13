package practicum.manager.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import practicum.manager.FileBackedTasksManager;
import practicum.manager.HistoryManager;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    private final KVTaskClient kvTaskClient;

    public HTTPTaskManager(HistoryManager historyManager, String url) {
        super(historyManager, "data.csv");
        kvTaskClient = new KVTaskClient(url);
        loadFromKVServer();
    }

    @Override
    public void save() {
        kvTaskClient.put("tasks", gson.toJson(getTasksList()));
        kvTaskClient.put("subtasks", gson.toJson(getSubtasksList()));
        kvTaskClient.put("epics", gson.toJson(getEpicsList()));
        String str = historyToString(getHistoryManager());
        kvTaskClient.put("history", gson.toJson(str));
    }

    private void loadFromKVServer() {
        Type taskListType = new TypeToken<ArrayList<Task>>(){}.getType();
        Type subtaskListType = new TypeToken<ArrayList<Subtask>>(){}.getType();
        Type epicListType = new TypeToken<ArrayList<Epic>>(){}.getType();

        int idMax = 0;
        List<Task> tasks = gson.fromJson(kvTaskClient.load("tasks"), taskListType);
        if (tasks != null)
        for (Task task: tasks) {
            getTasks().put(task.getId(), task);
            if (task.getId() > idMax) {
                idMax = task.getId();
            }
        }

        List<Subtask> subtasks = gson.fromJson(kvTaskClient.load("subtasks"), subtaskListType);
        if (subtasks != null)
        for (Subtask subtask: subtasks) {
            getSubtasks().put(subtask.getId(), subtask);
            if (subtask.getId() > idMax) {
                idMax = subtask.getId();
            }
        }

        List<Epic> epics = gson.fromJson(kvTaskClient.load("epics"), epicListType);
        if (epics != null)
        for (Epic epic: epics) {
            getEpics().put(epic.getId(), epic);
            if (epic.getId() > idMax) {
                idMax = epic.getId();
            }
        }
        setId(idMax);

        String historyString = gson.fromJson(kvTaskClient.load("history"), String.class);
        if (historyString != null)
        if (!(historyString.isBlank() || historyString.isBlank())) {
            List<Integer> history = historyFromString(historyString);
            for (int id : history) {
                if (getTasks().containsKey(id)) {
                    getHistoryManager().add(getTasks().get(id));
                } else if (getSubtasks().containsKey(id)) {
                    getHistoryManager().add(getSubtasks().get(id));
                } else {
                    getHistoryManager().add(getEpics().get(id));
                }
            }
        }
    }
}
