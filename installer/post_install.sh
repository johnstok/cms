#!/bin/bash

script_dir=`dirname $0`
cc_version="${project.version}"

echo
echo "CC7 post install tool"

app_path=$3

java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Create     -o $app_path -p $2 -u $1
java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Search     -b $app_path -p $2 -u $1 -c start
java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Scheduling -b $app_path -p $2 -u $1 -c start