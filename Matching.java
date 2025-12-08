import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import javafx.scene.text.Font;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Node;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Animation;

public class Matching extends VBox {
    private ArrayList<String> words;
    private ArrayList<String> meanings;
    private ArrayList<String> randomized;
    private Button wordButton = new Button();
    private Button meaningButton = new Button();
    private Button startButton = new Button("Start");
    private ArrayList<Button> wordButtonList = new ArrayList<Button>();
    private ArrayList<Button> meaningButtonList = new ArrayList<Button>();
    private ArrayList<Button> randomizedButtonList = new ArrayList<Button>();
    private Button selectedWord;
    private Button selectedMeaning;
    private Button randomizedButton;
    private int selectedWordIndex = -1;
    private int selectedMeaningIndex = -1;
    private boolean check1 = false;
    private boolean check2 = false;
    private int rand;
    private EXPBarUI xp;
    private Timeline timeline;
    private double time;
    private double avgTime;
    // gui components
    private Label matchingLabel = new Label("Matching");
    private Label feedback = new Label("Click start to begin!");
    private Label showTimer = new Label();
    private Label fail = new Label("unequal # of words and definitions");
    private VBox bvox = new VBox(16);    
    private HBox timer = new HBox(2);
    Font microwave;

    public Matching(ArrayList<String> w, ArrayList<String> m, EXPBarUI xpBar) {
        words = w;
        meanings = m;
        xp = xpBar;
        randomized = new ArrayList<String>(w);
        time = 0;
        avgTime = words.size() * 1.5;
        setSpacing(10);
        setPadding(new Insets(16));
        microwave = Font.loadFont("file:fonts/microwave.ttf", 36);
        showTimer.setText(String.format("%.2f", time));
        showTimer.getStyleClass().add("timer");
        showTimer.setFont(microwave);
        if (words.size() != meanings.size())
            bvox.getChildren().add(fail);
        else {
            for (int i = 0; i < words.size(); i++) {
                wordButton = new Button(words.get(i));
                meaningButton = new Button(meanings.get(i));
                wordButton.getStyleClass().addAll("match-button", "match-word");
                meaningButton.getStyleClass().addAll("match-button", "match-meaning");
                wordButtonList.add(wordButton);
                meaningButtonList.add(meaningButton);
                rand = (int)(Math.random() * randomized.size());
                randomizedButton = new Button(randomized.get(rand));
                randomizedButtonList.add(randomizedButton);
                HBox setWords = new HBox(2);
                setWords.getChildren().addAll(randomizedButton, meaningButton);
                bvox.getChildren().add(setWords);
                randomized.remove(rand);
            }
        }
        timer.getChildren().addAll(showTimer, startButton);
        getChildren().addAll(matchingLabel, timer, feedback);

        startButton.setOnAction(e -> {
            getChildren().add(2, bvox);
            startButton.setVisible(false);
            stopwatch();
        });

        for (int i = 0; i < meaningButtonList.size(); i++) {
            Button mb = meaningButtonList.get(i);
            mb.setOnAction(e -> {
                feedback.setText("Selected " + mb.getText());
                if (selectedMeaning != null) {
                    selectedMeaning.getStyleClass().remove("selected");
                }
                selectedMeaning = mb;
                mb.getStyleClass().add("selected");
                if (selectedWord != null) {
                    match(selectedWord, selectedMeaning);
                }
            });
        }
        for (int i = 0; i < randomizedButtonList.size(); i++) {  
            Button wb = randomizedButtonList.get(i);
            wb.setOnAction(e -> {
                feedback.setText("Selected " + wb.getText());
                if (selectedWord != null) {
                    selectedWord.getStyleClass().remove("selected");
                }
                selectedWord = wb;
                wb.getStyleClass().add("selected");
                if (selectedMeaning != null) {
                    match(selectedWord, selectedMeaning);
                }
            });

        }
    }

