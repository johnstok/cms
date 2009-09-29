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
 * MVP View for creating a text file.
 *
 * @author Civic Computing Ltd.
 */
public interface CreateTextFile extends View<Editable>, Validatable {


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
     * @return The folder name.
     */
    String getName();


    /**
     * Mutator.
     *
     * @param name The new folder name to set.
     */
    void setName(String name);
}

