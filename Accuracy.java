import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.text.Font;
import java.util.Random;

public class Accuracy extends VBox {
    private ArrayList<String> words;
    private ArrayList<String> meanings;
    private ArrayList<Double> weights;
    private int questionsCorrect;
    private double time;
    private double startTime = 7.0;
    private EXPBarUI expBar;
    private String correctAnswer;
    private String ans;
    private Timeline timeline;
    private boolean started;
    private Random rng = new Random();
    private int lastIndex = -1;
    private double addTime;
    private int XPadd;
    // more gui components
    private Label accuracylabel = new Label("Accuracy");
    private VBox container = new VBox(8);
    private Label feedback = new Label();
    private TextField answer = new TextField();
    private Button submit = new Button("Submit");
    private Button start = new Button("Start");
    private Label showTimer = new Label();
    private HBox buttonBox = new HBox(2);
    private Label score = new Label("Score: " + questionsCorrect);
    private Label definition = new Label("Definition: ");
    private Soundplayer soundplayer = new Soundplayer();
    private Button next = new Button("Next");
    Font microwave;
    private HBox timerButtons = new HBox(4);
    {
        microwave = Font.loadFont("file:fonts/microwave.ttf", 36);
    }

    public Accuracy(ArrayList<String> w, ArrayList<String> m, ArrayList<Double> we, EXPBarUI exp) {
        words = w;
        meanings = m;
        weights = we;
        questionsCorrect = 0;
        time = startTime;
        started = false;
        expBar = exp;
        timeline = new Timeline();
        addTime = 10;
        XPadd = 5;
        setSpacing(10);
        setPadding(new Insets(16));
        answer.getStyleClass().add("answer");
        answer.setDisable(!started);
        answer.setOnAction(ev -> {
            if (!submit.isDisabled()) {
                submit.fire();
            }
        });
        next.setVisible(started);
        showTimer.setText(String.format("%.2f", time));
        showTimer.getStyleClass().add("timer");
        score.getStyleClass().add("score");
        submit.setDisable(!started);
        if (microwave != null) {
            showTimer.setFont(microwave);
        }
        buttonBox.getChildren().addAll(submit, next);
        container.getChildren().addAll(answer, definition, feedback, buttonBox);
        timerButtons.getChildren().addAll(showTimer, start);
        getChildren().addAll(accuracylabel, score, timerButtons, container);

        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ans = "";
                if (!(answer.getText() == null)) {
                    ans = answer.getText();
                } else {
                    ans = "";
                }
                answer.setText(ans);
                if (ans.toLowerCase().equals(correctAnswer.toLowerCase())) {
                    if (addTime > 3) {
                        addTime -= 0.5;
                    }
                    questionsCorrect++;
                    time += addTime;
                    score.setText("Score: " + questionsCorrect);
                    expBar.addXP(XPadd);
                    XPadd += 5;
                    feedback.setText("Correct!");
                    soundplayer.playCorrect();
                    next.setVisible(true);
                    submit.setDisable(true);
                    next.requestFocus();
                } else {
                    feedback.setText("Incorrect! The correct answer was: " + correctAnswer);
                    soundplayer.playWrong();
                    next.setVisible(true);
                    submit.setDisable(true);
                    next.requestFocus();
                }
            }
        });
        next.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                next.setVisible(false);
                answer.clear();
                feedback.setText("");
                submit.setDisable(false);
                answer.requestFocus();
                mainLoop();
            }
        });
        timer();
    }
    public void timer() {
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mainLoop();
                started = true;
                answer.setDisable(false);
                submit.setDisable(false);
                start.setDisable(true);
                answer.requestFocus();
                if (timeline != null) {
                    timeline.stop();
                }
                time = startTime;
                showTimer.setText(String.format("%.2f", time));
                timeline = new Timeline(new KeyFrame(Duration.millis(10), ev -> {
                    time -= 0.01;
                    if (time <= 0) {
                        showTimer.setText("0.00");
                        timeline.stop();
                        feedback.setText("Time's Up!");
                        answer.setDisable(true);
                        submit.setDisable(true);
                        start.setDisable(false);
                        started = false;
                    } else {
                        showTimer.setText(String.format("%.2f", time));
                    }
                }));
                timeline.setCycleCount(Timeline.INDEFINITE); 
                timeline.play();
            }
        });
    }

    private void mainLoop() {
        int index = Utils.weightedIndex(weights, words);
        int attempts = 0;
        while (index == lastIndex && attempts < 8) {
            index = Utils.weightedIndex(weights, words);
            attempts++;
        }
        if (index < 0 || index >= words.size()) {
            if (words == null || words.size() == 0) {
                return;
            }
            index = rng.nextInt(words.size());
        }
        lastIndex = index;
        correctAnswer = words.get(index);
        definition.setText("Definition: " + meanings.get(index));
        
    }
}