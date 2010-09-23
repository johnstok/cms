/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
package ccc.client.gwt.widgets;

import ccc.api.types.Paragraph;
import ccc.client.core.ValidationResult;


/**
 * A field on a page.
 *
 * @param <T> The type of UI control this field uses.
 *
 * @author Civic Computing Ltd.
 */
public abstract class PageElement<T> {

    private String _id;


    /**
     * Constructor.
     *
     * @param id Id of the page element.
     */
    public PageElement(final String id) {
        super();
        _id = id;
    }


    /**
     * Get the field's name.
     *
     * @return The field's name, as a string.
     */
    public String getName() { return _id; }


    /**
     * Get the field's value.
     *
     * @return The current value of the field.
     */
    public abstract Paragraph getValue();


    /**
     * Set the field's value.
     *
     * @param para The new value to set.
     */
    public abstract void setValue(Paragraph para);


    /**
     * Return the underlying UI control for this field.
     *
     * @return The UI control
     */
    public abstract T getUI();


    /**
     * Shortens label string.
     *
     * @param name  The original string of the label.
     * @param title The field's title.
     *
     * @return The label, as a string, shortened if necessary.
     */
    protected final String createLabel(final String name, final String title) {
        final int labelLength = 13;
        final String label =
            (null!=title && title.trim().length()>0) ? title : name;
        if (label.length() > labelLength) {
            return label.substring(0, labelLength) + "...";
        }
        return label;
    }


    /**
     * Create a tool-tip for a field.
     *
     * @param name        The name of the field.
     * @param title       The title of the field.
     * @param description The description of the field.
     *
     * @return The tool-tip, as a string.
     */
    protected final String createTooltip(final String name,
                                         final String title,
                                         final String description) {
        final String label =
            (null!=title && title.trim().length()>0) ? title : name;
        return
            "<b>"+label+"</b><br>"
            + ((null==description) ? "" : description);
    }


    /**
     * Validate this field.
     *
     * @param vResult The validation result for collecting errors.
     */
    public void validate(final ValidationResult vResult) { return; }
}
