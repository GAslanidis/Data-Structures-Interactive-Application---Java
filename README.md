# Data Structures Interactive Application

A Java desktop application built with NetBeans and Swing that demonstrates core data structures and algorithms through an interactive GUI. The application is organized into several independent modules, each showcasing a different data structure concept.

---

## Features

### Topics Browser
Browse and search a dictionary of computer science topics loaded from a JSON file. Topics are indexed alphabetically and can be looked up by name. Users can also create and save their own custom JSON dictionaries through a built-in editor.

### Text Query & Analysis
Enter or load a block of text and analyze it using multiple data structures:
- **Tokenization** — splits input into individual words stored in an unordered array
- **Word frequency counting** — counts occurrences of each token
- **Alphabetical sorting** — sorts tokens using a sorted array and Bubble Sort
- **Undo / Redo** — tracks editor state using two stacks

### Query Router
Routes user queries across multiple data sources and returns the best matching result.

### Python Source Code Parser
Load a Python source file and parse it into a structured representation:
- Source lines are loaded into an **unordered array**
- Lines are queued using a **queue** for sequential processing
- The parsed output is displayed as a **program tree**

### User-Topic Interaction Graph
Displays a directed graph of user interactions with topics, ranked by influence.

---

## Data Structures Used

| Structure | Where Used |
|---|---|
| Unordered Array | Token storage, source code lines |
| Ordered Array | Alphabetically sorted tokens |
| Dictionary (Hash Table) | Topic lookups from JSON |
| Stack | Undo / Redo in the text editor |
| Queue | Python source line processing |
| Linked List | Internal list traversal |
| Binary Search Tree | Topic tree navigation |
| Directed Graph | User-topic interaction graph |

---

## JSON File Format

Topics and custom dictionaries are stored as flat key-value JSON files:

```json
{
  "Algorithm": "An algorithm is a finite sequence of well-defined instructions...",
  "Recursion": "Recursion is a programming technique where a function calls itself..."
}
```

The application ships with `Topics.json` (core CS definitions) and `Test.json` (a sample custom dictionary). Users can load any compatible JSON file or create new ones through the app.

---

## Project Structure

```
SemesterProject/
├── src/
│   ├── data/          # Data structure implementations (CArray, CStack, CQueue, CTree, etc.)
│   └── forms/         # Swing GUI forms (CFormMain, CFormTopics, CFormQuery, etc.)
├── Topics.json        # Built-in topic dictionary
├── Test.json          # Sample custom dictionary
└── Random Text 1-6.txt  # Sample text files for the Query module
```

---

## Requirements

- Java 8 or later
- NetBeans IDE (to open and build the project)
- `json-simple-1.1.jar` (included in `src/`)

## Running the Application

Open the project in NetBeans and run `CMain.java` located in the `data` package. The main menu will appear, giving access to all modules.
