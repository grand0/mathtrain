package ru.zeroapps.mathtrain;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.util.ArrayList;

public class Animator {
    private final ArrayList<Animation> animations = new ArrayList<>();

    public static final int MODE_TO = 0;
    public static final int MODE_BY = 1;

    private static double animationsSpeed = 1.0;

    /**
     * Plays fade animation, changing opacity of <code>node</code> from <code>1</code> to <code>0</code>
     * in <code>durMillis</code> milliseconds.
     *
     * @param node node on which the animation will be performed.
     * @param durMillis animation duration (in milliseconds).
     *
     * @see #playFadeAnimation(Node, int, int, int)
     * @see #playFadeAnimation(Node, int, int, int, EventHandler)
     * @see #playFadeAnimation(Node, int, int, int, int, EventHandler)
     */
    public void playFadeAnimation(Node node, int durMillis) {
        if (animationsSpeed == 1/Double.MAX_VALUE) {
            node.setOpacity(0);
            return;
        }
        else durMillis *= animationsSpeed;

        FadeTransition fade = new FadeTransition(Duration.millis(durMillis), node);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.play();

        animations.add(fade);
    }

    /**
     * Plays fade animation, changing opacity of <code>node</code> from <code>from</code> to <code>to</code>
     * in <code>durMillis</code> milliseconds.
     *
     * @param node node on which the animation will be performed.
     * @param durMillis animation duration (in milliseconds).
     * @param from opacity start value.
     * @param to opacity end value.
     *
     * @see #playFadeAnimation(Node, int)
     * @see #playFadeAnimation(Node, int, int, int, EventHandler)
     * @see #playFadeAnimation(Node, int, int, int, int, EventHandler)
     */
    public void playFadeAnimation(Node node, int durMillis, int from, int to) {
        if (animationsSpeed == 1/Double.MAX_VALUE) {
            node.setOpacity(to);
            return;
        }
        else durMillis *= animationsSpeed;

        FadeTransition fade = new FadeTransition(Duration.millis(durMillis), node);
        fade.setFromValue(from);
        fade.setToValue(to);
        fade.play();

        animations.add(fade);
    }

    public void playFadeAnimation(Node node, int delayMillis, int durMillis, int from, int to) {
        if (animationsSpeed == 1/Double.MAX_VALUE) {
            node.setOpacity(to);
            return;
        }
        else {
            durMillis *= animationsSpeed;
            delayMillis *= animationsSpeed;
        }

        FadeTransition fade = new FadeTransition(Duration.millis(durMillis), node);
        fade.setFromValue(from);
        fade.setToValue(to);
        fade.setDelay(Duration.millis(delayMillis));
        fade.play();

        animations.add(fade);
    }

    /**
     * Plays fade animation, changing opacity of <code>node</code> from <code>from</code> to <code>to</code>
     * in <code>durMillis</code> milliseconds. When the animation finishes, execute <code>onFinished</code>.
     *
     * @param node node on which the animation will be performed.
     * @param durMillis animation duration (in milliseconds).
     * @param from opacity start value.
     * @param to opacity end value.
     * @param onFinished actions to be executed on animation end.
     *
     * @see #playFadeAnimation(Node, int)
     * @see #playFadeAnimation(Node, int, int, int)
     * @see #playFadeAnimation(Node, int, int, int, int, EventHandler)
     */
    public void playFadeAnimation(Node node, int durMillis, int from, int to, EventHandler<ActionEvent> onFinished) {
        if (animationsSpeed == 1/Double.MAX_VALUE) {
            node.setOpacity(to);
            onFinished.handle(null);
            return;
        }
        else durMillis *= animationsSpeed;

        FadeTransition fade = new FadeTransition(Duration.millis(durMillis), node);
        fade.setFromValue(from);
        fade.setToValue(to);
        fade.setOnFinished(onFinished);
        fade.play();

        animations.add(fade);
    }

