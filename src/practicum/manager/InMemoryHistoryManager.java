package practicum.manager;

import practicum.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager{
    private Node head;
    private Node tail;
    private final Map<Integer, Node> mapOfEndTasks;

    public InMemoryHistoryManager() {
        mapOfEndTasks = new HashMap<>();
    }

    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.setNext(newNode);
        }
        mapOfEndTasks.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> list = new ArrayList<>();
        Node node = head;
        while (node != null) {
            list.add(node.getData());
            node = node.getNext();
        }
        return list;
    }

    public void removeNode(Node node) {
        final Node next = node.getNext();
        final Node prev = node.getPrev();

        if (prev == null) {
            head = next;
        } else {
            prev.setNext(next);
            node.setPrev(null);
        }
        if (next == null) {
            tail = prev;
        } else {
            next.setPrev(prev);
            node.setNext(null);
        }
        node.setData(null);
    }

    @Override
    public void add(Task task) {
        remove(task.getId());
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        if (mapOfEndTasks.containsKey(id)) {
        removeNode(mapOfEndTasks.get(id));
        mapOfEndTasks.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
