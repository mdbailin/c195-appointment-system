## QAM2 — QAM2 Task 1: Java Application Development 🖥️

### Purpose of application
The purpose of this application is to provide a GUI-based scheduling desktop application.

### Author Information
- Name: Matthew Bailin
- Student #: 000919530
- Application version: 1.0 📝
- Date: 03/30/2023 📅

### IDE and Java module Information
- IntelliJ IDEA 2021.3.2 (Community Edition) 🚀
- Build #IC-213.6667.63, built on March 30, 2023
- Runtime version: 11.0.13+8-b1511.21 amd64
- VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
- Non-Bundled plugins: com.intellij.ideolog, org.intellij.plugins.markdown, com.intellij.markdown, com.intellij.jrebel, org.sonarlint.idea, com.intellij.nevercode, MavenRunHelper, prezento, edu.hm.hafner.findbugs, com.intellij.ideavim, com.intellij.nyan, intellij.prettierJS, de.halirutan.mathematica

#### JavaFX: OpenJFX-17.0.1 🔲

#### MySQL Connector: mysql-connector-java-8.0.27 🗄️

### Additional report
For the custom report, I displayed the number of appointments per country 🌎. I made the SQL query do all the work and created inner joins that combined three tables to output two columns (Country and count). The query was complicated, but the Java side was simple. I was able to pull this information into a table view to present the data.

### How to run the program
To run the program:
1. Start the program
2. A login screen will be presented
3. Enter a valid username and password that matches the information in a MySQL database
4. Ensure that Java 11 is installed on your machine as the program has not been tested with any other JVM.