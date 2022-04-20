package practicum.manager;

import practicum.manager.util.Managers;
import practicum.task.Epic;
import practicum.task.State;
import practicum.task.Subtask;
import practicum.task.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private final String file;
    public FileBackedTasksManager(HistoryManager historyManager, String file) {
        super(historyManager);
        this.file = file;
    }

    public static void main(String[] args) throws ManagerSaveException {
        TaskManager taskManager =  Managers.loadFromFile("data.csv");

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

        System.out.println("After load from file");

        TaskManager taskManager2 =  Managers.loadFromFile("data.csv");
        System.out.println(taskManager2.history());
    }

    private String toString(Task task) {
        return task.getId() + "," +
                TypeTask.TASK + "," +
                task.getTitle() + "," +
                task.getState() + "," +
                task.getDescription() + ",";
    }

    private String toString(Subtask subtask) {
        return subtask.getId() + "," +
                TypeTask.SUBTASK + "," +
                subtask.getTitle() + "," +
                subtask.getState() + "," +
                subtask.getDescription() + "," +
                subtask.getEpicId();
    }

    private String toString(Epic epic) {
        return epic.getId() + "," +
                TypeTask.EPIC + "," +
                epic.getTitle() + "," +
                epic.getState() + "," +
                epic.getDescription() + ",";
    }

    public Task fromString(String value) {
        Task result = null;
        String description = null;
        String[] split = value.split(",");
        switch (split[1]) {
            case "TASK":
                if (!split[4].equals("null")) {
                    description = split[4];
                } else {
                    description = null;
                }
                Task task = new Task(split[2], description);
                task.setId(Integer.parseInt(split[0]));
                task.setState(State.valueOf(split[3]));
                result = task;
                break;
            case "EPIC":
                if (!split[4].equals("null")) {
                    description = split[4];
                } else {
                    description = null;
                }
                Epic epic = new Epic(split[2], description);
                epic.setId(Integer.parseInt(split[0]));
                epic.setState(State.valueOf(split[3]));
                result = epic;
                break;
            case "SUBTASK":
                if (!split[4].equals("null")) {
                    description = split[4];
                } else {
                    description = null;
                }
                Subtask subtask = new Subtask(split[2], description);
                subtask.setId(Integer.parseInt(split[0]));
                subtask.setState(State.valueOf(split[3]));
                subtask.setEpicId(Integer.parseInt(split[5]));
                result = subtask;
                break;
        }
        return result;
    }

    public static String historyToString(HistoryManager manager) {
        StringBuilder listId = new StringBuilder();
        for (Task task: manager.getHistory()) {
            listId.append(task.getId()).append(",");
        }
        if (listId.isEmpty()) {
            return "";
        }
        return listId.deleteCharAt(listId.lastIndexOf(",")).toString();
    }

    public static List<Integer> historyFromString(String value) {
        String[] ids = value.split(",");
        List<Integer> list = new ArrayList<>();
        for (String idString: ids) {
            int id = Integer.parseInt(idString);
            list.add(id);
        }
        return list;
    }

    public Path getResourcesPath() {
        Path filePath = Path.of(System.getProperty("user.dir"));
        return Paths.get(filePath.toString(), "resources");
    }

    public void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(getResourcesPath() + "\\" + file)) {
            fileWriter.write("id,type,name,status,description,epic\n");
            for (Task task : getTasksList()) {
                String line = toString(task);
                fileWriter.write(line + "\n");
            }

            for (Subtask subtask : getSubtasksList()) {
                String line = toString(subtask);
                fileWriter.write(line + "\n");
            }

            for (Epic epic : getEpicsList()) {
                String line = toString(epic);
                fileWriter.write(line + "\n");
            }
            String str = historyToString(getHistoryManager());
            if (!(str.isBlank() || str.isEmpty())) {
                fileWriter.write("\n");
                fileWriter.write(historyToString(getHistoryManager()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    @Override
    public void createTask(Task task) throws ManagerSaveException {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) throws ManagerSaveException {
        super.updateTask(task);
        save();
    }

    @Override
    public Task getTaskById(int id) throws ManagerSaveException {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void removeTaskById(int id) throws ManagerSaveException {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeAllTasks() throws ManagerSaveException {
        super.removeAllTasks();
        save();
    }

    @Override
    public void createSubtask(Subtask subtask, Epic epic) throws ManagerSaveException {
        super.createSubtask(subtask, epic);
        save();
    }

    @Override
    public Subtask getSubtaskById(int id) throws ManagerSaveException {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) throws ManagerSaveException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeSubtaskById(int id) throws ManagerSaveException {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeAllSubtasks() throws ManagerSaveException {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public void createEpic(Epic epic) throws ManagerSaveException {
        super.createEpic(epic);
        save();
    }

    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) throws ManagerSaveException {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpicById(int id) throws ManagerSaveException {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeAllEpics() throws ManagerSaveException {
        super.removeAllEpics();
        save();
    }

    @Override
    public List<Task> history() throws ManagerSaveException {
        List<Task> hist = super.history();
        save();
        return hist;
    }


}
