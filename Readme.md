Date : 25/02/2020

### How To Run the project ###

1. Through IntelliJ Community Edition :
	
	a) Load the project as Maven project :-- File-> New -> Project From Exisiting Sources
	b) Run the Application class


2. Run as a deployable jar
	
	a) Run " mvn clean compile assembly:single " command from the Application directory (where pom.xml is present) 
	b) then cd to target folder and run " java -jar ImpactHub_bot-1.0-SNAPSHOT-jar-with-dependencies.jar "



### How to interact with the bot ###

1. Run the project (see above steps).
2. Search for thinkitive_TestBot in Telegram
3. Send any message to the bot and an acknowledgement from the Application will be recieved
	Additionally
	-- /myname command will display full name of the user interacting with the bot (The name will be displayed on the application console where the project is running)



### Setting up Access to Google SpreadSheet ###
	
	1. The Google Sheets API requires OAuth 2.0 authorization before we can access it through an application.
	   We need to obtain the "google-sheets-client-secret.json" which will have the necessary information to authorize our requests.
	   The first step in the Google Quickstart guide contains detailed information on how to do this.
	   
	   Link :  https://developers.google.com/sheets/api/quickstart/java
	   		Sign-in with you account and click on the " Enable the Google Sheets API " button.
	   		This will give you an option to download the "google-sheets-client-secret.json" file.

	2. Once downloaded add the file to the src/main/resources/ folder in the Project



### Access Google Spreadsheet ###

	Funtions : 1) fetchWithNumber      : It'll retrieve the details of a User by querying his phone number(mentioned in the acceptance criteria) and display it on the console.
			   2) fetchWithAdminFlag   : It'll retrieve all records with admin flag set to true and display them on the console.
			   Note : Curently the arguments to these funtions have been harcoded.



### Write Google Spreadsheet ###
	
	Functions : 1) writeUserToSheet	    : It'll write a new User record to the Sheet.
	 			2) updateUserMembership : It'll update the IH-Membership of a particular User.
				Note : Curently the arguments to these funtions have been harcoded. 



Date : 26/02/2020

### Functionality Upgrade ###

	Implemented new functionality so that :

 	1) TestBot can now ask Users for their ContactNumbers and store it in the Applicaiton(not DB persistent)
	2) TestBot can also ask Users for their Location and store it in the Application(not DB persistent)


Date : 27/02/2020

### Functionality Upgrade ###

	Created ImpactHub GateWay bot :- Provides a list of options to the User.
	Implementation is in progress.