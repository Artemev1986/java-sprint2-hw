package practicum.manager;

import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int createTask(Task task);

    Task getTaskById(int id);

    void updateTask(Task task);

    boolean removeTaskById(int id);

    ArrayList<Task> getTasksList();

    void removeAllTasks();

    int createSubtask(Subtask subtask, Epic epic);

    Subtask getSubtaskById(int id);

    void updateSubtask(Subtask subtask);

    boolean removeSubtaskById(int id);

    ArrayList<Subtask> getSubtasksList();

    void removeAllSubtasks();

    int createEpic(Epic epic);

    Epic getEpicById(int id);

    void updateEpic(Epic epic);

    boolean removeEpicById(int id);

    ArrayList<Epic> getEpicsList();

    void removeAllEpics();

    ArrayList<Subtask> getSubtasksOfEpicList(Epic epic);

    List<Task> history();

    List<Task> getPrioritizedTasks();
}
