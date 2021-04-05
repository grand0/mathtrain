package ru.zeroapps.mathtrain;

public class Statistics {
    private final String name;
    private final int correctAnswers;
    private final int wrongAnswers;
    private final int skippedTasks;
    private final int target;
    private final Grade grade;
    private final long timeSpentMillis;
    public Statistics(String name, int correctAnswers, int wrongAnswers, int skippedTasks, int target, Grade grade, long timeSpentMillis) {
        this.name = name;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.skippedTasks = skippedTasks;
        this.target = target;
        this.grade = grade;
        this.timeSpentMillis = timeSpentMillis;
    }

    public String getName() {
        return name;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public int getSkippedTasks() {
        return skippedTasks;
    }

    public int getTarget() {
        return target;
    }

    public Grade getGrade() {
        return grade;
    }

    public long getTimeSpentMillis() {
        return timeSpentMillis;
    }
}
