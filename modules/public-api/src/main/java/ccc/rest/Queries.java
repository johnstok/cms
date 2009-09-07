/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest;

import java.util.Collection;
import java.util.UUID;

import javax.ws.rs.PathParam;

import ccc.rest.dto.FileDelta;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TemplateSummary;


/**
 * Query methods available for CCC.
 *
 * @author Civic Computing Ltd.
 */
public interface Queries extends QueriesBasic {
    Collection<TemplateSummary> templates();
    boolean templateNameExists(@PathParam("name") final String templateName);
    TemplateDelta templateDelta(@PathParam("id") UUID templateId);
    Collection<FileDto> getAllContentImages();
    String aliasTargetName(@PathParam("id") UUID aliasId);
    FileDelta fileDelta(@PathParam("id") UUID fileId);
}
