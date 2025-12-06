import java.util.ArrayList;
import java.util.Collections;

public class Flashcards {
    public static ArrayList<Flashcards> IDs = new ArrayList<Flashcards>();
    public static ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<ArrayList<String>> flashcardSet = new ArrayList<ArrayList<String>>();
    private ArrayList<Double> weights = new ArrayList<Double>();

    public Flashcards(ArrayList<String> terms, ArrayList<String> definitions){
        flashcardSet.add(terms);
        flashcardSet.add(definitions);
        for (int i = 0; i < terms.size(); i++) {
            weights.add(1.0);
        }
    }

    // Sorts flashcards alphabetically, using terms as alphabetical reference
    public void sortFlashcards(){
        ArrayList<String> srcTerms = new ArrayList<String>(flashcardSet.get(0));
        ArrayList<String> srcDefs  = new ArrayList<String>(flashcardSet.get(1));
        ArrayList<Double> srcWts   = new ArrayList<Double>(weights);
        ArrayList<String> tempT = new ArrayList<String>();
        ArrayList<String> tempD = new ArrayList<String>();
        ArrayList<Double> tempW = new ArrayList<Double>();

        while (!srcTerms.isEmpty()){
            int minIndex = 0;
            String min = srcTerms.get(0);
            for (int i = 1; i < srcTerms.size(); i++){
                String term = srcTerms.get(i);
                if (term.compareToIgnoreCase(min) < 0){
                    min = term;
                    minIndex = i;
                }
            }
            tempT.add(srcTerms.remove(minIndex));
            tempD.add(srcDefs.remove(minIndex));
            tempW.add(srcWts.remove(minIndex));
        }
        flashcardSet.clear();
        flashcardSet.add(tempT);
        flashcardSet.add(tempD);
        weights = tempW;
    }

    //Returns current flashcard set
    public ArrayList<ArrayList<String>> getFlashcardSet(){
        return flashcardSet;
    }

    // Updates flashcard set by removing flashcard at given index
    public void updateFlashcardSet(int index){
        flashcardSet.get(0).remove(index);
        flashcardSet.get(1).remove(index);
    }

    // Updates flashcard set by changing flashcard at given index
    public void updateFlashcardSet(ArrayList<String> flash, int index){
        flashcardSet.get(0).set(index, flash.get(0));
        flashcardSet.get(1).set(index, flash.get(1));
    }

    // Updates flashcard set by adding flashcard at given index
    public void updateFlashcardSet(int ind, ArrayList<String> fl){
        flashcardSet.get(0).add(ind, fl.get(0));
        flashcardSet.get(1).add(ind, fl.get(1));
    }

    // adds given flashcard to IDs
    public void addFlashcardSet(Flashcards card){
        IDs.add(card);
        titles.add("Set " + IDs.size());
    }
    public void addFlashcardSet(Flashcards card, String title){
        IDs.add(card);
        titles.add(title);
    }
    
    // Returns flashcards object that is stored at given index
    public Flashcards getFlashcardObject(int index){
        return (IDs.get(index)); 
    }

    public ArrayList<Flashcards> getAllFlashcardSets(){
        return IDs;
    }

    public String getTitle(int index){
        return titles.get(index);
    }

    public void setTitle(int index, String title){
        titles.set(index, title);
    }

    public ArrayList<String> getAllTitles(){
        return titles;
    }

    // Returns individual flashcard at given index. Will always follow the form [Term, Definition].
    public ArrayList<String> getFlashcard(int index){
        ArrayList<String> flashcard = new ArrayList<String>();
        flashcard.add((flashcardSet.get(0)).get(index));
        flashcard.add((flashcardSet.get(1)).get(index));
        return flashcard;
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    public void randomizeOrder(){
        ArrayList<Double> tempWeights = new ArrayList<Double>();
        ArrayList<String> tempTerms = new ArrayList<String>();
        ArrayList<String> tempDefinitions = new ArrayList<String>();
        int rand;
        ArrayList<Integer> indexes = new ArrayList<Integer>();
        for (int i = 0; i < flashcardSet.get(0).size(); i++){
            indexes.add(i);
        }
        while (!indexes.isEmpty()){
            int pickIndex = (int)(Math.random() * indexes.size());
            int originalIndex = indexes.remove(pickIndex);
            tempWeights.add(weights.get(originalIndex));
            tempTerms.add(flashcardSet.get(0).get(originalIndex));
            tempDefinitions.add(flashcardSet.get(1).get(originalIndex));
        }

        flashcardSet.clear();
        flashcardSet.add(tempTerms);
        flashcardSet.add(tempDefinitions);
        weights = tempWeights;
    }
}