/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
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
@Path("/secure")
@Consumes("application/json")
@Produces("application/json")
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