#!/bin/bash

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed


echo "CC7 Database connection configuration tool"
echo "Choose database type"
echo "1. H2"
echo "2. Oracle"
echo "3. MySQL"
echo "4. MS SQL"
echo -n "Please enter option [1 - 4] "
read dbtype

echo -n "$A Enter database connection URL: "
read DB_URL

echo -n "$A Enter database username: "
read DB_USERNAME
echo -n "$A Enter database password: "
read DB_PASSWORD

errors="0"
if [ "$DB_URL" == "" ]
then
    echo "No database connection URL entered"
    errors="1"
fi
if [ "$DB_USERNAME" == "" ]
then
    echo "No database username entered"
    errors="1"
fi
if [ "$DB_PASSWORD" == "" ]
then
    echo "No database password entered"
    errors="1"
fi
if [ "$errors" == 1 ]
then
exit
fi

case "$dbtype" in
        1)
                ds="h2-ds.xml";
                dialect="org.hibernate.dialect.H2Dialect";
                driver="org.h2.jdbcx.JdbcDataSource";
                ;;
        2)
                ds="oracle-ds.xml";
                dialect="org.hibernate.dialect.Oracle9Dialect";
                driver="oracle.jdbc.driver.OracleDriver";
                ;;
        3)
                ds="mysql-ds.xml";
                dialect="org.hibernate.dialect.MySQL5InnoDBDialect";
                driver="com.mysql.jdbc.Driver";
                ;;
        4)
                ds="mssql-ds.xml";
                dialect="org.hibernate.dialect.SQLServerDialect";
                driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
                ;;
        *)
                echo Invalid option selected;
                exit
                ;;

esac

$SED -i 's/<service>[a-zA-Z0-9]*-ds.xml<\/service>/<service>'"$ds"'<\/service>/' META-INF/jboss-app.xml
$SED -i 's/<property name=\"hibernate.dialect\" value=\"[a-zA-Z0-9\.]*\"/<property name=\"hibernate.dialect\" value=\"'"$dialect"'\$

function replace_db_config {
$SED -i 's#<connection-url>[^<]*</connection-url>#<connection-url>'"$DB_URL"'</connection-url>#'g $1
$SED -i 's/<driver-class>[^<]*<\/driver-class>/<driver-class>'"$driver"'<\/driver-class>/' $1
$SED -i 's/<user-name>[^<]*<\/user-name>/<user-name>'"$DB_USERNAME"'<\/user-name>/' $1
$SED -i 's/<password>[^<]*<\/password>/<password>'"$DB_PASSWORD"'<\/password>/' $1
}

replace_db_config mssql-ds.xml
replace_db_config mysql-ds.xml
replace_db_config oracle-ds.xml
replace_db_config h2-ds.xml


        