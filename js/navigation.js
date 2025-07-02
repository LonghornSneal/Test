/*
  navigation.js – Handles menu expansion, detail display, and overall content navigation for topics.
  
  Responsibilities:
  1. Populate the topics list in the "Topics view" (using data from medications.js and any other categories if applicable).
     - Insert list items for each medication under the Medications category (and similarly for other categories if data available).
     - Ensure no duplicate or overlapping info: rely on data files for content, not hard-coding details here except structure.
  2. Manage expanding/collapsing of topic categories in the list.
     - When a category title is clicked, toggle its sub-list (show or hide the subtopics).
     - Use the .hidden class to hide/show sub-lists and .expanded class on the category for styling the arrow icon.
  3. Handle selection of a topic (leaf item in the list).
     - When a subtopic (item) is clicked, display its detail in the #detail-view section and hide the #topics-view list.
     - Populate #detail-title and #detail-content with the item's information (e.g., name, dosage, notes, etc.).
     - Mark the selected item as .active in the list for context.
     - Update history via history.js (push the new state so Back/Forward works).
  4. Provide a way to return to the main list (e.g., Home button or Back button).
     - Possibly a function to show the main topics list (collapsing detail view).
     - Ensure that when returning to list, the previously selected item remains highlighted and its category expanded for context.
  5. Expose necessary functions globally (no module import/export). Other scripts (search.js, history.js, main.js) will call these.
  
  Predefined names & interactions:
  - The UL for medications list is #medications-list inside the Medications category LI (with class .category).
  - Each medication item LI will have class .item, and we will assign an identifier (e.g., data-topic or use the medication name as text).
  - Category toggling: category LI element has a child span.category-title that is clickable.
  - The detail container is #detail-view (with #detail-title and #detail-content inside it) which we will populate and show.
  - History integration: use history.pushHistory(state) when a new detail is shown. Use history.goBack()/goForward() for navigation events, which will call back into navigation to display the appropriate content.
  - Patient data integration: when showing detail, incorporate patient-specific info by calling patientData functions or using calculateDose.
  - Global data: uses `medicationsData` array from medications.js to build the list and get detail info.
*/

// Populate the topics list (especially Medications category) using medicationsData
function populateTopicsList() {
  var medListElement = document.getElementById("medications-list");
  if (!medListElement || typeof medicationsData === "undefined") return;
  medListElement.innerHTML = "";  // clear any placeholder items
  // Sort medications alphabetically by name for easier lookup (optional)
  var meds = medicationsData.slice().sort(function(a, b) {
    return a.name.localeCompare(b.name);
  });
  meds.forEach(function(med) {
    var li = document.createElement("li");
    li.className = "item";
    li.textContent = med.name;
    // Optionally, store an identifier (like med name) for reference:
    // li.setAttribute("data-topic", med.name);
    // Attach click event to each medication item (to show detail when clicked)
    li.addEventListener("click", function() {
      showTopicDetail(med.name);
    });
    medListElement.appendChild(li);
  });
  // The Medications category is initially collapsed (.subcategory ul has .hidden by default).
  // We won't auto-expand it on load (user can click to expand).
}

// Toggle a category's subtopic list (expand/collapse) 
function toggleCategory(categoryElement) {
  // categoryElement is expected to be the <li> element with class .category that was clicked
  if (!categoryElement) return;
  var subList = categoryElement.querySelector("ul.subcategory");
  if (!subList) return;
  if (subList.classList.contains("hidden")) {
    // Expand: show the sub-list
    subList.classList.remove("hidden");
    categoryElement.classList.add("expanded");
  } else {
    // Collapse: hide the sub-list
    subList.classList.add("hidden");
    categoryElement.classList.remove("expanded");
  }
}

// Show the detail view for a given topic (medication or other topic name)
// This function is used for user-initiated navigation; it will update history.
function showTopicDetail(topicName) {
  // Display the detail content for the given topicName and update history
  displayTopicDetail(topicName);       // Populate and show the detail view (helper function that does not push history)
  if (typeof history !== "undefined" && typeof pushHistory === "function") {
    pushHistory(topicName);            // Push this navigation state into history stack (so Back/Forward work)
  }
}

