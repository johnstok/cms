/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.api.jaxrs;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.core.SearchEngine;
import ccc.api.types.DBC;
import ccc.api.types.SearchResult;


/**
 * JAX-RS implementation of the {@link SearchEngine} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path(ccc.api.core.ResourceIdentifiers.SearchEngine.COLLECTION)
@Produces({"text/html", "application/json"})
@NoCache
public class SearchImpl
    extends
        JaxrsCollection
    implements
        SearchEngine {

    private final SearchEngine _delegate;


    /**
     * Constructor.
     *
     * @param search The search implementation delegated too.
     */
    public SearchImpl(final SearchEngine search) {
        _delegate = DBC.require().notNull(search);
    }


    /** {@inheritDoc} */
    @Override
    public SearchResult find(final String searchTerms,
                             final int noOfResultsPerPage,
                             final int page) {
        try {
            return _delegate.find(searchTerms, noOfResultsPerPage, page);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void index() {
        try {
            _delegate.index();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isRunning() {
        try {
            return _delegate.isRunning();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void start() {
        try {
            _delegate.start();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void stop() {
        try {
            _delegate.stop();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

    /** {@inheritDoc} */
    @Override
    public SearchResult similar(final String uuid,
                                final int noOfResultsPerPage,
                                final int page) {
        try {
            return _delegate.similar(uuid, noOfResultsPerPage, page);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }

}
