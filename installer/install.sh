#!/bin/bash

#-----------------------------------------------------------------------------
# Copyright (c) 2009 Civic Computing Ltd.
# All rights reserved.
#
# This file is part of Content Control.
#
# Content Control is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Content Control is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with Content Control.  If not, see http://www.gnu.org/licenses/.
#
# Revision      $Rev$
# Modified by   $Author$
# Modified on   $Date$
#
# Changes: see subversion log.
#-----------------------------------------------------------------------------

script_dir=`dirname $0`
cc_version="${project.version}"

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

echo
echo "Logging config"
echo -n " Full path to log file (eg /var/log/cc/"$app_name".log): "
read log_path

echo
echo "Apache Config"
echo -n " CC URL: "
read cc_url
echo -n " CC admin contact email: "
read cc_admin_email

mkdir filestore
chmod a+w filestore

mkdir lucene
chmod a+w lucene

mkdir cc-server-$cc_version
unzip cc-server-$cc_version.ear -d cc-server-$cc_version > /dev/null

cd cc-server-$cc_version
../$script_dir/rename.sh cc7 $app_name
../$script_dir/dbconfig.sh $db_type $db_url $db_user $db_password
../$script_dir/mailconfig.sh $mail_host $mail_username $mail_password
../$script_dir/apacheconfig.sh $cc_url $cc_admin_email  $app_name
../$script_dir/logconfig.sh $log_path
../$script_dir/messagingconfig.sh cc7 $app_name
cd ..

echo
echo "Creating database schema."
java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Schema   -c $db_url -u $db_user -p $db_password

echo
echo "Creating initial user."
java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Users    -c $db_url -u $db_user -p $db_password -ne $cc_email -np $cc_password -nu $cc_username

echo
echo "Saving further configuration."
java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Settings -c $db_url -u $db_user -p $db_password -path `pwd`/

echo
echo "Success."
