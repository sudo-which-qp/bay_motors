package com.baymotors.models;

import java.time.LocalDateTime;

import com.baymotors.patterns.state.TaskState;
import com.baymotors.patterns.state.WaitingState;
import com.baymotors.patterns.factory.NotificationFactory;
import com.baymotors.patterns.factory.NotificationType;
import com.baymotors.patterns.factory.NotificationType;
import com.baymotors.patterns.state.TaskState;
import com.baymotors.patterns.state.WaitingState;
import com.baymotors.patterns.state.CompletedState;

public class Task implements Comparable<Task>{
    private int id;
    private String description;
    private int priority;
    private Vehicle vehicle;
    private TaskState state;
    private LocalDateTime createdDate;
    private LocalDateTime completedDate;
    private Mechanic assignedMechanic;

    public Task(int id, String description, int priority, Vehicle vehicle) {
        this.id = id;
        this.description = description;
        this.priority = priority;
        this.vehicle = vehicle;
        this.state = new WaitingState();
        this.createdDate = LocalDateTime.now();
    }

    public void setState(TaskState state) {
        this.state = state;
    }

    public void assignMechanic(Mechanic mechanic) {
        this.assignedMechanic = mechanic;
    }

    public void complete() {
        this.completedDate = LocalDateTime.now();
        state.next(this); // Move to completed state

        // Notify vehicle owner
        if (vehicle != null && vehicle.getOwner() != null) {
            NotificationFactory.createNotification(
                    NotificationType.TASK_COMPLETE,
                    vehicle.getOwner()
            ).send();
        }
    }

    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority); // Higher priority first
    }


    // Getters
    public int getId() { return id; }
    public String getDescription() { return description; }
    public int getPriority() { return priority; }
    public Vehicle getVehicle() { return vehicle; }
    public TaskState getState() { return state; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getCompletedDate() { return completedDate; }
    public Mechanic getAssignedMechanic() { return assignedMechanic; }
}
