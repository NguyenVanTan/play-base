# play-base
# HOW TO CREATE A NEW PLAY PROJECT USING JAVA
- Install Java 8.0 or higher
- Install SBT - Scala Build Tool (download and install at http://www.scala-sbt.org/download.html). Reboot the PC if you install on Window
- Install Eclipse Oxygen (http://www.eclipse.org/downloads/packages/release/Oxygen/M2)
- Create a folder to store your projects (workspace) and open terminal, move to that folder
- Excecute command: sbt 

- Execute the command to init a play project for Java:
sbt new playframework/play-java-seed.g8
(waiting for download dependency). At the end of this step, enter some field like: project name, package core name, sbt version ....
Example: 
project name: framgia_demo
package root: com.framgia.self_study
.....
- Move to framgia_demo folder and execute command to create a project for eclipse
sbt eclipse

- Open eclipse, using Import wizard to import your project

# HOW TO RUN
- Open terminal and move to your project folder.
- Run command: sbt run
- When the message "Server started ...." display on console log, open browser and enter link: http://localhost:9000/login
