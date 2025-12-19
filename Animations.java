import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Animations {

    public static void fadeIn(Node node) {
        node.setOpacity(0);
        FadeTransition ft = new FadeTransition(Duration.millis(320), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public static void applyButtonHover(Button btn) {
        if (btn == null) return;
        final DropShadow hoverShadow = new DropShadow(10, Color.rgb(2,6,23,0.12));
        btn.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), btn);
            st.setToX(1.04);
            st.setToY(1.04);
            st.play();
            btn.setEffect(hoverShadow);
        });
        btn.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(120), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
            btn.setEffect(null);
        });
    }

    public static void applyCardHover(Node n) {
        if (n == null) return;
        n.setOnMouseEntered(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), n);
            tt.setToY(-4);
            tt.play();
            ScaleTransition st = new ScaleTransition(Duration.millis(140), n);
            st.setToX(1.02);
            st.setToY(1.02);
            st.play();
        });
        n.setOnMouseExited(e -> {
            TranslateTransition tt = new TranslateTransition(Duration.millis(140), n);
            tt.setToY(0);
            tt.play();
            ScaleTransition st = new ScaleTransition(Duration.millis(140), n);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
    }
}
