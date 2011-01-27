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
package ccc.web.scheduling;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import ccc.api.core.SearchEngine2;
import ccc.api.types.DBC;
import ccc.api.types.Permission;
import ccc.plugins.security.Sessions;


/**
 * A timer task that performs a full search re-index.
 *
 * @author Civic Computing Ltd.
 */
public class SearchTask
    extends
        TimerTask {

    private static final Logger LOG = Logger.getLogger(SearchTask.class);

    private final SearchEngine2 _search;
    private final Sessions      _sessions;


    /**
     * Constructor.
     *
     * @param search  The search service to invoke.
     * @param session The session under which this task will run.
     */
    public SearchTask(final SearchEngine2 search, final Sessions session) {
        _search = DBC.require().notNull(search);
        _sessions = DBC.require().notNull(session);
    }


    /** {@inheritDoc} */
    @Override
    public void run() {
        try {
            _sessions.pushRunAsRole(Permission.SEARCH_REINDEX);
            try {
                _search.index();
            } finally {
                _sessions.popRunAsRole();
            }
        } catch (final RuntimeException e) {
            LOG.error("Error running search re-index.", e);
        }
    }
}
