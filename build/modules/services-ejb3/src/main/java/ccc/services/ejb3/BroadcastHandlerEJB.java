/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import ccc.api.types.CommandType;
import ccc.commands.anonymous.AnonymousCommand;
import ccc.commands.anonymous.CommandFactory;


/**
 * Message driven bean that listens for CC broadcast messages.
 *
 * @author Civic Computing Ltd.
 */
@MessageDriven(
    activationConfig = {
       @ActivationConfigProperty(
           propertyName="destinationType", propertyValue="javax.jms.Topic"),
        // Must be single threaded for Lucene writes.
       @ActivationConfigProperty(
           propertyName = "maxSession", propertyValue = "1")
    })
public class BroadcastHandlerEJB
    extends
        AbstractEJB
    implements
        MessageListener {

    private static final Logger LOG =
        Logger.getLogger(BroadcastHandlerEJB.class);

    private CommandFactory _commandFactory;


    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked") // JEE API isn't aware of generics.
    public void onMessage(final Message msg) {
        try {
            final Map<String, String> props = new HashMap<String, String>();

            final Enumeration<String> pNames = msg.getPropertyNames();
            while (pNames.hasMoreElements()) {
                final String pName = pNames.nextElement();
                props.put(pName, msg.getStringProperty(pName));
            }

            final String cTypeString = msg.getStringProperty("command");
            final CommandType cType  = CommandType.valueOf(cTypeString);
            final AnonymousCommand c =
                _commandFactory.createCommand(cType, props);

            c.execute();

        } catch (final Exception e) {
            LOG.error("Error handling broadcast.", e);
        }
    }


    @PostConstruct
    @SuppressWarnings("unused")
    private void postConstruct() {
        _commandFactory =
            new CommandFactory(
                getRepoFactory(),
                getProducer());
    }
}
