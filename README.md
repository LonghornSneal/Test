# Paramedic Quick Reference

This repository outlines the planned structure for the **Paramedic Quick Reference** web application. The app is a single-page HTML, CSS, and JavaScript project that provides paramedics with quick access to critical information.

## Planned Directory Structure

```
/ (project root)
├── index.html           # Main page of the application
├── README.md            #
├── css/
│   └── styles.css       # Global styles
├── js/
│   ├── app.js           # Initializes the app and manages state
│   ├── history.js       # 
│   ├── main.js          #
│   ├── navigation.js    # Back, forward, and home navigation logic
│   ├── patientInfo.js   # Patient data sidebar and related effects
│   └── search.js        # Search bar functionality
├── data/
│   └── medications.js   # ALS medication information and dosage tools
└── assets/
    ├── images/          # UI images and icons
    ├── video/           # Optional embedded instructional videos
    └── assestsREADME.md #
```

## File Interaction and Order of Operations
1. **index.html** loads first when the user opens the app.
2. The `styles.css` file is referenced from the HTML to apply visual styles.
3. JavaScript files load in this order:
   - `app.js` initializes the app and registers event listeners.
   - `navigation.js`, `search.js`, and `patientInfo.js` are loaded next to provide specific functionality. These files rely on the global state managed by `app.js`.
   - `medications.js` is loaded last because its data is used by `patientInfo.js` for dosage calculations.
4. The `assets` directory is referenced whenever images, icons, or videos are needed in the UI.

All scripts work together within a single-page interface. No module system is used—all files attach their functionality to the global scope. The app.js file orchestrates interactions between scripts, ensuring that changes to patient data and navigation are reflected across all components.

