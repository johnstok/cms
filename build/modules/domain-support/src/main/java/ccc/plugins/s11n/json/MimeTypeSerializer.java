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

import ccc.api.types.MimeType;


/**
 * Serializer for MimeTypes.
 *
 * @author Civic Computing Ltd.
 */
class MimeTypeSerializer extends BaseSerializer<MimeType> {

    /**
     * Constructor.
     *
     * @param parser The text parser for this serializer.
     */
    MimeTypeSerializer(final TextParser parser) { super(parser); }


    /** {@inheritDoc} */
    @Override
    public MimeType read(final String data) {
        if (null==data) { return null; }
        final Json json = parse(data);

        final MimeType d = readMimeType(json);

        return d;
    }


    /** {@inheritDoc} */
    @Override
    public String write(final MimeType instance) {
        if (null==instance) { return null; }
        final Json json = newJson();

        writeMimeType(json, instance);

        return json.toString();
    }


    static MimeType readMimeType(final Json json) {
        if (null==json) { return null; }

        final MimeType d =
            new MimeType(
                json.getString(JsonKeys.PRIMARY_TYPE),
                json.getString(JsonKeys.SUB_TYPE));
        return d;
    }


    static void writeMimeType(final Json json, final MimeType instance) {
        json.set(JsonKeys.PRIMARY_TYPE, instance.getPrimaryType());
        json.set(JsonKeys.SUB_TYPE, instance.getSubType());
    }
}
