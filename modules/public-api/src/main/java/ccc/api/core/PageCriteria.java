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
package ccc.api.core;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import ccc.api.types.Paragraph;


/**
 * Criteria class to search for pages.
 *
 * @author Civic Computing Ltd.
 */
public class PageCriteria
    extends
        ResourceCriteria {

    private Set<Paragraph> _paraMatches = new HashSet<Paragraph>();


    /**
     * Match the specified paragraph.
     *
     * @param name The paragraph name.
     * @param value The paragraph value.
     */
    public void matchParagraph(final String name, final String value) {
        _paraMatches.add(Paragraph.fromText(name, value));
    }


    /**
     * Match the specified paragraph.
     *
     * @param name The paragraph name.
     * @param value The paragraph value.
     */
    public void matchParagraph(final String name, final boolean value) {
        _paraMatches.add(Paragraph.fromBoolean(name, Boolean.valueOf(value)));
    }


    /**
     * Match the specified paragraph.
     *
     * @param name The paragraph name.
     * @param value The paragraph value.
     */
    public void matchParagraph(final String name, final long value) {
        _paraMatches.add(Paragraph.fromNumber(name, value));
    }


    /**
     * Match the specified paragraph.
     *
     * @param name The paragraph name.
     * @param value The paragraph value.
     */
    public void matchParagraph(final String name, final double value) {
        _paraMatches.add(Paragraph.fromNumber(name, value));
    }


    /**
     * Match the specified paragraph.
     *
     * @param name The paragraph name.
     * @param value The paragraph value.
     */
    public void matchParagraph(final String name, final BigDecimal value) {
        _paraMatches.add(Paragraph.fromNumber(name, value));
    }


    /**
     * Match the specified paragraph.
     *
     * @param name The paragraph name.
     * @param value The paragraph value.
     */
    public void matchParagraph(final String name, final Date value) {
        _paraMatches.add(Paragraph.fromDate(name, value));
    }


    /**
     * Accessor.
     *
     * @return The paragraphs to match.
     */
    public Set<Paragraph> getParas() {
        return new HashSet<Paragraph>(_paraMatches);
    }


    /**
     * Mutator.
     *
     * @param paragraphs The paragraphs to match.
     */
    public void setParas(final Set<Paragraph> paragraphs) {
        _paraMatches.clear();
        _paraMatches.addAll(paragraphs);
    }
}
