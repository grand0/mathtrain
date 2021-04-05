package ru.zeroapps.mathtrain;

public enum TaskResult {
    CORRECT("Верно"), WRONG("Неверно"), SKIPPED("Пропущено");

    private final String label;

    TaskResult(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
