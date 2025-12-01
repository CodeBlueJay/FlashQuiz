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
    private VBox bvox = new VBox(16);
    private Label fail = new Label("unequal # of words and definitions");
    private Button wordButton = new Button();
    private Button meaningButton = new Button();
    private ArrayList<Button> wordButtonList = new ArrayList<Button>();
    private ArrayList<Button> meaningButtonList = new ArrayList<Button>();

    public Matching(ArrayList<String> w, ArrayList<String> m) {
        this.words = w;
        this.meanings = m;
        
        if (words.size() != meanings.size())
            bvox.getChildren().add(fail);
        else {
            for (int i = 0; i < words.size(); i++) {
                wordButton = new Button(words.get(i));
                meaningButton = new Button(meanings.get(i));
                wordButtonList.add(wordButton);
                meaningButtonList.add(meaningButton);
                //add css for buttons format
                HBox setWords = new HBox(4);
                setWords.getChildren().addAll(wordButton, meaningButton);
                bvox.getChildren().addAll(setWords);
            }
        }
        getChildren().add(bvox);

        // boolean check1 = false;
        // boolean check2 = false;
        // wordButton.setOnAction(new EventHandler<ActionEvent>(){
        //     @Override
        //     public void handle(ActionEvent e) {
        //         check1 = true;
        //         wordButton
        //     }
        // });

        
        

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
    }

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
            // display "Well Done!" or some other correct text onto screen
            // remove button1 and button2
        }
        else{
            // display incorrect text onto screen
            // do NOT remove button1 and button2
        }
    }
}