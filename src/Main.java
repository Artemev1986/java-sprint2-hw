public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Task1", "Simple task");
        Task task2 = new Task("Task2");
        Epic epic1 = new Epic("Epic1", "Epic task");
        Subtask subtask1 = new Subtask("Subtask1");
        Subtask subtask2 = new Subtask("Subtask2", "Subtask of epic1");
        Epic epic2 = new Epic("Epic2");
        Subtask subtask3 = new Subtask("Subtask1", "Subtask of epic1");

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1, epic1);
        taskManager.createSubtask(subtask2, epic1);

        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask3, epic2);

        System.out.println();
        System.out.println("Print lists after creation");
        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getSubtasksList());
        System.out.println(taskManager.getEpicsList());

        task1.setState("IN_PROGRESS");
        task2.setState("DONE");
        subtask1.setState("IN_PROGRESS");
        subtask2.setState("DONE");
        subtask3.setState("DONE");

        taskManager.updateTask(task1);
        taskManager.updateTask(task2);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        System.out.println();
        System.out.println("Print lists after updating");
        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getSubtasksList());
        System.out.println(taskManager.getEpicsList());

        taskManager.removeTask(1);
        taskManager.removeEpic(3);

        System.out.println();
        System.out.println("Print lists after removing");
        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getSubtasksList());
        System.out.println(taskManager.getEpicsList());
    }
}
