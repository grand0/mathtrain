package ru.zeroapps.mathtrain.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.zeroapps.mathtrain.Animator;
import ru.zeroapps.mathtrain.Grade;
import ru.zeroapps.mathtrain.Main;

import java.io.IOException;
import java.util.logging.Level;

public class StartSceneController {
    @FXML
    private GridPane mainPane;
    @FXML
    private ImageView settingsButton;
    @FXML
    private TextField pupilNameTextField;
    @FXML
    private CheckBox infiniteModeCheckBox;
    @FXML
    private Spinner<Integer> targetSpinner;
    @FXML
    private ComboBox<String> gradeComboBox;
    @FXML
    private Button startButton;

    private final Animator animator = new Animator();

    public void startTraining() {
        Main.logger.log(Level.INFO, "starting training");

        boolean cantContinue = false;

        if (!infiniteModeCheckBox.isSelected() && (targetSpinner.getEditor().getText().equals("0") ||
                targetSpinner.getEditor().getText().equals(""))) {
            targetSpinner.setStyle("-fx-border-color: #F00");
            cantContinue = true;
        }
        if (pupilNameTextField.getText().equals("")) {
            pupilNameTextField.setStyle("-fx-border-color: #F00");
            cantContinue = true;
        }

        if (cantContinue) {
            Main.logger.log(Level.INFO, "some fields are not filled, one must fill them in order to continue");
            return;
        }

        String gradeStr = gradeComboBox.getValue();
        int grade = Grade.getVariableByLabel(gradeStr).getNumber();
        if (grade == 0) {
            UnsupportedOperationException throwable = new UnsupportedOperationException("Unexpected value: " + grade);
            Main.logger.log(Level.SEVERE, "unexpected value", throwable);
            throw throwable;
        }

        Main.logger.log(Level.INFO, "launching trainingScene");

        animator.playFadeAnimation(mainPane, 500, 1, 0, event -> {
            try {
                Stage stage = (Stage) startButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/trainingScene.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                TrainingSceneController con = loader.getController();
                int target = infiniteModeCheckBox.isSelected() ? 0 : targetSpinner.getValue();
                con.setupController(Grade.getVariableByLabel(gradeStr), target, pupilNameTextField.getText());
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void onInfiniteModeAction() {
        targetSpinner.setDisable(infiniteModeCheckBox.isSelected());
        targetSpinner.setStyle("");
    }

    @FXML
    public void initialize() {
        mainPane.setOpacity(0);

        Main.logger.log(Level.INFO, "initializing StartSceneController");

        gradeComboBox.getItems().removeAll();
        gradeComboBox.getItems().addAll(Grade.getEveryLabel());
        gradeComboBox.getSelectionModel().select(Grade.GRADE_1.getLabel());

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        targetSpinner.setValueFactory(valueFactory);
        targetSpinner.getStyleClass().add(Spinner.STYLE_CLASS_ARROWS_ON_RIGHT_HORIZONTAL);
        targetSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) targetSpinner.getEditor().setText(oldValue);
            if (newValue.equals("")) targetSpinner.getEditor().setText("0");
            if (newValue.length() > 1 && newValue.charAt(0) == '0') targetSpinner.getEditor().setText(newValue.substring(1));
            targetSpinner.setStyle("");
        });

        pupilNameTextField.textProperty().addListener((observable, oldValue, newValue) -> pupilNameTextField.setStyle(""));

        animator.playFadeAnimation(mainPane, 500, 0, 1);
    }

    public void onSettingsButtonMouseClicked(MouseEvent mouseEvent) {
        Main.logger.log(Level.INFO, "launching settingsScene");

        animator.playFadeAnimation(mainPane, 500, 1, 0, event -> {
            try {
                Stage stage = (Stage) startButton.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/settingsScene.fxml"));
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

    public void onSettingsButtonMousePressed(MouseEvent mouseEvent) {
        settingsButton.setOpacity(0.6);
    }

    public void onSettingsButtonMouseReleased(MouseEvent mouseEvent) {
        if (settingsButton.isHover()) settingsButton.setOpacity(1.0);
        else settingsButton.setOpacity(0.2);
    }

    public void onSettingsButtonMouseEntered(MouseEvent mouseEvent) {
        settingsButton.setOpacity(1.0);
    }

    public void onSettingsButtonMouseExited(MouseEvent mouseEvent) {
        settingsButton.setOpacity(0.2);
    }
}
