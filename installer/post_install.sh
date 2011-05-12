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
echo "CC7 post install tool"

app_path=$3

java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Create     -o $app_path -p $2 -u $1
java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Search     -b $app_path -p $2 -u $1 -c start
java -cp $script_dir/../client-shell-$cc_version.jar ccc.cli.Scheduling -b $app_path -p $2 -u $1 -c start