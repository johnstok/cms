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

echo
echo "Configuring mail settings."

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

mail_host=$1
mail_username=$2
mail_password=$3

$SED -i 's/auth\.username=.*/auth.username='"$mail_username"'/' content-creator.war/WEB-INF/classes/mail.properties
$SED -i 's/auth\.password=.*/auth.password='"$mail_password"'/' content-creator.war/WEB-INF/classes/mail.properties
$SED -i 's/mail\.user=.*/mail.user='"$mail_username"'/' content-creator.war/WEB-INF/classes/mail.properties
$SED -i 's/mail\.host=.*/mail.host='"$mail_host"'/' content-creator.war/WEB-INF/classes/mail.properties
$SED -i 's/mail\.pop3\.host=mail\..*/mail.pop3.host=mail.'"$mail_host"'/' content-creator.war/WEB-INF/classes/mail.properties
$SED -i 's/mail\.smtp\.host=mail\..*/mail.smtp.host=mail.'"$mail_host"'/' content-creator.war/WEB-INF/classes/mail.properties
