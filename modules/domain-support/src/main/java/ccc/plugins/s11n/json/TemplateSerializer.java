/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.plugins.s11n.json;

import static ccc.plugins.s11n.JsonKeys.*;
import ccc.api.core.Template;
import ccc.plugins.s11n.Json;


/**
 * Serializer for {@link Template}s.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateSerializer
    extends
        ResourceSerializer<Template> {


    /** {@inheritDoc} */
    @Override
    public Template read(final Json json) {
        if (null==json) { return null; }

        final Template t = super.read(json);

        t.setDefinition(json.getString(DEFINITION));
        t.setBody(json.getString(BODY));
        t.setMimeType(new MimeTypeSerializer().read(json.getJson(MIME_TYPE)));

        return t;
    }


    /** {@inheritDoc} */
    @Override protected Template createObject() { return new Template(); }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final Template instance) {
        if (null==instance) { return null; }

        super.write(json, instance);

        json.set(DEFINITION, instance.getDefinition());
        json.set(BODY,       instance.getBody());
        json.set(
            MIME_TYPE,
            new MimeTypeSerializer().write(
                json.create(), instance.getMimeType()));

        return json;
    }
}
