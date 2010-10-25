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
# Revision      $Rev: 3186 $
# Modified by   $Author: keith $
# Modified on   $Date: 2010-10-06 17:33:56 +0100 (Wed, 06 Oct 2010) $
#
# Changes: see subversion log.
#-----------------------------------------------------------------------------

echo
echo "Creating Apache configuration file."

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

function replace_name {
#    echo
    echo " Fixing $3:"
    $SED -r -i 's/([^a-zA-Z]+)'"$1"'/\1'"$2"'/g' $3
}

replace_name cc7server "$1" apache_template
replace_name cc7admin "$2" apache_template
replace_name cc7 "$3" apache_template
