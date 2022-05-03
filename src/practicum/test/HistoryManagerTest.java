package practicum.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import practicum.manager.HistoryManager;
import practicum.manager.util.Managers;
import practicum.task.Task;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    HistoryManager historyManager;

    @BeforeEach
    void setManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    void add() {
        Task task = new Task("Task", "Task description");
        task.setId(1);
        historyManager.add(task);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(1, history.size(), "History size not equals \"1\".");
        historyManager.remove(1);
        history = historyManager.getHistory();
        assertNotNull(history, "History is null.");
        assertEquals(0, history.size(), "History size not equals \"0\".");
    }

    @Test
    void removeInStart() {
        Task task1 = new Task("Task1", "Task1 description");
        Task task2 = new Task("Task2", "Task2 description");
        Task task3 = new Task("Task3", "Task3 description");
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(1);
        assertEquals(task2, historyManager.getHistory().get(0), "Task isn't Task2.");
        assertEquals(task3, historyManager.getHistory().get(1), "Task isn't Task3.");
    }

    @Test
    void removeFromMiddle() {
        Task task1 = new Task("Task1", "Task1 description");
        Task task2 = new Task("Task2", "Task2 description");
        Task task3 = new Task("Task3", "Task3 description");
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(2);
        assertEquals(task1, historyManager.getHistory().get(0), "Task isn't Task1.");
        assertEquals(task3, historyManager.getHistory().get(1), "Task isn't Task3.");
    }

    @Test
    void removeFromEnd() {
        Task task1 = new Task("Task1", "Task1 description");
        Task task2 = new Task("Task2", "Task2 description");
        Task task3 = new Task("Task3", "Task3 description");
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(3);
        assertEquals(task1, historyManager.getHistory().get(0), "Task isn't Task1.");
        assertEquals(task2, historyManager.getHistory().get(1), "Task isn't Task2.");
    }
}