# ClassConnect — Southern University A&M College

A campus navigation and schedule management tool built for Southern University students (Jaguars!).

## What It Does

- **Dashboard** — See today's classes, next class info, and quick stats
- **My Schedule** — View your weekly class schedule by day (Mon–Fri), add/drop classes
- **Campus Map** — Interactive SVG map of Southern University buildings with your class locations highlighted
- **Class Finder** — Search the Spring 2026 course catalog by name, code, department, or instructor
- **Profile** — View your student info, GPA, credits, and advisor

---

## Project Structure

```
ClassConnect/
├── backend/
│   ├── Main.java         ← Entry point (console app)
│   ├── Database.java     ← Course catalog, buildings, student data
│   ├── Course.java       ← Course model
│   ├── Building.java     ← Building model
│   └── Student.java      ← Student model
├── frontend/
│   └── index.html        ← Full web application (open in browser)
└── README.md
```

---

## How to Run

### Option 1: Web Frontend (Recommended — No Setup Needed)

1. Open the `frontend/index.html` file in any web browser
   - In VS Code: right-click `index.html` → **"Open with Live Server"** (if you have the Live Server extension)
   - Or just double-click the file in your file explorer
2. Enter anything to sign in (it's a demo)
3. Explore: Dashboard, Schedule, Campus Map, Class Finder, Profile

### Option 2: Java Backend (Console App)

#### In VS Code:

1. **Install the Java Extension Pack** if you haven't:
   - Open VS Code → Extensions (Ctrl+Shift+X) → Search **"Extension Pack for Java"** → Install

2. **Open the backend folder**:
   - File → Open Folder → select the `backend/` folder

3. **Run it**:
   - Open `Main.java`
   - Click the **▶ Run** button that appears above `public static void main`
   - OR right-click in the file → **"Run Java"**
   - OR open the terminal (Ctrl+`) and type:
     ```
     cd backend
     javac *.java
     java Main
     ```

4. **Use the menu**:
   - Press Enter at the login prompt for the demo account
   - Type a number (1–7) to navigate, 0 to exit

#### Requirements:
- **Java 11+** (JDK) installed — download from https://adoptium.net if needed
- Verify with: `java --version` in your terminal

---

## Features

| Feature | Frontend (HTML) | Backend (Java) |
|---------|:-:|:-:|
| Login screen | ✅ | ✅ |
| View schedule by day | ✅ | ✅ |
| Add/drop classes | ✅ | ✅ |
| Search course catalog | ✅ | ✅ |
| Campus map | ✅ (interactive SVG) | ✅ (ASCII art) |
| Find buildings | ✅ | ✅ |
| Student profile | ✅ | ✅ |
| Next class alert | ✅ | — |
| Toast notifications | ✅ | — |

---

## Tech Stack

- **Frontend**: HTML5, CSS3, vanilla JavaScript (no frameworks, single-file)
- **Backend**: Java 11+ (no external dependencies)
- **Data**: In-memory (no database required)

---

## Southern University Buildings Included

- Pinchback Hall (PH) — Science & Engineering
- T.H. Harris Hall (TH) — College of Business
- Stewart Hall (SH) — Humanities
- Smith-Brown Memorial Union (SB) — Student Life
- Clark Activity Center (CA) — Athletics
- A.C. Mumford Stadium (MS) — Football
- John B. Cade Library (LB) — Library
- Lee Hall (LH) — Fine Arts
- Mechanical Engineering Bldg (ME)
- Nursing Building (NB)
- J.S. Clark Admin Building (AD)
- Seymour Gymnasium (SG)
- Augustus C. Blanks Hall (BH) — CS Dept
- Pennington Hall (PN) — Residence
- Totty Hall (TT) — Residence

---

*Built for Southern University A&M College — Go Jaguars! 🐆💛💙*
