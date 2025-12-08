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

public class Matching extends VBox {
    private ArrayList<String> words;
    private ArrayList<String> meanings;
    private ArrayList<String> randomized;
    private VBox bvox = new VBox(16);
    private Label fail = new Label("unequal # of words and definitions");
    private Button wordButton = new Button();
    private Button meaningButton = new Button();
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
    // gui components
    private Label matchingLabel = new Label("Matching");
    private Label feedback = new Label("");

    public Matching(ArrayList<String> w, ArrayList<String> m, EXPBarUI xpBar) {
        words = w;
        meanings = m;
        randomized = new ArrayList<String>(w);
        setSpacing(10);
        setPadding(new Insets(16));
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
        getChildren().addAll(matchingLabel, bvox, feedback);
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
            feedback.setText("Well Done!");

            // remove button1 and button2
        }
        else{
            // display incorrect text onto screen
            // do NOT remove button1 and button2  
        }
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