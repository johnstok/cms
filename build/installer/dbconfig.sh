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
                datasource="org.h2.jdbcx.JdbcDataSource";
                ;;
        2)
                dialect="org.hibernate.dialect.Oracle9Dialect";
                datasource="oracle.jdbc.xa.client.OracleXADataSource";
                ;;
        3)
                dialect="org.hibernate.dialect.MySQL5InnoDBDialect";
                datasource="com.mysql.jdbc.jdbc2.optional.MysqlXADataSource";
                ;;
        4)
                dialect="org.hibernate.dialect.SQLServerDialect";
                datasource="com.microsoft.sqlserver.jdbc.SQLServerXADataSource";
                ;;
        *)
                echo WARNING Invalid database type selected - ignoring;
                ;;
esac

$SED -i 's/<property name=\"hibernate.dialect\" value=\"[^\"]*\"/<property name=\"hibernate.dialect\" value=\"'"$dialect"'\"/' services-ejb3.jar/META-INF/persistence.xml

$SED -i 's/<xa-datasource-property name=\"URL\">[^<]*<\/xa-datasource-property>/<xa-datasource-property name=\"URL\">'"$DB_URL"'<\/xa-datasource-property>/' database-ds.xml
$SED -i 's/<xa-datasource-class>[^<]*<\/xa-datasource-class>/<xa-datasource-class>'"$datasource"'<\/xa-datasource-class>/' database-ds.xml
$SED -i 's/<user-name>[^<]*<\/user-name>/<user-name>'"$DB_USERNAME"'<\/user-name>/' database-ds.xml
$SED -i 's/<password>[^<]*<\/password>/<password>'"$DB_PASSWORD"'<\/password>/' database-ds.xml
