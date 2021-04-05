package ru.zeroapps.mathtrain.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ru.zeroapps.mathtrain.*;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.prefs.Preferences;

public class TrainingSceneController {
    @FXML
    private BorderPane mainPane;
    @FXML
    private Label gradePlate;
    @FXML
    private Label remainProgressPlate;
    @FXML
    private GridPane statisticsPanel;
    @FXML
    private Label progressPlate;
    @FXML
    private ProgressBar targetProgressBar;
    @FXML
    private ImageView imageView;
    @FXML
    private Button skipButton;
    @FXML
    private Button startButton;
    @FXML
    private Button answerButton;
    @FXML
    private Label correctAnswerPlate;
    @FXML
    private Label wrongAnswerPlate;
    @FXML
    private Label correctAnswersCounter;
    @FXML
    private Label skippedAnswersCounter;
    @FXML
    private Label wrongAnswersCounter;
    @FXML
    private Label currentTaskPlate;
    @FXML
    private Spinner<Integer> answerSpinner;

    private static final int CORRECT = 0;
    private static final int WRONG = 1;
    private static final int SKIPPED = 2;

    final Animator animator = new Animator();

    private int target;
    private boolean isTargetSet = false;
    private Grade grade = Grade.UNKNOWN;

    private int currentProgress = 0;

    private boolean remainProgressModeGeneral = true;

    private Task currentTask;
    private Random rand;

    private long timeStartMillis;

    private String userName;

    private final TasksStatistics tasksStats = new TasksStatistics();

