echo "CC7 install tool"

echo -n "$A CC version: "
read cc_version

echo -n "$A DB username: "
read db_user

echo -n "$A DB password: "
read db_password

echo -n "$A DB url: "
read db_url

echo -n "$A DB version: "
read db_version

echo -n "$A CC initial user's username: "
read cc_username

echo -n "$A CC initial user's password: "
read cc_password

echo -n "$A CC initial user's email: "
read cc_email

mkdir filestore
chmod a+w filestore

mkdir lucene
chmod a+w lucene

java -cp shell-tools-$cc_version-jar-with-dependencies.jar ccc.cli.Schema   -c $db_url -u $db_user -p $db_password -v $db_version
java -cp shell-tools-$cc_version-jar-with-dependencies.jar ccc.cli.Users    -c $db_url -u $db_user -p $db_password -ne $cc_email -np $cc_password -nu $cc_username
java -cp shell-tools-$cc_version-jar-with-dependencies.jar ccc.cli.Settings -c $db_url -u $db_user -p $db_password -path `pwd`/
