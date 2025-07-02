/*
  history.js – Manages Back/Forward navigation and the history panel of visited topics.
  
  Responsibilities:
  1. Maintain an internal history of navigation states (topics viewed, plus the "home" state for main list).
     - Use data structures (e.g., arrays or stacks) to track the sequence of visited topic details and to enable back/forward movement.
     - Ensure that the history does not conflict with browser's own history (no external navigation, but optionally we could use History API).
     - Keep track of which state is current, what can be gone back to, and what is forward.
  2. Provide functions to navigate: goBack(), goForward(), goHome().
     - goBack(): move to the previous state (if available) and update the UI to show that state (either a detail or the main list).
     - goForward(): move forward to the next state if available and update UI.
     - goHome(): jump directly to the main list (initial state), clearing any forward history.
  3. Provide a function pushHistory(topicName) to record a new detail view state when a topic is opened.
     - This will add an entry to history (and clear forward history, as typical behavior).
     - Represent "home" (main list) as a special state (could be null or a keyword) that is not pushed but implicitly considered the base.
  4. Manage the History panel UI (#history-panel and #history-list).
     - When a new topic is viewed, add it to the #history-list (avoid duplicates in sequence if desired, or list each visit).
     - Provide a way to toggle the visibility of the history panel (e.g., via History button).
     - If history entries in the panel are clickable, handle their click to navigate directly to that entry (perhaps treated as a new navigation or actual history jump).
  5. Update the Back/Forward buttons' enabled/disabled state based on available history.
     - Disable Back if no previous state, disable Forward if no forward state.
     - This should be done whenever history changes (after push, back, forward, etc.).
  
  Predefined names & interactions:
  - Back button: #back-btn, Forward button: #forward-btn, Home button: #home-btn, History panel toggle: #history-btn.
  - Navigation functions: use navigation.showMainList() to show the main list (home state), and navigation.displayTopicDetail(topic) to show a detail without adding new history (when moving backward/forward).
  - The history is stored internally (not using window.history.pushState here, to keep it simple, though it could be extended to do so).
  - Use global arrays: e.g., historyStack (for back history) and forwardStack.
  - The #history-list in the panel displays topic names in the order they were visited (could be simply the historyStack plus current, or entire sequence including past).
  - On clicking a history entry (if implemented), we can navigate to that topic (either by simulating back/forward jumps or as a new detail open).
*/

// Arrays to hold history states
var historyStack = [];   // stack of topics user navigated through (for "back" history)
var forwardStack = [];   // stack for forward navigation

// Push a new topic into history (called when a new detail view is opened)
function pushHistory(topicName) {
  if (!topicName) return;
  // Add current topic to history stack
  historyStack.push(topicName);
  // Once a new state is added, clear the forward stack (forward history is reset)
  forwardStack = [];
  // Update the history panel list
  addHistoryListEntry(topicName);
  // Update back/forward buttons state
  updateHistoryButtons();
}

// Navigate one step back in history
function goBack() {
  if (historyStack.length === 0) {
    return;  // no history to go back to (already at home)
  }
  // Pop the current state from historyStack
  var currentTopic = historyStack.pop();
  // Save this state into forward stack for potential forward navigation
  forwardStack.push(currentTopic);
  // Determine what the new current state is after popping
  var prevTopic = null;
  if (historyStack.length === 0) {
    // No more history, this means go back to Home (main list)
    prevTopic = null;
    if (typeof showMainList === "function") {
      showMainList();
    }
  } else {
    prevTopic = historyStack[historyStack.length - 1];
    // Display this previous topic detail (without pushing history again)
    if (prevTopic && typeof displayTopicDetail === "function") {
      displayTopicDetail(prevTopic);
    }
  }
  // Update buttons and history panel highlighting if needed
  updateHistoryButtons();
}

// Navigate one step forward in history
function goForward() {
  if (forwardStack.length === 0) {
    return;  // nothing to go forward to
  }
  // Take the most recent forward entry
  var nextTopic = forwardStack.pop();
  if (nextTopic) {
    // Show this topic's detail (without adding a new history entry, since it's revisiting)
    if (typeof displayTopicDetail === "function") {
      displayTopicDetail(nextTopic);
    }
    // Push it back onto the historyStack as current state
    historyStack.push(nextTopic);
  } else {
    // If nextTopic is null, that would represent going forward to home (unlikely scenario; typically we don't forward to home)
    if (typeof showMainList === "function") {
      showMainList();
    }
  }
  updateHistoryButtons();
}

// Go directly to Home (main list of topics)
function goHome() {
  // Clear both history and forward stacks, as we are resetting to initial state
  historyStack = [];
  forwardStack = [];
  if (typeof showMainList === "function") {
    showMainList();
  }
  updateHistoryButtons();
  // Optionally, we might keep historyStack as is and just push to forward, but typically Home is a reset.
  // We'll treat it as a fresh start.
}

// Add an entry to the History panel list (called whenever a new topic is viewed via pushHistory)
function addHistoryListEntry(topicName) {
  var historyList = document.getElementById("history-list");
  if (!historyList || !topicName) return;
  // Create a new list item for the topic
  var li = document.createElement("li");
  li.textContent = topicName;
  // Attach click event to allow user to jump to that history entry (optional)
  li.addEventListener("click", function() {
    // On history entry click:
    // For simplicity, treat it as a new navigation to that topic (push new history entry)
    // In a more complex implementation, we could jump within the stack.
    if (typeof showTopicDetail === "function") {
      showTopicDetail(topicName);
    }
  });
  historyList.appendChild(li);
  // (We do not prevent duplicate entries; each view is listed. Optionally, could avoid adding if topicName same as last entry.)
}

// Toggle the display of the History panel
function toggleHistoryPanel() {
  var panel = document.getElementById("history-panel");
  if (!panel) return;
  if (panel.classList.contains("hidden")) {
    panel.classList.remove("hidden");
  } else {
    panel.classList.add("hidden");
  }
}

// Update the Back/Forward buttons enabled state based on current history stacks
function updateHistoryButtons() {
  var backBtn = document.getElementById("back-btn");
  var fwdBtn = document.getElementById("forward-btn");
  if (backBtn) {
    backBtn.disabled = (historyStack.length === 0);
  }
  if (fwdBtn) {
    fwdBtn.disabled = (forwardStack.length === 0);
  }
}

// Initialize history management (attach event listeners to buttons)
function initializeHistory() {
  var backBtn = document.getElementById("back-btn");
  var fwdBtn = document.getElementById("forward-btn");
  var homeBtn = document.getElementById("home-btn");
  var historyBtn = document.getElementById("history-btn");
  if (backBtn) backBtn.addEventListener("click", goBack);
  if (fwdBtn) fwdBtn.addEventListener("click", goForward);
  if (homeBtn) homeBtn.addEventListener("click", goHome);
  if (historyBtn) historyBtn.addEventListener("click", toggleHistoryPanel);
  // Initially, ensure correct state of buttons (likely Back/Forward disabled, Home enabled)
  updateHistoryButtons();
}
