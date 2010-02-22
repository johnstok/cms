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
package ccc.rest.impl;

import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;

import ccc.rest.Pages;
import ccc.rest.RestException;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.PageDto;
import ccc.rest.dto.ResourceSummary;
import ccc.serialization.Json;


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


    /** {@inheritDoc} */
    @Override
    public PageDelta pageDelta(final UUID pageId) throws RestException {
        return getPages().pageDelta(pageId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final PageDto page)
    throws RestException {
        return getPages().createPage(page);
    }


    /** {@inheritDoc} */
    @Override
    public String validateFields(final Json json) {
        return getPages().validateFields(json);
    }


    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final UUID pageId, final PageDelta delta)
    throws RestException {
        getPages().updateWorkingCopy(pageId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void updatePage(final UUID pageId, final Json json)
    throws RestException {
        getPages().updatePage(pageId, json);
    }
}
