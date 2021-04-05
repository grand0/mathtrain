package ru.zeroapps.mathtrain;

import java.util.ArrayDeque;
import java.util.Queue;

public class TasksStatistics {
    Queue<TaskResultEntry> tasks;

    public TasksStatistics() {
        this.tasks = new ArrayDeque<>();
    }

    private TasksStatistics(Queue<TaskResultEntry> tasks) {
        this.tasks = tasks;
    }

    public boolean hasNextEntry() {
        return !tasks.isEmpty();
    }

    public TaskResultEntry getNextEntry() {
        return tasks.poll();
    }

    public void addEntry(Task task, TaskResult result, int userAnswer) {
        tasks.add(new TaskResultEntry(task, result, userAnswer));
    }

    public TasksStatistics createCopy() {
        Queue<TaskResultEntry> tasksCopy = new ArrayDeque<>(tasks);
        return new TasksStatistics(tasksCopy);
    }
}
