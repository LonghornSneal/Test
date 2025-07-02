/*
  medications.js – Contains ALS medication reference data and dosage calculation tools.
  
  Responsibilities:
  1. Define a global data structure (e.g., an array of medication objects) containing critical information for each medication:
     - name: the name of the medication.
     - doses: an object with adult and pediatric dosage guidelines (as human-readable strings, possibly with dosage per weight for pediatric).
     - contraindications: an array of strings listing contraindications for that medication.
     - notes: any additional important notes or usage guidelines.
  2. Provide helper functions related to medication data, such as calculateDose(), which uses the current patient information to give a recommended dose.
     - calculateDose(medName, patient): returns a string recommendation based on patientData (for example, if pediatric, might calculate per kg dose).
     - Ensure this function is global so other files (patientData.js, navigation.js) can call it.
  3. Keep data and logic separate from UI: this file does not manipulate the DOM or handle events; it simply provides data and pure functions.
  4. Use placeholders or example values for demonstration. Additional medications can be added following the same structure.
  5. No modules: everything here is in the global scope by inclusion in index.html.
  
  Note: The dosage calculation provided is basic (checks if patient is pediatric by age or weight threshold). 
  In a more advanced app, this could be extended to perform actual dose calculations (e.g., weight-based calculation) if more detailed data (like mg per kg) were available.
*/

// Example medications data array (global)
const medicationsData = [
  {
    name: "Epinephrine",
    doses: {
      adult: "1 mg IV push every 3-5 min", 
      pediatric: "0.01 mg/kg IV (max 1 mg per dose)"
    },
    contraindications: [
      "None in emergency (life-threatening) situations",
      "Use caution in patients with hypertension or coronary artery disease"
    ],
    notes: "Commonly used in cardiac arrest. Monitor heart rhythm and blood pressure."
  },
  {
    name: "Aspirin",
    doses: {
      adult: "324 mg (chewable tablets) PO",
      pediatric: "Not indicated for pediatric patients in pre-hospital setting"
    },
    contraindications: [
      "Allergy to aspirin/NSAIDs",
      "Recent gastrointestinal bleeding or active ulcer"
    ],
    notes: "Administer for chest pain suggestive of cardiac ischemia. Instruct patient to chew tablets for faster absorption."
  }
  // ...additional medications can be added here in the same format.
];

/**
 * Calculate a recommended dose string for a medication based on the current patient data.
 * @param {string} medName - Name of the medication (must match an entry in medicationsData).
 * @param {Object} patient - Patient info object with properties like age (years) and weight (kg).
 * @returns {string} Recommended dose (as a text string) for the patient, or a default message if not applicable.
 */
function calculateDose(medName, patient) {
  var med = medicationsData.find(function(m) { return m.name === medName; });
  if (!med) {
    return "Medication not found";
  }
  var adultDose = med.doses.adult || "";
  var pediatricDose = med.doses.pediatric || "";
  // Determine if patient is pediatric (using age < 12 years, or if age not provided use weight < 40 kg as criterion)
  var isPediatric = false;
  if (patient) {
    if (patient.age !== undefined && patient.age !== null) {
      isPediatric = patient.age < 12;
    } else if (patient.weight !== undefined && patient.weight !== null) {
      isPediatric = patient.weight < 40;
    }
  }
  // Return appropriate dose based on patient type
  if (isPediatric && pediatricDose) {
    // If we had precise weight dosing info, we could calculate an exact number here. For now, just return the guideline string.
    return pediatricDose;
  } else if (!isPediatric && adultDose) {
    return adultDose;
  } else {
    // If one of the doses is not specified or patient type doesn't match available data
    return adultDose || pediatricDose || "Dose not specified";
  }
}

// (Other medication-related utility functions could be added here, e.g., for calculating drip rates, converting units, etc.)
