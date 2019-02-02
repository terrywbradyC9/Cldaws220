## CLDAWS 220 Files

TODO: modify maven to deploy jsp file appropriately.

[dns]:8080/app/?url=http://www.textfiles.com/etext/FICTION/warpeace.txt&count=20

## Actions required

- Create EC2 with Amazon Linux
- Install Java 8: https://serverfault.com/questions/664643/how-can-i-upgrade-to-java-1-8-on-an-amazon-linux-server?newreg=c859f7fa6cd84574ad4cd9438fcb5b4f
- Install Jetty
- Configure Jetty App (done manually on Cloud9)
  - deploy.ini
  - http.ini
  - jsp.ini

## Server startup script

```
export JETTY=https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.14.v20181114/jetty-distribution-9.4.14.v20181114.zip
export MAVEN=http://apache.osuosl.org/maven/maven-3/3.6.0/binaries/apache-maven-3.6.0-bin.zip

sudo -n yum install java-1.8.0-openjdk-devel
sudo -n yum remove java-1.7.0-openjdk
sudo -n yum install git

curl -o jetty.zip $JETTY
unzip jetty.zip
mv jetty-distribution* jetty
cd jetty
export JETTY_HOME=$(pwd)
cd ..

curl -o maven.zip $MAVEN
unzip maven.zip
export PATH=$PATH:/home/ec2-user/apache-maven-3.6.0/bin
```

## Project Build
```
git clone https://github.com/terrywbrady/CldAws220.git
cd CldAws220
mvn install

mkdir ~/awsProj
cd awsProj
java -jar $JETTY_HOME/start.jar --create-startd --add-to-start=jsp,http,deploy
mkdir webapps
cd webapps
mkdir wordCount
cd wordCount
mkdir WEB-INF
cd WEB-INF
mkdir lib
cp /home/ec2-user/CldAws220/src/main/resources/* .
cp /home/ec2-user/CldAws220/target/*.jar WEB-INF/lib
```

## Enable Service startup

`sudo vi /etc/rc.local`

Add the following

```
export JETTY_HOME=/home/ec2-user/jetty
cd /home/ec2-user/awsProj
java -jar $JETTY_HOME/start.jar
```
