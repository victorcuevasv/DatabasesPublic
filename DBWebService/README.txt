1) Configure Tomcat

In case the port 8080 is occupied change it to another port, for example 8082. In
the file apache-tomcat-7.0.69/conf/server.xml change the port in the element

<Connector port="8082" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" />


Add these lines to conf/tomcat-users.xml

<role rolename="manager-gui"/>  
<role rolename="manager-script"/>  
<role rolename="manager-jmx"/>  
<role rolename="manager-status"/>  
<user username="tomcat" password="tomcat" roles="manager-gui,manager-script,manager-jmx,manager-status"/>



2) Create the webapp project:

mvn archetype:generate -DarchetypeGroupId=org.glassfish.jersey.archetypes  
-DarchetypeArtifactId=jersey-quickstart-webapp -DgroupId=org.mydb.projects.dbwebservice 
-DartifactId=DBWebService -DinteractiveMode=false



3) Deploy the webapp:

Go to the Tomcat manager

http://localhost:8082/manager/

Upload the .war file



4) Execute the web service

Invoke the web service directy from the browser

http://localhost:8082/DBWebService/webapi/tableresource?tablename=emp&limit=10


5) Deploy the webapp directly from Maven to Tomcat 7

a) In the file %TOMCAT7_PATH%/conf/tomcat-users.xml

Add an user with roles manager-gui and manager-script:

<tomcat-users>

	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<user username="admin" password="password" roles="manager-gui,manager-script" />

</tomcat-users>


b) Tomcat 7 authentication

In the file %MAVEN_PATH%/conf/settings.xml

Add the Tomcat 7 server

<settings ...>
	<servers>
	   
		<server>
			<id>TomcatServer</id>
			<username>admin</username>
			<password>password</password>
		</server>

	</servers>
</settings>

c) Add the Tomcat7 Maven Plugin in pom.xml
  (verify that the port is the correct one if it was changed from 8080)


	<plugin>
		<groupId>org.apache.tomcat.maven</groupId>
		<artifactId>tomcat7-maven-plugin</artifactId>
		<version>2.2</version>
		<configuration>
			<url>http://localhost:8080/manager/text</url>
			<server>TomcatServer</server>
			<path>/mkyongWebApp</path>
		</configuration>
	</plugin>


d) Deploy to Tomcat

Commands to manipulate WAR file on Tomcat.

mvn tomcat7:deploy 
mvn tomcat7:undeploy 
mvn tomcat7:redeploy




