# ViperRocksSeniorDesign
=========================
Database
=========================

Postgres Database: viper
Database name: viper
Database owner: viper
Database schema: public
Password: viperr0cks!

=========================
Viper Webservice
=========================
Java version:
open jdk version “11.0.22” 2024-01-16 LTS

Tomcat 10
=========================
Location: /usr/share/tomcat

To start:
cd /usr/share/tomcat
./bin/startup.sh

To Stop tomcat
cd /usr/share/tomcat
./bin/shutdown.sh

Viper Webservice
=========================
Deployment location: /usr/share/tomcat/webapps
Source Location: /opt/viper/source/viperws

To develop on your local environment or your local computer, install Java (zulu 11), and Tomcat 10. You can use any Java IDE for your development. I used Apache Netbeans to create this sample application. Apache Netbeans is a free IDE. First, copy the viperws project folder from source to your local computer. Open the project with Netbean or other IDE. You would need to configure it to use java11 and tomcat10.

Sample service code is located at src/main/java/gov/nasa/jpl/viperws/SampleWebService.java jdbc configuration to connect to Postgres Database is located at src/main/resources/jdbc.properties application configuration file is located at src/main/resources/application.json

Once you build from the IDE, you will see a war file under target directory.

To deploy viperws webservices, you need to copy the war file to machine where you want to host it. Here is sample command to transfer. 
scp viperws-1.0-SNAPSHOT war viper@<host name>:/tmp

Once it has been transferred, ssh to the hosting machine. First, go to the deployment location and uninstall by removing the existing war file.

rm viperws.war

You would need to wait until the viperws directory is also deleted. Then, you can copy the new war file to here.

cp/tmp/viperus-oo.war viperws.war

in about 5 seconds, you will see viperws directory created. Your new viperws has been deployed and ready to be used.

Just to start, I have created this sample webservice.

Sample Webservice call :
curl https://<host>/viperws/ws/sample_service/sample_function?sampleInput=asf

This webservice will connect to the postgres database and retreive data out and create ison result and return.

=========================
UI
=========================

To develop:
Copy the src directory from /opt/viper/source/viper-rocks-src to your computer.
Refer to the read me there for how to develop. All development should be on your computer and github.

To Deploy:
Refer to the readme in the viper-rocks src for build instructions.
After the project is built upload the contents of the dist folder to /usr/share/tomcat./webapps/ROOT
index.html should be in that directory.



![viper-db](https://github.com/user-attachments/assets/4e2a6460-54ac-42cc-a52e-f600212ec6e4)


