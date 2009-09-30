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
     * Accessor.
     *
     * @return The text of the file.
     */
    String getText();

    /**
     * Accessor.
     *
     * @return The text file name.
     */
    String getName();

    /**
     * Accessor.
     *
     * @return The mime sub type of the text file.
     */
    String getSubMime();

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
}

