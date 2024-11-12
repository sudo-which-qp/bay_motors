package com.baymotors.patterns.state;

import com.baymotors.models.Task;

public class WaitingState implements TaskState{
    @Override
    public void next(Task task) {
        task.setState(new InProgressState());
    }

    @Override
    public void prev(Task task) {
        // Cannot go back from waiting
    }

    @Override
    public String getStatus() {
        return "Waiting";
    }
}
