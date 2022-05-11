
DOWNOADING, BUILDING AND RUNNING THE APPLICATION
Instruction to download, build and run  the application
1) crate a new folder, get in to console mode typing cmd on address bar
2) CLONE REPOSITORY: git clone https://github.com/ruggeromontesi/supermarket.git
3) get into root folder: supermarket typing cd supermarket
4) build :mvn clean install -f pom.xml
5) launch the application typing :      mvn compile exec:java -Dexec.mainClass="org.reiz.App"


Instruction to download and run (WITHOUT BUILDING) the application.
1) crate a new folder, get in to console mode typing cmd on address bar
2) CLONE REPOSITORY: git clone https://github.com/ruggeromontesi/supermarket.git
3) get into root folder: supermarket typing cd supermarket
4) type java -jar reiztask-1.0-SNAPSHOT.jar


###################################################################################################

RUNNING AND INSTALLATION WIZARD(Windows environment)
Launch the script install.bat (Required maven and java 11 installed).
This will clone github repository, build and launch the application. 
