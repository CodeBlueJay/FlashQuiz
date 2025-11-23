# ✨Flashcard Quiz Program✨

**Problem:** Studying vocabulary or facts gets boring (Or just studying in general), and some people may not get much value out of just staring at terms and definitions.

**Application Description:** Our program will be a studying-based application where the user inputs terms they would like to memorize, and answers they need to memorize that correspond with each term. The program will have different options when it comes to the method of studying, similar to Quizlet, Anki, and other flashcard-based study websites, potentially including games, matching, and randomized tests on each of the flashcards.

## Class Breakdown

### Class: Flashcards
**Variables:**
1. *IDs*: Holds an array of IDs that each correspond to a different flashcard set
2. *Flashcard set*: 2D Array that holds a list of terms and definitions for each array
- Input system for flashcards will be based on a max of 100 flashcards, where a base array of length 100 will be created, and a new array will be created if the user does not use up all 100 flashcard slots.

**Potential Methods:**
1. *Sort Flashcards*: Takes in a parameter of what the user would like to sort the flashcards based off of (alphabetical order, etc.), and re-orders the array with that new sorting system.
2. *Get Flashcard Set*: Returns flashcard set
3. *Update Flashcard*: Updates specified values at inputted indices of the flashcards
4. *Add Flashcard Set*: Creates a new ID and flashcard set and adds it to the array of flashcard set IDs
5. *Get Flashcard*: Returns flashcard from inputted index from current flashcard set 

Side note: Must implement a way to keep track of which flashcard set is currently being viewed, most likely an input in the constructor

**Flashcards and Flashcard IDs arrays will be class variables, to allow the user to access previous flashcard sets without re-creating them.

### Class: Games / Study Options
**Game modes for studying**
1. *Standard learn option* (similar to quizlet, both typed and mc answering capabilities) - Shows the definition OR answer and then either a textbox or mcq answer choices ****This will be where the weights likely come in****
2. *Match* (Connect with wires among us wires basically) - Shows different options, and the answers, and connect all of them correctly, get a set amount of exp.
3. *“Boss Battle”* - A lightweight boss-battle styled event where the player answers questions correctly to damage the “AI” boss, which has a 50% chance of doing damage back, both sides have 5-10 lives to ensure playtime doesn’t go on for too long. Also a time limit for answers, maybe 5-10 seconds.
4. *Accuracy Challenge* - All typed answers, MUST be correctly typed 100%, counts how many the user is able to do in a row. There is a time limit that slowly decreases by 0.2 seconds, with the base being 5 or something. High score is saved.
- Includes attributes for user data related to progression
- XP will likely be a class variable
- Level as well

**Methods**
1. *Get data*: Shows the user’s data
2. *Add xp (amount)*: Adds xp to the users profile, also calculates leveling
3. *Select gamemode(String)*: selects the gamemode

Side Note: Will take in a Flashcard object as a parameter for use in methods

## Features to add
1. Randomize order
2. Adaptive Learning (weights)
3. Track correct answers and show a score
4. Save cards for re-use
5. Structure flash-card learning methods around a game / accomplish an objective theme.
6. Implement a way of holding multiple flashcard sets.

## Tools
- VSCode (IDE)
- JavaFX (GUI Library)
- Git (Version Control and Remote Collaborative Programming)
