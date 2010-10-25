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
echo "Configuring messaging settings."

SED=sed
[ "`uname`" != Linux ] && type gsed >&/dev/null && SED=gsed

APP_NAME=$1

$SED -i 's|<message-destination-link>topic_broadcast_[^<]*</message-destination-link>|<message-destination-link>topic_broadcast_'"$APP_NAME"'</message-destination-link>"|' services-ejb3.jar/META-INF/jboss.xml
$SED -i 's|<message-destination-name>topic_broadcast_[^<]*</message-destination-name>|<message-destination-name>topic_broadcast_'"$APP_NAME"'</message-destination-name>"|' services-ejb3.jar/META-INF/jboss.xml
$SED -i 's|<destination-jndi-name>topic/broadcast_[^<]*</destination-jndi-name>|<destination-jndi-name>topic/broadcast_'"$APP_NAME"'</destination-jndi-name>"|' services-ejb3.jar/META-INF/jboss.xml
$SED -i 's|<jndi-name>topic/broadcast_[^<]*</jndi-name>|<jndi-name>topic/broadcast_'"$APP_NAME"'</jndi-name>"|' services-ejb3.jar/META-INF/jboss.xml

$SED -i 's|\"jboss.messaging.destination:service=Topic,name=broadcast_[^\"]*\"|\"jboss.messaging.destination:service=Topic,name=broadcast_'"$APP_NAME"'\"|' topics-service.xml
