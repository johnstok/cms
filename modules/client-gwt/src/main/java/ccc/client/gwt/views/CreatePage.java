/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

import java.util.Set;

import ccc.api.types.Paragraph;
import ccc.client.gwt.binding.TemplateSummaryModelData;
import ccc.client.gwt.core.Editable;
import ccc.client.gwt.core.Validatable;
import ccc.client.gwt.core.View;

import com.extjs.gxt.ui.client.widget.form.TextField;

/**
 * MVP view for creating a page.
 *
 * @author Civic Computing Ltd.
 */
public interface CreatePage extends View<Editable>, Validatable {

    /**
     * Display an alert with the specified message.
     *
     * @param message The message to display.
     */
    void alert(String message);

    /**
     * Accessor.
     *
     * @return The selected template.
     */
    TemplateSummaryModelData getSelectedTemplate();

    /**
     * Accessor.
     *
     * @return The name.
     */
    TextField<String> getName();

    /**
     * Accessor.
     *
     * @return The comment.
     */
    String getComment();

    /**
     * Accessor.
     *
     * @return The major edit.
     */
    boolean getMajorEdit();

    /**
     * Accessor.
     *
     * @return The paragraphs.
     */
    Set<Paragraph> getParagraphs();

}
