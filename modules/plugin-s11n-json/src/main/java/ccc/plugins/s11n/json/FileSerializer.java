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

import ccc.api.core.File;


/**
 * Serializer for {@link Folder}s.
 *
 * @author Civic Computing Ltd.
 */
class FileSerializer
    extends
        BaseSerializer<File> {

    /**
     * Constructor.
     *
     * @param parser The text parser for this serializer.
     */
    FileSerializer(final TextParser parser) { super(parser); }


    /** {@inheritDoc} */
    @Override
    public File read(final String data) {
        if (null==data) { return null; }
        final Json json = parse(data);

        final File f = new File();

        ResourceMappings.readRes(json, f);
        ResourceSummarySerializer.readResSummary(json, f);
        ResourceMappings.readResource(json, f);
        ResourceMappings.readFile(json, f);

        return f;
    }


    /** {@inheritDoc} */
    @Override
    public String write(final File instance) {
        if (null==instance) { return null; }

        final Json json = newJson();

        ResourceMappings.writeRes(json, instance);
        ResourceSummarySerializer.writeResSummary(instance, json);
        ResourceMappings.writeResource(json, instance);
        ResourceMappings.writeFile(json, instance);

        return json.toString();
    }
}
