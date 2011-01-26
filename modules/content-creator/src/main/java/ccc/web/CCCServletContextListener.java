/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
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
package ccc.web;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;

import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;

import ccc.api.core.Actions;
import ccc.api.core.Actions2;
import ccc.plugins.PluginFactory;
import ccc.web.scheduling.ActionScheduler;
import ccc.web.scheduling.Schedulers;


/**
 * Configure the servlet context for CC.
 *
 * @author Civic Computing Ltd.
 */
public class CCCServletContextListener
    extends
        ResteasyBootstrap {

    @EJB(name = Actions.NAME) private Actions2 _actions;

    /** {@inheritDoc} */
    @Override
    public void contextInitialized(final ServletContextEvent event) {
        Schedulers.setInstance(
            new ActionScheduler(
                _actions, new PluginFactory().createSessions()));
        super.contextInitialized(event);
    }

    /** {@inheritDoc} */
    @Override
    public void contextDestroyed(final ServletContextEvent event) {
        super.contextDestroyed(event);
        Schedulers.clearInstance();
    }
}