    /**
     * Plays fade animation, changing opacity of <code>node</code> from <code>from</code> to <code>to</code>
     * in <code>durMillis</code> milliseconds. Before the animation starts there is a delay in <code>delayMillis</code>
     * milliseconds. When the animation finishes, execute <code>onFinished</code>.
     *
     * @param node node on which the animation will be performed.
     * @param delayMillis delay before animation starts (in milliseconds).
     * @param durMillis animation duration (in milliseconds).
     * @param from opacity start value.
     * @param to opacity end value.
     * @param onFinished actions to be executed on animation end.
     *
     * @see #playFadeAnimation(Node, int)
     * @see #playFadeAnimation(Node, int, int, int)
     * @see #playFadeAnimation(Node, int, int, int, EventHandler)
     */
    public void playFadeAnimation(Node node, int delayMillis, int durMillis, int from, int to, EventHandler<ActionEvent> onFinished) {
        if (animationsSpeed == 1/Double.MAX_VALUE) {
            node.setOpacity(to);
            onFinished.handle(null);
            return;
        }
        else {
            durMillis *= animationsSpeed;
            delayMillis *= animationsSpeed;
        }

        FadeTransition fade = new FadeTransition(Duration.millis(durMillis), node);
        fade.setFromValue(from);
        fade.setToValue(to);
        fade.setOnFinished(onFinished);
        fade.setDelay(Duration.millis(delayMillis));
        fade.play();

        animations.add(fade);
    }

    /**
     * Plays text fill animation, which changes color of <code>label</code> for <code>durMillis</code> milliseconds from
     * <code>fromColor</code> to <code>toColor</code>. When the animation finishes, execute <code>onFinished</code>.
     *
     * @param label <code>Label</code> node on which the animation will be performed.
     * @param durMillis animation duration (in milliseconds).
     * @param fromColor <code>String</code> representation of start color.
     * @param toColor <code>String</code> representation of end color
     * @param onFinished actions to be executed on animation end.
     *
     * @see Color#valueOf(String)
     */
    public void playTextFillAnimation(Label label, int durMillis, String fromColor, String toColor, EventHandler<ActionEvent> onFinished) {
        if (animationsSpeed == 1/Double.MAX_VALUE) {
            label.setTextFill(Paint.valueOf(toColor));
            onFinished.handle(null);
            return;
        }
        else durMillis *= animationsSpeed;

        label.setTextFill(Paint.valueOf("white"));
        int finalDurMillis = durMillis;
        Transition trans = new Transition() {
            {
                setCycleDuration(Duration.millis(finalDurMillis));
            }

            @Override
            protected void interpolate(double frac) {
                Color from = Color.valueOf(fromColor);
                Color to = Color.valueOf(toColor);
                double redDiff = to.getRed() - from.getRed();
                double greenDiff = to.getGreen() - from.getGreen();
                double blueDiff = to.getBlue() - from.getBlue();
                int nowRed = (int) ((from.getRed() + redDiff * frac) * 255.0);
                int nowGreen = (int) ((from.getGreen() + greenDiff * frac) * 255.0);
                int nowBlue = (int) ((from.getBlue() + blueDiff * frac) * 255.0);
                String colorValue = "rgb(" + nowRed + "," + nowGreen + "," + nowBlue + ")";
                Paint paint = Paint.valueOf(colorValue);
                label.setTextFill(paint);
            }
        };
        trans.setOnFinished(onFinished);
        trans.play();

        animations.add(trans);
    }

