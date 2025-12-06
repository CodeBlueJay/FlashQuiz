import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class Boss extends VBox {
    private int health;
    private int bossHealth;
    private String[] words;
    private String[] meanings;
    private boolean mcq;
    private final ProgressBar bossHp;
    private final ProgressBar playerHp;

    public Boss(String[] w, String[] m, boolean isMcq) {
        super(10);
        setPadding(new Insets(16));
        this.health = 5;
        this.bossHealth = 5;
        this.words = w;
        this.meanings = m;
        this.mcq = isMcq;

        this.playerHp = new ProgressBar(1.0);
        this.bossHp = new ProgressBar(1.0);

        getChildren().addAll(
            new Label("Player HP"),
            playerHp,
            new Label("Boss HP"),
            bossHp
        );
    }
}