# TODO check arguments
# Limit changes to old values prefixed by the following chars >/=":

function replace_name {
    echo
    echo Fixing $3:
    sed -r -n 's/([^a-zA-Z]+)'"$1"'/\1'"$2"'/gp' $3
    sed -r -i 's/([^a-zA-Z]+)'"$1"'/\1'"$2"'/g' $3
}

echo Renaming CCC configuration files.
echo Replacing $1 with $2

replace_name $1 $2 META-INF/application.xml
replace_name $1 $2 META-INF/jboss-app.xml
replace_name $1 $2 services-ejb3-7.0.0-SNAPSHOT.jar/META-INF/persistence.xml
replace_name $1 $2 auth-config.xml
replace_name $1 $2 auth-service.xml
replace_name $1 $2 mail-service.xml
replace_name $1 $2 oracle-ds.xml
replace_name $1 $2 mysql-ds.xml
replace_name $1 $2 h2-ds.xml
replace_name $1 $2 mssql-ds.xml
replace_name $1 $2 content-creator-7.0.0-SNAPSHOT.war/WEB-INF/web.xml

echo

