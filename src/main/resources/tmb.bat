@echo off
java -Dlogback.configurationFile=logback.xml -Dorg.neotech.config.app=etc/tmb-app.properties -Dorg.neotech.config.passwd=etc/tmb-pswd.properties -jar tmb-1.0.jar %*