/*
Manages the patient information input panel and related dynamic content:
- Controls the patient info sidebar panel’s behavior (expand and collapse) and form inputs for patient data (e.g., age, weight, allergies).
- Provides an initialization function (`initPatientDataPanel()`) to attach event listeners: opens the panel when the toggle button (`#patientInfoToggle`) is clicked, and updates internal data whenever an input field changes.
- Stores the user’s patient data in a global structure and uses browser storage (localStorage) to persist this information between sessions.
- Applies patient-specific context to the content: recalculates medication dosages and displays warnings based on current inputs, modifying or annotating content sections as needed (for example, adding a `.not-applicable` CSS class to irrelevant sections for a pediatric patient).
- Exposes utility functions for other modules to use (e.g., an `applyToSection(sectionElement)` function to update a newly shown detail section with patient-specific adjustments), ensuring that whenever a topic’s details are displayed, they reflect the current patient parameters.
*/







/*
patientData.js – Manages the patient information input panel and dynamic content filtering/calculations. This script makes the app responsive to user-provided patient data, tailoring the displayed information (e.g., protocols, dosages) to the context of the patient. Major functions include:
Sidebar toggle and form behavior: Controls showing and hiding of the patient info sidebar. When the user clicks the top-left button to open the sidebar, patientData.js will remove the collapsed state class (allowing the sidebar to slide out or appear). If the user clicks outside the sidebar or on a close button, this script will hide it again. It ensures that the UI is not obstructive: the sidebar may overlay the content or push it depending on design, but it auto-collapses when not in use.
Input handling and storage: Captures data from form fields such as patient age, weight, vital signs, allergies, etc. Each time the user enters or changes a field, this script can validate the input (e.g., ensure numbers are within sensible ranges) and then store the values (possibly in a global state object or directly in localStorage for persistence). By using localStorage (or a similar mechanism), the app can remember these inputs between sessions so the paramedic doesn’t have to re-enter info if they close and reopen the app in the field.
Dynamic content adjustments: Based on the current patient data, patientData.js applies or triggers changes throughout the app’s content:
It might add or remove CSS classes on certain elements. For example, if a protocol is not applicable to a pediatric patient and the user inputs an age that classifies the patient as a child, the script will find all adult-only protocol entries and add a .not-applicable or .disabled class to them, causing them to be visibly struck through or faded via CSS. Conversely, pediatric-only content might be highlighted or enabled.
It can display warnings if the input data triggers any red flags. For instance, if the patient has an allergy to a medication, and the user navigates to that medication’s page, the script could inject a warning message in that detail view (or turn the text red) to alert the paramedic. Similarly, if a calculated dose would exceed a recommended maximum based on weight, it might show a warning in that section.
It performs dosage calculations and fills in results. Many medication protocols use formulas (like dose per kilogram). If the detail content includes placeholders or formula references (e.g., “Dose: 0.1 mg/kg IV”), patientData.js can calculate the actual dose using the patient’s entered weight (e.g., 0.1 mg * 70 kg = 7 mg) and update the content to show “Dose: 0.1 mg/kg IV (≈7 mg for this patient)”. These calculations would update in real-time as the input changes. The script may maintain a set of formula rules or identify elements in the DOM with data attributes (like <span data-formula="0.1*weight">) to compute and inject values.
It enables or introduces new options when relevant. For example, if the user indicates the patient has a certain condition, the app might reveal an extra protocol step or medication that’s normally hidden. patientData.js would handle making those previously hidden elements visible (or adding new elements) when conditions are met.
It fades or narrows lists to guide the user. For instance, if the user’s data indicates the patient is an adult, pediatric categories could appear faded or be automatically collapsed, focusing the UI on adult-relevant sections. The script could temporarily filter the main list to only show relevant items (or just visually de-emphasize the irrelevant ones).
*/

/*
Context display: This script can also update a small summary display of key patient info on the main interface. For example, after entering data, a fixed bar or sidebar section might show “Patient: 70kg, Adult, Allergies: Penicillin” so that as the medic navigates, they’re reminded of the current context. patientData.js would construct and update this summary whenever inputs change.
Collaboration with other scripts: While patientData.js directly manipulates content (e.g., adding classes or altering text in the DOM), other scripts may consult the patient data state as well. For instance, navigation.js might skip adding certain items to history if they’re not applicable, or search.js might rank applicable results higher. To facilitate this, patientData.js might expose some global state (like an object window.patientData containing the current inputs and perhaps some derived flags such as isPediatric:true/false) that other scripts can read. Because no modules are used, this global sharing is how data flows between components.

*/