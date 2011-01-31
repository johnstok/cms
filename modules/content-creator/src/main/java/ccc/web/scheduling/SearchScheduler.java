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

import ccc.api.core.SearchEngine2;
import ccc.api.types.DBC;
import ccc.api.types.Permission;
import ccc.plugins.security.Sessions;
import ccc.web.jaxrs.CCCProperties;


/**
 * Scheduler for search re-indexing.
 *
 * @author Civic Computing Ltd.
 */
public class SearchScheduler
    extends
        AbstractScheduler {

    private final SearchEngine2 _search;
    private final Sessions      _sessions;


    /**
     * Constructor.
     *
     * @param search  The search implementation to call.
     * @param session The session under which tasks will run.
     *
     */
    public SearchScheduler(final SearchEngine2 search, final Sessions session) {
        super("CC-search-"+CCCProperties.getAppName());
        _search = DBC.require().notNull(search);
        _sessions = DBC.require().notNull(session);
    }


    /** {@inheritDoc} */
    @Override
    void schedule() {
        final int delay = 30*1000;
        final int period = 60*60*1000;

        final Runnable r =
            new RunAsRunnable(
                _sessions,
                Permission.SEARCH_REINDEX,
                new SearchTask(_search));

        schedule(r, period, delay);
    }
}