    /**
     * Plays animation on <code>ProgressBar</code> node. Changes the progress of a <code>bar</code> to
     * <code>progress</code> in <code>durMillis</code> milliseconds. When the animation finishes,
     * execute <code>onFinished</code>.
     *
     * @param bar <code>ProgressBar</code> node on which the animation will be performed.
     * @param durMillis animation duration (in milliseconds).
     * @param progress progress to which the <code>bar</code> will be set.
     * @param onFinished actions to be executed on animation end.
     */
    public void playProgressBarAnimation(ProgressBar bar, int durMillis, double progress, EventHandler<ActionEvent> onFinished) {
        if (animationsSpeed == 1/Double.MAX_VALUE) {
            bar.setProgress(progress);
            onFinished.handle(null);
            return;
        }
        else durMillis *= animationsSpeed;

        double startProgress = bar.getProgress();
        int finalDurMillis = durMillis;
        Transition trans = new Transition() {
            {
                setCycleDuration(Duration.millis(finalDurMillis));
            }
            @Override
            protected void interpolate(double frac) {
                double diff = progress - startProgress;
                bar.setProgress(startProgress + diff * frac);
            }
        };
        trans.setOnFinished(onFinished);
        trans.play();

        animations.add(trans);
    }

    public void playTranslateAnimation(Node node, int delayMillis, int durMillis, int mode, double x, double y) {
        if (animationsSpeed == 1/Double.MAX_VALUE) {
            if (mode == MODE_TO) {
                node.setTranslateX(x);
                node.setTranslateY(y);
            } else if (mode == MODE_BY) {
                node.setTranslateX(node.getTranslateX() + x);
                node.setTranslateY(node.getTranslateY() + y);
            } else {
                throw new IllegalArgumentException("No such mode");
            }
            return;
        }
        else {
            durMillis *= animationsSpeed;
            delayMillis *= animationsSpeed;
        }

        TranslateTransition tt = new TranslateTransition(Duration.millis(durMillis), node);
        tt.setDelay(Duration.millis(delayMillis));
        if (mode == MODE_TO) {
            tt.setToX(x);
            tt.setToY(y);
        } else if (mode == MODE_BY) {
            tt.setByX(x);
            tt.setByY(y);
        } else {
            throw new IllegalArgumentException("No such mode");
        }
        tt.play();

        animations.add(tt);
    }

    public void playScaleAnimation(Node node, int delayMillis, int durMillis, double toX, double toY) {
        if (animationsSpeed == 1/Double.MAX_VALUE) {
            node.setScaleX(toX);
            node.setScaleY(toY);
            return;
        }
        else {
            durMillis *= animationsSpeed;
            delayMillis *= animationsSpeed;
        }

        ScaleTransition st = new ScaleTransition(Duration.millis(durMillis), node);
        st.setDelay(Duration.millis(delayMillis));
        st.setToX(toX);
        st.setToY(toY);
        st.play();

        animations.add(st);
    }

    public void playHeightAnimation(Region node, int delayMillis, int durMillis, double from, double to) {
        if (animationsSpeed == 1/Double.MAX_VALUE) {
            node.setPrefHeight(to);
            return;
        }
        else {
            durMillis *= animationsSpeed;
            delayMillis *= animationsSpeed;
        }

        int finalDurMillis = durMillis;
        Transition ht = new Transition() {
            {
                setCycleDuration(Duration.millis(finalDurMillis));
            }
            @Override
            protected void interpolate(double frac) {
                node.setPrefHeight(from + (to - from) * frac);
            }
        };
        ht.setDelay(Duration.millis(delayMillis));
        ht.play();

        animations.add(ht);
    }

    public void playRotateAnimationIndependently(Node node, int durMillis, double byAngle, double x, double y, double z) {
        RotateTransition rt = new RotateTransition(Duration.millis(durMillis), node);
        rt.setByAngle(byAngle);
        rt.setAxis(new Point3D(x, y, z));
        rt.play();

        animations.add(rt);
    }

    public static void setAnimationsSpeed(double animationsSpeed) {
        Animator.animationsSpeed = 1/animationsSpeed;
    }

    /**
     * Finishes all animations, animations' <code>onFinished</code> actions will not be executed.
     */
    public void finishAllAnimations() {
        for (Animation a :
                animations) {
            a.jumpTo("end");
            a.stop();
        }
        animations.clear();
    }
}
