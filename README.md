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
sudo -n yum install java-1.8.0
sudo -n yum remove java-1.7.0-openjdk
export JETTY=https://repo1.maven.org/maven2/org/eclipse/jetty/jetty-distribution/9.4.14.v20181114/jetty-distribution-9.4.14.v20181114.zip
curl -o jetty.zip $JETTY
unzip jetty.zip
mv jetty-distribution* jetty
cd jetty
export JETTY_HOME=$(pwd)
```
