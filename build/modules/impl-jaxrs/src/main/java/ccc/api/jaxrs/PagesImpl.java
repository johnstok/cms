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

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.api.core.Page;
import ccc.api.core.PageCriteria;
import ccc.api.core.PagedCollection;
import ccc.api.core.Pages;
import ccc.api.core.ResourceSummary;
import ccc.api.types.DBC;


/**
 * Implementation of the {@link Pages} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class PagesImpl
    extends
        JaxrsCollection
    implements
        Pages {

    private final Pages _pages;


    /**
     * Constructor.
     *
     * @param pages The pages implementation delegated to.
     */
    public PagesImpl(final Pages pages) {
        _pages = DBC.require().notNull(pages);
    }


    /** {@inheritDoc} */
    @Override
    public Page retrieve(final UUID pageId) {
        try {
            return _pages.retrieve(pageId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Page create(final Page page) {
        try {
            return _pages.create(page);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String validate(final Page page) {
        try {
            return _pages.validate(page);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Page updateWorkingCopy(final UUID pageId, final Page delta) {
        try {
            return _pages.updateWorkingCopy(pageId, delta);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Page update(final UUID pageId, final Page json) {
        try {
            return _pages.update(pageId, json);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Page retrieveWorkingCopy(final UUID pageId) {
        try {
            return _pages.retrieveWorkingCopy(pageId);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public PagedCollection<ResourceSummary> list(final PageCriteria criteria,
                                                 final int pageNo,
                                                 final int pageSize) {
        try {
            return _pages.list(criteria, pageNo, pageSize);
        } catch (final RuntimeException cfe) {
            throw convertException(cfe);
        }
    }
}
