package practicum.test;

import org.junit.jupiter.api.Test;
import practicum.manager.FileBackedTasksManager;
import practicum.manager.TaskManager;
import practicum.manager.util.Managers;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(Managers.getDefaultHistory(),"data.csv"));
    }

    @Test
    void taskListIsEmpty() {
        getTaskManager().removeAllTasks();
        getTaskManager().removeAllEpics();
        TaskManager taskManager =  FileBackedTasksManager.loadFromFile("data.csv");
        assertEquals(true, taskManager.getTasksList().isEmpty(), "Task list isn't empty.");
        assertEquals(true, taskManager.getSubtasksList().isEmpty(), "Task list isn't empty.");
        assertEquals(true, taskManager.getEpicsList().isEmpty(), "Task list isn't empty.");
    }

    @Test
    void epicWithoutSubtasks() {
        Epic epic = new Epic("Epic", "Epic without task");
        int epicId = getTaskManager().createEpic(epic);
        TaskManager taskManager =  FileBackedTasksManager.loadFromFile("data.csv");
        assertEquals(epic, taskManager.getEpicById(epicId), "Epic isn't equals loaded epic.");
        assertEquals(0, taskManager.getSubtasksList().size(), "Subtask list isn't empty.");
    }

    @Test
    void historyListIsEmpty() {
        Task task1 = new Task("Task1", "Simple task");
        task1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task1.setDuration(Duration.ofHours(1));
        Task task2 = new Task("Task2");
        task2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        task2.setDuration(Duration.ofHours(1));

        Epic epic1 = new Epic("Epic1", "Epic task");
        Subtask subtask1 = new Subtask("Subtask1");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,13,13));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 of epic1");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,15,12));
        subtask2.setDuration(Duration.ofHours(1));
        Subtask subtask3 = new Subtask("Subtask3", "Subtask3 of epic1");
        subtask3.setStartTime(LocalDateTime.of(2023,5,3,17,12));
        subtask3.setDuration(Duration.ofHours(1));

        getTaskManager().createTask(task1);
        getTaskManager().createTask(task2);
        getTaskManager().createEpic(epic1);
        getTaskManager().createSubtask(subtask1, epic1);
        getTaskManager().createSubtask(subtask2, epic1);
        getTaskManager().createSubtask(subtask3, epic1);

        TaskManager taskManager =  FileBackedTasksManager.loadFromFile("data.csv");
        assertEquals(true, taskManager.history().isEmpty(), "History list isn't empty.");
    }
}