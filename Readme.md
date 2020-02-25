Date : 25/02/2020

## How To Run the project ##

1. Through IntelliJ Community Edition :
	
	a) Load the project as Maven project :-- File-> New -> Project From Exisiting Sources
	b) Run the Application class


2. Run as a deployable jar
	
	a) Run " mvn clean compile assembly:single " command from the Application directory (where pom.xml is present) 
	b) then cd to target folder and run " java -jar ImpactHub_bot-1.0-SNAPSHOT-jar-with-dependencies.jar "


## How to interact with the bot ##

1. Run the project (see above steps).
2. Search for thinkitive_TestBot in Telegram
3. Send any message to the bot and an acknowledgement from the Application will be recieved
	Additionally
	-- /myname command will display full name of the user interacting with the bot (The name will be displayed on the application console where the project is running)
