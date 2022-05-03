package practicum.test;

import org.junit.jupiter.api.Test;
import practicum.manager.TaskManager;
import practicum.task.Epic;
import practicum.task.State;
import practicum.task.Subtask;
import practicum.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    private final T t;

    public TaskManagerTest(T taskManager) {
        this.t = taskManager;
    }

    public T getT() {
        return t;
    }

    @Test
    void createTask() {
        List<Task> tasksEmpty = t.getTasksList();
        assertEquals(0, tasksEmpty.size(), "List isn't empty.");
        Task task = new Task("Test createTask", "Test createTask description");
        task.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task.setDuration(Duration.ofHours(1));

        int taskId = t.createTask(task);

        Task savedTask = t.getTaskById(taskId);

        assertNotNull(savedTask, "Task not found.");
        assertEquals(task, savedTask, "Tasks don't match.");

        List<Task> tasks = t.getTasksList();
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
        int taskId = t.createTask(task);
        Task savedTask = t.getTaskById(taskId);
        savedTask.setState(State.DONE);
        t.updateTask(savedTask);
        assertEquals(State.DONE, t.getTaskById(taskId).getState(), "Status doesn't match.");
    }

    @Test
    void getTaskById() {
        Task task = new Task("Test createTask", "Test createTask description");
        task.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task.setDuration(Duration.ofHours(1));
        int taskId = t.createTask(task);

        Task savedTask = t.getTaskById(taskId);

        Task TaskNull = t.getTaskById(44);

        assertNull(TaskNull, "Task not null.");
        assertNotNull(savedTask, "Task not found.");
        assertEquals(task, savedTask, "Tasks don't match.");
    }

    @Test
    void removeTaskById() {
        Task task = new Task("Test createTask", "Test createTask description");
        task.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task.setDuration(Duration.ofHours(1));

        int taskId = t.createTask(task);

        t.removeTaskById(taskId);

        assertNull(t.getTaskById(taskId), "Task found.");
    }

    @Test
    void removeAllTasks() {
        Task task = new Task("Test createTask", "Test createTask description");
        task.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task.setDuration(Duration.ofHours(1));
        t.createTask(task);
        t.removeAllTasks();

        assertTrue(t.getTasksList().isEmpty(), "List isn't empty.");
    }

    @Test
    void createSubtask() {
        List<Subtask> subtasksEmpty = t.getSubtasksList();
        assertEquals(0, subtasksEmpty.size(), "List isn't empty.");

        Epic epic = new Epic("Epic", "Epic task");
        Subtask subtask = new Subtask("Test createSubtask", "Test createSubtask description");
        subtask.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask.setDuration(Duration.ofHours(1));
        t.createEpic(epic);
        int subtaskId = t.createSubtask(subtask, epic);

        Subtask savedSubTask = t.getSubtaskById(subtaskId);

        assertNotNull(savedSubTask, "Subtask not found.");
        assertEquals(subtask, savedSubTask, "Subtasks don't match.");

        List<Subtask> subtasks = t.getSubtasksList();

        int epicId = savedSubTask.getEpicId();
        assertEquals(epic, t.getEpicById(epicId), "Subtasks don't match.");

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
        t.createEpic(epic);

        int subtaskId = t.createSubtask(subtask, epic);

        Subtask savedSubTask = t.getSubtaskById(subtaskId);

        Subtask SubtaskNull = t.getSubtaskById(44);

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

        t.createEpic(epic);
        int subtaskId = t.createSubtask(subtask, epic);
        Subtask savedSubtask = t.getSubtaskById(subtaskId);
        savedSubtask.setState(State.DONE);
        t.updateSubtask(savedSubtask);

        assertEquals(State.DONE, t.getSubtaskById(subtaskId).getState(), "Status doesn't match.");
    }

    @Test
    void removeSubtaskById() {
        Epic epic = new Epic("Epic", "Epic task");
        Subtask subtask = new Subtask("Test removeSubtask", "Test removeSubtask description");
        subtask.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask.setDuration(Duration.ofHours(1));
        t.createEpic(epic);
        int subtaskId = t.createSubtask(subtask, epic);

        t.removeSubtaskById(subtaskId);

        assertNull(t.getSubtaskById(subtaskId), "Subtask found.");
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
        t.createEpic(epic);
        t.createSubtask(subtask1, epic);
        t.createSubtask(subtask2, epic);

        t.removeAllSubtasks();

        assertTrue(t.getSubtasksList().isEmpty(), "List isn't empty.");
    }

    @Test
    void createEpic() {
        List<Epic> epicsEmpty = t.getEpicsList();
        assertEquals(0, epicsEmpty.size(), "List isn't empty.");

        Epic epic = new Epic("Test createEpic", "Test createEpic description");

        int EpicId = t.createEpic(epic);

        Epic savedEpic = t.getEpicById(EpicId);

        assertNotNull(savedEpic, "Epic not found.");
        assertEquals(epic, savedEpic, "Epics don't match.");

        List<Epic> epics = t.getEpicsList();

        assertNotNull(epics, "Epics don't return.");
        assertEquals(1, epics.size(), "Invalid number of epics.");
        assertEquals(epic, epics.get(0), "Epics don't match.");
    }

    @Test
    void epicStatusNoSubtasks() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        t.createEpic(epic);

        assertEquals(epic.getState(), State.NEW, "Status not NEW.");
    }

    @Test
    void epicStatusSubtasksWithStatusNEW() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        t.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        subtask2.setDuration(Duration.ofHours(1));

        t.createSubtask(subtask1, epic);
        t.createSubtask(subtask2, epic);

        assertEquals(epic.getState(), State.NEW, "Status not NEW.");
    }

    @Test
    void epicStatusSubtasksWithStatusDONE() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        t.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        subtask2.setDuration(Duration.ofHours(1));

        t.createSubtask(subtask1, epic);
        t.createSubtask(subtask2, epic);

        subtask1.setState(State.DONE);
        subtask2.setState(State.DONE);

        t.updateSubtask(subtask1);
        t.updateSubtask(subtask2);

        assertEquals(epic.getState(), State.DONE, "Status not NEW.");
    }

    @Test
    void epicStatusSubtasksWithStatusNEWandDONE() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        t.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        subtask2.setDuration(Duration.ofHours(1));

        t.createSubtask(subtask1, epic);
        t.createSubtask(subtask2, epic);

        subtask1.setState(State.DONE);

        t.updateSubtask(subtask1);

        assertEquals(epic.getState(), State.IN_PROGRESS, "Status not NEW.");
    }

    @Test
    void epicStatusSubtasksWithStatusIN_PROGRESS() {
        Epic epic = new Epic("Test createEpic", "Test createEpic description");
        t.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,13,12));
        subtask2.setDuration(Duration.ofHours(1));

        t.createSubtask(subtask1, epic);
        t.createSubtask(subtask2, epic);

        subtask1.setState(State.IN_PROGRESS);
        subtask2.setState(State.IN_PROGRESS);

        t.updateSubtask(subtask1);
        t.updateSubtask(subtask2);

        assertEquals(epic.getState(), State.IN_PROGRESS, "Status not NEW.");
    }

    @Test
    void getEpicById() {
        Epic epic = new Epic("Test getEpicById", "Test getEpicById description");

        int epicId = t.createEpic(epic);

        Epic savedEpic = t.getEpicById(epicId);

        assertNotNull(savedEpic, "Task not found.");
        assertEquals(epic, savedEpic, "Tasks don't match.");
    }

    @Test
    void removeEpicById() {
        Epic epic = new Epic("Test removeEpicById", "Test removeEpicById description");
        int epicId = t.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        subtask2.setDuration(Duration.ofHours(1));

        int subtaskIs1 = t.createSubtask(subtask1, epic);
        int subtaskIs2 = t.createSubtask(subtask2, epic);

        t.removeEpicById(epicId);

        assertNull(t.getSubtaskById(subtaskIs1), "Subtask1 found.");
        assertNull(t.getSubtaskById(subtaskIs2), "Subtask2 found.");
        assertNull(t.getEpicById(epicId), "epic found.");
    }

    @Test
    void removeAllEpics() {
        Epic epic1 = new Epic("Test removeEpicById", "Test removeAllEpics description");
        int epicId1 = t.createEpic(epic1);
        Epic epic2 = new Epic("Test removeEpicById", "Test removeAllEpics description");
        int epicId2 = t.createEpic(epic2);

        Subtask subtask1 = new Subtask("Subtask1", "Subtask1 description");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 description");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,13,12));
        subtask2.setDuration(Duration.ofHours(1));

        int subtaskIs1 = t.createSubtask(subtask1, epic1);
        int subtaskIs2 = t.createSubtask(subtask2, epic2);

        t.removeAllEpics();

        assertNull(t.getSubtaskById(subtaskIs1), "Subtask1 found.");
        assertNull(t.getSubtaskById(subtaskIs2), "Subtask2 found.");
        assertNull(t.getEpicById(epicId1), "epic1 found.");
        assertNull(t.getEpicById(epicId2), "epic2 found.");
    }

    @Test
    void history() {
    }
}
