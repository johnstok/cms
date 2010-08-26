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

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import ccc.api.types.Paragraph;
import ccc.api.types.ParagraphType;


/**
 * Serializer for Paragraphs.
 *
 * @author Civic Computing Ltd.
 */
class ParagraphSerializer extends BaseSerializer<Paragraph> {

    /**
     * Constructor.
     *
     * @param parser The text parser for this serializer.
     */
    ParagraphSerializer(final TextParser parser) { super(parser); }


    /** {@inheritDoc} */
    @Override
    public Paragraph read(final String data) {
        if (null==data) { return null; }
        final Json json = parse(data);

        return read(json);
    }


    /** {@inheritDoc} */
    @Override
    public String write(final Paragraph instance) {
        if (null==instance) { return null; }
        final Json json = newJson();

        writeParagraph(instance, json);

        return json.toString();
    }


    static Paragraph read(final Json json) {
        if (null==json) { return null; }

        final String name = json.getString(JsonKeys.NAME);
        final ParagraphType type =
            ParagraphType.valueOf(json.getString(JsonKeys.TYPE));
        switch (type) {
            case BOOLEAN:
                return Paragraph.fromBoolean(
                    name, json.getBool(JsonKeys.BOOLEAN));

            case DATE:
                return Paragraph.fromDate(
                    name, json.getDate(JsonKeys.DATE));

            case LIST:
            case TEXT:
                return Paragraph.fromText(
                    name, json.getString(JsonKeys.TEXT));

            case NUMBER:
                return Paragraph.fromNumber(
                    name, new BigDecimal(json.getString(JsonKeys.TEXT)));

            default:
                throw new IllegalArgumentException(
                    "Paragraph type unsupported: "+type);
        }
    }


    static void writeParagraph(final Paragraph instance, final Json json) {
        json.set(JsonKeys.NAME, instance.getName());
        json.set(JsonKeys.TYPE, instance.getType().name());
        json.set(JsonKeys.TEXT, instance.getText());
        json.set(JsonKeys.BOOLEAN, instance.getBoolean());
        json.set(JsonKeys.DATE, instance.getDate());
    }


    static Set<Json> writeParagraphs(final Json json,
                                     final Set<Paragraph> instance) {
        if (null==instance) { return null; }

        final Set<Json> jsons = new HashSet<Json>();
        for (final Paragraph p : instance) {
            final Json pJson = json.create();
            writeParagraph(p, pJson);
            jsons.add(pJson);
        }

        return jsons;
    }
}
