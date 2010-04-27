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
import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.Pages;
import ccc.api.dto.PageDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.types.DBC;


/**
 * Implementation of the {@link Pages} interface.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/pages")
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
    public PageDto pageDelta(final UUID pageId) {
        try {
            return _pages.pageDelta(pageId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final PageDto page) {
        try {
            return _pages.createPage(page);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String validateFields(final PageDto page) {
        try {
            return _pages.validateFields(page);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final UUID pageId, final PageDto delta) {
        try {
            _pages.updateWorkingCopy(pageId, delta);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updatePage(final UUID pageId, final PageDto json) {
        try {
            _pages.updatePage(pageId, json);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }
}