    public void stopwatch() {
        timeline = new Timeline(new KeyFrame(Duration.millis(10), ev -> {
            time += 0.01;
            showTimer.setText(String.format("%.2f", time));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /** Needs to acount for: 
     * Creating buttons
     * Actions that occur on button click
     * Setting buttons equal to term / definition
     * Setting button pairs (one term on the left side that corresponds with term on the right side)
     * Removal of buttons that were clicked
     * E
     * Match action (clicking one button, then clicking another on the other side, runs check after completed)
     * Check action (Checking to see if buttons that were clicked are a correct pair)
     * */ 

    public boolean check(String wordClicked, String definitionClicked){
        int ind1 = words.indexOf(wordClicked);
        int ind2 = meanings.indexOf(definitionClicked);
        return(ind1 == ind2);
    }
    
    public void match(Button button1, Button button2){
        String term = button1.getText();
        String definition = button2.getText();
        boolean correct = check(term, definition);
        if (correct){
            xp.addXP(10);
            feedback.setText("Well Done!");
            selectedWord.setVisible(false);
            selectedMeaning.setVisible(false);
            meaningButtonList.remove(selectedMeaning);
            if (meaningButtonList.size() == 0) {
                xp.addXP(50);
                feedback.setText("All matched!\nBonus 50 XP!\nClick Matching to try again.");
                timeline.stop();
                if (time < avgTime) {
                    xp.addXP(50);
                    feedback.setText("All matched quickly!\nBonus 100 XP!\nClick Matching to try again.");
                }
            }
        }
        else{
            feedback.setText("Incorrect! Try again.");
            // do NOT remove button1 and button2  
        }
        selectedMeaning = null;
        selectedWord = null;
        button1.getStyleClass().remove("selected");
        button2.getStyleClass().remove("selected");
    }
}

        // for (int i = 0; i < wordButtonList.size(); i++) {
        //     final int idx = i;
        //     Button wb = wordButtonList.get(i);
        //     wb.setOnAction(e -> {
        //         if (selectedWord != null && selectedWord != wb) {
        //             selectedWord.getStyleClass().remove("selected");
        //         }
        //         selectedWordIndex = idx;
        //         selectedWord = wb;
        //         wb.getStyleClass().add("selected");

        //         if (selectedMeaningIndex >= 0 && selectedMeaning != null) {
        //             boolean correct = check(selectedWord.getText(), selectedMeaning.getText());
        //             if (correct) {
        //                 xpBar.addXP(10);
        //                 selectedWord.getStyleClass().add("matched");
        //                 selectedMeaning.getStyleClass().add("matched");
        //                 selectedWord.setVisible(false);
        //                 selectedMeaning.setVisible(false);
        //             } else {
        //                 selectedWord.getStyleClass().remove("selected");
        //                 selectedMeaning.getStyleClass().remove("selected");
        //             }
        //             selectedWord = null;
        //             selectedMeaning = null;
        //             selectedWordIndex = -1;
        //             selectedMeaningIndex = -1;
        //         }
        //     });
        // }

        // for (int i = 0; i < meaningButtonList.size(); i++) {
        //     final int idx = i;
        //     Button mb = meaningButtonList.get(i);
        //     mb.setOnAction(e -> {
        //         if (selectedMeaning != null && selectedMeaning != mb) {
        //             selectedMeaning.getStyleClass().remove("selected");
        //         }
        //         selectedMeaningIndex = idx;
        //         selectedMeaning = mb;
        //         mb.getStyleClass().add("selected");
        //         if (selectedWordIndex >= 0 && selectedWord != null) {
        //             boolean correct = check(selectedWord.getText(), selectedMeaning.getText());
        //             if (correct) {
        //                 xpBar.addXP(10);
        //                 selectedWord.getStyleClass().add("matched");
        //                 selectedMeaning.getStyleClass().add("matched");
        //                 selectedWord.setVisible(false);
        //                 selectedMeaning.setVisible(false);
        //             } else {
        //                 selectedWord.getStyleClass().remove("selected");
        //                 selectedMeaning.getStyleClass().remove("selected");
        //             }
        //             selectedWord = null;
        //             selectedMeaning = null;
        //             selectedWordIndex = -1;
        //             selectedMeaningIndex = -1;
        //         }
        //     });
        // }