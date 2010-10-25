#!/bin/bash

echo
echo "Configuring logging settings."

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

LOG_PATH=$1

$SED -i 's|log4j\.appender\.CC\.File=.*|log4j.appender.CC.File='"$LOG_PATH"'|' log4j.properties
