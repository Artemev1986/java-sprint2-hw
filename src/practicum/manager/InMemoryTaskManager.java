package practicum.manager;

import practicum.task.Epic;
import practicum.task.State;
import practicum.task.Subtask;
import practicum.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        id++;
        task.setId(id);
        tasks.put(id,task);
    }

    @Override
    public Task getTaskById(int id) throws ManagerSaveException {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        tasks.put(task.getId(), task);
    }

    @Override
    public void removeTaskById(int id) throws ManagerSaveException {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("No task with key: " + id);
        }
    }

    @Override
    public ArrayList<Task> getTasksList() {
        ArrayList<Task> list = new ArrayList<>();
        for (int id: tasks.keySet()) {
            list.add(tasks.get(id));
        }
        return list;
    }

    @Override
    public void removeAllTasks() throws ManagerSaveException {
        for (Integer id:tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    @Override
    public void createSubtask(Subtask subtask, Epic epic) throws ManagerSaveException {
        id++;
        int epicId = epic.getId();
        epic.getSubtaskIds().add(id);
        subtask.setId(id);
        subtask.setEpicId(epicId);
        subtasks.put(id,subtask);
        updateEpic(epic);
    }

    @Override
    public Subtask getSubtaskById(int id) throws ManagerSaveException {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        subtasks.put(subtask.getId(), subtask);
        int epicId = subtask.getEpicId();
        updateEpic(getEpicById(epicId));
    }

    @Override
    public void removeSubtaskById(int id) throws ManagerSaveException {
        if (subtasks.containsKey(id)) {
           int epicId = getSubtaskById(id).getEpicId();
            subtasks.remove(id);
            Epic epic = getEpicById(epicId);
            epic.getSubtaskIds().remove((Integer) id);
            updateEpic(epic);
            historyManager.remove(id);
        } else {
            System.out.println("No subtask with key: " + id);
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksList() {
        ArrayList<Subtask> list = new ArrayList<>();
        for (int id: subtasks.keySet()) {
            list.add(subtasks.get(id));
        }
        return list;
    }

    @Override
    public void removeAllSubtasks() throws ManagerSaveException {
        for (Integer id: subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
        for (Integer id: epics.keySet()) {
            epics.get(id).getSubtaskIds().clear();
            epics.get(id).setState(State.NEW);
        }
    }

    @Override
    public void createEpic(Epic epic) throws ManagerSaveException {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        boolean isEquals = true;
        State state;
        for (int subtaskId: epic.getSubtaskIds()) {
            isEquals &= subtasks.get(subtaskId).getState() == State.NEW;
        }
        if (isEquals) {
            state = State.NEW;
        } else {
            isEquals = true;
            for (int subtaskId: epic.getSubtaskIds()) {
                isEquals &= subtasks.get(subtaskId).getState() == State.DONE;
            }
            if (isEquals) {
                state = State.DONE;
            } else {
                state = State.IN_PROGRESS;
            }
        }
        epic.setState(state);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void removeEpicById(int id) throws ManagerSaveException {
        if (epics.containsKey(id)) {
            Epic epic = getEpicById(id);
            for (int subtaskId: epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        } else {
            System.out.println("No epic with key: " + id);
        }
    }

    @Override
    public ArrayList<Epic> getEpicsList() {
       ArrayList<Epic> list = new ArrayList<>();
        for (int id: epics.keySet()) {
            list.add(epics.get(id));
        }
        return list;
    }

    @Override
    public void removeAllEpics() throws ManagerSaveException {
        for (Integer id: epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        for (Integer id: subtasks.keySet()) {
            historyManager.remove(id);
        }
        subtasks.clear();
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpicList(Epic epic) {
        ArrayList<Subtask> list = new ArrayList<>();
        for (int id: epic.getSubtaskIds()) {
            list.add(subtasks.get(id));
        }
        return list;
    }

    @Override
    public List<Task> history() throws ManagerSaveException {
        return historyManager.getHistory();
    }
}
