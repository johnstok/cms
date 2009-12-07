/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
 * Changes: See subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.migration;


/**
 * Paragraph bean for migration.
 *
 * @author Civic Computing Ltd
 */
public class ParagraphBean {
    private final String _key;
    private final String _text;

    /**
     * Constructor.
     *
     * @param key Key of the paragraph
     * @param text Text of the paragraph
     */
    public ParagraphBean(final String key, final String text) {
        _key = key;
        _text = text;
    }


    /**
     * Accessor for key.
     *
     * @return Key of the paragraph
     */
    public String key() {
        return _key;
    }


    /**
     * Accessor for text.
     *
     * @return Text of the paragraph
     */
    public String text() {
        return _text;
    }

}
