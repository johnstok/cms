#!/bin/bash

clear
mvn clean install
echo Press RETURN to continue

read $answer

echo  --------------------
echo   CCC7 Deploy Script
echo  --------------------

cd core-domain
mvn jboss:deploy
cd ..
cd business-services
mvn jboss:deploy
cd ..
cd services-ejb3
mvn jboss:deploy
cd ..

