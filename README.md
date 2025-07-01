# Paramedic Quick Reference

This repository outlines the planned structure for the **Paramedic Quick Reference** web application. The app is a single-page HTML, CSS, and JavaScript project that provides paramedics with quick access to critical information.

## Planned Directory Structure

```
/ (project root)
├── index.html           # Main page of the application
├── css/
│   └── styles.css       # Global styles
├── js/
│   ├── app.js           # Initializes the app and manages state
│   ├── navigation.js    # Back, forward, and home navigation logic
│   ├── search.js        # Search bar functionality
│   └── patientInfo.js   # Patient data sidebar and related effects
├── data/
│   └── medications.js   # ALS medication information and dosage tools
└── assets/
    ├── images/          # UI images and icons
    └── video/           # Optional embedded instructional videos
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
