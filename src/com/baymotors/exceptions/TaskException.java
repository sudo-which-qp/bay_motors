package com.baymotors.exceptions;

public class TaskException extends BayMotorsException{
    public TaskException(String message) {
        super(message, ErrorCode.TASK_ERROR);
    }
}
