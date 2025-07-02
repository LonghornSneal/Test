**assets/** – Contains static assets (images, videos, etc.) used in the application.

- **images/** – Image files for icons and illustrations.  
    - *Examples:* Arrow icons for the menu (e.g. `arrow-right.png` and `arrow-down.png` used as expand/collapse indicators). The app might use a single arrow graphic rotated via CSS, but separate image files or an SVG sprite can also be used.  
    - Other images, such as protocol diagrams or medication reference illustrations, are stored here. These are referenced in the HTML detail content (for example: `<img src="assets/images/diagram.png" alt="Protocol Diagram">`).  
    - Any logo or branding images for the app would also reside in this folder.

- **video/** – Video files for training or instructional content (if any).  
    - Large videos are typically streamed or embedded from external sites (e.g. YouTube), but smaller clips could be included here for offline use.  
    - The app’s detail sections might use `<video>` tags to play these files or `<iframe>` elements for externally hosted videos.

- *(Other assets)* – Additional subfolders can be added as needed (for example, a **fonts/** directory for custom font files).  
    - In this project, most reference information is handled within the application code itself (for example, medication data is in `data/medications.js`), so few external data files are needed at this stage.