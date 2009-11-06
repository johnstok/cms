# TODO check arguments
# TODO other datasources
echo "Renaming CCC configuration files."

echo
echo "Fixing application.xml:"
sed -n 's/'"$1"'/'"$2"'/gp' target/application-ear-7.0.0-SNAPSHOT/META-INF/application.xml

echo
echo "Fixing jboss-app.xml:"
sed -n 's/'"$1"'/'"$2"'/gp' target/application-ear-7.0.0-SNAPSHOT/META-INF/jboss-app.xml

echo
echo "Fixing persistence.xml:"
sed -n 's/'"$1"'/'"$2"'/gp' target/application-ear-7.0.0-SNAPSHOT/META-INF/persistence.xml

echo
echo "Fixing auth-config.xml:"
sed -n 's/'"$1"'/'"$2"'/gp' target/application-ear-7.0.0-SNAPSHOT/auth-config.xml

echo
echo "Fixing auth-service.xml:"
sed -n 's/'"$1"'/'"$2"'/gp' target/application-ear-7.0.0-SNAPSHOT/auth-service.xml

echo
echo "Fixing mail-service.xml:"
sed -n 's/'"$1"'/'"$2"'/gp' target/application-ear-7.0.0-SNAPSHOT/mail-service.xml

echo
echo "Fixing oracle-ds.xml:"
sed -n 's/'"$1"'/'"$2"'/gp' target/application-ear-7.0.0-SNAPSHOT/oracle-ds.xml

echo
echo "Fixing web.xml:"
sed -n 's/'"$1"'/'"$2"'/gp' target/application-ear-7.0.0-SNAPSHOT/content-creator-7.0.0-SNAPSHOT.war/WEB-INF/web.xml

echo

