import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Boss extends VBox {
    private int health;
    private int bossHealth;
    private ArrayList<String> words;
    private ArrayList<String> meanings;
    private boolean mcq;
    private int maxBoss;
    private int max;
    private final ProgressBar bossHp;
    private final ProgressBar playerHp;
    private final DoubleProperty bossProgress = new SimpleDoubleProperty(1);
    private final DoubleProperty playerProgress = new SimpleDoubleProperty(1);
    private Label playerHpUI;
    private Label bossHpUI;

    public Boss(ArrayList<String> w, ArrayList<String> m, boolean isMcq) {
        setSpacing(10);
        setPadding(new Insets(16));
        health = 5;
        bossHealth = 5;
        max = health;
        maxBoss = bossHealth;
        words = w;
        meanings = m;
        mcq = isMcq;
        bossHp = new ProgressBar(); 
        playerHp = new ProgressBar();
        bossHp.progressProperty().bind(bossProgress);
        playerHp.progressProperty().bind(playerProgress);
        playerHpUI = new Label("Player HP: " + health + "/" + max);
        bossHpUI = new Label("Boss HP: " + bossHealth + "/" + maxBoss);
        Label title = new Label("Boss Battle");
        Label info = new Label("Cards: " + (words != null ? words.size() : 0));
        HBox playerBar = new HBox(playerHpUI, playerHp);
        HBox bossBar = new HBox(bossHpUI, bossHp);
        getChildren().addAll(title, info, playerBar, bossBar);
        dmgBoss();
    }

    public void updateHp(Label progressUI, int hp, String object, int m) {
        progressUI.setText(object + " HP: " + hp + "/" + m);
    }

    public void dmgBoss() {
        bossHealth--;
        bossProgress.set((double) bossHealth/maxBoss);
        updateHp(bossHpUI, bossHealth, "Boss", maxBoss);
    }

    public void dmgPlayer() {
        health--;
        playerProgress.set((double) health/max);
        updateHp(playerHpUI, health, "Player", max);
    }

    public boolean checkHp(int hp) {
        return hp == 0;
    }

    public void endGame() {
        //finish animations and delete boss off the screen.
    }
}