// Internal function to populate the detail view with the topic's information (without affecting history)
function displayTopicDetail(topicName) {
  var topicsView = document.getElementById("topics-view");
  var detailView = document.getElementById("detail-view");
  var titleElem = document.getElementById("detail-title");
  var contentElem = document.getElementById("detail-content");
  if (!detailView || !topicsView || !titleElem || !contentElem) return;

  // Lookup topic data. For medications, find the object in medicationsData by name.
  var topicData = null;
  if (typeof medicationsData !== "undefined") {
    topicData = medicationsData.find(function(med) { return med.name === topicName; });
  }
  // (If other categories exist with separate data sources, include similar lookups here.)

  // Populate the detail title and content
  titleElem.textContent = topicName;
  contentElem.innerHTML = "";  // clear previous content
  if (topicData) {
    // If this is a medication entry with known structure:
    // Display dosage information, contraindications, notes, etc.
    var doses = topicData.doses;
    if (doses) {
      // Create paragraphs for adult and pediatric doses
      var adultP = document.createElement("p");
      adultP.className = "dose-info adult-dose";
      adultP.textContent = "Adult Dose: " + (doses.adult || "N/A");
      var pedP = document.createElement("p");
      pedP.className = "dose-info pediatric-dose";
      pedP.textContent = "Pediatric Dose: " + (doses.pediatric || "N/A");
      contentElem.appendChild(adultP);
      contentElem.appendChild(pedP);
    }
    if (topicData.contraindications) {
      var contraHeader = document.createElement("p");
      contraHeader.textContent = "Contraindications:";
      contraHeader.style.fontWeight = "bold";
      contentElem.appendChild(contraHeader);
      var contraList = document.createElement("ul");
      topicData.contraindications.forEach(function(item) {
        var li = document.createElement("li");
        li.textContent = item;
        contraList.appendChild(li);
      });
      contentElem.appendChild(contraList);
    }
    if (topicData.notes) {
      var notesP = document.createElement("p");
      notesP.textContent = "Notes: " + topicData.notes;
      contentElem.appendChild(notesP);
    }
  } else {
    // If topicData not found (or if it were a different kind of topic), show a generic message or handle accordingly
    var msg = document.createElement("p");
    msg.textContent = "Details for \"" + topicName + "\" are not available.";
    contentElem.appendChild(msg);
  }

  // Switch views: hide the topics list and show the detail view
  topicsView.classList.add("hidden");
  detailView.classList.remove("hidden");

  // Highlight the selected topic in the list (add .active to its <li>, remove from any other)
  var listItems = document.querySelectorAll("#topics-list .item");
  listItems.forEach(function(li) {
    if (li.textContent === topicName) {
      li.classList.add("active");
      // Ensure its parent category is expanded to show it (if not already)
      var parentCategory = li.closest(".category");
      if (parentCategory && parentCategory.querySelector("ul.subcategory").classList.contains("hidden")) {
        parentCategory.querySelector("ul.subcategory").classList.remove("hidden");
        parentCategory.classList.add("expanded");
      }
    } else {
      li.classList.remove("active");
    }
  });

  // If patient data is applicable, adjust content for current patient context:
  if (typeof updateDetailForPatient === "function") {
    updateDetailForPatient();  // This will highlight doses or add recommended dose based on current patient info
  }

  // Scroll to top of the detail content (in case the list was scrolled down)
  window.scrollTo(0, 0);
}

// Show the main topics list view (hide detail view). Used for Home navigation or when returning to list.
function showMainList() {
  var topicsView = document.getElementById("topics-view");
  var detailView = document.getElementById("detail-view");
  if (!topicsView || !detailView) return;
  // Hide detail view, show topics list
  detailView.classList.add("hidden");
  topicsView.classList.remove("hidden");
  // We do NOT remove the .active highlight from the last viewed item, 
  // so that when returning to the list, the user sees which item they had open.
  // (If we wanted to clear it on Home, we could remove .active from all items here.)
}

// Initialize navigation on page load
function initializeNavigation() {
  // Populate the topic list from data
  populateTopicsList();
  // Attach event listeners for category toggles:
  var categoryTitles = document.querySelectorAll(".category .category-title");
  categoryTitles.forEach(function(titleElem) {
    titleElem.addEventListener("click", function() {
      // Toggle the category (parent of this title)
      var categoryItem = titleElem.parentElement;
      toggleCategory(categoryItem);
    });
  });
  // The list is ready; no need to show any detail at start (detail view stays hidden).
}
