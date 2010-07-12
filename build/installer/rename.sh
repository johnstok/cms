#!/bin/bash

# TODO check arguments
# Limit changes to old values prefixed by the following chars >/=":

echo
echo "Renaming CCC configuration files (replacing $1 with $2)."

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

function replace_name {
#    echo
    echo " Fixing $3:"
#    $SED -r -n 's/([^a-zA-Z]+)'"$1"'/\1'"$2"'/gp' $3
    $SED -r -i 's/([^a-zA-Z]+)'"$1"'/\1'"$2"'/g' $3
}

replace_name "$1" "$2" META-INF/application.xml
replace_name "$1" "$2" META-INF/jboss-app.xml
replace_name "$1" "$2" services-ejb3.jar/META-INF/persistence.xml
replace_name "$1" "$2" auth-config.xml
replace_name "$1" "$2" auth-service.xml
replace_name "$1" "$2" database-ds.xml
replace_name "$1" "$2" content-creator.war/WEB-INF/web.xml
replace_name "$1" "$2" content-creator.war/WEB-INF/classes/build.properties
