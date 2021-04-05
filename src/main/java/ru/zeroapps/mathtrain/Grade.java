package ru.zeroapps.mathtrain;

public enum Grade {
    GRADE_1("1 класс", 1),
    GRADE_2("2 класс", 2),
    GRADE_3("3 класс", 3),
    GRADE_4("4 класс", 4),
    UNKNOWN("", 0);

    private final String LABEL;
    private final int NUMBER;

    Grade(String label, int number) {
        this.LABEL = label;
        this.NUMBER = number;
    }

    public String getLabel() {
        return LABEL;
    }

    public int getNumber() {
        return NUMBER;
    }

    public static String[] getEveryLabel() {
        Grade[] elements = Grade.values();
        String[] labels = new String[elements.length-1];
        for (int i = 0; i < elements.length; i++) {
            if (elements[i].getNumber() == 0) continue;
            labels[i] = elements[i].getLabel();
        }
        return labels;
    }

    public static Grade getVariableByLabel(String label) {
        Grade[] elements = Grade.values();
        for (Grade g :
                elements) {
            if (g.getLabel().equals(label)) return g;
        }
        return UNKNOWN;
    }
}
