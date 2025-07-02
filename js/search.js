/*
  search.js – Provides search bar suggestions and dynamic filtering of topics.
  
  Responsibilities:
  1. Capture user input from the search bar (#search-input) and provide real-time suggestions.
     - As the user types, generate a list of matching topics (e.g., medication names or other topic names).
     - Show these matches in the suggestions dropdown (#suggestions-list) for quick selection.
  2. Optionally, filter the topics list in the main view to narrow down visible items.
     - As the query is typed, hide categories or items that do not match, and highlight those that do.
     - This gives the user immediate visual feedback in the topics list.
  3. Handle selection of a suggestion.
     - When the user clicks a suggestion (or presses Enter when typing a full name), navigate to that topic's detail.
     - This should trigger the same behavior as clicking the item in the list (i.e., call navigation to show detail, update history, etc.).
  4. Ensure the search suggestions and filtering are cleared when appropriate.
     - If the search query is cleared or the user navigates away, hide the suggestions and restore the full list.
  
  Predefined names & data:
  - Search input field: #search-input.
  - Suggestions list container: #suggestions-list (initially hidden, use .hidden class).
  - Global data source: `medicationsData` array (from medications.js) for medication names.
    (If other topics exist, we might compile a combined list of all topic names for search.)
  - We'll gather all topic names from available data/categories at initialization for quick search access.
  - Navigation integration: use navigation.js's function (showTopicDetail or similar) to open a topic when selected from search.
  - The topics list in DOM (#topics-list) can be filtered:
    * Category <li> have class .category and sub-items .item.
    * We can show/hide .item elements based on match, and show/hide entire .category if none of its items match.
    * Also possibly auto-expand categories that have matches for easier viewing.
  
  Implementation plan:
  - Compile an array of all searchable topics (e.g., medication names and any other topic names). This can be done in initializeSearch().
  - On input event of #search-input, call a handler function (filterTopics or handleSearchInput):
      * Read current query string.
      * If query is empty: clear suggestions, show all topics (unfilter).
      * If not empty: find matches (case-insensitive substring match) in the topics list.
          - Populate #suggestions-list with up to a certain number of matches (for example, top 5 or all).
          - Also perform dynamic filtering in the #topics-list:
              + Hide all .item that don't match.
              + For each category, if none of its .item are visible, hide that category (or keep it collapsed).
              + Show categories that have matches (could auto-expand them to reveal matches).
      * Show the #suggestions-list (remove .hidden) if there are suggestions, or hide it if none.
  - On selecting a suggestion (click on a suggestion <li> or pressing Enter when one is highlighted):
      * Determine which topic was selected.
      * Call showTopicDetail(topicName) from navigation.js to open the detail (which handles history).
      * Clear the search input (or not, but likely clear to reset filter).
      * Hide the suggestions list and reset filtering (so the full list is restored if user goes back to list).
  - Ensure no overlapping info: e.g., do not duplicate the data list manually; use medicationsData (and any other data arrays) to build search index.
*/

// Array to hold all topic names for search suggestions
var topicIndex = [];

// Initialize search functionality (prepare topic index and set up event listeners)
function initializeSearch() {
  var searchInput = document.getElementById("search-input");
  var suggestionsList = document.getElementById("suggestions-list");

  // Build the list of searchable topics from available data
  topicIndex = [];
  if (typeof medicationsData !== "undefined") {
    medicationsData.forEach(function(med) {
      topicIndex.push(med.name);
    });
  }
  // (If other categories have separate data arrays, include them here as needed.)

  // Attach input event handler for live search
  if (searchInput) {
    searchInput.addEventListener("input", function() {
      handleSearchInput(searchInput.value);
    });
  }

  // Attach click handler to suggestions list (using event delegation for dynamic items)
  if (suggestionsList) {
    suggestionsList.addEventListener("click", function(event) {
      var target = event.target;
      if (target && target.tagName.toLowerCase() === "li") {
        // User clicked on a suggestion item
        var selectedTopic = target.textContent;
        if (selectedTopic) {
          // Use navigation to open the selected topic detail
          if (typeof showTopicDetail === "function") {
            showTopicDetail(selectedTopic);
          }
          // Clear the search field and suggestions
          if (searchInput) searchInput.value = "";
          clearSearchResults();
        }
      }
    });
  }

  // (Optional) If we want to handle "Enter" key when typing to select first suggestion:
  if (searchInput) {
    searchInput.addEventListener("keyup", function(event) {
      if (event.key === "Enter") {
        var firstSuggestion = suggestionsList && suggestionsList.querySelector("li");
        if (firstSuggestion) {
          firstSuggestion.click();  // trigger click on first suggestion
        }
      }
    });
  }
}

