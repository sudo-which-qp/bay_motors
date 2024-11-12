package com.baymotors.patterns.state;

import com.baymotors.models.Task;

public interface TaskState {
    void next(Task task);
    void prev(Task task);
    String getStatus();
}
