import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.net.URL;


public class Soundplayer {
    private MediaPlayer correctPlayer = null;
    private MediaPlayer wrongPlayer = null;

    public Soundplayer() {
        try {
            URL correctUrl = getClass().getResource("/sounds/correct.mp3");
            if (correctUrl != null) {
                Media correctSound = new Media(correctUrl.toExternalForm());
                correctPlayer = new MediaPlayer(correctSound);
            } else {
                java.io.File f = new java.io.File("sounds/correct.mp3");
                if (f.exists()) {
                    Media correctSound = new Media(f.toURI().toString());
                    correctPlayer = new MediaPlayer(correctSound);
                }
            }
        } catch (Exception e) {
        }
        try {
            URL wrongUrl = getClass().getResource("/sounds/wrong.mp3");
            if (wrongUrl != null) {
                Media wrongSound = new Media(wrongUrl.toExternalForm());
                wrongPlayer = new MediaPlayer(wrongSound);
            } else {
                java.io.File f = new java.io.File("sounds/wrong.mp3");
                if (f.exists()) {
                    Media wrongSound = new Media(f.toURI().toString());
                    wrongPlayer = new MediaPlayer(wrongSound);
                }
            }
        } catch (Exception e) {
        }
    }

    private void playSound(MediaPlayer player) {
        if (player == null) return;
        try {
            player.stop();
            player.setStartTime(Duration.ZERO);
            player.play();
        } catch (Exception e) {
        }
    }

    public void playCorrect() {
        playSound(correctPlayer);
    }

    public void playWrong() {
        playSound(wrongPlayer);
    }
}