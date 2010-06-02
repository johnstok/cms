#!/bin/bash

script_dir=`dirname $0`
cc_version="${project.version}"
db_version="${ccc.db.version}"

echo
echo "CC7 install tool"

echo
echo -n "Application name: "
read app_name

echo
echo "Database connection"
echo -n " DB username: "
read db_user
echo -n " DB password: "
read db_password
echo -n " DB url: "
read db_url
echo " Choose database type"
echo " 1. H2"
echo " 2. Oracle"
echo " 3. MySQL"
echo " 4. MS SQL"
echo -n " Please enter option [1 - 4]: "
read db_type

echo
echo "Mail configuration"
echo -n " Enter a username: "
read mail_username
echo -n " Enter a password: "
read mail_password
echo -n " Enter a host: "
read mail_host

echo
echo "Initial user"
echo -n " CC initial user's username: "
read cc_username
echo -n " CC initial user's password: "
read cc_password
echo -n " CC initial user's email: "
read cc_email

mkdir filestore
chmod a+w filestore

mkdir lucene
chmod a+w lucene

mkdir cc-$cc_version
unzip cc-$cc_version.ear -d cc-$cc_version > /dev/null

cd cc-$cc_version
../$script_dir/rename.sh cc7 $app_name
../$script_dir/dbconfig.sh $db_type $db_url $db_user $db_password
../$script_dir/mailconfig.sh $mail_host $mail_username $mail_password
cd ..

echo
echo "Creating database schema."
java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Schema   -c $db_url -u $db_user -p $db_password -v $db_version

echo
echo "Creating initial user."
java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Users    -c $db_url -u $db_user -p $db_password -ne $cc_email -np $cc_password -nu $cc_username

echo
echo "Saving further configuration."
java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Settings -c $db_url -u $db_user -p $db_password -path `pwd`/

echo
echo "Success."
