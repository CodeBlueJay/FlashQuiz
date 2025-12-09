import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class Learn extends VBox {
    // instance variables
    private boolean text;
    private ArrayList<String> words;
    private ArrayList<Double> weights;
    private ArrayList<String> definitions;
    // user interface components
    private Label titleLabel = new Label("Learn");
    private ToggleButton modeToggle = new ToggleButton("Multiple Choice");
    private Label prompt = new Label("");
    private Label feedback = new Label("");
    private Label progressLabel = new Label("");
    private Button nextBtn = new Button("Next");
    private VBox choicesBox = new VBox(8);
    private Button[] choiceBtns = new Button[] { new Button(), new Button(), new Button(), new Button() };
    private HBox freeBox = new HBox(8);
    private TextField answerField = new TextField();
    private Button submitBtn = new Button("Submit");
    private int currentIndex = -1;
    private boolean askForDefinition = true;
    private boolean awaitingAnswer = true;
    private int totalAsked = 0;
    private int totalCorrect = 0;
    private EXPBarUI expBar;
    private Random rng = new Random();
    private Soundplayer soundPlayer = new Soundplayer();
    private Label streak;
    private int correctStreak;

    public Learn(ArrayList<String> w, ArrayList<Double> we, ArrayList<String> d, EXPBarUI exp) {
        text = false;
        words = w;
        weights = we;
        definitions = d;
        double maxWidth = 360;
        expBar = exp;
        correctStreak = 0;
        streak = new Label("Start a Streak!");
        setSpacing(10);
        setPadding(new Insets(16));
        modeToggle.getStyleClass().add("switch-button");
        for (int i = 0; i < choiceBtns.length; i++) {
            choiceBtns[i].setMaxWidth(maxWidth);
            choicesBox.getChildren().add(choiceBtns[i]);
        }
        freeBox.getChildren().addAll(answerField, submitBtn);

        answerField.setOnAction(e -> submitBtn.fire());

        modeToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                switchText();
                if (isText()) {
                    modeToggle.setText("Free Response");
                } else {
                    modeToggle.setText("Multiple Choice");
                }
                feedback.setText("");
                answerField.clear();
                nextQuestion();
            }
        });
        nextBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                nextQuestion();
            }
        });
        EventHandler<ActionEvent> choiceHandler = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!awaitingAnswer) return;
                Button b = (Button) e.getSource();
                String chosen = b.getText();
                evaluateAnswer(chosen);
            }
        };
        for (int i = 0; i < choiceBtns.length; i++) {
            choiceBtns[i].setOnAction(choiceHandler);
        }

        submitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (!awaitingAnswer) return;
                String ans;
                if (answerField.getText() == null) {
                    ans = "";
                } else {
                    ans = answerField.getText().trim();
                }
                evaluateAnswer(ans);
                if (isText()) {
                    nextBtn.requestFocus();
                }
            }
        });
        getChildren().addAll(titleLabel, modeToggle, progressLabel, streak, prompt, choicesBox, freeBox, feedback, nextBtn);
        nextQuestion();
    }

    public boolean isText() {
        return text;
    }

    public void switchText() {
        text = !text;
    }

    private void nextQuestion() {
        if (words == null || definitions == null || words.size() < 1 || definitions.size() < 1) {
            prompt.setText("No cards available.");
            choicesBox.setVisible(false);
            choicesBox.setManaged(false);
            freeBox.setVisible(false);
            freeBox.setManaged(false);
            nextBtn.setDisable(true);
            return;
        }
        awaitingAnswer = true;
        feedback.setText("");
        nextBtn.setDisable(true);
        answerField.clear();

        askForDefinition = rng.nextBoolean();
        currentIndex = Utils.weightedIndex(weights, words);
        if (currentIndex < 0) {
            prompt.setText("No cards available.");
            return;
        }

        String term = words.get(currentIndex);
        String def = definitions.get(currentIndex);
        if (askForDefinition) {
            prompt.setText("Define: " + term);
        } else {
            prompt.setText("Term for: " + def);
        }

        if (isText()) {
            freeBox.setVisible(true);
            freeBox.setManaged(true);
            choicesBox.setVisible(false);
            choicesBox.setManaged(false);
            // focus the free response input so the user can type immediately
            answerField.requestFocus();
        } else {
            freeBox.setVisible(false);
            freeBox.setManaged(false);
            choicesBox.setVisible(true);
            choicesBox.setManaged(true);
            fillChoices(currentIndex, askForDefinition);
        }

        updateProgressLabel();
    }

    private void fillChoices(int correctIdx, boolean askDef) {
        String correct;
        if (askDef) {
            correct = definitions.get(correctIdx);
        } else {
            correct = words.get(correctIdx);
        }
        ArrayList<String> options = new ArrayList<String>();
        options.add(correct);
        Set<Integer> used = new HashSet<Integer>();
        used.add(Integer.valueOf(correctIdx));
        while (options.size() < 4 && used.size() < words.size()) {
            int candidate = rng.nextInt(words.size());
            if (used.contains(Integer.valueOf(candidate))) continue;
            used.add(Integer.valueOf(candidate));
            String wrong;
            if (askDef) {
                wrong = definitions.get(candidate);
            } else {
                wrong = words.get(candidate);
            }
            if (!options.contains(wrong)) {
                options.add(wrong);
            }
        }
        Collections.shuffle(options);
        for (int i = 0; i < choiceBtns.length; i++) {
            if (i < options.size()) {
                choiceBtns[i].setText(options.get(i));
                choiceBtns[i].setDisable(false);
                choiceBtns[i].setVisible(true);
                choiceBtns[i].setManaged(true);
            } else {
                choiceBtns[i].setText("");
                choiceBtns[i].setDisable(true);
                choiceBtns[i].setVisible(false);
                choiceBtns[i].setManaged(false);
            }
        }
    }

    private void evaluateAnswer(String given) {
        if (currentIndex < 0) return;
        String correct;
        if (askForDefinition) {
            correct = definitions.get(currentIndex);
        } else {
            correct = words.get(currentIndex);
        }
        boolean correctAns;
        if (isText()) {
            String normGiven;
            if (given == null) {
                normGiven = "";
            } else {
                normGiven = given.trim().toLowerCase();
            }
            String normCorrect = correct.trim().toLowerCase();
            correctAns = normGiven.equals(normCorrect);
        } else {
            correctAns = correct.equals(given);
        }

        awaitingAnswer = false;
        totalAsked++;
        if (correctAns) {
            totalCorrect++;
            correctStreak++;
            feedback.setText("Correct!");
            adjustWeight(currentIndex, true);
            soundPlayer.playCorrect();
            expBar.addXP(10 + Math.max((correctStreak - 5) * 5, 0));  //temp value for actually leveling up
            streak.setText("Current Streak: " + correctStreak);
            if (isText()) {
                expBar.addXP(10 + Math.max((correctStreak - 5) * 5, 0)); 
            }
        } else {
            correctStreak = 0;
            streak.setText("Streak reset to 0!");
            feedback.setText("Incorrect. Correct answer: " + correct);
            adjustWeight(currentIndex, false);
            soundPlayer.playWrong();
        }
        nextBtn.setDisable(false);
        updateProgressLabel();
    }

    private void adjustWeight(int idx, boolean correct) {
        if (idx < 0 || idx >= weights.size()) return;
        double w = weights.get(idx).doubleValue();
        if (correct) {
            w = w - 0.2;
        } else {
            w = w + 0.2;
        }
        weights.set(idx, Double.valueOf(w));
    }

    private void updateProgressLabel() {
        progressLabel.setText("Progress: " + totalCorrect + " / " + totalAsked);
    }
}
