import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import javafx.scene.control.ProgressBar;

public class Boss extends VBox {
    private int health;
    private int bossHealth;
    private ArrayList<String> words;
    private ArrayList<String> meanings;
    private boolean mcq;
    private final ProgressBar bossHp;
    private final ProgressBar playerHp;


    public Boss(ArrayList<String> w, ArrayList<String> m, boolean isMcq) {
        setSpacing(10);
        setPadding(new Insets(16));
        health = 5;
        bossHealth = 5;
        words = w;
        meanings = m;
        mcq = isMcq;
        bossHp = new ProgressBar(); 
        playerHp = new ProgressBar();

        Label title = new Label("Boss Battle");
        Label info = new Label("Cards: " + (words != null ? words.size() : 0));
        Label playerHpUI = new Label("Player HP: " + health);
        Label bossHpUI = new Label("Boss HP: " + bossHealth);
        getChildren().addAll(title, info, playerHp, bossHp, bossHpUI, playerHpUI);

    }

    public boolean checkHp(int hp) {
        return hp == 0;
    }

    public void endGame() {
        //finish animations and delete boss off the screen.
    }
}