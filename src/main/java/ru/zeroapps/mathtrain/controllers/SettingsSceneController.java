package ru.zeroapps.mathtrain.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import ru.zeroapps.mathtrain.Animator;
import ru.zeroapps.mathtrain.Defaults;
import ru.zeroapps.mathtrain.Main;
import ru.zeroapps.mathtrain.dialogs.AboutDialog;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.prefs.Preferences;

public class SettingsSceneController {
    @FXML
    private CheckBox animationsOffCheckBox;
    @FXML
    private Slider animationSpeedSlider;
    @FXML
    private GridPane mainPane;
    @FXML
    private TextField statsDirectoryTextField;
    @FXML
    private TextField statsFileTextField;
    @FXML
    private TextField seedTextField;
    private final Animator animator = new Animator();

    public void onBrowseButtonAction(ActionEvent actionEvent) {
        Main.logger.log(Level.INFO, "showing DirectoryChooser dialog");

        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Выберите папку");
        chooser.setInitialDirectory(new File(statsDirectoryTextField.getText()));
        File selected = chooser.showDialog(statsDirectoryTextField.getScene().getWindow());
        if (selected != null) statsDirectoryTextField.setText(selected.getAbsolutePath());
    }

    public void onCancelButtonAction(ActionEvent actionEvent) throws IOException {
        Main.logger.log(Level.INFO, "cancel changes in settings");

        returnToStartScene();
    }

    public void onSaveButtonAction(ActionEvent actionEvent) throws IOException {
        Main.logger.log(Level.INFO, "save settings");

        if (statsFileTextField.getText().isBlank()) {
            statsFileTextField.setStyle("-fx-border-color: #F00");
            return;
        }

        Preferences prefs = Preferences.userNodeForPackage(ru.zeroapps.mathtrain.Main.class);
        prefs.put(Defaults.PREFS_STATS_DIR_ENTRY.getValue(), statsDirectoryTextField.getText());
        prefs.put(Defaults.PREFS_STATS_FILE_ENTRY.getValue(), statsFileTextField.getText());
        prefs.put(Defaults.PREFS_SEED_ENTRY.getValue(), seedTextField.getText());
        double speed = animationsOffCheckBox.isSelected() ? Double.MAX_VALUE : animationSpeedSlider.getValue();
        prefs.putDouble(Defaults.PREFS_ANIMATION_SPEED_ENTRY.getValue(), speed);
        Animator.setAnimationsSpeed(speed);

        returnToStartScene();
    }

    private void returnToStartScene() {
        Main.logger.log(Level.INFO, "launching startScene");

        animator.playFadeAnimation(mainPane, 500, 1, 0, event -> {
            try {
                Stage stage = (Stage) statsDirectoryTextField.getScene().getWindow();
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

    public void onResetButtonAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Сброс");
        alert.setHeaderText(null);
        alert.setContentText("Вы уверены что хотите сбросить настройки к первоначальным?");
        alert.initStyle(StageStyle.UTILITY);
        ButtonType yesButtonType = new ButtonType("Да", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButtonType = new ButtonType("Нет", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yesButtonType, noButtonType);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty() || result.get() != yesButtonType) return;

        Main.logger.log(Level.INFO, "reset settings");

        statsDirectoryTextField.setText(Defaults.STATS_DIR_PATH.getValue());
        statsFileTextField.setText(Defaults.STATS_FILE_NAME.getValue());
        seedTextField.setText("");
        animationSpeedSlider.adjustValue(Double.parseDouble(Defaults.ANIMATION_SPEED.getValue()));
    }

    public void onAnimationsOffAction(ActionEvent actionEvent) {
        animationSpeedSlider.setDisable(animationsOffCheckBox.isSelected());
    }

    @FXML
    public void initialize() {
        mainPane.setOpacity(0);

        Main.logger.log(Level.INFO, "initializing SettingsSceneController");

        Preferences prefs = Preferences.userNodeForPackage(ru.zeroapps.mathtrain.Main.class);
        String statsDirPath = prefs.get(Defaults.PREFS_STATS_DIR_ENTRY.getValue(), Defaults.STATS_DIR_PATH.getValue());
        String statsFileName = prefs.get(Defaults.PREFS_STATS_FILE_ENTRY.getValue(), Defaults.STATS_FILE_NAME.getValue());
        String seed = prefs.get(Defaults.PREFS_SEED_ENTRY.getValue(), "");
        double animationSpeed = prefs.getDouble(Defaults.PREFS_ANIMATION_SPEED_ENTRY.getValue(),
                Double.parseDouble(Defaults.ANIMATION_SPEED.getValue()));
        statsDirectoryTextField.setText(statsDirPath);
        statsFileTextField.setText(statsFileName);
        seedTextField.setText(seed);
        animationSpeedSlider.setDisable(animationSpeed == Double.MAX_VALUE);
        animationSpeedSlider.adjustValue(animationSpeed);
        animationsOffCheckBox.setSelected(animationSpeed == Double.MAX_VALUE);

        statsFileTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(".*[\\\\/|:?*\"<>]+.*")) statsFileTextField.setText(oldValue);
            statsFileTextField.setStyle("");
        });

        animator.playFadeAnimation(mainPane, 500, 0, 1);
    }

    public void onAboutButtonAction(ActionEvent actionEvent) throws IOException {
        Window window = mainPane.getScene().getWindow();
        AboutDialog dialog = new AboutDialog(window);
//        dialog.setOnCloseRequest(event -> dialog.close());
        dialog.show();
    }
}
