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

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;

import ccc.search.SearchHelper;


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


    /** {@inheritDoc} */
    @Override
    public void onMessage(final Message msg) {
        try {
            new SearchHelper(
                getRepoFactory().createResourceRepository(),
                getRepoFactory().createDataRepository(),
                getRepoFactory().createSettingsRepository()
            ).index();
        } catch (final Exception e) {
            LOG.error("Error handling broadcast.", e);
        }
    }
}
