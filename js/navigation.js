/*
Handles the interactive topic list navigation and content display logic:
- Controls the expandable menu of topics and subtopics: expands or collapses sections when a topic header is clicked, and toggles the direction of the corresponding arrow icon indicator.
- Manages showing and hiding content detail sections: when a user selects a specific subtopic, this script displays the relevant detail content and hides or overlays the main topics list as appropriate.
- Keeps track of which topic detail is currently active, highlighting the selected menu item and ensuring only one detail section is visible at a time.
- Provides utility functions for direct access by other components or UI events, for example: `openDetail(topicId)` to jump to a specific topic’s detail content (used by the search and history modules), and `resetToHome()` to collapse all sections and return to the top-level view.
- Notifies the history module whenever a new topic detail is opened so that the action gets recorded for Back/Forward navigation and the history log.
- Triggers patientData’s update routines when displaying a detail page (e.g., calling `patientData.applyToSection()` for the newly shown content) to ensure dosage information and warnings are adjusted to the current patient data.
*/