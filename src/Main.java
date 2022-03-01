public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task1", "Simple task");
        Task task2 = new Task("Task2");
        Task task3 = new Task("Task3", "Simple task");
        Task task4 = new Task("Task4");
        Task task5 = new Task("Task5", "Simple task");
        Task task6 = new Task("Task6");
        Task task7 = new Task("Task7", "Simple task");
        Task task8 = new Task("Task8");

        Epic epic1 = new Epic("Epic1", "Epic task");
        Subtask subtask1 = new Subtask("Subtask1");
        Subtask subtask2 = new Subtask("Subtask2", "Subtask of epic1");
        Epic epic2 = new Epic("Epic2");
        Subtask subtask3 = new Subtask("Subtask1", "Subtask of epic2");
        Epic epic3 = new Epic("Epic3", "Epic task");
        Subtask subtask4 = new Subtask("Subtask4");
        Subtask subtask5 = new Subtask("Subtask5", "Subtask of epic3");
        Epic epic4 = new Epic("Epic4");
        Subtask subtask6 = new Subtask("Subtask6", "Subtask of epic4");

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);
        inMemoryTaskManager.createTask(task3);
        inMemoryTaskManager.createTask(task4);
        inMemoryTaskManager.createTask(task5);
        inMemoryTaskManager.createTask(task6);
        inMemoryTaskManager.createTask(task7);
        inMemoryTaskManager.createTask(task8);

        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubtask(subtask1, epic1);
        inMemoryTaskManager.createSubtask(subtask2, epic1);

        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createSubtask(subtask3, epic2);

        inMemoryTaskManager.createEpic(epic3);
        inMemoryTaskManager.createSubtask(subtask4, epic3);
        inMemoryTaskManager.createSubtask(subtask5, epic3);

        inMemoryTaskManager.createEpic(epic4);
        inMemoryTaskManager.createSubtask(subtask6, epic4);

        inMemoryTaskManager.getTaskById(1);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(2);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(3);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(4);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(5);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getEpicById(9);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getSubtaskById(10);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getSubtaskById(11);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(3);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(4);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(5);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(4);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(5);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(4);
        System.out.println(inMemoryTaskManager.history());
        inMemoryTaskManager.getTaskById(5);
    }
}
