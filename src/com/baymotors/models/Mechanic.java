package com.baymotors.models;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import com.baymotors.exceptions.TaskException;

public class Mechanic extends User{
    private List<Task> assignedTasks;
    private PriorityQueue<Task> taskQueue; // For priority-based task management

    public Mechanic(int id, String name, String email, String password) {
        super(id, name, email, password);
        this.assignedTasks = new ArrayList<>();
        this.taskQueue = new PriorityQueue<>();
    }

    @Override
    public boolean hasPermission(String action) {
        return action.startsWith("TASK_") || action.equals("VIEW_VEHICLE");
    }

    public void assignTask(Task task) throws TaskException {
        if (task == null) {
            throw new TaskException("Task cannot be null");
        }
        assignedTasks.add(task);
        taskQueue.offer(task);
    }

    public Task getNextTask() {
        return taskQueue.peek();
    }

    public void completeTask(Task task) throws TaskException {
        if (task == null || !assignedTasks.contains(task)) {
            throw new TaskException("Invalid task");
        }
        task.complete();
        assignedTasks.remove(task);
        taskQueue.remove(task);
    }

    public List<Task> getAssignedTasks() {
        return new ArrayList<>(assignedTasks);
    }
}
