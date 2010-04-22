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
package ccc.api;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import ccc.api.dto.ResourceSummary;
import ccc.api.dto.TemplateDto;


/**
 * API for manipulating templates.
 *
 * @author Civic Computing Ltd.
 */
@Consumes("application/json")
@Produces("application/json")
public interface Templates {

    /** NAME : String. */
    String NAME = "Templates";


    /**
     * List all the templates currently available in CCC.
     *
     * @return A list of templates.
     */
    @GET
    Collection<TemplateDto> templates();


    /**
     * Returns true if template name exists in the template folder.
     *
     * @param templateName The name to look up.
     *
     * @return True if name exists.
     */
    @GET @Path("/{name}/exists")
    Boolean templateNameExists(@PathParam("name") final String templateName);


    /**
     * Retrieve the delta for a template.
     *
     * @param templateId The template's id.
     *
     * @return The corresponding delta.
     */
    @GET @Path("/{id}/delta")
    TemplateDto templateDelta(@PathParam("id") UUID templateId);

    /**
     * Update the specified template on the server.
     *
     * @param templateId The id of the template to update.
     * @param delta The changes to apply.
     */
    @POST @Path("/{id}")
    void updateTemplate(
        @PathParam("id") UUID templateId, TemplateDto delta);

    /**
     * Create a new template in CCC.
     *
     * @param template The template's details.
     *
     * @return A resource summary describing the new template.
     */
    @POST
    ResourceSummary createTemplate(TemplateDto template);
}
