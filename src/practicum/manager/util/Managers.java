package practicum.manager.util;

import practicum.manager.*;
import practicum.manager.network.HTTPTaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new HTTPTaskManager(getDefaultHistory(), "http://localhost:8078");
    }

    public static TaskManager getFileBackedTasksManager()
        {
            return new FileBackedTasksManager(getDefaultHistory(), "data.csv");
        }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
