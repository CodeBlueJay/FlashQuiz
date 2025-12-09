import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.util.ArrayList;

public class SetsEditor extends VBox {
    private ArrayList<String> terms = new ArrayList<>();
    private ArrayList<String> definitions = new ArrayList<>();
    private TextField titleField;
    private TextField termField;
    private TextField defField;
    private ListView<String> listView;
    private Button saveBtn;
    private Flashcards editingTarget = null;
    private int editIndex = -1;
    private Button cancelEditBtn;

    public SetsEditor() {
        initUI();
    }

    public SetsEditor(Flashcards target) {
        this.editingTarget = target;
        initUI();
        try {
            ArrayList<ArrayList<String>> set = target.getFlashcardSet();
            if (set != null && set.size() >= 2) {
                terms = new ArrayList<>(set.get(0));
                definitions = new ArrayList<>(set.get(1));
                for (int i = 0; i < terms.size(); i++) {
                    listView.getItems().add(terms.get(i) + " — " + definitions.get(i));
                }
            }
            int idx = Flashcards.IDs.indexOf(target);
            if (idx >= 0 && idx < Flashcards.titles.size()) {
                titleField.setText(Flashcards.titles.get(idx));
            }
        } catch (Exception ignored) {}
    }

    private void initUI() {
        this.setSpacing(8);
        this.setPadding(new Insets(12));

        Label heading = new Label("Create / Edit Set");
        titleField = new TextField();
        titleField.setPromptText("Set title (optional)");

        HBox entryBox = new HBox(8);
        termField = new TextField();
        termField.setPromptText("Term");
        termField.setPrefWidth(200);
        defField = new TextField();
        defField.setPromptText("Definition");
        defField.setPrefWidth(300);
        Button addBtn = new Button("Add Card");
        cancelEditBtn = new Button("Cancel");
        cancelEditBtn.setVisible(false);
        entryBox.getChildren().addAll(termField, defField, addBtn, cancelEditBtn);

        listView = new ListView<>();
        listView.setPrefHeight(200);

        HBox actions = new HBox(8);
        Button removeBtn = new Button("Remove Selected");
        Button editBtn = new Button("Edit Selected");
        Button clearBtn = new Button("Clear All");
        saveBtn = new Button("Save Set");
        actions.getChildren().addAll(removeBtn, editBtn, clearBtn, saveBtn);

        this.getChildren().addAll(heading, titleField, entryBox, listView, actions);

        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String term = termField.getText().trim();
                String def = defField.getText().trim();
                if (term.isEmpty() && def.isEmpty()) return;
                if (editIndex >= 0) {
                    // save edit
                    terms.set(editIndex, term);
                    definitions.set(editIndex, def);
                    listView.getItems().set(editIndex, term + " — " + def);
                    editIndex = -1;
                    cancelEditBtn.setVisible(false);
                    addBtn.setText("Add Card");
                } else {
                    terms.add(term);
                    definitions.add(def);
                    listView.getItems().add(term + " — " + def);
                }
                termField.clear();
                defField.clear();
            }
        });

        removeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int sel = listView.getSelectionModel().getSelectedIndex();
                if (sel >= 0) {
                    listView.getItems().remove(sel);
                    terms.remove(sel);
                    definitions.remove(sel);
                }
            }
        });

        editBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                int sel = listView.getSelectionModel().getSelectedIndex();
                if (sel >= 0) {
                    editIndex = sel;
                    termField.setText(terms.get(sel));
                    defField.setText(definitions.get(sel));
                    addBtn.setText("Save Edit");
                    cancelEditBtn.setVisible(true);
                }
            }
        });

        cancelEditBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                editIndex = -1;
                termField.clear();
                defField.clear();
                addBtn.setText("Add Card");
                cancelEditBtn.setVisible(false);
            }
        });

        clearBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                terms.clear();
                definitions.clear();
                listView.getItems().clear();
            }
        });

        saveBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (terms.isEmpty()) return; // nothing to save
                String title = titleField.getText().trim();
                if (editingTarget == null) {
                    // Create new Flashcards set
                    Flashcards newSet = new Flashcards(new ArrayList<>(terms), new ArrayList<>(definitions));
                    if (title.isEmpty()) {
                        newSet.addFlashcardSet(newSet);
                    } else {
                        newSet.addFlashcardSet(newSet, title);
                    }
                } else {
                    int idx = Flashcards.IDs.indexOf(editingTarget);
                    if (idx >= 0) {
                        ArrayList<ArrayList<String>> targetSet = editingTarget.getFlashcardSet();
                        targetSet.clear();
                        targetSet.add(new ArrayList<>(terms));
                        targetSet.add(new ArrayList<>(definitions));
                        ArrayList<Double> w = editingTarget.getWeights();
                        w.clear();
                        for (int i = 0; i < terms.size(); i++) w.add(1.0);
                        if (!title.isEmpty()) {
                            Flashcards.titles.set(idx, title);
                        }
                    }
                }

                // Optionally clear editor after save
                terms.clear();
                definitions.clear();
                listView.getItems().clear();
                titleField.clear();
            }
        });
    }
}

