#!/bin/bash

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

echo "CC7 Mail configuration tool"

echo -n "$A Enter a username: "
read mail_username

echo -n "$A Enter a password: "
read mail_password

echo -n "$A Enter a host: "
read mail_host

errors="0"
if [ "$mail_username" == "" ]
then
    echo "No username entered"
    errors="1"
fi
if [ "$mail_password" == "" ]
then
    echo "No password entered"
    errors="1"
fi
if [ "$mail_host" == "" ]
then
    echo "No mail host entered"
    errors="1"
fi
if [ "$errors" == 1 ]
then
exit
fi

$SED -i 's/<attribute name=\"User\">[^<]*<\/attribute>/<attribute name=\"User\">'"$mail_username"'<\/attribute>/' "mail-service (copy).xml"
$SED -i 's/<attribute name=\"Password\">[^<]*<\/attribute>/<attribute name=\"Password\">'"$mail_password"'<\/attribute>/' "mail-service (copy).xml"
$SED -i 's/<property name=\"mail.user\" value=\"[^\"]*\"/<property name=\"mail.user\" value=\"'"$mail_username"'\"/' "mail-service (copy).xml"
$SED -i 's/<property name=\"mail.host\" value=\"[^\"]*\"/<property name=\"mail.host\" value=\"'"$mail_host"'\"/' "mail-service (copy).xml"
$SED -i 's/<property name=\"mail.pop3.host\" value=\"mail\.[^\"]*\"/<property name=\"mail.pop3.host\" value=\"mail.'"$mail_host"'\"/' "mail-service (copy).xml"
$SED -i 's/<property name=\"mail.smtp.host\" value=\"mail\.[^\"]*\"/<property name=\"mail.smtp.host\" value=\"mail.'"$mail_host"'\"/' "mail-service (copy).xml"

