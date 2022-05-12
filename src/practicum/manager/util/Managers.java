package practicum.manager.util;

import practicum.manager.*;
import practicum.manager.Network.HTTPTaskManager;

public class Managers {

    public static HTTPTaskManager getDefault() {
        return new HTTPTaskManager(getDefaultHistory(), "http://localhost:8078");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
