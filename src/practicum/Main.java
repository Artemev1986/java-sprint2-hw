package practicum;

import practicum.manager.*;
import practicum.manager.Network.HTTPTaskManager;
import practicum.manager.Network.HttpTaskServer;
import practicum.manager.Network.KVServer;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;
import practicum.manager.util.Managers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws IOException {
        new KVServer().start();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.startServer();
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Task1", "Simple task");
        task1.setStartTime(LocalDateTime.of(2022,5,3,10,12));
        task1.setDuration(Duration.ofHours(1));
        Task task2 = new Task("Task2");
        task2.setStartTime(LocalDateTime.of(2022,5,3,12,12));
        task2.setDuration(Duration.ofHours(1));

        Epic epic1 = new Epic("Epic1", "Epic task");
        Subtask subtask1 = new Subtask("Subtask1");
        subtask1.setStartTime(LocalDateTime.of(2022,5,3,14,12));
        subtask1.setDuration(Duration.ofHours(1));
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 of epic1");
        subtask2.setStartTime(LocalDateTime.of(2022,5,3,16,12));
        subtask2.setDuration(Duration.ofHours(1));

        int idTask1 = taskManager.createTask(task1);
        int idTask2 = taskManager.createTask(task2);

        int idEpic1 = taskManager.createEpic(epic1);
        int idSubtask1 = taskManager.createSubtask(subtask1, epic1);
        int idSubtask2 = taskManager.createSubtask(subtask2, epic1);

        taskManager.getTaskById(idTask1);
        System.out.println("1" + taskManager.history());
        taskManager.getTaskById(idTask2);
        System.out.println("2" + taskManager.history());

        taskManager.getEpicById(idEpic1);
        System.out.println("3" + taskManager.history());

        taskManager.getSubtaskById(idSubtask1);
        System.out.println("4" + taskManager.history());

        taskManager.getSubtaskById(idSubtask2);
        System.out.println("5" + taskManager.history());

        HTTPTaskManager taskManagerLoad = HTTPTaskManager.loadFromKVServer("http://localhost:8078");

        System.out.println(taskManagerLoad.history());
    }
}