// Handle search input changes: filter topics and show suggestions
function handleSearchInput(query) {
  var suggestionsList = document.getElementById("suggestions-list");
  if (!suggestionsList) return;
  query = query.trim();
  // If query is empty, reset everything
  if (query === "") {
    suggestionsList.innerHTML = "";
    suggestionsList.classList.add("hidden");
    // Restore full topics list visibility
    clearSearchResults();
    return;
  }

  // Case-insensitive matching for suggestions
  var lowerQuery = query.toLowerCase();
  var matches = topicIndex.filter(function(name) {
    return name.toLowerCase().includes(lowerQuery);
  });
  // Sort matches alphabetically (optional) or by relevance (length of string, etc.)
  matches.sort();

  // Populate suggestions list (limit to a reasonable number, e.g., 10)
  suggestionsList.innerHTML = "";
  var maxSuggestions = 10;
  for (var i = 0; i < matches.length && i < maxSuggestions; i++) {
    var li = document.createElement("li");
    li.textContent = matches[i];
    suggestionsList.appendChild(li);
  }
  // Show or hide the suggestions dropdown
  if (matches.length > 0) {
    suggestionsList.classList.remove("hidden");
  } else {
    suggestionsList.classList.add("hidden");
  }

  // Dynamic filter on the topics list
  applyTopicFilter(lowerQuery);
}

// Filter the topics list in the main view based on the query
function applyTopicFilter(queryLower) {
  var topicsList = document.getElementById("topics-list");
  if (!topicsList) return;
  var categories = topicsList.querySelectorAll(".category");
  categories.forEach(function(category) {
    var items = category.querySelectorAll(".item");
    var anyMatch = false;
    items.forEach(function(item) {
      var text = item.textContent.toLowerCase();
      if (text.includes(queryLower)) {
        // Item matches query
        item.style.display = "list-item";
        anyMatch = true;
        // Optionally highlight the matching text (not required, but could wrap the matching part in <mark> or so)
      } else {
        // Item does not match
        item.style.display = "none";
      }
    });
    if (anyMatch) {
      // Show category if it has any matching item
      category.style.display = "list-item";
      // Optionally auto-expand this category to reveal matches:
      var subList = category.querySelector("ul.subcategory");
      if (subList) {
        subList.classList.remove("hidden");
        category.classList.add("expanded");
      }
    } else {
      // Hide entire category if no items match
      category.style.display = "none";
    }
  });
}

// Clear search results and restore the original list state (called when search is cancelled or after selecting a suggestion)
function clearSearchResults() {
  var suggestionsList = document.getElementById("suggestions-list");
  if (suggestionsList) {
    suggestionsList.innerHTML = "";
    suggestionsList.classList.add("hidden");
  }
  // Restore all topic items and categories to visible
  var topicsList = document.getElementById("topics-list");
  if (!topicsList) return;
  var categories = topicsList.querySelectorAll(".category");
  categories.forEach(function(category) {
    // Show category
    category.style.display = "list-item";
    // Collapse category back to default (optional):
    var subList = category.querySelector("ul.subcategory");
    if (subList) {
      // Only collapse if it wasn't expanded by a user action? For simplicity, collapse all after search.
      subList.classList.add("hidden");
      category.classList.remove("expanded");
    }
    // Show all items in that category
    var items = category.querySelectorAll(".item");
    items.forEach(function(item) {
      item.style.display = "list-item";
    });
  });
}
