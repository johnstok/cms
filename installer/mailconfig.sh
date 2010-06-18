#!/bin/bash

echo
echo "Configuring mail settings."

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

mail_host=$1
mail_username=$2
mail_password=$3

$SED -i 's/auth\.username=.*/auth.username='"$mail_username"'/' content-creator.war/WEB-INF/classes/mail.properties
$SED -i 's/auth\.password=.*/auth.password='"$mail_password"'/' content-creator.war/WEB-INF/classes/mail.properties
$SED -i 's/mail\.user=.*/mail.user='"$mail_username"'/' content-creator.war/WEB-INF/classes/mail.properties
$SED -i 's/mail\.host=.*/mail.host='"$mail_host"'/' content-creator.war/WEB-INF/classes/mail.properties
$SED -i 's/mail\.pop3\.host=mail\..*/mail.pop3.host=mail.'"$mail_host"'/' content-creator.war/WEB-INF/classes/mail.properties
$SED -i 's/mail\.smtp\.host=mail\..*/mail.smtp.host=mail.'"$mail_host"'/' content-creator.war/WEB-INF/classes/mail.properties
