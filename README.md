# Paramedic Quick Reference

This repository outlines the planned structure for the **Paramedic Quick Reference** web application. The app is a single-page HTML, CSS, and JavaScript project that provides paramedics with quick access to critical information.

## Planned Directory Structure

```
/ (project root)
├── index.html           # Main page of the application
├── css/
│   └── styles.css       # Global styles
├── js/
│   ├── app.js           # Initializes the app and manages state
│   ├── navigation.js    # Back, forward, and home navigation logic
│   ├── search.js        # Search bar functionality
│   └── patientInfo.js   # Patient data sidebar and related effects
├── data/
│   └── medications.js   # ALS medication information and dosage tools
└── assets/
    ├── images/          # UI images and icons
    └── video/           # Optional embedded instructional videos
```

## File Descriptions

### index.html
The single HTML file that loads the entire interface. It contains:
- A centered title and search bar on the main contents page.
- A collapsible sidebar for patient information entry.
- A nested list of topics and subtopics. Each entry has a rotating blue arrow to expand or collapse subtopics.
- Navigation buttons (back, forward, home) positioned in the top right.
- A persistent history button that shows previously accessed content.
All UI elements are assigned IDs or classes so the JavaScript files can manipulate them.

### css/styles.css
Defines all visual styling for the application:
- Layout of the main content page, sidebar, and navigation controls.
- Styles for the blue arrows, expandable lists, hidden text sections, and warning messages.
- Font declarations and responsive design rules.

### js/app.js
Acts as the entry point for the JavaScript code:
- Runs after the HTML content loads and sets up event listeners for user interactions.
- Coordinates with `navigation.js`, `search.js`, and `patientInfo.js` to update the page.
- Maintains a central record of the current topic, navigation history, and user-entered patient data.

### js/navigation.js
Handles the back, forward, and home navigation buttons:
- Updates the navigation history and highlights the last selected item when navigating backward.
- Prevents the forward button from being used unless the user has moved back first.
- Controls the visibility of content sections based on history.

### js/search.js
Implements search bar functionality:
- Filters topic buttons as the user types.
- Provides autocomplete suggestions and focuses on matching topics.

### js/patientInfo.js
Manages the collapsible patient info sidebar:
- Collects user-entered data and stores it in `app.js`.
- Applies effects throughout the app, such as striking through text, showing warnings, and pre-calculating dosages using data from `data/medications.js`.
- Auto-collapses when the user clicks outside the sidebar.

### data/medications.js
Contains structured data for ALS medications:
- Each drug entry stores dosage ranges, contraindications, and administration notes.
- Functions to calculate dosage based on patient information are exposed for `patientInfo.js` to use.

### assets/
A collection of static resources such as images, icons, or instructional videos referenced by the HTML.

## File Interaction and Order of Operations
1. **index.html** loads first when the user opens the app.
2. The `styles.css` file is referenced from the HTML to apply visual styles.
3. JavaScript files load in this order:
   - `app.js` initializes the app and registers event listeners.
   - `navigation.js`, `search.js`, and `patientInfo.js` are loaded next to provide specific functionality. These files rely on the global state managed by `app.js`.
   - `medications.js` is loaded last because its data is used by `patientInfo.js` for dosage calculations.
4. The `assets` directory is referenced whenever images, icons, or videos are needed in the UI.

All scripts work together within a single-page interface. No module system is used—all files attach their functionality to the global scope. The app.js file orchestrates interactions between scripts, ensuring that changes to patient data and navigation are reflected across all components.
