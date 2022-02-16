import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
         //   taskManager.printMenu();
            int command;
            try {
                command = scanner.nextInt();
            } catch (InputMismatchException e) {
                command = 20;
                scanner.next();
            }
            switch (command) {
                case 0:
                    System.exit(0);
                case 1:
                    Task task = new Task("Task1", "Simple task");
                    taskManager.createTask(task);
                    Task task1 = new Task("Task2");
                    taskManager.createTask(task1);

                    Epic epic1 = new Epic("Epic1", "Epic task");
                    taskManager.createEpic(epic1);
                    Subtask subtask1 = new Subtask("Subtask1");
                    taskManager.createSubtask(subtask1, epic1);
                    Subtask subtask2 = new Subtask("Subtask2", "Subtask of epic1");
                    taskManager.createSubtask(subtask2, epic1);

                    Epic epic2 = new Epic("Epic2");
                    taskManager.createEpic(epic2);
                    Subtask subtask3 = new Subtask("Subtask1", "Subtask of epic1");
                    taskManager.createSubtask(subtask3, epic2);
                    break;
                case 2:
                    System.out.println(taskManager.getTasksList());
                    break;
                case 3:
                    System.out.println(taskManager.getSubtasksList());
                    break;
                case 4:
                    System.out.println(taskManager.getEpicsList());
                    break;
                case 5:
                    System.out.println("Remove Task id:");
                    int idTask = scanner.nextInt();
                    taskManager.removeTask(idTask);
                    break;
                case 6:
                    System.out.println("Remove epic id:");
                    int idEpic = scanner.nextInt();
                    taskManager.removeEpic(idEpic);
                    break;
                case 20:
                    System.out.println();
                    System.out.println("Введено значение в неправильном формате");
                    System.out.println();
                    break;
                default:
                    System.out.println();
                    System.out.println("Команда с таким номером отсутствует");
                    System.out.println();
                    break;
            }
        }
    }
}
