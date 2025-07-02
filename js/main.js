/*
Orchestrates the application startup and overall state management:
- Waits for the DOM content to load, then triggers initialization of all modules.
- Calls each module’s setup function (e.g., `initPatientDataPanel()`, `initNavigation()`, `initSearch()`, `initHistory()`) to prepare event handlers and UI interactions.
- Ensures all main UI controls are hooked up: sets up the search bar input (`#searchInput`), navigation buttons (`#backButton`, `#forwardButton`, `#homeButton`), the history toggle (`#historyButton`), and the patient info sidebar toggle (`#patientInfoToggle`) via the respective modules.
- Restores any saved state by delegating to other components (loading stored patient info and history data from localStorage through patientData.js and history.js).
- Coordinates cross-module updates so that the current view (selected topic detail, navigation history, patient context) remains consistent throughout the app.
*/




/*
js/ (folder) – Contains all the JavaScript files, each handling a specific aspect of interactivity. Since no module system is used, these scripts operate in the global scope and must be loaded in a logical order. They collectively enable dynamic behavior: menu toggling, content display, search suggestions, patient data handling, navigation history, and so on.
main.js – Main initialization and orchestrator script. This file coordinates the startup of the application and ties together the other modules. Its responsibilities include:
Attaching global event listeners after the DOM is loaded. For example, it might use document.addEventListener('DOMContentLoaded', ...) or simply execute (if included at end of body) to call initialization functions from other scripts.
Initializing sub-components by calling setup functions defined in the other JS files. For instance, main.js may call something like setupPatientDataPanel(), setupNavigationMenu(), setupSearchBar(), and setupHistory() to initialize event handlers and state for the sidebar, menu, search, and history features respectively.
Ensuring the correct execution order: since it is loaded last, it can safely assume that functions/variables from patientData.js, navigation.js, etc., are already available. It uses those to set the initial state of the app.
Example startup sequence that might be coded here: load any saved patient info (via a function in patientData.js) and apply it (e.g., pre-fill form fields, apply strikethroughs to content that’s not applicable); then render or reveal the main topics list (in case it was dynamically generated, though likely static HTML); attach click handlers to all topic arrows and items (using functions from navigation.js); set up the search input event listener (from search.js); configure the Back/Forward buttons to call history functions; and populate the history list dropdown with any past session data.
Essentially, main.js is the glue that calls into the other scripts to get the app ready for user interaction once the page has loaded.
*/