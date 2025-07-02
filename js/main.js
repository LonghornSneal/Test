/*
  main.js – Orchestrates initial UI setup and event handlers, tying together all components.
  
  Responsibilities:
  1. Initialize the application once the DOM is fully loaded, calling initialization functions from other modules (patientData, navigation, search, history).
  2. Ensure that scripts execute in the correct order (this file is last, so by now global variables and functions from other files are available).
  3. Set up any remaining event listeners that were not already set in their modules (if any cross-module interactions need binding).
  4. Handle any startup logic, such as showing the initial view (topics list) and possibly loading any saved state (like a remembered last topic or patient data if needed).
  5. Overall, coordinate interactions:
     - e.g., when a new detail is shown via navigation, main.js doesn't need to do much since navigation.js and history.js handle their parts.
     - But main.js ensures all components are started and can interact (like search input events calling nav functions, etc.).
  
  Steps on load:
  - Wait for DOMContentLoaded event (or load event) to ensure HTML elements are present.
  - Call initializePatientData() to set up patient info management.
  - Call initializeNavigation() to populate the topics list and prepare category toggles.
  - Call initializeSearch() to prepare search suggestions and filtering.
  - Call initializeHistory() to attach history button events and set initial state of back/forward buttons.
  - If needed, display a welcome or default state (in this app, the default is just the topics list visible).
  - Ensure the main topics list (#topics-view) is visible and detail view is hidden (should already be via classes in HTML).
  - No conflicting actions: each init function does its own setup; main.js just triggers them in the proper order.
*/

// Wait for DOM content to be fully loaded before initialization
document.addEventListener("DOMContentLoaded", function() {
  // Initialize patient data and sidebar functionality
  if (typeof initializePatientData === "function") {
    initializePatientData();
  }
  // Initialize navigation (build topic list and set up category toggle events)
  if (typeof initializeNavigation === "function") {
    initializeNavigation();
  }
  // Initialize search (prepare suggestions and attach search events)
  if (typeof initializeSearch === "function") {
    initializeSearch();
  }
  // Initialize history (attach back/forward/home/history button events)
  if (typeof initializeHistory === "function") {
    initializeHistory();
  }
  // Ensure initial view is the main topics list
  var topicsView = document.getElementById("topics-view");
  var detailView = document.getElementById("detail-view");
  if (topicsView && detailView) {
    topicsView.classList.remove("hidden");
    detailView.classList.add("hidden");
  }
  // Hide patient info panel and history panel just in case (they should be hidden by default via class)
  var patientPanel = document.getElementById("patient-info");
  var historyPanel = document.getElementById("history-panel");
  if (patientPanel) patientPanel.classList.add("hidden");
  if (historyPanel) historyPanel.classList.add("hidden");
  // Attach event for Patient Info toggle button (to open/close sidebar)
  var patientToggleBtn = document.getElementById("patient-btn");
  if (patientToggleBtn && typeof togglePatientPanel === "function") {
    patientToggleBtn.addEventListener("click", togglePatientPanel);
  }

  // (Optional) If we wanted to restore the last viewed topic on load, 
  // we could check localStorage for a saved "lastTopic" and if present, automatically open it:
  // let lastTopic = localStorage.getItem("lastTopic");
  // if (lastTopic) { showTopicDetail(lastTopic); }
  // For now, we start at the main list view by default.
});
