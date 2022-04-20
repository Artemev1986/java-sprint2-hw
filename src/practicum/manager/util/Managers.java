package practicum.manager.util;

import practicum.manager.*;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static FileBackedTasksManager loadFromFile(String file) {
        FileBackedTasksManager load = new FileBackedTasksManager(getDefaultHistory(),file);
        Path filePath = Paths.get(load.getResourcesPath().toString(), file);
        try {
            String fileContents = Files.readString(filePath);
            String[] lines = fileContents.split("\\n");
            for (int i=1; i < lines.length; i++) {
                if (!(lines[i].isBlank() || lines[i].isEmpty())) {
                    int idMax = 0;
                    switch (load.fromString(lines[i]).toString().charAt(0)) {
                        case 'T':
                            Task task = load.fromString(lines[i]);
                            load.getTasks().put(task.getId(), task);
                            if (task.getId() > idMax) {
                                idMax = task.getId();
                            }
                            break;
                        case 'S':
                            Subtask subtask = (Subtask) load.fromString(lines[i]);
                            load.getSubtasks().put(subtask.getId(), subtask);
                            if (subtask.getId() > idMax) {
                                idMax = subtask.getId();
                            }
                            break;
                        case 'E':
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

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
