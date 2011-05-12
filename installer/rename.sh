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

# TODO check arguments
# Limit changes to old values prefixed by the following chars >/=":

echo
echo "Renaming CCC configuration files (replacing $1 with $2)."

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

function replace_name {
#    echo
    echo " Fixing $3:"
#    $SED -r -n 's/([^a-zA-Z]+)'"$1"'/\1'"$2"'/gp' $3
    $SED -r -i 's/([^a-zA-Z]+)'"$1"'/\1'"$2"'/g' $3
}

replace_name "$1" "$2" META-INF/application.xml
replace_name "$1" "$2" META-INF/jboss-app.xml
replace_name "$1" "$2" services-ejb3.jar/META-INF/persistence.xml
replace_name "$1" "$2" auth-config.xml
replace_name "$1" "$2" auth-service.xml
replace_name "$1" "$2" database-ds.xml
replace_name "$1" "$2" content-creator.war/WEB-INF/web.xml
replace_name "$1" "$2" content-creator.war/WEB-INF/classes/build.properties
