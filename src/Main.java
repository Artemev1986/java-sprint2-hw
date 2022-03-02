public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();

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

        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        taskManager.createTask(task4);
        taskManager.createTask(task5);
        taskManager.createTask(task6);
        taskManager.createTask(task7);
        taskManager.createTask(task8);

        taskManager.createEpic(epic1);
        taskManager.createSubtask(subtask1, epic1);
        taskManager.createSubtask(subtask2, epic1);

        taskManager.createEpic(epic2);
        taskManager.createSubtask(subtask3, epic2);

        taskManager.createEpic(epic3);
        taskManager.createSubtask(subtask4, epic3);
        taskManager.createSubtask(subtask5, epic3);

        taskManager.createEpic(epic4);
        taskManager.createSubtask(subtask6, epic4);

        taskManager.getTaskById(1);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(2);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(3);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(4);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(5);
        System.out.println(historyManager.getHistory());
        taskManager.getEpicById(9);
        System.out.println(historyManager.getHistory());
        taskManager.getSubtaskById(15);
        System.out.println(historyManager.getHistory());
        taskManager.getSubtaskById(11);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(3);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(4);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(5);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(4);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(5);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(4);
        System.out.println(historyManager.getHistory());
        taskManager.getTaskById(5);
    }
}
