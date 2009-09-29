/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.views;

import ccc.contentcreator.client.Editable;
import ccc.contentcreator.client.Validatable;
import ccc.contentcreator.client.View;


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
}

