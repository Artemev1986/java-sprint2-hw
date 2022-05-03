package practicum.manager;

import practicum.manager.util.Managers;
import practicum.task.Epic;
import practicum.task.State;
import practicum.task.Subtask;
import practicum.task.Task;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private final String file;

    public FileBackedTasksManager(HistoryManager historyManager, String file) {
        super(historyManager);
        this.file = file;
    }

    public static void main(String[] args) {
        TaskManager taskManager = new FileBackedTasksManager(Managers.getDefaultHistory(),"data.csv");

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

        TaskManager taskManager2 =  loadFromFile("data.csv");
        System.out.println(taskManager2.history());

        System.out.println(taskManager2.getTasksList());
        System.out.println(taskManager2.getSubtasksList());
        System.out.println(taskManager2.getEpicsList());
    }

    private String toString(Task task) {
        return task.getId() + "," +
                TypeTask.TASK + "," +
                task.getTitle() + "," +
                task.getState() + "," +
                task.getStartTime() + "," +
                task.getDuration() + "," +
                task.getDescription() + ",";
    }

    private String toString(Subtask subtask) {
        return subtask.getId() + "," +
                TypeTask.SUBTASK + "," +
                subtask.getTitle() + "," +
                subtask.getState() + "," +
                subtask.getStartTime() + "," +
                subtask.getDuration() + "," +
                subtask.getDescription() + "," +
                subtask.getEpicId();
    }

    private String toString(Epic epic) {
        return epic.getId() + "," +
                TypeTask.EPIC + "," +
                epic.getTitle() + "," +
                epic.getState() + "," +
                epic.getStartTime() + "," +
                epic.getDuration() + "," +
                epic.getEndTime() + "," +
                epic.getDescription() + ",";
    }

    public Task fromString(String value) {
        Task result = null;
        String description;
        String[] split = value.split(",");
        switch (TypeTask.valueOf(split[1])) {
            case TASK:
                if (!split[4].equals("null")) {
                    description = split[6];
                } else {
                    description = null;
                }
                Task task = new Task(split[2], description);
                task.setId(Integer.parseInt(split[0]));
                task.setState(State.valueOf(split[3]));
                task.setStartTime(LocalDateTime.parse(split[4]));
                task.setDuration(Duration.parse(split[5]));
                result = task;
                break;
            case EPIC:
                if (!split[4].equals("null")) {
                    description = split[7];
                } else {
                    description = null;
                }
                Epic epic = new Epic(split[2], description);
                epic.setId(Integer.parseInt(split[0]));
                epic.setState(State.valueOf(split[3]));
                epic.setStartTime(LocalDateTime.parse(split[4]));
                epic.setDuration(Duration.parse(split[5]));
                epic.setEndTime(LocalDateTime.parse(split[6]));
                result = epic;
                break;
            case SUBTASK:
                if (!split[4].equals("null")) {
                    description = split[6];
                } else {
                    description = null;
                }
                Subtask subtask = new Subtask(split[2], description);
                subtask.setId(Integer.parseInt(split[0]));
                subtask.setState(State.valueOf(split[3]));
                subtask.setStartTime(LocalDateTime.parse(split[4]));
                subtask.setDuration(Duration.parse(split[5]));
                subtask.setEpicId(Integer.parseInt(split[7]));
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
        if (listId.length() == 0) {
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

    public void save() {
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
            throw new ManagerSaveException("Save error Exception");
        }
    }

    public static FileBackedTasksManager loadFromFile(String file) {
        FileBackedTasksManager load = new FileBackedTasksManager(Managers.getDefaultHistory(),file);
        Path filePath = Paths.get(load.getResourcesPath().toString(), file);
        try {
            String fileContents = Files.readString(filePath);
            String[] lines = fileContents.split("\\n");
            for (int i=1; i < lines.length; i++) {
                if (!(lines[i].isBlank() || lines[i].isEmpty())) {
                    int idMax = 0;
                    String[] lineSplit = lines[i].split(",");
                    switch (TypeTask.valueOf(lineSplit[1])) {
                        case TASK:
                            Task task = load.fromString(lines[i]);
                            load.getTasks().put(task.getId(), task);
                            if (task.getId() > idMax) {
                                idMax = task.getId();
                            }
                            break;
                        case SUBTASK:
                            Subtask subtask = (Subtask) load.fromString(lines[i]);
                            load.getSubtasks().put(subtask.getId(), subtask);
                            if (subtask.getId() > idMax) {
                                idMax = subtask.getId();
                            }
                            break;
                        case EPIC:
                            Epic epic = (Epic) load.fromString(lines[i]);
                            load.getEpics().put(epic.getId(), epic);
                            if (epic.getId() > idMax) {
                                idMax = epic.getId();
                            }
                            break;
                    }
                    load.setId(idMax);
                } else {
                    break;
                }
            }

            for (Subtask subtask: load.getSubtasks().values()) {
                Epic epic = load.getEpics().get(subtask.getEpicId());
                epic.getSubtaskIds().add(subtask.getId());
                load.getEpics().put(subtask.getEpicId(), epic);
            }

            if (lines.length >=3 && (lines[lines.length-2].isBlank())) {
                for (int id: FileBackedTasksManager.historyFromString(lines[lines.length-1])) {
                    if (load.getTasks().containsKey(id)) {
                        load.getHistoryManager().add(load.getTasks().get(id));
                    } else if (load.getSubtasks().containsKey(id)) {
                        load.getHistoryManager().add(load.getSubtasks().get(id));
                    } else {
                        load.getHistoryManager().add(load.getEpics().get(id));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Невозможно прочитать файл. Возможно, файл не находится в нужной директории.");
        }
        return load;
    }

    @Override
    public int createTask(Task task) {
        int idTask = super.createTask(task);
        save();
        return idTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public int createSubtask(Subtask subtask, Epic epic) {
        int idSubtask = super.createSubtask(subtask, epic);
        save();
        return idSubtask;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = super.getSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeSubtaskById(int id) {
        super.removeSubtaskById(id);
        save();
    }

    @Override
    public void removeAllSubtasks() {
        super.removeAllSubtasks();
        save();
    }

    @Override
    public int createEpic(Epic epic) {
        int epicId = super.createEpic(epic);
        save();
        return epicId;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = super.getEpicById(id);
        save();
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeEpicById(int id) {
        super.removeEpicById(id);
        save();
    }

    @Override
    public void removeAllEpics() {
        super.removeAllEpics();
        save();
    }

    @Override
    public List<Task> history() {
        List<Task> hist = super.history();
        save();
        return hist;
    }


}
