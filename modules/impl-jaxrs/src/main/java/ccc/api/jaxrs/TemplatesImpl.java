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

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.client.ClientResponseFailure;

import ccc.api.Templates;
import ccc.api.dto.ResourceSummary;
import ccc.api.dto.TemplateDelta;
import ccc.api.dto.TemplateDto;
import ccc.api.dto.TemplateSummary;
import ccc.api.types.DBC;


/**
 * Implementation of the {@link Templates} API.
 *
 * @author Civic Computing Ltd.
 */
@Path("/secure/templates")
@Consumes("application/json")
@Produces("application/json")
@NoCache
public class TemplatesImpl
    extends
        JaxrsCollection
    implements
        Templates {

    private final Templates _templates;


    /**
     * Constructor.
     *
     * @param templates The templates implementation delegated to.
     */
    public TemplatesImpl(final Templates templates) {
        _templates = DBC.require().notNull(templates);
    }


    /** {@inheritDoc} */
    @Override
    public TemplateDelta templateDelta(final UUID templateId) {
        try {
            return _templates.templateDelta(templateId);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Boolean templateNameExists(final String templateName) {
        try {
            return _templates.templateNameExists(templateName);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public Collection<TemplateSummary> templates() {
        try {
            return _templates.templates();
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void updateTemplate(final UUID templateId,
                               final TemplateDelta delta) {
        try {
            _templates.updateTemplate(templateId, delta);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }


    /** {@inheritDoc} */
    @Override
    public ResourceSummary createTemplate(final TemplateDto template) {
        try {
            return _templates.createTemplate(template);
        } catch (final ClientResponseFailure cfe) {
            throw convertException(cfe);
        }
    }
}
