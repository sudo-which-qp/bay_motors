package com.baymotors.patterns.state;

import com.baymotors.models.Task;

public class InProgressState implements TaskState{
    @Override
    public void next(Task task) {
        task.setState(new CompletedState());
    }

    @Override
    public void prev(Task task) {
        task.setState(new WaitingState());
    }

    @Override
    public String getStatus() {
        return "In Progress";
    }
}
