package com.baymotors.patterns.state;

import com.baymotors.models.Task;

public class CompletedState implements TaskState{
    @Override
    public void next(Task task) {
        // Cannot go forward from completed
    }

    @Override
    public void prev(Task task) {
        task.setState(new InProgressState());
    }

    @Override
    public String getStatus() {
        return "Completed";
    }
}
