package practicum.manager.util;

import practicum.manager.HistoryManager;
import practicum.manager.InMemoryHistoryManager;
import practicum.manager.InMemoryTaskManager;
import practicum.manager.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
