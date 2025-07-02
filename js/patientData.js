/*
  patientData.js – Manages the Patient Info sidebar and patient-specific effects on the app.
  
  Responsibilities:
  1. Maintain a global patient data object with properties like age and weight (initialize with default or saved values).
  2. Provide functions to open/close the patient info panel (toggle the sidebar visibility).
  3. Attach event listeners to patient info input fields (age, weight) so that any changes update the global data and trigger recalculations of any patient-dependent information.
  4. If patient data changes affect displayed content (e.g., medication dosages), update the content appropriately (for example, highlight the pediatric dose or recalc a recommended dose).
  5. Avoid conflicts: use the same global patient data for all calculations; ensure naming consistency across files (e.g., if `calculateDose` expects certain patient object structure).
  6. Do not rely on any module system – all functions/variables here are in the global scope for use by other scripts.
  
  Key elements & interactions:
  - Global object `patientData` (or similar) to store patient info (e.g., patientData.age, patientData.weight).
  - Function to initialize patient data (possibly check localStorage for saved values, else defaults).
  - Function to toggle the patient info sidebar (show/hide #patient-info element), triggered by the "Patient Info" button (#patient-btn).
  - Within toggle, possibly handle closing by clicking outside the panel (attach document event listener when open, remove when closed).
  - Event handlers for input fields (#patient-age, #patient-weight) to update patientData and call an update function.
  - Update function (maybe part of the input handler) that applies changes:
      * e.g., if a medication detail is currently displayed, recalc or adjust dosage info using the updated patient data.
      * Could call `updateCurrentDetail()` or similar to refresh the displayed content.
      * Uses helper from medications.js like `calculateDose()` if needed.
  - Utility function to determine if patient is pediatric (based on age < certain threshold or weight < threshold).
  
  Predefined names and structures:
  - Use a global variable or object named `patientData` to store patient info. For example:
        patientData = { age: <number>, weight: <number> }
    Other scripts (medications.js) use patient info as an object (the calculateDose function expects an object with age and/or weight properties).
  - Ensure `calculateDose(medName, patientObj)` from medications.js can be used with `patientData` object.
  - ID #patient-info is the sidebar container; #patient-age and #patient-weight are input fields.
  - The toggle button has ID #patient-btn.
  - Classes: uses .hidden to show/hide the sidebar.
*/

"use strict";  // (Optional strict mode for good practice)

// Global patient data object with default values
var patientData = {
  age: null,    // default to null or a specific value (e.g., 0 or adult age if preferred)
  weight: null  // default to null (meaning not set yet)
  // We assume null means "no data entered"; scripts can treat that as adult by default.
};

// Function to determine if current patient should be considered pediatric
function isPediatricPatient() {
  // Define pediatric criteria: e.g., age < 12 years OR (age not set and weight < 40 kg)
  if (patientData.age !== null) {
    return patientData.age < 12;
  } else if (patientData.weight !== null) {
    return patientData.weight < 40;
  }
  return false;
}

// Initialize patient data (e.g., on app load)
function initializePatientData() {
  // If needed, load saved patient info from localStorage (for now, just use defaults)
  // Example: 
  // let savedAge = localStorage.getItem("patientAge");
  // let savedWeight = localStorage.getItem("patientWeight");
  // if (savedAge) patientData.age = Number(savedAge);
  // if (savedWeight) patientData.weight = Number(savedWeight);
  // For now, assume no saved data; patientData is already set to defaults.

  // Set up event listeners for patient info inputs:
  var ageInput = document.getElementById("patient-age");
  var weightInput = document.getElementById("patient-weight");
  if (ageInput && weightInput) {
    ageInput.addEventListener("input", function() {
      // Update global patient data age
      patientData.age = ageInput.value ? Number(ageInput.value) : null;
      handlePatientDataChange();
    });
    weightInput.addEventListener("input", function() {
      // Update global patient data weight
      patientData.weight = weightInput.value ? Number(weightInput.value) : null;
      handlePatientDataChange();
    });
  }

  // Set up event listener for clicking outside the patient info panel to close it
  document.addEventListener("click", function(event) {
    var panel = document.getElementById("patient-info");
    var toggleBtn = document.getElementById("patient-btn");
    if (!panel || panel.classList.contains("hidden")) {
      return;  // panel is not open, nothing to do
    }
    var isClickInside = panel.contains(event.target) || (toggleBtn && toggleBtn.contains(event.target));
    if (!isClickInside) {
      // Clicked outside the panel (and not on the toggle button) => close the panel
      panel.classList.add("hidden");
    }
  });
}