To further illustrate how the files interact and the order in which operations occur, here is a structured breakdown (as would be detailed in the README):
Initial Page Load (HTML & CSS): The user opens index.html in a web browser. The HTML is parsed and the linked CSS (style.css) is loaded. At this stage, the static structure of the page is in place: the title and search bar are visible, the navigation buttons and sidebar button are present, the main topics list is rendered (with all subtopic lists likely hidden by default), and the patient info sidebar is in the DOM (but initially collapsed/invisible). The CSS ensures everything is styled correctly (e.g., arrows are showing next to topics, albeit not yet interactive, and any hidden elements are not displayed).
Loading JavaScript Files: Still during the page load, before closing the </body>, the sequence of script files in index.html is loaded and executed in order:
patientData.js – This loads first to set up the patient data system. On execution, it may immediately check for any saved patient info in localStorage and pre-store it in a global variable or prepare default values. It defines functions (and possibly a PatientData object or similar) that other scripts can use to query patient context. It might also attach an event to close the sidebar when clicking outside, and prepare to listen for input changes (actual event binding could also happen in an init function called later by main.js).
navigation.js – This script runs next, and it likely defines the menu handling functions and perhaps initializes some structure for the content. It may not fully attach all event listeners yet (that might be done in a initNavigation() function called by main.js once everything is ready), but it sets up global functions like expandTopic(id), openDetail(topicId), highlightItem(id), etc., and possibly reads the current patient data (via patientData.js) to decide if any menu items should be initially disabled or marked (for example, if patient data says pediatric, it could immediately mark adult sections in the DOM as .not-applicable during this load).
search.js – This is loaded third. It defines the logic for the search suggestions. Upon loading, it might create a global list of all topic names for easy searching, by scanning the DOM (which is available now) or using data from navigation.js. It sets up a function like initSearch() and perhaps a handler for search input events (though binding the event could also be deferred to main.js). No suggestions are shown yet because the user hasn’t typed anything.
history.js – Loading fourth, this script establishes the history management. On execution, it may immediately retrieve the saved history list from localStorage and store it in a structure. It sets up functions such as pushHistory(state), goBack(), goForward(), and showHistoryList(), but doesn’t activate them yet. It could also pre-render the history dropdown items if it’s designed to do so on load, or simply store them for later rendering when needed.
main.js – Finally, the main script loads and executes. By now, all necessary global functions and utilities from the other scripts are available. main.js can now call initialization routines:
It might wrap things in a DOMContentLoaded event to ensure the DOM is fully ready (although since scripts are at the bottom, the DOM is likely already complete). In either case, it triggers the setup.
For example, main.js calls initPatientDataPanel() (function in patientData.js) to attach event listeners to the sidebar toggle button and to each form field for input (for updating calculations and storing data). The patientData script might also now apply any effects of existing data: e.g., if localStorage had a weight/age, it calls a function to apply strikethroughs/fades to certain elements based on that data.
Next, main.js calls initNavigation() (in navigation.js) which attaches click handlers to each topic and arrow in the list. From now on, clicking a topic will trigger navigation.js’s toggle functions. It might also ensure the menu reflects current patient context (though that could have been done on load in navigation.js as well).
Then initSearch() is called to set up the search bar’s event listener (on keyup or input events). This links the input field to the search.js suggestion logic. It also makes sure that selecting a suggestion triggers the appropriate navigation.
initHistory() (or similar) is called to set up the Back, Forward, and History UI. This would attach click events: Back button calls history.goBack (which will use navigation functions), Forward calls history.goForward, and the History button calls history.showHistoryList (to display the list of past items). Also, if the history script needs to display an initial history list (from a past session), it might do so now by updating the History panel’s DOM.
Lastly, main.js might perform any final touches, like ensuring the home screen (main topic list) is in the correct state. If the app is meant to restore the last viewed detail on load, main.js could detect if there was a saved “last state” in history and call navigation.js to open that topic; otherwise, it ensures we start at the top of the main menu.
User Interaction – Menu Navigation: Once initialization is complete, the app is interactive:
Expanding Topics: When the user clicks a topic that has subtopics, the event listener (set by navigation.js) triggers the expand/collapse function. The corresponding sub-list is shown, and the arrow icon rotates downward. This is purely handled by navigation.js (adding/removing classes and updating aria attributes for accessibility if needed). If another topic was previously expanded, it may remain open (unless the code chooses to collapse others). The state of expansion isn’t typically pushed to the history stack (as it’s just an intermediate view), but if needed, navigation.js could keep track internally.
Viewing Details: When the user clicks on a final subtopic (a detail entry):
Navigation.js intercepts the click and will display the detail section. It hides or overlays the main list (e.g., by adding a .hidden class to the list container and removing .hidden from the detail container for that topic). The detail content becomes visible. It may also scroll to top of that content section if needed.
Navigation.js then calls history.pushState (via history.js) to record that this particular topic detail is now viewed. This updates the internal history stack for back/forward. It also likely adds this topic to the persistent history list (so it appears under the History panel).
The patientData script might immediately react (if not already done) by applying any relevant annotations to the detail content. For example, if the detail content contains dosage information or warnings, patientData.js might have listeners or triggers to update those as soon as the section is shown. In practice, navigation.js could call a function like patientData.applyToSection(detailSection) right after showing it, to refresh any dynamic content in that section according to the current patient info.
The interface now is showing the detail view. The Back button becomes enabled (if it wasn’t already).
Back Navigation: When the user clicks the Back button:
history.js handles the click. It looks at its stack of history states to determine where to go back to. For example, if the user was viewing a detail, the previous state might be “main list with X topic expanded” or possibly another detail they viewed earlier. Assuming the immediate previous is the main list, history.js will instruct navigation.js to restore that. It might call a function like navigation.showMainList(previousTopicId); navigation.js then ensures the main list is visible (unhides it), and perhaps re-expands the category that contains the detail they just came from (so the user sees where they were) and highlights that subtopic item (using a CSS highlight class). The detail section that was open is hidden again.
If the previous state was another detail (perhaps the user navigated from one detail directly to another via search or links), then navigation.js would hide the current detail and show the previous detail’s content instead, highlighting that in the menu.
History.js updates its stacks (the current state is pushed to forward stack). It also disables the Back button if there’s no further back history. Now the Forward button becomes active since we have a forward state available.
Forward Navigation: If the user clicks Forward (after having gone back):
history.js takes the state from its forward stack and re-applies it. For instance, if they went back to the main list and Forward is pressed, history will call navigation.js to reopen the detail that they had left. Navigation.js then hides the list and shows that detail content again, re-highlighting it. The Back stack is restored as well. Essentially it’s the inverse of the back operation. After moving forward, if no forward states remain, the Forward button is disabled again.
Home Button: If at any point the user clicks the Home button, the expectation is to return to the top-level main menu view quickly:
Navigation.js will handle this by collapsing all expanded sections and showing the main list from the beginning (perhaps it can remove any highlights and ensure only top-level topics are visible). It might also clear any detail content that was showing. This essentially resets the view.
History.js might treat this as a special navigation (it could optionally push a “home” state onto history, but it might also just clear the stacks since it’s a fresh start). In many cases, Home is just a convenience to get back to start without affecting history too much.
User Interaction – Search and History:
Searching for a Topic: As the user types in the search bar:
search.js captures each keystroke. It filters the topics for matching substrings. Let’s say the user types “Epi” looking for “Epinephrine.” The script finds matches (e.g., “Epinephrine” medication, maybe “EpiPen usage” if such exists) and displays them in a suggestion list below the search field. Each suggestion is an item (perhaps styled similar to the menu entries) that the user can click or arrow-key to and press Enter.
If the user selects a suggestion, search.js will handle that selection. Suppose “Epinephrine” (a medication detail) was chosen:
The search script can directly call navigation logic to open the Epinephrine detail section. For example, it might call navigation.openDetail('epinephrine') (using some identifier or the name) which will locate that item in the menu, ensure its parent categories are expanded, then show the detail content.
Alternatively, search could simulate the clicks: first programmatically trigger the expansion of the Medications category if it’s not already (by calling navigation functions for expansion), then trigger the detail. But a direct function is cleaner if available.
The detail view for Epinephrine is shown to the user. Navigation.js will highlight it in the menu and so on (as per its normal behavior).
History.js is notified of this navigation. Either navigation.js takes care of pushing to history (since it was a detail open), or search.js explicitly calls history.pushState with that item. In any case, the result is that the action is recorded. The Back button now would take the user back to whatever they were looking at before they used search (be it the main list or another detail).
If the user’s search selection was a category or intermediate topic (not a final detail), then the app would expand that category and possibly scroll to it. For instance, searching “Cardiac” might match a category “Cardiac Protocols” – selecting it would cause the menu to expand that section. The user could then click a specific protocol within. We might or might not treat the expansion itself as a history state (likely not, to keep history to actual content views).
The suggestions list will disappear once a selection is made (or if the user clears the search field). The search script likely also clears the list on blur (when the search box loses focus).
Viewing History List: The user can click the History button at any time to review what topics they’ve looked at:
history.js handles this click by toggling the history panel’s visibility (e.g., showing a dropdown or sidebar with the list of past items). If not already populated, it will create list items for each stored history entry (e.g., “Epinephrine – Medication”, “Cardiac Arrest Protocol”) in reverse chronological order. Each entry has an onclick handler (set by history.js when creating the list, or via event delegation) so that clicking it will navigate to that topic.
When a history item is clicked, history.js knows the identifier of that topic and calls the appropriate navigation function to display it. This effectively works like a jump similar to search:
Navigation.js will open that item’s detail (or expand a category, depending on what the history entry is, but likely it’s for detail pages primarily). It shows the content, highlights the item in the menu, etc.
A new history state is then pushed (because this is a new navigation event from the current view). The Back stack will now include the page the user was on before clicking the history item.
The history panel then hides (maybe automatically after a selection for a clean view).
The persistent history list itself can grow; the app might decide to cap it (for example, last 50 items) to avoid infinite growth. There might also be a “clear history” option (not specified, but a possible addition).
Dynamic Content Updates with Patient Data: Throughout the above interactions, the patient data context continuously influences the content:
If the user has entered data in the patient info panel (e.g., set patient age=4 years old, weight=18kg, indicating a pediatric case), patientData.js will have applied classes and changes to the content. So when navigation.js shows a detail section or even when listing topics, some items might already be marked as not applicable or warnings visible. For instance, an “Adult Dosage” section within a medication detail might be greyed out or struck through because the patient is a child.
If the user updates the patient info while a detail is open (say they change weight from 18kg to 20kg, or toggle an allergy), the patientData.js handlers for input change will immediately recalc and update the visible content. This could mean the dose displayed in the open Epinephrine page updates from (e.g.) 1.8 mg to 2.0 mg, or a warning appears/disappears. This live update is one reason the patientData script and navigation content script must work closely. Typically, patientData.js might expose a function like refreshContent() that recalculates all dynamic fields in whichever section is currently shown.
Additionally, changing patient info could also trigger navigation changes. For example, if a certain input makes an entire category irrelevant (imagine selecting “Adult” vs “Pediatric” case might hide the entire Pediatrics protocols section), the script might remove those from view or dim them. These are design decisions, but the capability is there.
The interplay is such that patientData.js ensures the content is always context-appropriate without requiring the user to mentally filter what doesn’t apply.
Persistence and Session Restoration: When the user closes the app (or browser tab) and later reopens it:
Because the app stores patient inputs and history in localStorage, upon reloading index.html, those values are retrieved by patientData.js and history.js in the initial loading phase (step 2 above). This means:
The patient info panel fields will repopulate with the last entered values (e.g., the age, weight, etc., previously provided). Immediately after load, patientData.js will then apply the corresponding conditional formatting to the content (so even before any interaction, the menu might show strikethroughs if appropriate).
The history list is reconstructed, so the History panel will show the past topics viewed, including ones from the last session.
Optionally, the app could even remember the last viewed detail page and automatically show it on load. The prompt doesn’t explicitly say this happens, but it’s a possible enhancement. If implemented, history.js would have saved the last state and main.js could trigger navigation.js to open that view on startup.
This persistence ensures that the user’s context is not lost between uses – an important feature for quick reference in the field, as it saves time re-entering data or finding where they left off.
All data is stored locally; since this is a static front-end app, there’s no server involved. The design relies solely on the browser’s storage and the in-memory state maintained by the JavaScript files.

