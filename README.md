# Introduction
# Requirements
To run this project there are two main requirements:
1. JAVA > 1.8
2. Maven 
# Usage Instructions

* To install the package,download the source code
> git clone https://github.com/yadavprash08/krishigrainclassifier.git

* Install the local libraries into maven local repository required to build the project.
> sh ./install-local-maven.sh

* Once the repository is downloaded, move to the source git repository and run the command
> mvn install

* This will download all the dependent libraries required to run this project. Once the maven install is completed 
successfully, you should be able to spin the web-server quickly.
 
 Once the installation is done, you could start the local server using this command:

> java -jar target/krishi-grain-classifier-1.0-SNAPSHOT.jar


