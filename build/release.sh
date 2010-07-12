mvn clean install -P assemble
mvn release:prepare
mvn clean install
svn update --force
mvn release:prepare
mvn release:clean
