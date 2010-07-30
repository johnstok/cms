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

import java.util.HashSet;
import java.util.Set;

import ccc.api.core.PageCriteria;
import ccc.api.types.Paragraph;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;


/**
 * Serializer for {@link Resource}s.
 *
 * @author Civic Computing Ltd.
 */
public class PageCriteriaSerializer
    extends
        ResourceCriteriaSerializer<PageCriteria> {


    /** {@inheritDoc} */
    @Override
    public PageCriteria read(final Json json) {
        if (null==json) { return null; }

        final PageCriteria p = super.read(json);

        final Set<Paragraph> paragraphs = new HashSet<Paragraph>();
        for (final Json jsonPara : json.getCollection(JsonKeys.PARAGRAPHS)) {
            paragraphs.add(new ParagraphSerializer().read(jsonPara));
        }
        p.setParas(paragraphs);

        return p;
    }


    /** {@inheritDoc} */
    @Override protected PageCriteria createObject() {
        return new PageCriteria();
    }


    /** {@inheritDoc} */
    @Override
    public Json write(final Json json, final PageCriteria instance) {
        if (null==instance) { return null; }

        super.write(json, instance);

        json.setJsons(
            JsonKeys.PARAGRAPHS,
            new ParagraphSerializer().write(json, instance.getParas()));

        return json;
    }
}
