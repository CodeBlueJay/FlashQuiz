import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.ArrayList;

public class Boss extends VBox {
    private int health;
    private int bossHealth;
    private ArrayList<String> words;
    private ArrayList<String> meanings;
    private boolean mcq;

    public Boss(ArrayList<String> w, ArrayList<String> m, boolean isMcq) {
        super(10);
        setPadding(new Insets(16));
        health = 5;
        bossHealth = 5;
        words = w;
        meanings = m;
        mcq = isMcq;

        Label title = new Label("Boss Battle");
        Label info = new Label("Cards: " + (words != null ? words.size() : 0));
        getChildren().addAll(title, info);
    }
}