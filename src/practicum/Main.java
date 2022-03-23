package practicum;

import practicum.manager.TaskManager;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;
import practicum.manager.util.Managers;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Task1", "Simple task");
        Task task2 = new Task("Task2");

        Epic epic1 = new Epic("Epic1", "Epic task");
        Subtask subtask1 = new Subtask("Subtask1");
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2 of epic1");
        Subtask subtask3 = new Subtask("Subtask3", "Subtask3 of epic1");
        Epic epic2 = new Epic("Epic2", "Epic2 without task");

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1, epic1);
        taskManager.createSubtask(subtask2, epic1);
        taskManager.createSubtask(subtask3, epic1);

        taskManager.createEpic(epic2);

        taskManager.getTaskById(1);
        System.out.println(taskManager.history());
        taskManager.getTaskById(2);
        System.out.println(taskManager.history());

        taskManager.getEpicById(3);
        System.out.println(taskManager.history());

        taskManager.getSubtaskById(4);
        System.out.println(taskManager.history());

        taskManager.getSubtaskById(5);
        System.out.println(taskManager.history());

        taskManager.getSubtaskById(6);
        System.out.println(taskManager.history());

        taskManager.getEpicById(7);
        System.out.println(taskManager.history());

        System.out.println("Repeat getting tasks");

        taskManager.getTaskById(1);
        System.out.println(taskManager.history());
        taskManager.getTaskById(2);
        System.out.println(taskManager.history());

        taskManager.getEpicById(3);
        System.out.println(taskManager.history());

        taskManager.getSubtaskById(4);
        System.out.println(taskManager.history());

        taskManager.getSubtaskById(5);
        System.out.println(taskManager.history());

        taskManager.getEpicById(7);
        System.out.println(taskManager.history());

        System.out.println("remove task1");

        taskManager.removeTaskById(1);
        System.out.println(taskManager.history());

        System.out.println("remove epic1");
        
        taskManager.removeEpicById(3);
        System.out.println(taskManager.history());
    }
}
