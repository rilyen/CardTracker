# CardTracker
Java application to manage and categorize playing cards based on type and rarity.
Consists of two projects: webserver and client.

# Requirements
This project uses Java 22 and JavaFX SDK version 22.0.1. 
Ensure the dependencies are loaded from Maven and SDK is added to library.
``File -> Project Structure -> libraries -> + -> Java``
and select the lib folder from the JavaFX SDK.

# Build and Run the Project
I am using IntelliJ to run this project, so I will describe it from that perspective.
The webserver and the client application need to be run simultaneously. This can be done by opening the projects in separate windows.

Build and run the WebServerApplication class found in 
``/webserver/src/main/java/ca.cmpt213.webserver/``

Build and run the TokimonApplication class found in
``/webserver/src/main/java/ca.cmpt213.client/``

# Using the Project
A separate application window will open up, where you can add, edit, delete, view all your cards, and view a specific card.
The data is stored in a JSON file to ensure persistence.
The card image shown depends on the type (based on Pokemon types), and are included in resources folder in the webserver application.
In this sample, I have included some cards already in the tracker.
