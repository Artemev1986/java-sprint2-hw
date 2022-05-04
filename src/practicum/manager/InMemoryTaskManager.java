package practicum.manager;

import practicum.task.Epic;
import practicum.task.State;
import practicum.task.Subtask;
import practicum.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private int id;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;
    private final Set<Task> sortedTaskList = new TreeSet<>((task1, task2) -> {
        if (task1.getStartTime().isBefore(task2.getStartTime())) {
            return -1;
        } else if (task1.getStartTime().isAfter(task2.getStartTime())) {
            return 1;
        } else {
            return 0;
        }
    });

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

    private boolean isReserved(Task task, Set<Task> sortedTasks) {
        LocalDateTime startTimeOfNewTask = task.getStartTime();
        LocalDateTime endTimeOfNewTask = task.getEndTime();
        for (Task taskFromList: sortedTasks) {
            if (taskFromList.getId() != task.getId()) {
                LocalDateTime startTimeOfOldTask = taskFromList.getStartTime();
                LocalDateTime endTimeOfOldTask = taskFromList.getEndTime();
                if (!(startTimeOfNewTask.isBefore(startTimeOfOldTask) && endTimeOfNewTask.isBefore(startTimeOfOldTask) ||
                        startTimeOfNewTask.isAfter(endTimeOfOldTask) && endTimeOfNewTask.isAfter(endTimeOfOldTask))) {
                    System.out.println("Time is reserved for other task");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int createTask(Task task) {
        if (isReserved(task, sortedTaskList)) {
            return -1;
        }
        id++;
        task.setId(id);
        tasks.put(id,task);
        sortedTaskList.add(task);
        return id;
    }

    @Override
    public Task getTaskById(int id) {
        if (tasks.get(id) == null) {
            return null;
        }
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        Task tempTask = getTaskById(task.getId());
        sortedTaskList.remove(tempTask);
        if (isReserved(task, sortedTaskList)) {
            sortedTaskList.add(tempTask);
            return;
        }
        tasks.put(task.getId(), task);

        if (sortedTaskList.contains(tempTask)) {
            sortedTaskList.remove(tempTask);

            sortedTaskList.add(task);
        } else {
            sortedTaskList.add(task);
        }
    }

    @Override
    public boolean removeTaskById(int id) {
        if (tasks.containsKey(id)) {
            sortedTaskList.remove(tasks.get(id));
            tasks.remove(id);
            historyManager.remove(id);
            return true;
        } else {
            System.out.println("No task with key: " + id);
            return false;
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
        for (Integer id:tasks.keySet()) {
            historyManager.remove(id);
            sortedTaskList.remove(tasks.get(id));
        }
        tasks.clear();
    }

    @Override
    public int createSubtask(Subtask subtask, Epic epic) {
        if (isReserved(subtask, sortedTaskList)) {
            return -1;
        }
        id++;
        int epicId = epic.getId();
        epic.getSubtaskIds().add(id);
        subtask.setId(id);
        subtask.setEpicId(epicId);
        subtasks.put(id,subtask);
        updateEpic(epic);
        sortedTaskList.add(subtask);
        return id;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        if (subtasks.get(id) == null) {
            return null;
        }
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Task tempSubtask = getSubtaskById(subtask.getId());
        sortedTaskList.remove(tempSubtask);
        if (isReserved(subtask, sortedTaskList)) {
            sortedTaskList.add(tempSubtask);
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        if (sortedTaskList.contains(tempSubtask)) {
            sortedTaskList.remove(tempSubtask);
            sortedTaskList.add(subtask);
        } else {
            sortedTaskList.add(subtask);
        }
        int epicId = subtask.getEpicId();
        updateEpic(getEpicById(epicId));
    }

    @Override
    public boolean removeSubtaskById(int id) {
        if (subtasks.containsKey(id)) {
           int epicId = getSubtaskById(id).getEpicId();
            sortedTaskList.remove(subtasks.get(id));
            subtasks.remove(id);
            Epic epic = getEpicById(epicId);
            epic.getSubtaskIds().remove((Integer) id);
            updateEpic(epic);
            historyManager.remove(id);
            return true;
        } else {
            System.out.println("No subtask with key: " + id);
            return false;
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
        for (Integer id: subtasks.keySet()) {
            sortedTaskList.remove(subtasks.get(id));
            historyManager.remove(id);
        }
        subtasks.clear();
        for (Integer id: epics.keySet()) {
            epics.get(id).getSubtaskIds().clear();
            epics.get(id).setState(State.NEW);
        }
    }

    @Override
    public int createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
    public Epic getEpicById(int id) {
        if (epics.get(id) == null) {
            return null;
        }
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateEpic(Epic epic) {
        boolean isEquals = true;
        State state;
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (!subtasks.isEmpty()) {
            LocalDateTime minStartTime = subtasks.get(subtaskIds.get(0)).getStartTime();
            LocalDateTime maxEndTime = subtasks.get(subtaskIds.get(0)).getEndTime();
            for (int subtaskId : subtaskIds) {
                Subtask subtask = subtasks.get(subtaskId);
                if (minStartTime.isAfter(subtask.getStartTime())) {
                    minStartTime = subtask.getStartTime();
                }
                if (maxEndTime.isBefore(subtask.getEndTime())) {
                    maxEndTime = subtask.getEndTime();
                }
                isEquals &= subtasks.get(subtaskId).getState() == State.NEW;
            }

            epic.setStartTime(minStartTime);
            epic.setDuration(Duration.between(minStartTime, maxEndTime));
            epic.setEndTime(maxEndTime);
        }

        if (isEquals) {
            state = State.NEW;
        } else {
            isEquals = true;
            for (int subtaskId: subtaskIds) {
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
    public boolean removeEpicById(int id) {
        if (epics.containsKey(id)) {
            Epic epic = getEpicById(id);
            for (int subtaskId: epic.getSubtaskIds()) {
                sortedTaskList.remove(subtasks.get(subtaskId));
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
            return true;
        } else {
            System.out.println("No epic with key: " + id);
            return false;
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
        for (Integer id: epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear();
        for (Integer id: subtasks.keySet()) {
            sortedTaskList.remove(subtasks.get(id));
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
    public List<Task> history() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return List.copyOf(sortedTaskList);
    }
}
