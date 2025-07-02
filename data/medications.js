/*
Contains structured data for ALS medications and related helper functions:
- Medication entries include dosage guidelines, contraindications, and administration notes.
- Provides a global `medicationsData` array and a `calculateDose()` function for use by other scripts (like patientData.js).
*/

// Example medication data array:
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
    notes: "Commonly used in cardiac arrest. Monitor patient heart rhythm and blood pressure."
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
  // ...additional medications can be added here
];

/**
 * Calculate a recommended dose string for a medication based on patient info.
 * @param {string} medName - Name of the medication (must match an entry in medicationsData).
 * @param {Object} patient - Patient info with properties like age (years) and weight (kg).
 * @returns {string} Recommended dose (as text) for the patient.
 */
function calculateDose(medName, patient) {
  const med = medicationsData.find(m => m.name === medName);
  if (!med) {
    return "Medication not found";
  }
  // Determine if patient is pediatric (using age < 12 years, or weight < 40 kg if age not provided)
  const isPediatric = (patient.age !== undefined && patient.age < 12) 
                   || (patient.age === undefined && patient.weight !== undefined && patient.weight < 40);
  // Return pediatric dose if applicable and available; otherwise return adult dose (or a default message)
  if (isPediatric && med.doses.pediatric) {
    return med.doses.pediatric;
  } else {
    return med.doses.adult || med.doses.pediatric || "Dose not specified";
  }
}

// Other medication-related utility functions could be added here (e.g., checking contraindications based on patient allergies).