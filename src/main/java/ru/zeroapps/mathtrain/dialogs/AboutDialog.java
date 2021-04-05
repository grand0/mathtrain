package ru.zeroapps.mathtrain.dialogs;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Window;
import ru.zeroapps.mathtrain.Animator;
import ru.zeroapps.mathtrain.Main;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class AboutDialog extends Dialog<Boolean> {
    @FXML
    private TextArea textArea;
    @FXML
    private ImageView logoImageView;

    public AboutDialog(Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/aboutDialog.fxml"));
            loader.setController(this);
            DialogPane dialogPane = loader.load();
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setResizable(true);
            setTitle("О программе");
            setDialogPane(dialogPane);
            setResultConverter(buttonType -> true);
            setOnShowing(dialogEvent -> Platform.runLater(this::initialize));
            setOnCloseRequest(dialogEvent -> close());
        } catch (IOException e) {
            Main.logger.log(Level.SEVERE, "couldn't show 'about' dialog", e);
            e.printStackTrace();
        }
    }

    public void initialize() {
        logoImageView.setOnMouseClicked(this::onLogoClicked);

        try {
            InputStream is = Main.class.getResourceAsStream("/texts/about.txt");
            String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            textArea.setText(text);
        } catch (IOException e) {
            Main.logger.log(Level.SEVERE, "couldn't load 'about' text", e);
        }
    }

    private void onLogoClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            logoImageView.setRotate(0.0);
            int x = 0;
            int y = 0;
            int z = 0;
            while (x == 0 && y == 0 && z == 0) {
                x = (int) (Math.random() * 3 - 1);
                y = (int) (Math.random() * 3 - 1);
                z = (int) (Math.random() * 3 - 1);
            }
            new Animator().playRotateAnimationIndependently(
                    logoImageView, 2000, 360, x, y, z);
        }
    }
}
