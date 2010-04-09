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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views;

import ccc.client.gwt.core.Editable;
import ccc.client.gwt.core.Validatable;
import ccc.client.gwt.core.View;


/**
 * MVP View for updating a text file content.
 *
 * @author Civic Computing Ltd.
 */
public interface EditTextFile extends View<Editable>, Validatable {


    /**
     * Mutator.
     *
     * @param text The new text to set.
     */
    void setText(final String text);


    /**
     * Accessor.
     *
     * @return The current text of the file.
     */
    String getText();

    /**
     * Accessor.
     *
     * @return The comment.
     */
    String getComment();

    /**
     * Accessor.
     *
     * @return The boolean value for major edit.
     */
    boolean isMajorEdit();

    /**
     * Accessor.
     *
     * @return The mime primary type of the text file.
     */
    String getPrimaryMime();

    /**
     * Accessor.
     *
     * @return The mime sub type of the text file.
     */
    String getSubMime();

    /**
     * Mutator.
     *
     * @param sub The new sub mime type to set.
     */
    void setSubMime(String sub);

    /**
     * Mutator.
     *
     * @param primary The new primary mime type to set.
     */
    void setPrimaryMime(String primary);
}

