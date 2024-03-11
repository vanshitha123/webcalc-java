# Dev-sec-Ops
### Github+Jenkins+Maven+Tomcat+MySQL
Requirements:
Ubuntu 22.04

Jenkins

Maven

Tomcat

MySQL

Git commands:
------------

```
git clone <repo_url>
git add .
git commit -m "message"
git push origin -u <branch_name>
git branch 			#To check barnch name
git checkout -b <branch_name> 	#To create banch name
git checkout <branch-name> 	#To switch branch
```

#### Jenkins Installations in ubuntu22.04:

```
sudo apt update
sudo apt install openjdk-11-jdk -y
wget -q -O - https://pkg.jenkins.io/debian-stable/jenkins.io.key |sudo gpg --dearmor -o /usr/share/keyrings/jenkins.gpg
sudo sh -c 'echo deb [trusted=yes] https://pkg.jenkins.io/debian binary/ > /etc/apt/sources.list.d/jenkins.list'
sudo apt update
sudo apt install jenkins
sudo systemctl status jenkins
sudo cat /var/lib/jenkins/secrets/initialAdminPassword
```
Once complete the installation open your browser 
```
http://<your_ip>:8080/
```
ex: http://192.168.138.110:8080/

Now give administrative password  
to check administrative password ```sudo cat /var/lib/jenkins/secrets/initialAdminPassword```

now select "Install suggested plugins" option 

Create user details then save & continue.

##### Install plugins: Maven, deploy to container
To install plugins: Go to Manage Jenkins > Plugins > available plugins

Maven installation
-----------------
```
sudo apt install maven -y
```
integrate java and maven with jenkins :
---------------------------------------
configure JAVA_HOME // to get this ```sudo find / -name java-11-openjdk*```

path like : /usr/lib/jvm/java-11-openjdk-amd64


Configure MAVEN_HOME // to get this ```mvn --version```

path like : /usr/share/mvn

Go to jenkins>manage Jenkins> Tools> Maven
![Screenshot from 2024-02-29 15-33-12](https://github.com/beeru405/webcalc-java/assets/101712802/8d4879a5-0e31-419e-bb04-032acc3fc225)

Go to jenkins>manage Jenkins> Tools> jdk
![Screenshot from 2024-02-29 15-32-25](https://github.com/beeru405/webcalc-java/assets/101712802/8fd6e732-9792-4e69-90fd-8bdd955c5324)

Tomcat Installation:
------------------
```
wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.86/bin/apache-tomcat-9.0.86.tar.gz
tar -xvzf apache-tomcat-9.0.86.tar.gz
```
To start tomact 
```
cd apache-tomcat-9.0.86
cd bin/
./startup.sh
```
To stop tomcat
```
cd apache-tomcat-9.0.86
cd bin/
./shutdown.sh
```

If running jenkins and tomcat in single machine you shoud change port of tomcat or jenkins because both default port is 8080

To change port number of tomcat

go to tomcat 
```
cd apache-tomcat-9.0.86.tar.gz
cd conf/
#nano server.xml
#change port number instead 8080 change any number like 8090
```
#### Check point :
Access tomcat application from browser on port 8090  
 - http://<Public_IP>:8090

1. now application is accessible on port 8090. but tomcat application doesnt allow to login from browser. changing a default parameter in context.xml does address this issue
   ```sh
   #search for context.xml
   find / -name context.xml
   ```
1. above command gives 3 context.xml files. comment (<!-- & -->) `Value ClassName` field on files which are under webapp directory. 
After that restart tomcat services to effect these changes. 
At the time of writing this lecture below 2 files are updated. 
   ```sh 
   /opt/tomcat/webapps/host-manager/META-INF/context.xml
   /opt/tomcat/webapps/manager/META-INF/context.xml
   
   # Restart tomcat services
   ```
1. Update users information in the tomcat-users.xml file
goto tomcat home directory and Add below users to conf/tomcat-users.xml file
   ```sh
	<role rolename="manager-gui"/>
	<role rolename="manager-script"/>
	<user username="admin" password="admin" roles="manager-gui, manager-script"/>
	<user username="deployer" password="deployer" roles="manager-script"/>
	<user username="tomcat" password="tomcat" roles="manager-gui"/>
   ```
1. Restart serivce and try to login to tomcat application from the browser. This time it should be Successful

Now Setup tomcat in Jenkins:
---------------------------
Go to mange jenkins>credentials>system>globalcredential> Add credential

give tomcat credentials
![Screenshot from 2024-02-29 15-30-12](https://github.com/beeru405/webcalc-java/assets/101712802/625c1ee2-7ceb-4d51-ac88-0d76e21c0e77)



-------
Install MySQL:
--------------
```
sudo apt update
sudo apt install mysql-server -y
sudo systemctl start mysql.service
sudo mysql_secure_installation
```
mysql user creation
```
CREATE USER 'mysql'@'%' IDENTIFIED BY 'mysql' ;
```
Grant All Privileges:
```
GRANT ALL PRIVILEGES ON *.* TO 'mysql'@'%' WITH GRANT OPTION;
```
Flush Privileges:
```
FLUSH PRIVILEGES;
```
Create database 
```
create database myDB;
```
use database
```
use myDB
```
create table
```
CREATE TABLE calculations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    operation VARCHAR(255) NOT NULL,
    result BIGINT NOT NULL
);
```

To check the data 
```
select * from calculations;
```
#### Sonarqube Installation
-----------------------------
We will be installing Docker for installing Sonarqube in the container
```
sudo apt-get update
sudo apt-get install docker.io -y
```

After this gran the Ubuntu user the permission to access Docker
```
sudo usermod -aG docker $USER
newgrp docker
sudo chmod 777 /var/run/docker.sock
```
Starting Sonarqube container:
```
docker run -d --name sonarqube-cont -p 9000:9000 sonarqube:lts-community
```
#To login
```
http://<PUBLIC-IP-OF-INSTANCE>:9000
```

###### changes you need to do
1. calculator.java - Give your MySQL details
2. Jenkinsfile - Github link,Tomcat details

##### Reference
GitHub Link: ```https://github.com/kishore3k/webcalculator/tree/master```
