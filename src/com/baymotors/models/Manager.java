package com.baymotors.models;

import java.util.ArrayList;
import java.util.List;
import com.baymotors.exceptions.TaskException;

public class Manager extends User{
    private List<Mechanic> teamMembers;
    private List<Task> pendingTasks;

    public Manager(int id, String name, String email, String password) {
        super(id, name, email, password);
        this.teamMembers = new ArrayList<>();
        this.pendingTasks = new ArrayList<>();
    }

    @Override
    public boolean hasPermission(String action) {
        // Managers have all permissions
        return true;
    }

    public void allocateTask(Task task, Mechanic mechanic) throws TaskException {
        if (task == null || mechanic == null) {
            throw new TaskException("Task and mechanic must not be null");
        }

        if (!teamMembers.contains(mechanic)) {
            throw new TaskException("Mechanic is not in manager's team");
        }

        mechanic.assignTask(task);
        pendingTasks.remove(task);
    }

    public void addTeamMember(Mechanic mechanic) {
        if (mechanic != null && !teamMembers.contains(mechanic)) {
            teamMembers.add(mechanic);
        }
    }

    public List<Task> getPendingTasks() {
        return new ArrayList<>(pendingTasks);
    }

    public List<Mechanic> getTeamMembers() {
        return new ArrayList<>(teamMembers);
    }
}
