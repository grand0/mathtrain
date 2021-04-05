package ru.zeroapps.mathtrain;

public class TaskResultEntry {
    private final Task task;
    private final TaskResult result;
    private final int userAnswer;

    public TaskResultEntry(Task task, TaskResult result, int userAnswer) {
        this.task = task;
        this.result = result;
        this.userAnswer = userAnswer;
    }

    public Task getTask() {
        return task;
    }

    public TaskResult getResult() {
        return result;
    }

    public int getUserAnswer() {
        return userAnswer;
    }

    @Override
    public String toString() {
        return task.getTask(true) + "\t" + userAnswer + "\t" + result.getLabel();
    }
}
