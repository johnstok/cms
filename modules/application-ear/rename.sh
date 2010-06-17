# TODO check arguments
# Limit changes to old values prefixed by the following chars >/=":

# Alexios (2009-12-10) fix sed on machines that don't use GNU as their
# default toolset (if it's not Linux and gsed(1) is available, use that
# instead).
SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed


function replace_name {
    echo
    echo "Fixing $3:"
    $SED -r -n 's/([^a-zA-Z]+)'"$1"'/\1'"$2"'/gp' $3
    $SED -r -i 's/([^a-zA-Z]+)'"$1"'/\1'"$2"'/g' $3
}

echo Renaming CCC configuration files.
echo "Replacing $1 with $2"

replace_name "$1" "$2" META-INF/application.xml
replace_name "$1" "$2" META-INF/jboss-app.xml
replace_name "$1" "$2" services-ejb3.jar/META-INF/persistence.xml
replace_name "$1" "$2" auth-config.xml
replace_name "$1" "$2" auth-service.xml
replace_name "$1" "$2" oracle-ds.xml
replace_name "$1" "$2" mysql-ds.xml
replace_name "$1" "$2" h2-ds.xml
replace_name "$1" "$2" mssql-ds.xml
replace_name "$1" "$2" content-creator.war/WEB-INF/web.xml

echo

# End of file.
