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

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.CommandFailedException;
import ccc.rest.PagesBasic;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.PageNew;
import ccc.rest.dto.ResourceSummary;
import ccc.serialization.Json;


/**
 * Implementation of the {@link PagesBasic} interface.
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
        PagesBasic {


    /** {@inheritDoc} */
    @Override
    public PageDelta pageDelta(final UUID pageId) {
        return getPageCommands().pageDelta(pageId);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createPage(final PageNew page)
    throws CommandFailedException {
        return getPageCommands().createPage(page);
    }


    /** {@inheritDoc} */
    @Override
    public List<String> validateFields(final Json json) {
        return getPageCommands().validateFields(json);
    }


    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final UUID pageId, final PageDelta delta)
    throws CommandFailedException {
        getPageCommands().updateWorkingCopy(pageId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public void updatePage(final UUID pageId, final Json json)
    throws CommandFailedException {
        getPageCommands().updatePage(pageId, json);
    }
}
