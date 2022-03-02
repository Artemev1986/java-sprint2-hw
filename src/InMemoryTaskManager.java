import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;
    private final List<Task> listOfEndTasks;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
        listOfEndTasks = new ArrayList<>();
    }

    @Override
    public void createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id,task);
    }

    @Override
    public Task getTaskById(int id) {
        if (listOfEndTasks.size() >= 10) {
            listOfEndTasks.remove(0);
        }
        listOfEndTasks.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
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
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void createSubtask(Subtask subtask, Epic epic) {
        id++;
        int epicId = epic.getId();
        epic.getSubtaskIds().add(id);
        subtask.setId(id);
        subtask.setEpicId(epicId);
        subtasks.put(id,subtask);
        updateEpic(epic);
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (listOfEndTasks.size() >= 10) {
            listOfEndTasks.remove(0);
        }
        listOfEndTasks.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        int epicId = subtask.getEpicId();
        updateEpic(getEpicById(epicId));
    }

    @Override
    public void removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
           int epicId = getSubtaskById(id).getEpicId();
            subtasks.remove(id);
            Epic epic = getEpicById(epicId);
            epic.getSubtaskIds().remove((Integer) id);
            updateEpic(epic);
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
    public void removeAllSubtasks() {
        subtasks.clear();
        for (int id: epics.keySet()) {
            epics.get(id).getSubtaskIds().clear();
            epics.get(id).setState(State.NEW);
        }
    }

    @Override
    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public Epic getEpicById(int id) {
        if (listOfEndTasks.size() >= 10) {
            listOfEndTasks.remove(0);
        }
        listOfEndTasks.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        boolean isEquals = true;
        State state;
        for (int subtaskId: epic.getSubtaskIds()) {
            isEquals &= subtasks.get(subtaskId).getState().equals(State.NEW);
        }
        if (isEquals) {
            state = State.NEW;
        } else {
            isEquals = true;
            for (int subtaskId: epic.getSubtaskIds()) {
                isEquals &= subtasks.get(subtaskId).getState().equals(State.DONE);
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
    public void removeEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = getEpicById(id);
            for (int subtaskId: epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(id);
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
    public void removeAllEpics() {
        epics.clear();
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

    public List<Task> history() {
        return listOfEndTasks;
    }
}
