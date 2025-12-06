import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class Main extends Application {
    private BorderPane root;
    private Flashcards currentSet;
    // Default flashcard set
    ArrayList<String> default_terms = new ArrayList<>(Arrays.asList("Sus", "69", "Owen", "Jaden", "Jayden (a lazy person)"));
    ArrayList<String> default_definitions = new ArrayList<>(Arrays.asList("Short for 'suspicious', originating from Among Us", "OG funny number that came before 67", "Does too little, gay", "Doing too much", "is a bum"));
    Flashcards flashcards = new Flashcards(default_terms, default_definitions);
    private EXPBarUI expBar;
    @Override
    public void start(Stage stage) {
        stage.setTitle("Flashcard Program");

        flashcards.addFlashcardSet(flashcards);
        if (!Flashcards.IDs.isEmpty()) {
            Flashcards.titles.set(Flashcards.IDs.size()-1, "Default Set");
        }

        // Two test sets
        ArrayList<String> t2 = new ArrayList<>(Arrays.asList("Dog", "Cat", "Bird"));
        ArrayList<String> d2 = new ArrayList<>(Arrays.asList("Canine", "Feline", "Avian"));
        Flashcards set2 = new Flashcards(t2, d2);
        set2.addFlashcardSet(set2);
        if (!Flashcards.IDs.isEmpty()) {
            Flashcards.titles.set(Flashcards.IDs.size()-1, "Animals");
        }
        ArrayList<String> t3 = new ArrayList<>(Arrays.asList("CPU", "RAM", "SSD", "GPU"));
        ArrayList<String> d3 = new ArrayList<>(Arrays.asList("Processor", "Memory", "Storage", "Graphics"));
        Flashcards set3 = new Flashcards(t3, d3);
        set3.addFlashcardSet(set3);
        if (!Flashcards.IDs.isEmpty()) {
            Flashcards.titles.set(Flashcards.IDs.size()-1, "Computer Parts");
        }
        ArrayList<String> t4 = new ArrayList<>(Arrays.asList());
        ArrayList<String> d4 = new ArrayList<>(Arrays.asList());
        Flashcards set4 = new Flashcards(t4, d4);
        set4.addFlashcardSet(set4);
        if (!Flashcards.IDs.isEmpty()) {
            Flashcards.titles.set(Flashcards.IDs.size()-1, "Empty List");
        }

        VBox menu = new VBox(10);
        menu.setPadding(new Insets(12));
        Button homeBtn = new Button("Home");
        Button setsBtn = new Button("Sets");
        Button learnBtn = new Button("Learn");
        Button matchingBtn = new Button("Matching");
        Button bossBtn = new Button("Boss Battle");
        Button accuracyBtn = new Button("Accuracy");

        homeBtn.getStyleClass().addAll("nav-button", "primary");
        setsBtn.getStyleClass().addAll("nav-button", "danger");
        learnBtn.getStyleClass().addAll("nav-button", "accent");
        matchingBtn.getStyleClass().addAll("nav-button", "accent");
        bossBtn.getStyleClass().addAll("nav-button", "accent");
        accuracyBtn.getStyleClass().addAll("nav-button", "accent");

        menu.getChildren().addAll(homeBtn, setsBtn, learnBtn, matchingBtn, bossBtn, accuracyBtn);
        root = new BorderPane();
        
        expBar = EXPBarUI.getInstance();
        root.setTop(expBar);

        root.setLeft(menu);
        root.setCenter(buildHomeScreen());
        homeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.setCenter(buildHomeScreen());
            }
        });
        learnBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.setCenter(buildLearnScreen());
            }
        });
        matchingBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.setCenter(buildMatchingScreen());
            }
        });
        bossBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.setCenter(buildBossScreen());
            }
        });
        accuracyBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.setCenter(buildAccuracyScreen());
            }
        });
        setsBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                root.setCenter(buildSetsScreen());
            }
        });
        Scene scene = new Scene(root, 900, 650);
        try {
            java.net.URL cssUrl = getClass().getResource("/styles.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                java.io.File css = new java.io.File("styles.css");
                java.io.File cssApp = new java.io.File("app/styles.css");
                if (css.exists()) {
                    scene.getStylesheets().add(css.toURI().toString());
                } else if (cssApp.exists()) {
                    scene.getStylesheets().add(cssApp.toURI().toString());
                }
            }

            java.net.URL fontUrl = getClass().getResource("/fonts/microwave.ttf");
            if (fontUrl != null) {
                Font.loadFont(fontUrl.toExternalForm(), 10);
            } else {
                java.io.File fnt = new java.io.File("fonts/microwave.ttf");
                java.io.File fntApp = new java.io.File("app/fonts/microwave.ttf");
                if (fnt.exists()) Font.loadFont(fnt.toURI().toString(), 10);
                else if (fntApp.exists()) Font.loadFont(fntApp.toURI().toString(), 10);
            }
        } catch (Exception ignored) {
        }
        stage.setScene(scene);
        stage.show();
    }

    private Node buildHomeScreen() {
        // temp button functionality test;
        // File soundFile = new File("sounds/vineBoom.mp3");
        // Button playSoundBtn = new Button("Play Sound");
        // String uri = soundFile.toURI().toString();
        // Media media = new Media(uri);
        // MediaPlayer mediaPlayer = new MediaPlayer(media);
        // playSoundBtn.setOnAction(new EventHandler<ActionEvent>() {
        //     @Override
        //     public void handle(ActionEvent e) {
        //         mediaPlayer.stop();
        //         mediaPlayer.seek(Duration.ZERO);
        //         mediaPlayer.play();
        //     }
        // });
        // box.getChildren().add(playSoundBtn);
        // end temp button functionality test
        Label title = new Label("Flashcard Program Home");
        Label subtitle = new Label("Selected Set");
        VBox box = new VBox(12);
        box.setPadding(new Insets(16));
        FlowPane wrap = new FlowPane();
        wrap.setHgap(12);
        wrap.setVgap(12);

        int selectedIndex = 0;
        if (currentSet != null) {
            int idx = Flashcards.IDs.indexOf(currentSet);
            if (idx >= 0) selectedIndex = idx;
        }

        for (int i = 0; i < Flashcards.IDs.size(); i++) {
            Flashcards fc = Flashcards.IDs.get(i);
            int index = i;
            int count = 0;
            try {
                var s = fc.getFlashcardSet();
                if (s != null && s.size() >= 2 && s.get(0) != null) {
                    count = s.get(0).size();
                }
            } catch (Exception ignored) {}
            String name = (index < Flashcards.titles.size()) ? Flashcards.titles.get(index) : ("Set " + (index + 1));
            Button card = new Button(name + " - " + count + " cards");
            card.getStyleClass().add("set-card");
            if (index == selectedIndex) {
                card.getStyleClass().add("selected");
            }
            card.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    currentSet = fc;
                    for (Node n : wrap.getChildren()) {
                        n.getStyleClass().remove("selected");
                    }
                    card.getStyleClass().add("selected");
                }
            });
            wrap.getChildren().add(card);
        }

        if (!wrap.getChildren().isEmpty()) {
            if (currentSet == null) {
                currentSet = Flashcards.IDs.get(selectedIndex);
            }
            int idx = Flashcards.IDs.indexOf(currentSet);
            if (idx < 0) idx = selectedIndex;
        }

        box.getChildren().addAll(title, subtitle, wrap);
        return box;
    }

    private Node buildLearnScreen() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(16));
        Label title = new Label("Learn");

        if (currentSet == null || currentSet.getFlashcardSet() == null || currentSet.getFlashcardSet().size() < 2) {
            box.getChildren().addAll(title, new Label("Pick a set on Home first."));
            return box;
        }

        ArrayList<ArrayList<String>> cs = currentSet.getFlashcardSet();
        ArrayList<String> terms = cs.get(0);
        ArrayList<String> definitions = cs.get(1);
        ArrayList<Double> weights = currentSet.getWeights();
        Learn learnView = new Learn(terms, weights, definitions, expBar);
        return learnView;
    }

        private Node buildMatchingScreen() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(16));
        Label title = new Label("Matching");
        if (currentSet == null || currentSet.getFlashcardSet() == null || currentSet.getFlashcardSet().size() < 2) {
            return box;
        }
        int count = 0;
        try {
            ArrayList<ArrayList<String>> s = currentSet.getFlashcardSet();
            if (s != null && s.size() >= 2 && s.get(0) != null) {
                count = s.get(0).size();
            }
        } catch (Exception ignored) {}
        ArrayList<ArrayList<String>> cs = currentSet.getFlashcardSet(); 
        Matching matchview = new Matching(cs.get(0), cs.get(1));
        return matchview;
    }

    private Node buildBossScreen() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(16));
        Label title = new Label("Boss Battle");
        if (currentSet == null || currentSet.getFlashcardSet() == null || currentSet.getFlashcardSet().size() < 2) {
            box.getChildren().addAll(title, new Label("Pick a set on Home first."));
            return box;
        }
        int count = 0;
        try {
            ArrayList<ArrayList<String>> s = currentSet.getFlashcardSet();
            if (s != null && s.size() >= 2 && s.get(0) != null) {
                count = s.get(0).size();
            }
        } catch (Exception ignored) {}
        ArrayList<ArrayList<String>> cs = currentSet.getFlashcardSet();
        ArrayList<String> terms = cs.get(0);
        ArrayList<String> definitions = cs.get(1);
        boolean isMCQ = false; // placeholder mode selection
        Boss bossview = new Boss(terms, definitions, isMCQ);
        return bossview;
    }

    private Node buildAccuracyScreen() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(16));
        Label title = new Label("Accuracy Challenge");
        if (currentSet == null || currentSet.getFlashcardSet() == null || currentSet.getFlashcardSet().size() < 2) {
            box.getChildren().addAll(title, new Label("Pick a set on Home first."));
            return box;
        }
        ArrayList<ArrayList<String>> cs = currentSet.getFlashcardSet();
        ArrayList<String> terms = cs.get(0);
        ArrayList<String> definitions = cs.get(1);
        ArrayList<Double> weights = currentSet.getWeights();
        Accuracy accuracy = new Accuracy(terms, definitions, weights, expBar);
        return accuracy;
    }

    private Node buildSetsScreen() {
        VBox box = new VBox(10);
        box.setPadding(new Insets(16));

        Label title = new Label("Sets");
        ListView<String> list = new ListView<>();

        int index = 1;
        for (Flashcards fc : Flashcards.IDs) {
            int count = 0;
            try {
                var s = fc.getFlashcardSet();
                if (s != null && s.size() >= 2 && s.get(0) != null) {
                    count = s.get(0).size();
                }
            } catch (Exception ignored) {}
            String name = (index-1 < Flashcards.titles.size()) ? Flashcards.titles.get(index-1) : ("Set " + index);
            list.getItems().add(name + " - " + count + " cards");
            index++;
        }

        int curIdx = Flashcards.IDs.indexOf(currentSet);
        if (curIdx >= 0) {
            list.getSelectionModel().select(curIdx);
        }

        list.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> obs, Number oldVal, Number newVal) {
                int sel = newVal == null ? -1 : newVal.intValue();
                if (sel >= 0 && sel < Flashcards.IDs.size()) {
                    currentSet = Flashcards.IDs.get(sel);
                }
            }
        });

        box.getChildren().addAll(title, list);
        return box;
    }

    public static void main(String[] args) {
        launch(args);
    }
}