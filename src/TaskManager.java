import java.util.ArrayList;

public interface TaskManager {
    void createTask(Task task);

    Task getTaskById(int id);

    void updateTask(Task task);

    void removeTaskById(int id);

    ArrayList<Task> getTasksList();

    void removeAllTasks();

    void createSubtask(Subtask subtask, Epic epic);

    Subtask getSubtaskById(int id);

    void updateSubtask(Subtask subtask);

    void removeSubtaskById(int id);

    ArrayList<Subtask> getSubtasksList();

    void removeAllSubtasks();

    void createEpic(Epic epic);

    Epic getEpicById(int id);

    void updateEpic(Epic epic);

    void removeEpicById(int id);

    ArrayList<Epic> getEpicsList();

    void removeAllEpics();

    ArrayList<Subtask> getSubtasksOfEpicList(Epic epic);
}
