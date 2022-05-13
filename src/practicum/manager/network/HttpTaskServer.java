package practicum.manager.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import practicum.manager.TaskManager;
import practicum.manager.util.Managers;
import practicum.task.Epic;
import practicum.task.Subtask;
import practicum.task.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private static final TaskManager taskManager = Managers.getFileBackedTasksManager();

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
    }

    public void startServer() {
        System.out.println("Start the server on port " + PORT);
        httpServer.start();
    }
    public void stop() {
        System.out.println("Stop the server on port " + PORT);
        httpServer.stop(1);
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            String response = "";
            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);
            String method = httpExchange.getRequestMethod();
            String path = httpExchange.getRequestURI().getPath();
            String[] split = path.split("/");
            int id = 0;
            boolean isQueryNotEmpty = false;
            boolean isGettingSubtaskList = false;

            String rawQuery = httpExchange.getRequestURI().getRawQuery();
            if (rawQuery != null) {
                if (rawQuery.contains("id=")) {
                    isQueryNotEmpty = true;
                    id = Integer.parseInt(rawQuery.replace("id=", ""));
                    if (split.length >= 4)
                    if (split[3].equals("epic")) {
                        isGettingSubtaskList = true;
                    }
                }
            }

            Task task;
            Subtask subtask;
            Epic epic;
            switch(method) {
                case "GET":
                    if (split.length == 2) {
                        response = gson.toJson(taskManager.getPrioritizedTasks());
                        httpExchange.sendResponseHeaders(200, 0);
                    } else
                    switch (split[2]) {
                        case "task":
                            if (isQueryNotEmpty) {
                            task = taskManager.getTaskById(id);
                            response = gson.toJson(task);
                            } else {
                                response = gson.toJson(taskManager.getTasksList());
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        case "subtask":
                            if (isQueryNotEmpty) {
                                if (!isGettingSubtaskList) {
                                subtask = taskManager.getSubtaskById(id);
                                response = gson.toJson(subtask);
                                } else {
                                    List<Subtask> subtasksOfEpic = taskManager
                                            .getSubtasksOfEpicList(taskManager.getEpicById(id));
                                    response = gson.toJson(subtasksOfEpic);
                                }
                            } else {
                                response = gson.toJson(taskManager.getSubtasksList());
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        case "epic":
                            if (isQueryNotEmpty) {
                                epic = taskManager.getEpicById(id);
                                response = gson.toJson(epic);
                            } else {
                                response = gson.toJson(taskManager.getEpicsList());
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        case "history":
                            response = gson.toJson(taskManager.history());
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        default:
                            System.out.println("/tasks waited correct request, but got " + split[2]);
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                    }
                    break;
                case "POST":
                    switch (split[2]) {
                        case "task":
                            task = gson.fromJson(body, Task.class);
                            if (task.getId() == 0) {
                                taskManager.createTask(task);
                            } else {
                                taskManager.updateTask(task);
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        case "subtask":
                            subtask = gson.fromJson(body, Subtask.class);
                            if (subtask.getId() == 0) {
                                epic = taskManager.getEpicById(subtask.getEpicId());
                                taskManager.createSubtask(subtask, epic);
                            } else {
                                taskManager.updateSubtask(subtask);
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        case "epic":
                            epic = gson.fromJson(body, Epic.class);
                            if (epic.getId() == 0) {
                            taskManager.createEpic(epic);
                            } else {
                                taskManager.updateEpic(epic);
                            }
                            break;
                        default:
                            System.out.println("/tasks waited correct request, but got " + split[2]);
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                    }
                    break;
                case "DELETE":
                    switch (split[2]) {
                        case "task":
                            if (isQueryNotEmpty) {
                                taskManager.removeTaskById(id);
                            } else {
                                taskManager.removeAllTasks();
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        case "subtask":
                            if (isQueryNotEmpty) {
                                taskManager.removeSubtaskById(id);
                            } else {
                                taskManager.removeAllSubtasks();
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        case "epic":
                            if (isQueryNotEmpty) {
                                taskManager.removeEpicById(id);
                            } else {
                                taskManager.removeAllEpics();
                            }
                            httpExchange.sendResponseHeaders(200, 0);
                            break;
                        default:
                            System.out.println("/tasks waited correct request, but got " + split[2]);
                            httpExchange.sendResponseHeaders(405, 0);
                            break;
                    }
                    break;
                default:
                    System.out.println("/tasks waited correct request, but got " + httpExchange.getRequestMethod());
                    httpExchange.sendResponseHeaders(405, 0);
                    break;
            }
            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}
