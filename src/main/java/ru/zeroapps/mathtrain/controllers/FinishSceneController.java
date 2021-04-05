package ru.zeroapps.mathtrain.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ru.zeroapps.mathtrain.*;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

public class FinishSceneController {
    @FXML
    private BorderPane mainPane;
    @FXML
    private GridPane statsBox;
    @FXML
    private Label correctAnswerCounter;
    @FXML
    private Label wrongAnswersCounter;
    @FXML
    private Label skippedTasksCounter;
    @FXML
    private Label targetPlate;
    @FXML
    private Label gradePlate;
    @FXML
    private Label timePlate;
    @FXML
    private Spinner<Integer> answerSpinner;
    @FXML
    private HBox bottomBox;
    @FXML
    private ProgressBar targetProgressBar;
    @FXML
    private Label progressPlate;
    @FXML
    private Label finishPlate;
    @FXML
    private ImageView finalImageView;

    Statistics statistics;
    TasksStatistics tasksStatistics;
    private final Animator animator = new Animator();

    public void exitButtonPressed() {
        Main.logger.log(Level.INFO, "launching startScene");

        animator.playFadeAnimation(mainPane, 500, 1, 0, event -> {
            try {
                Stage stage = (Stage) finishPlate.getScene().getWindow();
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

    public void doAnimation() {
        Main.logger.log(Level.INFO, "process animation");

        animator.playTranslateAnimation(bottomBox, 1000, 1500, Animator.MODE_BY, 0, 50);
        animator.playFadeAnimation(bottomBox, 1000, 1500, 1, 0);

        animator.playScaleAnimation(finishPlate, 1000, 1500, 0.4, 0.4);
        animator.playTranslateAnimation(finishPlate, 1000, 1500, Animator.MODE_BY, 0, -140);

        animator.playTranslateAnimation(targetProgressBar, 1000, 1500, Animator.MODE_BY, 0, 40);
        animator.playHeightAnimation(targetProgressBar, 1000, 1500, 12, 20);
        animator.playTranslateAnimation(progressPlate, 1000, 1500, Animator.MODE_BY, 0, 30);

        animator.playTranslateAnimation(statsBox, 2500, 1000, Animator.MODE_TO, statsBox.getTranslateX(), 0);
        animator.playFadeAnimation(statsBox, 2500, 1000, 0, 1);

        animator.playFadeAnimation(finalImageView, 3500, 1500, 0, 1);
    }

    public void setStatistics(Statistics stats, TasksStatistics tasksStats) {
        correctAnswerCounter.setText(String.valueOf(stats.getCorrectAnswers()));
        wrongAnswersCounter.setText(String.valueOf(stats.getWrongAnswers()));
        skippedTasksCounter.setText(String.valueOf(stats.getSkippedTasks()));
        targetPlate.setText(String.valueOf(stats.getTarget()));
        gradePlate.setText(String.valueOf(stats.getGrade().getNumber()));
        timePlate.setText(LocalTime.ofSecondOfDay(stats.getTimeSpentMillis()/1000).format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        statistics = stats;
        tasksStatistics = tasksStats;

        StatsWriter.writeStatisticsSafely(stats, tasksStats);
    }

    @FXML
    public void initialize() {
        Main.logger.log(Level.INFO, "initializing FinishSceneController");

        answerSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);

        finalImageView.setOpacity(0.0);
        Image image = ResourcesHelper.getRandomGIFFromDirectory(
                "/images/correct/",
                ResourcesHelper.CORRECT_ANSWER_IMAGES_NUMBER
        );
        finalImageView.setImage(image);

        doAnimation();
    }
}