// Function to toggle the patient info sidebar panel
function togglePatientPanel() {
  var panel = document.getElementById("patient-info");
  if (!panel) return;
  if (panel.classList.contains("hidden")) {
    // Show the panel
    panel.classList.remove("hidden");
    // (Optionally, focus the first input for convenience)
    var ageInput = document.getElementById("patient-age");
    if (ageInput) ageInput.focus();
  } else {
    // Hide the panel
    panel.classList.add("hidden");
  }
}

// Handle changes to patient data (called whenever age/weight inputs change)
function handlePatientDataChange() {
  // If a detail view is currently open, update it to reflect new patient data.
  var detailSection = document.getElementById("detail-view");
  if (detailSection && !detailSection.classList.contains("hidden")) {
    // A detail is currently displayed
    updateDetailForPatient();
  }
  // We could also persist the patient data to localStorage here for future sessions.
  // Example:
  // localStorage.setItem("patientAge", patientData.age !== null ? patientData.age : "");
  // localStorage.setItem("patientWeight", patientData.weight !== null ? patientData.weight : "");
}

// Update the currently displayed detail content according to the current patient data
function updateDetailForPatient() {
  // This function will adjust dosage info or other patient-specific elements in the detail view.
  // For example, highlight pediatric vs adult dosages or recalculate recommended dose.
  var titleElem = document.getElementById("detail-title");
  var contentElem = document.getElementById("detail-content");
  if (!contentElem || !titleElem) return;
  // Determine current topic being viewed (could use title text or store current topic in a global variable)
  var currentTopic = titleElem.textContent;
  if (!currentTopic) return;
  // If currentTopic corresponds to a medication name in medicationsData, we can recalc dosage.
  var recommendedDoseStr = "";
  if (typeof calculateDose === "function") {
    // Use the global calculateDose(medName, patientData) provided by medications.js
    recommendedDoseStr = calculateDose(currentTopic, patientData);
  }
  // Find dosage elements in detail content (assuming they were created with identifiable classes or IDs)
  // Let's assume detail content includes elements for adult and pediatric dose with classes 'adult-dose' and 'pediatric-dose'
  var adultDoseElem = contentElem.querySelector(".adult-dose");
  var pedDoseElem = contentElem.querySelector(".pediatric-dose");
  if (adultDoseElem && pedDoseElem) {
    // Apply highlighting or hiding based on patient type
    if (isPediatricPatient()) {
      // Pediatric patient: highlight pediatric dose, dim adult dose
      pedDoseElem.classList.remove("inactive");
      adultDoseElem.classList.add("inactive");
    } else {
      // Adult or no specific pediatric criteria: ensure adult dose is normal, ped may be dimmed if not applicable
      adultDoseElem.classList.remove("inactive");
      // If this medication is not indicated for pediatric (pediatric dose text might say "Not indicated"), we might highlight that if patient is pediatric. 
      // But if not pediatric, ensure ped dose is not highlighted (we could dim it to indicate it's not currently relevant).
      pedDoseElem.classList.remove("inactive");
    }
    // Optionally, if we want to display a "Recommended dose for patient" line:
    // We could insert or update a line in contentElem with recommendedDoseStr.
    var recDoseElem = contentElem.querySelector(".recommended-dose");
    if (recommendedDoseStr && recDoseElem) {
      recDoseElem.textContent = "Recommended Dose: " + recommendedDoseStr;
    } else if (recommendedDoseStr) {
      // If a recommended dose element doesn't exist yet, create one at the top or bottom of content
      var p = document.createElement("p");
      p.className = "recommended-dose";
      p.textContent = "Recommended Dose: " + recommendedDoseStr;
      contentElem.appendChild(p);
    }
  }
}

// (No code executes on load by itself; functions above will be called from main.js for initialization and events.)
