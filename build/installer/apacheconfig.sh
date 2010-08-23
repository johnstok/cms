#!/bin/bash

echo
echo "Creating Apache configuration file."

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

function replace_name {
#    echo
    echo " Fixing $3:"
    $SED -r -i 's/([^a-zA-Z]+)'"$1"'/\1'"$2"'/g' $3
}

replace_name cc7server "$1" apache_template
replace_name cc7admin "$2" apache_template
replace_name cc7 "$3" apache_template
