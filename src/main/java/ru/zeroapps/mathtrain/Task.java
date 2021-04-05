package ru.zeroapps.mathtrain;

import java.util.Random;

public class Task {
    int firstNumber;
    int secondNumber;
    char operator;
    int answer;

    public Task(int firstNumber, int secondNumber, char operator, int answer) {
        this.firstNumber = firstNumber;
        this.secondNumber = secondNumber;
        this.operator = operator;
        this.answer = answer;
    }

    public int getAnswer() {
        return answer;
    }

    public String getTask(boolean showAnswer) {
        return String.valueOf(firstNumber) +
                ' ' +
                operator +
                ' ' +
                secondNumber +
                " = " +
                (showAnswer ? answer : "?");
    }

    public String getTaskWithCustomAnswer(int answer) {
        return String.valueOf(firstNumber) +
                ' ' +
                operator +
                ' ' +
                secondNumber +
                " = " +
                answer;
    }

    public String getTaskWithoutEquation() {
        return String.valueOf(firstNumber) +
                ' ' +
                operator +
                ' ' +
                secondNumber;
    }

    public static Task generateTask(Grade grade, Random rand) {
        int first, second, answer;
        char oper;

        switch (grade) {
            case GRADE_1 -> {
                first = rand.nextInt(10);
                int o = rand.nextInt(2);
                if (o == 0) { // addition
                    second = rand.nextInt(10-first);
                    oper = '+';
                    answer = first + second;
                } else { // subtraction
                    second = rand.nextInt(first+1);
                    oper = '-';
                    answer = first - second;
                }
            }
            case GRADE_2 -> {
                first = rand.nextInt(100);
                int o = rand.nextInt(2);
                if (o == 0) { // addition
                    second = rand.nextInt(100-first);
                    oper = '+';
                    answer = first + second;
                } else { // subtraction
                    second = rand.nextInt(first+1);
                    oper = '-';
                    answer = first - second;
                }
            }
            case GRADE_3 -> {
                int o = rand.nextInt(4);
                if (o == 0) { // addition
                    first = rand.nextInt(1000);
                    second = rand.nextInt(1000-first);
                    oper = '+';
                    answer = first + second;
                } else if (o == 1) { // subtraction
                    first = rand.nextInt(1000);
                    second = rand.nextInt(first+1);
                    oper = '-';
                    answer = first - second;
                } else if (o == 2) { // multiplication
                    first = rand.nextInt(10);
                    second = rand.nextInt(10);
                    oper = '×';
                    answer = first * second;
                } else { // division
                    second = rand.nextInt(9) + 1;
                    first = rand.nextInt(10) * second;
                    oper = ':';
                    answer = first / second;
                }
            }
            case GRADE_4 -> {
                int o = rand.nextInt(4);
                if (o == 0) { // addition
                    first = rand.nextInt(10000);
                    second = rand.nextInt(10000-first);
                    oper = '+';
                    answer = first + second;
                } else if (o == 1) { // subtraction
                    first = rand.nextInt(10000);
                    second = rand.nextInt(first+1);
                    oper = '-';
                    answer = first - second;
                } else if (o == 2) { // multiplication
                    first = rand.nextInt(100);
                    second = rand.nextInt(10);
                    oper = '×';
                    answer = first * second;
                } else { // division
                    second = (rand.nextInt(9) + 1) * 10;
                    first = rand.nextInt(10) * second * 10;
                    oper = ':';
                    answer = first / second;
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + grade);
        }

        return new Task(first, second, oper, answer);
    }
}
