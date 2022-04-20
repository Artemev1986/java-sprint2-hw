package practicum.manager;

import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void createTask(Task task) throws ManagerSaveException;

    Task getTaskById(int id) throws ManagerSaveException;

    void updateTask(Task task) throws ManagerSaveException;

    void removeTaskById(int id) throws ManagerSaveException;

    ArrayList<Task> getTasksList();

    void removeAllTasks() throws ManagerSaveException;

    void createSubtask(Subtask subtask, Epic epic) throws ManagerSaveException;

    Subtask getSubtaskById(int id) throws ManagerSaveException;

    void updateSubtask(Subtask subtask) throws ManagerSaveException;

    void removeSubtaskById(int id) throws ManagerSaveException;

    ArrayList<Subtask> getSubtasksList();

    void removeAllSubtasks() throws ManagerSaveException;

    void createEpic(Epic epic) throws ManagerSaveException;

    Epic getEpicById(int id) throws ManagerSaveException;

    void updateEpic(Epic epic) throws ManagerSaveException;

    void removeEpicById(int id) throws ManagerSaveException;

    ArrayList<Epic> getEpicsList();

    void removeAllEpics() throws ManagerSaveException;

    ArrayList<Subtask> getSubtasksOfEpicList(Epic epic);

    List<Task> history() throws ManagerSaveException;

}
