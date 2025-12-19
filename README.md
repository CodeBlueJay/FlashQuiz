# Flashcard Quiz Program

Modern, lightweight flashcard study app built with Java + JavaFX.

Features

- Create and manage multiple flashcard sets
- Multiple study modes: Learn, Matching, Accuracy challenge, Boss Battle
- XP / leveling systems and visual progress bar
- Simple, keyboard-friendly UI with editable sets

Getting started

Prerequisites

- Java 17+ installed and available on `PATH` (or the JDK version you prefer).
- JavaFX SDK matching your JDK (if running outside the bundled scripts).

Run (development)

From the project root you can run the app quickly (development mode):

```powershell
.\run.bat
```

Compile / Package (release build)

To compile and create a packaged release use the provided scripts:

```powershell
.\compile.bat
.\package.bat
```

These scripts wrap the project compilation and packaging steps. If you only want to compile without packaging, run `compile.bat`. (This will only compile the project into a .jar file)

Manual compile / run (without scripts)

If you prefer to compile manually, include JavaFX on the module path. Example (adjust paths for your JavaFX SDK location):

```powershell
javac -d out --module-path "javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml src\*.java
java --module-path "javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp out Main
```

Releases

Prebuilt releases are available at the project's releases page:

https://github.com/CodeBlueJay/FlashQuiz/releases/latest

Notes about data persistence

- The app attempts to persist XP to a local SQLite database file `data.db` (uses `sqlite-jdbc` if present on the classpath). If SQLite is not available it falls back to a simple `data.properties` file.
- To enable the SQLite option include a compatible `sqlite-jdbc` jar in the runtime classpath (for example add to `lib/` and update `run.bat` if needed).


How the project is organized

- `Main.java` – application entry, navigation and screen builders
- `Flashcards.java` – flashcard set model and helpers
- `Sets.java` – UI for browsing and selecting sets
- `Learn.java`, `Matching.java`, `Accuracy.java`, `Boss.java` – study modes
- `styles.css` – theme and look-and-feel
- `fonts/` and `sounds/` – assets

Set editor (what to expect)

Select a set from `Sets` to open an editor where you can:

- Edit the set title
- Add / edit / delete term–definition pairs
- Return to the Sets view

Roadmap ideas

- Drag & drop card reordering
- Import/export (CSV / Anki format)
- User profiles and persistent local storage

## Problem

Studying vocabulary or facts can become boring and ineffective when presented as static lists. This application aims to make studying more engaging by providing multiple study modes, progress tracking, and interactive UI elements.

## Application description

Users create sets of terms and definitions for study. The app offers several study methods (Learn, Matching, Accuracy challenge, Boss Battle) and additional features such as XP, leveling, and a set editor. The goal is to present flashcards in ways that encourage repetition, variety, and game-like progression.

## Class breakdown

### Flashcards

- Variables


  - `IDs`: holds a list of `Flashcards` objects (each is a set)
  - `titles`: human-readable title for each set
  - `flashcardSet`: internal 2D ArrayList storing `terms` and `definitions`
  - `weights`: per-card weights for adaptive learning

- Core behavior and helpers


  - `Flashcards(ArrayList<String> terms, ArrayList<String> definitions)` — constructor
  - `sortFlashcards()` — sorts the set alphabetically by term
  - `getFlashcardSet()` — returns internal 2D array structure
  - `toPairList()` — returns an ArrayList of `[term, definition]` pairs
  - `getSize()` — returns number of cards
  - `updateFlashcardSet(...)` overloads — add, remove, or replace a card
  - `addFlashcardSet(Flashcards card[, String title])` — register a new set globally
  - `allSetsAsPairs()` — helper returning all sets as lists of pairs

### Potential methods and notes

  - Sort by different criteria (alphabetical, weights)
  - Export/import sets (CSV/Anki-style) for portability
  - Track which set is currently selected in the app state

## Games / Study options

- Standard Learn: typed answers and multiple-choice variants; uses weights for adaptive order
- Match: pair terms and definitions in a timed or scored matching mode
- Boss Battle: answer questions to damage a boss; boss can damage back; small life pools and time limits
- Accuracy Challenge: consecutive correct typed answers under a time limit that tightens as the run continues; high score saved

### Data and progression

  - XP and level tracking
  - Per-user or per-session statistics (future enhancement)

## Features to add (original list)

1. Randomize order
1. Adaptive learning (weights)
1. Track correct answers and show a score
1. Save cards for re-use
1. Structure flash-card learning methods around a game/objective theme
1. Implement a way of holding multiple flashcard sets

## Tools

- VSCode (IDE)
- JavaFX (GUI Library)
- Git (Version Control)
