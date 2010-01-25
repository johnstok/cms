#!/bin/bash

clear
h2_version="1.1.118"
echo  -----------------
echo   H2 [V. $h2_version]
echo  -----------------

cd $HOME/.m2/repository/com/h2database/h2/${h2_version}
java -cp h2-${h2_version}.jar org.h2.tools.Server -tcpAllowOthers

