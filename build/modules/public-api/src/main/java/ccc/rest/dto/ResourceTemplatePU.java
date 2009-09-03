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
package ccc.rest.dto;

import java.util.UUID;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;



/**
 * A partial update, changing a resource's template.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTemplatePU implements Jsonable {

    private final UUID _templateId;


    /**
     * Constructor.
     *
     * @param templateId The template to set (may be NULL).
     */
    public ResourceTemplatePU(final UUID templateId) {
        _templateId = templateId;
    }


    /**
     * Accessor.
     *
     * @return Returns the templateId.
     */
    public final UUID getTemplateId() {
        return _templateId;
    }


    /** {@inheritDoc} */
    @Override public void toJson(final Json json) {
        json.set(JsonKeys.TEMPLATE_ID, _templateId);
    }
}
