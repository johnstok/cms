#!/bin/bash

echo
echo "Configuring database settings."

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

dbtype=$1
DB_URL=$2
DB_USERNAME=$3
DB_PASSWORD=$4

case "$dbtype" in
        1)
                dialect="org.hibernate.dialect.H2Dialect";
                driver="org.h2.jdbcx.JdbcDataSource";
                ;;
        2)
                dialect="org.hibernate.dialect.Oracle9Dialect";
                driver="oracle.jdbc.driver.OracleDriver";
                ;;
        3)
                dialect="org.hibernate.dialect.MySQL5InnoDBDialect";
                driver="com.mysql.jdbc.Driver";
                ;;
        4)
                dialect="org.hibernate.dialect.SQLServerDialect";
                driver="com.microsoft.sqlserver.jdbc.SQLServerDriver";
                ;;
        *)
                echo WARNING Invalid database type selected - ignoring;
                ;;
esac

$SED -i 's/<property name=\"hibernate.dialect\" value=\"[^\"]*\"/<property name=\"hibernate.dialect\" value=\"'"$dialect"'\"/' services-ejb3.jar/META-INF/persistence.xml

$SED -i 's#<connection-url>[^<]*</connection-url>#<connection-url>'"$DB_URL"'</connection-url>#'g database-ds.xml
$SED -i 's/<driver-class>[^<]*<\/driver-class>/<driver-class>'"$driver"'<\/driver-class>/' database-ds.xml
$SED -i 's/<user-name>[^<]*<\/user-name>/<user-name>'"$DB_USERNAME"'<\/user-name>/' database-ds.xml
$SED -i 's/<password>[^<]*<\/password>/<password>'"$DB_PASSWORD"'<\/password>/' database-ds.xml
