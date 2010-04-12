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

import ccc.api.Pages;
import ccc.api.dto.PageDelta;
import ccc.api.dto.PageDto;
import ccc.api.dto.ResourceSummary;
import ccc.api.types.DBC;
import ccc.plugins.s11n.Json;


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
    public PageDelta pageDelta(final UUID pageId) {
        return _pages.pageDelta(pageId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final PageDto page) {
        return _pages.createPage(page);
    }


    /** {@inheritDoc} */
    @Override
    public String validateFields(final Json json) {
        return _pages.validateFields(json);
    }


    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final UUID pageId, final PageDelta delta) {
        _pages.updateWorkingCopy(pageId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void updatePage(final UUID pageId, final Json json) {
        _pages.updatePage(pageId, json);
    }
}
