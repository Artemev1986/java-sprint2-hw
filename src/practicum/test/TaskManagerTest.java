package practicum.test;

import org.junit.jupiter.api.Test;
import practicum.manager.TaskManager;
import practicum.task.Epic;
import practicum.task.State;
import practicum.task.Subtask;
import practicum.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    private T taskManager;

    public T getTaskManager() {
        return taskManager;
    }

    public void setTaskManager(T taskManager) {
        this.taskManager = taskManager;
    }

    @Test
    void createTask() {
        List<Task> tasksEmpty = taskManager.getTasksList();
        assertEquals(0, tasksEmpty.size(), "List isn't empty.");
        Task task = new Task("Test createTask", "Test createTask description");
        task.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task.setDuration(Duration.ofHours(1));

        int taskId = taskManager.createTask(task);

        Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Task not found.");
        assertEquals(task, savedTask, "Tasks don't match.");

        List<Task> tasks = taskManager.getTasksList();
        assertNotNull(tasks, "Tasks don't return.");
        assertEquals(1, tasks.size(), "Invalid number of tasks.");
        assertEquals(task, tasks.get(0), "Tasks don't match.");
    }

    @Test
    void updateTask() {
        Task task = new Task("Test createTask", "Test createTask description");
        task.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task.setDuration(Duration.ofHours(1));
        assertEquals(State.NEW, task.getState(), "Status doesn't match.");
        int taskId = taskManager.createTask(task);
        Task savedTask = taskManager.getTaskById(taskId);
        savedTask.setState(State.DONE);
        taskManager.updateTask(savedTask);
        assertEquals(State.DONE, taskManager.getTaskById(taskId).getState(), "Status doesn't match.");
    }

    @Test
    void getTaskById() {
        Task task = new Task("Test createTask", "Test createTask description");
        task.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task.setDuration(Duration.ofHours(1));
        int taskId = taskManager.createTask(task);

        Task savedTask = taskManager.getTaskById(taskId);

        Task taskNull = taskManager.getTaskById(44);

        assertNull(taskNull, "Task not null.");
        assertNotNull(savedTask, "Task not found.");
        assertEquals(task, savedTask, "Tasks don't match.");
    }

    @Test
    void removeTaskById() {
        Task task = new Task("Test createTask", "Test createTask description");
        task.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task.setDuration(Duration.ofHours(1));

        int taskId = taskManager.createTask(task);

        taskManager.removeTaskById(taskId);
        assertNull(taskManager.getTaskById(taskId), "Task found.");

        boolean isRemoved = taskManager.removeTaskById(44);
        assertFalse(isRemoved, "The task is missing.");
    }

    @Test
    void removeAllTasks() {
        Task task = new Task("Test createTask", "Test createTask description");
        task.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);
        taskManager.removeAllTasks();

        assertTrue(taskManager.getTasksList().isEmpty(), "List isn't empty.");
    }

    @Test
    void createSubtask() {
        List<Subtask> subtasksEmpty = taskManager.getSubtasksList();
        assertEquals(0, subtasksEmpty.size(), "List isn't empty.");

        Epic epic = new Epic("Epic", "Epic task");
        Subtask subtask = new Subtask("Test createSubtask", "Test createSubtask description");
        subtask.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask.setDuration(Duration.ofHours(1));
        taskManager.createEpic(epic);
        int subtaskId = taskManager.createSubtask(subtask, epic);

        Subtask savedSubTask = taskManager.getSubtaskById(subtaskId);

        assertNotNull(savedSubTask, "Subtask not found.");
        assertEquals(subtask, savedSubTask, "Subtasks don't match.");

        List<Subtask> subtasks = taskManager.getSubtasksList();

        int epicId = savedSubTask.getEpicId();
        assertEquals(epic, taskManager.getEpicById(epicId), "Subtasks don't match.");

        assertNotNull(subtasks, "Subtasks don't return.");
        assertEquals(1, subtasks.size(), "Invalid number of subtasks.");
        assertEquals(subtask, subtasks.get(0), "Subtasks don't match.");
    }

    @Test
    void getSubtaskById() {
        Epic epic = new Epic("Epic", "Epic task");
        Subtask subtask = new Subtask("Test createSubtask", "Test createSubtask description");
        subtask.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask.setDuration(Duration.ofHours(1));
        taskManager.createEpic(epic);

        int subtaskId = taskManager.createSubtask(subtask, epic);

        Subtask savedSubTask = taskManager.getSubtaskById(subtaskId);

        Subtask SubtaskNull = taskManager.getSubtaskById(44);

        assertNull(SubtaskNull, "Subtask not null.");

        assertNotNull(savedSubTask, "Task not found.");
        assertEquals(subtask, savedSubTask, "Tasks don't match.");
    }

    @Test
    void updateSubtask() {
        Epic epic = new Epic("Epic", "Epic task");
        Subtask subtask = new Subtask("Test updateSubtask", "Test updateSubtask description");
        subtask.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask.setDuration(Duration.ofHours(1));
        assertEquals(State.NEW, subtask.getState(), "Status doesn't match.");

        taskManager.createEpic(epic);
        int subtaskId = taskManager.createSubtask(subtask, epic);
        Subtask savedSubtask = taskManager.getSubtaskById(subtaskId);
        savedSubtask.setState(State.DONE);
        taskManager.updateSubtask(savedSubtask);

        assertEquals(State.DONE, taskManager.getSubtaskById(subtaskId).getState(), "Status doesn't match.");
    }

    @Test
    void removeSubtaskById() {
        Epic epic = new Epic("Epic", "Epic task");
        Subtask subtask = new Subtask("Test removeSubtask", "Test removeSubtask description");
        subtask.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask.setDuration(Duration.ofHours(1));
        taskManager.createEpic(epic);
        int subtaskId = taskManager.createSubtask(subtask, epic);

        taskManager.removeSubtaskById(subtaskId);
        assertNull(taskManager.getSubtaskById(subtaskId), "Subtask found.");

        boolean isRemoved = taskManager.removeSubtaskById(44);
        assertFalse(isRemoved, "The subtask is missing.");
    }

    @Test
    void removeAllSubtasks() {
        Epic epic = new Epic("Epic", "Epic task");
        Subtask subtask1 = new Subtask("Test removeAllSubtasks", "Test removeAllSubtasks description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Test removeAllSubtasks", "Test removeAllSubtasks description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        subtask2.setDuration(Duration.ofHours(1));
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask1, epic);
        taskManager.createSubtask(subtask2, epic);

        taskManager.removeAllSubtasks();

        assertTrue(taskManager.getSubtasksList().isEmpty(), "List isn't empty.");
    }

    @Test
    void createEpic() {
        List<Epic> epicsEmpty = taskManager.getEpicsList();
        assertEquals(0, epicsEmpty.size(), "List isn't empty.");

        Epic epic = new Epic("Test createEpic", "Test createEpic description");

        int EpicId = taskManager.createEpic(epic);

        Epic savedEpic = taskManager.getEpicById(EpicId);

        assertNotNull(savedEpic, "Epic not found.");
        assertEquals(epic, savedEpic, "Epics don't match.");

        List<Epic> epics = taskManager.getEpicsList();

        assertNotNull(epics, "Epics don't return.");
        assertEquals(1, epics.size(), "Invalid number of epics.");
        assertEquals(epic, epics.get(0), "Epics don't match.");
    }

    @Test
    void epicStatusNoSubtasks() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        taskManager.createEpic(epic);

        assertEquals(epic.getState(), State.NEW, "Status not NEW.");
    }

    @Test
    void epicStatusSubtasksWithStatusNEW() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        subtask2.setDuration(Duration.ofHours(1));

        taskManager.createSubtask(subtask1, epic);
        taskManager.createSubtask(subtask2, epic);

        assertEquals(epic.getState(), State.NEW, "Status not NEW.");
    }

    @Test
    void epicStatusSubtasksWithStatusDONE() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        subtask2.setDuration(Duration.ofHours(1));

        taskManager.createSubtask(subtask1, epic);
        taskManager.createSubtask(subtask2, epic);

        subtask1.setState(State.DONE);
        subtask2.setState(State.DONE);

        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        assertEquals(epic.getState(), State.DONE, "Status not NEW.");
    }

    @Test
    void epicStatusSubtasksWithStatusNEWandDONE() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        subtask2.setDuration(Duration.ofHours(1));

        taskManager.createSubtask(subtask1, epic);
        taskManager.createSubtask(subtask2, epic);

        subtask1.setState(State.DONE);

        taskManager.updateSubtask(subtask1);

        assertEquals(epic.getState(), State.IN_PROGRESS, "Status not NEW.");
    }

    @Test
    void epicStatusSubtasksWithStatusIN_PROGRESS() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,13,12));
        subtask2.setDuration(Duration.ofHours(1));

        taskManager.createSubtask(subtask1, epic);
        taskManager.createSubtask(subtask2, epic);

        subtask1.setState(State.IN_PROGRESS);
        subtask2.setState(State.IN_PROGRESS);

        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);

        assertEquals(State.IN_PROGRESS, epic.getState(), "Status not IN_PROGRESS.");
    }

    @Test
    void getEpicById() {
        Epic epic = new Epic("Test getEpicById", "Test getEpicById description");

        int epicId = taskManager.createEpic(epic);

        Epic savedEpic = taskManager.getEpicById(epicId);

        assertNotNull(savedEpic, "Task not found.");
        assertEquals(epic, savedEpic, "Tasks don't match.");
    }

    @Test
    void removeEpicById() {
        Epic epic = new Epic("Test removeEpicById", "Test removeEpicById description");
        int epicId = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        subtask2.setDuration(Duration.ofHours(1));

        int subtaskIs1 = taskManager.createSubtask(subtask1, epic);
        int subtaskIs2 = taskManager.createSubtask(subtask2, epic);

        taskManager.removeEpicById(epicId);

        assertNull(taskManager.getSubtaskById(subtaskIs1), "Subtask1 found.");
        assertNull(taskManager.getSubtaskById(subtaskIs2), "Subtask2 found.");
        assertNull(taskManager.getEpicById(epicId), "epic found.");

        boolean isRemoved = taskManager.removeEpicById(44);
        assertFalse(isRemoved, "The epic is missing.");
    }

    @Test
    void removeAllEpics() {
        Epic epic1 = new Epic("Test removeEpicById", "Test removeAllEpics description");
        int epicId1 = taskManager.createEpic(epic1);
        Epic epic2 = new Epic("Test removeEpicById", "Test removeAllEpics description");
        int epicId2 = taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,13,12));
        subtask2.setDuration(Duration.ofHours(1));

        int subtaskIs1 = taskManager.createSubtask(subtask1, epic1);
        int subtaskIs2 = taskManager.createSubtask(subtask2, epic2);

        taskManager.removeAllEpics();

        assertNull(taskManager.getSubtaskById(subtaskIs1), "Subtask1 found.");
        assertNull(taskManager.getSubtaskById(subtaskIs2), "Subtask2 found.");
        assertNull(taskManager.getEpicById(epicId1), "epic1 found.");
        assertNull(taskManager.getEpicById(epicId2), "epic2 found.");
    }

    @Test
    void history() {
        Epic epic1 = new Epic("Epic1", "Epic1 description");
        int epicId1 = taskManager.createEpic(epic1);
        Epic epic2 = new Epic("Epic2", "Epic2 description");
        int epicId2 = taskManager.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,13,12));
        subtask2.setDuration(Duration.ofHours(1));

        int subtaskId1 = taskManager.createSubtask(subtask1, epic1);
        int subtaskId2 = taskManager.createSubtask(subtask2, epic2);

        taskManager.getSubtaskById(subtaskId1);
        taskManager.getSubtaskById(subtaskId2);
        taskManager.getEpicById(epicId1);
        taskManager.getEpicById(epicId2);
        taskManager.history();

        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(subtask1);
        expectedTaskList.add(subtask2);
        expectedTaskList.add(epic1);
        expectedTaskList.add(epic2);

        assertEquals(taskManager.history(), expectedTaskList, "History list isn't actual.");

        taskManager.getSubtaskById(subtaskId1);
        expectedTaskList.remove(subtask1);
        expectedTaskList.add(subtask1);

        assertEquals(taskManager.history(), expectedTaskList, "History list isn't actual.");

        taskManager.removeEpicById(epicId1);
        expectedTaskList.remove(subtask1);
        expectedTaskList.remove(epic1);

        assertEquals(taskManager.history(), expectedTaskList, "History list isn't actual.");
    }

    @Test
    void getPrioritizedTasks() {
        Task task = new Task("task1", "task1 description");
        task.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task.setDuration(Duration.ofHours(1));
        taskManager.createTask(task);
        List<Task> expectedTaskList = new ArrayList<>();
        expectedTaskList.add(task);
        assertEquals(expectedTaskList, taskManager.getPrioritizedTasks(), "Task list isn't actual.");

        Epic epic1 = new Epic("Epic1", "Epic1 description");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,1,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,8,13,12));
        subtask2.setDuration(Duration.ofHours(1));

        taskManager.createSubtask(subtask1, epic1);
        taskManager.createSubtask(subtask2, epic1);

        expectedTaskList.clear();
        expectedTaskList.add(subtask1);
        expectedTaskList.add(task);
        expectedTaskList.add(subtask2);

        assertEquals(expectedTaskList, taskManager.getPrioritizedTasks(), "Task list isn't actual.");

        subtask2.setStartTime(LocalDateTime.of(2022,5,1,13,12));
        taskManager.updateSubtask(subtask2);
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,13,12));
        taskManager.updateSubtask(subtask1);
        task.setStartTime(LocalDateTime.of(2022,5,6,10,12));

        taskManager.updateTask(task);



        expectedTaskList.clear();
        expectedTaskList.add(subtask2);
        expectedTaskList.add(subtask1);
        expectedTaskList.add(task);

        assertEquals(expectedTaskList, taskManager.getPrioritizedTasks(), "Task list isn't actual.");
    }
}
