package practicum.manager.Network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import practicum.manager.FileBackedTasksManager;
import practicum.manager.HistoryManager;
import practicum.manager.util.Managers;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    static KVTaskClient kvTaskClient;

    public HTTPTaskManager(HistoryManager historyManager, String url) {
        super(historyManager, "data.csv");
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    public void save() {
        kvTaskClient.put("tasks", gson.toJson(getTasksList()));
        kvTaskClient.put("subtasks", gson.toJson(getSubtasksList()));
        kvTaskClient.put("epics", gson.toJson(getEpicsList()));
        String str = historyToString(getHistoryManager());
        kvTaskClient.put("history", gson.toJson(str));
    }

    public static HTTPTaskManager loadFromKVServer(String url) {
        Type taskListType = new TypeToken<ArrayList<Task>>(){}.getType();
        Type subtaskListType = new TypeToken<ArrayList<Subtask>>(){}.getType();
        Type epicListType = new TypeToken<ArrayList<Epic>>(){}.getType();

        HTTPTaskManager load = new HTTPTaskManager(Managers.getDefaultHistory(),url);

        int idMax = 0;
        List<Task> tasks = gson.fromJson(kvTaskClient.load("tasks"), taskListType);
        for (Task task: tasks) {
            load.getTasks().put(task.getId(), task);
            if (task.getId() > idMax) {
                idMax = task.getId();
            }
        }

        List<Subtask> subtasks = gson.fromJson(kvTaskClient.load("subtasks"), subtaskListType);
        for (Subtask subtask: subtasks) {
            load.getSubtasks().put(subtask.getId(), subtask);
            if (subtask.getId() > idMax) {
                idMax = subtask.getId();
            }
        }

        List<Epic> epics = gson.fromJson(kvTaskClient.load("epics"), epicListType);
        for (Epic epic: epics) {
            load.getEpics().put(epic.getId(), epic);
            if (epic.getId() > idMax) {
                idMax = epic.getId();
            }
        }
        load.setId(idMax);

        String historyString = gson.fromJson(kvTaskClient.load("history"), String.class);

        if (!(historyString.isBlank() || historyString.isBlank())) {
            List<Integer> history = historyFromString(historyString);
            for (int id : history) {
                if (load.getTasks().containsKey(id)) {
                    load.getHistoryManager().add(load.getTasks().get(id));
                } else if (load.getSubtasks().containsKey(id)) {
                    load.getHistoryManager().add(load.getSubtasks().get(id));
                } else {
                    load.getHistoryManager().add(load.getEpics().get(id));
                }
            }
        }
        return load;
    }
}
