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

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ccc.rest.RestException;
import ccc.rest.Templates;
import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateDto;
import ccc.rest.dto.TemplateSummary;


/**
 * Implementation of the {@link Templates} API.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure")
@Consumes("application/json")
@Produces("application/json")
public class TemplatesImpl
    extends
        JaxrsCollection
    implements
        Templates {


    /** {@inheritDoc} */
    @Override
    public TemplateDelta templateDelta(final UUID templateId) {
        return getQueries().templateDelta(templateId);
    }


    /** {@inheritDoc} */
    @Override
    public boolean templateNameExists(final String templateName) {
        return getQueries().templateNameExists(templateName);
    }


    /** {@inheritDoc} */
    @Override
    public Collection<TemplateSummary> templates() {
        return getQueries().templates();
    }


    /** {@inheritDoc} */
    @Override
    public void updateTemplate(final UUID templateId, final TemplateDelta delta)
    throws RestException {
        getQueries().updateTemplate(templateId, delta);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final TemplateDto template)
    throws RestException {
        return getQueries().createTemplate(template);
    }
}
