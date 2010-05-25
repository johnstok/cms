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
import ccc.api.core.File;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * Serializer for {@link Folder}s.
 *
 * @author Civic Computing Ltd.
 */
public class FileSerializer
    extends
        ResourceSerializer<File> {


    /** {@inheritDoc} */
    @Override
    public File read(final Json json) {
        if (null==json) { return null; }

        final File f = super.read(json);

        f.setMimeType(new MimeTypeSerializer().read(json.getJson(MIME_TYPE)));
        f.setPath(json.getString(PATH));
        f.setProperties(json.getStringMap(PROPERTIES));
        f.setSize(json.getLong(JsonKeys.SIZE).longValue());
        f.setData(json.getId(JsonKeys.DATA));
        f.setMajorEdit(json.getBool(MAJOR_CHANGE).booleanValue());
        f.setComment(json.getString(COMMENT));
        f.setContent(json.getString(TEXT));

        return f;
    }


    /** {@inheritDoc} */
    @Override protected File createObject() { return new File(); }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final File instance) {
        if (null==instance) { return null; }

        super.write(json, instance);

        json.set(
            MIME_TYPE,
            new MimeTypeSerializer().write(
                json.create(), instance.getMimeType()));
        json.set(PATH, instance.getPath());
        json.set(PROPERTIES, instance.getProperties());
        json.set(SIZE, Long.valueOf(instance.getSize()));
        json.set(DATA, instance.getData());
        json.set(MAJOR_CHANGE, Boolean.valueOf(instance.isMajorEdit()));
        json.set(COMMENT, instance.getComment());
        json.set(TEXT, instance.getContent());

        return json;
    }
}
