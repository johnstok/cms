#!/bin/bash

echo
echo "Configuring mail settings."

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

mail_host=$1
mail_username=$2
mail_password=$3

$SED -i 's/<attribute name=\"User\">[^<]*<\/attribute>/<attribute name=\"User\">'"$mail_username"'<\/attribute>/' mail-service.xml
$SED -i 's/<attribute name=\"Password\">[^<]*<\/attribute>/<attribute name=\"Password\">'"$mail_password"'<\/attribute>/' mail-service.xml
$SED -i 's/<property name=\"mail.user\" value=\"[^\"]*\"/<property name=\"mail.user\" value=\"'"$mail_username"'\"/' mail-service.xml
$SED -i 's/<property name=\"mail.host\" value=\"[^\"]*\"/<property name=\"mail.host\" value=\"'"$mail_host"'\"/' mail-service.xml
$SED -i 's/<property name=\"mail.pop3.host\" value=\"mail\.[^\"]*\"/<property name=\"mail.pop3.host\" value=\"mail.'"$mail_host"'\"/' mail-service.xml
$SED -i 's/<property name=\"mail.smtp.host\" value=\"mail\.[^\"]*\"/<property name=\"mail.smtp.host\" value=\"mail.'"$mail_host"'\"/' mail-service.xml
