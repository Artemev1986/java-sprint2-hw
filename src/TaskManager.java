import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public void createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id,task);
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        } else {
            System.out.println("No task with key: " + id);
        }
    }

    public ArrayList<Task> getTasksList() {
        ArrayList<Task> list = new ArrayList<>();
        for (int id: tasks.keySet()) {
            list.add(tasks.get(id));
        }
        return list;
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void createSubtask(Subtask subtask, Epic epic) {
        id++;
        int epicId = epic.getId();
        epic.getSubtaskIds().add(id);
        subtask.setId(id);
        subtask.setEpicId(epicId);
        subtasks.put(id,subtask);
        updateEpic(epic);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        int epicId = subtask.getEpicId();
        updateEpic(getEpicById(epicId));
    }

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

    public ArrayList<Subtask> getSubtasksList() {
        ArrayList<Subtask> list = new ArrayList<>();
        for (int id: subtasks.keySet()) {
            list.add(subtasks.get(id));
        }
        return list;
    }

    public void removeAllSubtasks() {
        subtasks.clear();
        for (int id: epics.keySet()) {
            epics.get(id).getSubtaskIds().clear();
            epics.get(id).setState("NEW");
        }
    }

    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void updateEpic(Epic epic) {
        boolean isEquals = true;
        String state;
        for (int subtaskId: epic.getSubtaskIds()) {
            isEquals &= subtasks.get(subtaskId).getState().equals("NEW");
        }
        if (isEquals) {
            state = "NEW";
        } else {
            isEquals = true;
            for (int subtaskId: epic.getSubtaskIds()) {
                isEquals &= subtasks.get(subtaskId).getState().equals("DONE");
            }
            if (isEquals) {
                state = "DONE";
            } else {
                state = "IN_PROGRESS";
            }
        }
        epic.setState(state);
        epics.put(epic.getId(), epic);
    }

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

    public ArrayList<Epic> getEpicsList() {
       ArrayList<Epic> list = new ArrayList<>();
        for (int id: epics.keySet()) {
            list.add(epics.get(id));
        }
        return list;
    }

    public void removeAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public ArrayList<Subtask> getSubtasksOfEpicList(Epic epic) {
        ArrayList<Subtask> list = new ArrayList<>();
        for (int id: epic.getSubtaskIds()) {
            list.add(subtasks.get(id));
        }
        return list;
    }
}
