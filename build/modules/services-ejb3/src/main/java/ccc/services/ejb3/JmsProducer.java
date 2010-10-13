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

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

import ccc.api.types.CommandType;
import ccc.api.types.DBC;
import ccc.messaging.Producer;


/**
 * A JMS implementation of the {@link Producer} interface.
 *
 * @author Civic Computing Ltd.
 */
class JmsProducer
    implements
        Producer {

    private final TopicConnectionFactory _connectionFactory;
    private final Topic                  _topic;


    /**
     * Constructor.
     *
     * @param connectionFactory The JMS connection factory.
     * @param topic             The JMS topic accepting broadcasts.
     */
    JmsProducer(final TopicConnectionFactory connectionFactory,
                final Topic topic) {
        _connectionFactory = DBC.require().notNull(connectionFactory);
        _topic             = DBC.require().notNull(topic);
    }


    /** {@inheritDoc} */
    @Override
    public void broadcastMessage(final CommandType command) {
        try {
            final Connection connection =
                _connectionFactory.createConnection();
              final Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
              final MessageProducer producer =
                  session.createProducer(_topic);

              final TextMessage message = session.createTextMessage();
              message.setStringProperty(
                  "command", command.name());

              producer.send(message);

              session.close();
              connection.close();

        } catch (final JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
