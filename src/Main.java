public class Main {
    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task1", "Simple task");
        Task task2 = new Task("Task2");
        Epic epic1 = new Epic("Epic1", "Epic task");
        Subtask subtask1 = new Subtask("Subtask1");
        Subtask subtask2 = new Subtask("Subtask2", "Subtask of epic1");
        Epic epic2 = new Epic("Epic2");
        Subtask subtask3 = new Subtask("Subtask1", "Subtask of epic1");

        inMemoryTaskManager.createTask(task1);
        inMemoryTaskManager.createTask(task2);

        inMemoryTaskManager.createEpic(epic1);
        inMemoryTaskManager.createSubtask(subtask1, epic1);
        inMemoryTaskManager.createSubtask(subtask2, epic1);

        inMemoryTaskManager.createEpic(epic2);
        inMemoryTaskManager.createSubtask(subtask3, epic2);

        System.out.println();
        System.out.println("Print lists after creation");
        System.out.println(inMemoryTaskManager.getTasksList());
        System.out.println(inMemoryTaskManager.getSubtasksList());
        System.out.println(inMemoryTaskManager.getEpicsList());

        task1.setState("IN_PROGRESS");
        task2.setState("DONE");
        subtask1.setState("IN_PROGRESS");
        subtask2.setState("DONE");
        subtask3.setState("DONE");

        inMemoryTaskManager.updateTask(task1);
        inMemoryTaskManager.updateTask(task2);
        inMemoryTaskManager.updateSubtask(subtask1);
        inMemoryTaskManager.updateSubtask(subtask2);
        inMemoryTaskManager.updateSubtask(subtask3);

        System.out.println();
        System.out.println("Print lists after updating");
        System.out.println(inMemoryTaskManager.getTasksList());
        System.out.println(inMemoryTaskManager.getSubtasksList());
        System.out.println(inMemoryTaskManager.getEpicsList());

        inMemoryTaskManager.removeTaskById(1);
        inMemoryTaskManager.removeEpicById(3);

        System.out.println();
        System.out.println("Print lists after removing");
        System.out.println(inMemoryTaskManager.getTasksList());
        System.out.println(inMemoryTaskManager.getSubtasksList());
        System.out.println(inMemoryTaskManager.getEpicsList());
    }
}
