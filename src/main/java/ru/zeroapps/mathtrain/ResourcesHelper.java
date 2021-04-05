package ru.zeroapps.mathtrain;

import javafx.scene.image.Image;

public class ResourcesHelper {
    public static final int CORRECT_ANSWER_IMAGES_NUMBER = 6;
    public static final int WRONG_ANSWER_IMAGES_NUMBER = 4;

    public static Image getRandomGIFFromDirectory(String dir, int numberOfImages) {
        int n = (int) (Math.random() * numberOfImages + 1);
        return new Image(ResourcesHelper.class.getResourceAsStream(dir + n + ".gif"));
    }
}
