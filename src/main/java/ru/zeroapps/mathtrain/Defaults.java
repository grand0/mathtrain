package ru.zeroapps.mathtrain;

public enum Defaults {
    STATS_DIR_PATH(System.getProperty("user.home") + "\\Тренажёр"),
    STATS_FILE_NAME("Статистика"),
    ANIMATION_SPEED("1.0"),
    PREFS_STATS_DIR_ENTRY("statsDirectoryPath"),
    PREFS_STATS_FILE_ENTRY("statsFileName"),
    PREFS_SEED_ENTRY("seed"),
    PREFS_ANIMATION_SPEED_ENTRY("animationSpeed");

    String value;

    Defaults(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
