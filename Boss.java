import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

public class Boss {
    private int health;
    private int bossHealth;
    private String[] words;
    private String[] meanings;
    private boolean mcq;
    private EXPBarUI playerHealth;
    private EXPBarUI bossHealth;
    private VBox vbox;
    
    public Boss(String[] w, String[] m, boolean isMcq) {
        health = 5;
        bossHealth = 5;
        words = w;
        meanings = m;
        mcq = isMcq;

        vbox.getChildren().addAll(playerHealth, bossHealth);
    
    }

    


}