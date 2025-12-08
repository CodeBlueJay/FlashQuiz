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

public class Sets extends VBox {
    private ArrayList<String> words;
    private ArrayList<Double> weights;
    private ArrayList<String> definitions;
    private Button newSet;
    private EXPBarUI xp;
    

    public Sets(ArrayList<String> w, ArrayList<Double> we, ArrayList<String> d, EXPBarUI exp) {
        xp = exp;
        words = w;
        weights = we;
        definitions = d;
        
    }
}