package ru.zeroapps.mathtrain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public class Main extends Application {
    public static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("/logging.properties"));

        Preferences prefs = Preferences.userNodeForPackage(ru.zeroapps.mathtrain.Main.class);
        double animationsSpeed = prefs.getDouble(Defaults.PREFS_ANIMATION_SPEED_ENTRY.getValue(),
                Double.parseDouble(Defaults.ANIMATION_SPEED.getValue()));
        Animator.setAnimationsSpeed(animationsSpeed);

        logger.log(Level.INFO, "launching startScene");

        Parent root = FXMLLoader.load(getClass().getResource("/scenes/startScene.fxml"));
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Математический тренажёр");
        stage.setScene(scene);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/icon.png")));
        stage.show();
    }
}