    public void exitButtonPressed() {
        if (isTargetSet) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Выход");
            alert.setHeaderText(null);
            alert.setContentText("Выйти? Ваш прогресс не сохранится.");
            alert.initStyle(StageStyle.UTILITY);
            ButtonType exitButtonType = new ButtonType("Выйти", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(exitButtonType, cancelButtonType);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != exitButtonType) return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Выход");
            alert.setHeaderText(null);
            alert.setContentText("Сохранить ваш прогресс в файле статистики?");
            alert.initStyle(StageStyle.UTILITY);
            ButtonType saveExitButtonType = new ButtonType("Сохранить");
            ButtonType dontSaveExitButtonType = new ButtonType("Не сохранять");
            ButtonType cancelButtonType = new ButtonType("Отмена");
            alert.getButtonTypes().setAll(saveExitButtonType, dontSaveExitButtonType, cancelButtonType);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() == cancelButtonType) return;
            if (result.get() == saveExitButtonType) {
                StatsWriter.writeStatisticsSafely(collectStats(), tasksStats);
            }
        }

        Main.logger.log(Level.INFO, "exiting from training");

        Main.logger.log(Level.INFO, "launching startScene");

        animator.playFadeAnimation(mainPane, 500, 1, 0, event -> {
            try {
                Stage stage = (Stage) startButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/startScene.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void startButtonPressed() {
        timeStartMillis = System.currentTimeMillis();
        newTask();
        startButton.setVisible(false);
        answerSpinner.setDisable(false);
        answerButton.setDisable(false);
        skipButton.setDisable(false);
    }

    public void answerButtonPressed() {
        Main.logger.log(Level.INFO, "checking answer");

        int userAnswer = (answerSpinner.getEditor().getText() == null ||
                answerSpinner.getEditor().getText().equals("")) ? 0 : answerSpinner.getValue();
        int realAnswer = currentTask.getAnswer();
        if (userAnswer == realAnswer) {
            Main.logger.log(Level.INFO, "correct answer");

            answerButton.setDisable(true);

            tasksStats.addEntry(currentTask, TaskResult.CORRECT, userAnswer);

            animator.finishAllAnimations();
            currentTaskPlate.setTextFill(Paint.valueOf("#6430ff"));
            currentTaskPlate.setText(currentTask.getTask(true));
            animator.playFadeAnimation(currentTaskPlate, 500, 1500, 1, 0, event -> {
                newTask();
                currentTaskPlate.setTextFill(Paint.valueOf("#000"));
                animator.playFadeAnimation(currentTaskPlate, 1500, 0, 1);
                answerButton.setDisable(false);
            });

            animator.playFadeAnimation(correctAnswerPlate, 3000);

            Image im = ResourcesHelper.getRandomGIFFromDirectory(
                    "/images/correct/",
                    ResourcesHelper.CORRECT_ANSWER_IMAGES_NUMBER
            );
            imageView.setImage(im);
            animator.playFadeAnimation(imageView, 3000);

            int correct = Integer.parseInt(correctAnswersCounter.getText()) + 1;
            correctAnswersCounter.setText(String.valueOf(correct));

            if (isTargetSet) {
                updateProgressBar(CORRECT);
            }

        } else { // wrong answer
            Main.logger.log(Level.INFO, "wrong answer");

            tasksStats.addEntry(currentTask, TaskResult.WRONG, userAnswer); // add entry for statistics
            // play animation for task plate...
            animator.finishAllAnimations();
//            currentTaskPlate.setText(currentTask.getTaskWithCustomAnswer(userAnswer));
            currentTaskPlate.setText(currentTask.getTaskWithoutEquation() + " ≠ " + userAnswer);
            animator.playTextFillAnimation(currentTaskPlate, 3000, "#FF5400", "#000", event -> currentTaskPlate.setText(currentTask.getTask(false)));
            // ...and for "wrong answer" plate
            animator.playFadeAnimation(wrongAnswerPlate, 3000);

            Image im = ResourcesHelper.getRandomGIFFromDirectory(
                    "/images/wrong/",
                    ResourcesHelper.WRONG_ANSWER_IMAGES_NUMBER
            );
            imageView.setImage(im);
            animator.playFadeAnimation(imageView, 3000);

            // increment wrong answers counter
            int wrong = Integer.parseInt(wrongAnswersCounter.getText()) + 1;
            wrongAnswersCounter.setText(String.valueOf(wrong));

            answerSpinner.getEditor().setText("");

            if (isTargetSet) {
                updateProgressBar(WRONG);
            }
        }
    }

    public void skipTaskButtonPressed() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Пропустить");
        alert.setHeaderText(null);
        alert.setContentText("Пропустить вопрос? Вы НЕ сможете вернуться к нему позже, а в статистике это будет засчитано за ошибку!");
        alert.initStyle(StageStyle.UTILITY);
        ButtonType skipButtonType = new ButtonType("Пропустить", ButtonBar.ButtonData.YES);
        ButtonType cancelButtonType = new ButtonType("Отмена", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(skipButtonType, cancelButtonType);
        alert.showAndWait().ifPresent(button -> {
            if (button.getButtonData().equals(skipButtonType.getButtonData())) {
                Main.logger.log(Level.INFO, "skipping task");

                tasksStats.addEntry(currentTask, TaskResult.SKIPPED, -1); // add entry for statistics

                newTask();
                int skipped = Integer.parseInt(skippedAnswersCounter.getText()) + 1;
                skippedAnswersCounter.setText(String.valueOf(skipped));

                if (isTargetSet) {
                    updateProgressBar(SKIPPED);
                }
            }
        });
    }

    public void onRemainProgressPlateClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            remainProgressModeGeneral = !remainProgressModeGeneral;
            if (remainProgressModeGeneral) remainProgressPlate.setText(currentProgress + " / " + target);
            else remainProgressPlate.setText("Осталось: " + (target - currentProgress));
        }
    }

    private void updateProgressBar(int result) {
        switch (result) {
            case CORRECT -> currentProgress = currentProgress + 1;
            case WRONG, SKIPPED -> currentProgress = Math.max(currentProgress - 1, 0);
        }
        double percentage = (double) currentProgress / (double) target;
        progressPlate.setText((int) (percentage * 100.0) + "%");
        if (remainProgressModeGeneral) remainProgressPlate.setText(currentProgress + " / " + target);
        else remainProgressPlate.setText("Осталось: " + (target - currentProgress));
        animator.playProgressBarAnimation(targetProgressBar, 1000, percentage, event -> {
            if (percentage == 1.0) {
                finishTarget();
            }
        });
    }

    private void finishTarget() {
        Statistics stats = collectStats();

        animator.finishAllAnimations();
        answerSpinner.setDisable(true);
        answerButton.setDisable(true);
        skipButton.setDisable(true);
        animator.playFadeAnimation(statisticsPanel, 1500, 1, 0);
        animator.playFadeAnimation(remainProgressPlate, 1500, 1, 0);
        animator.playFadeAnimation(gradePlate, 1500, 1, 0);
        currentTaskPlate.setOpacity(0);
        currentTaskPlate.setTextFill(Paint.valueOf("#000"));
        currentTaskPlate.setText("Цель выполнена!");
        animator.playFadeAnimation(currentTaskPlate, 1500, 0, 1, event -> {
            Main.logger.log(Level.INFO, "launching finishScene");

            Stage stage = (Stage) startButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/finishScene.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert root != null;
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            FinishSceneController fsc = loader.getController();
            fsc.setStatistics(stats, tasksStats);
            stage.show();
        });
    }

    private Statistics collectStats() {
        int correctAnswers = Integer.parseInt(correctAnswersCounter.getText());
        int wrongAnswers = Integer.parseInt(wrongAnswersCounter.getText());
        int skippedTasks = Integer.parseInt(skippedAnswersCounter.getText());
        long timeSpentMillis = System.currentTimeMillis() - timeStartMillis;
        return new Statistics(userName, correctAnswers, wrongAnswers, skippedTasks, target, grade, timeSpentMillis);
    }

    private void newTask() {
        Main.logger.log(Level.INFO, "generating new task");

        currentTask = Task.generateTask(grade, rand);
        currentTaskPlate.setText(currentTask.getTask(false));
        answerSpinner.getEditor().setText("");
        Platform.runLater(() -> answerSpinner.getEditor().positionCaret(answerSpinner.getEditor().getText().length()));
    }

    public void setupController(Grade grade, int target, String pupilName) {
        this.grade = grade;

        this.target = target;
        isTargetSet = target != 0;
        if (isTargetSet) {
            targetProgressBar.setVisible(true);
            progressPlate.setVisible(true);
            remainProgressPlate.setText("0 / " + target);
            remainProgressPlate.setVisible(true);
        }
        gradePlate.setText(grade.getLabel());

        this.userName = pupilName;
    }

    @FXML
    public void initialize() {
        mainPane.setOpacity(0);

        Main.logger.log(Level.INFO, "initializing TrainingSceneController");

        Preferences prefs = Preferences.userNodeForPackage(ru.zeroapps.mathtrain.Main.class);
        rand = new Random();
        if (prefs.get(Defaults.PREFS_SEED_ENTRY.getValue(), "").length() != 0)
            rand.setSeed(prefs.get(Defaults.PREFS_SEED_ENTRY.getValue(), "").hashCode());

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(Integer.MIN_VALUE, Integer.MAX_VALUE, 0);
        answerSpinner.setValueFactory(valueFactory);
        answerSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
        answerSpinner.getEditor().textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) answerSpinner.getEditor().setText(oldValue);
            if (newValue.equals("")) answerSpinner.getEditor().setText("0");
            if (newValue.length() > 1 && newValue.charAt(0) == '0') answerSpinner.getEditor().setText(newValue.substring(1));
        }));

        animator.playFadeAnimation(mainPane, 500, 0, 1);
    }
}
