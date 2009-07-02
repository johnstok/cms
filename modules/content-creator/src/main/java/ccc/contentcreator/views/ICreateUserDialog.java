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

import ccc.contentcreator.dialogs.Closeable;

import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * API for create user dialogs.
 *
 * @author Civic Computing Ltd.
 */
public interface ICreateUserDialog extends Closeable {

    /**
     * Accessor.
     *
     * @return Returns the username.
     */
    TextField<String> getUsername();

    /**
     * Accessor.
     *
     * @return Returns the password1.
     */
    TextField<String> getPassword1();

    /**
     * Accessor.
     *
     * @return Returns the password2.
     */
    TextField<String> getPassword2();

    /**
     * Accessor.
     *
     * @return Returns the email.
     */
    TextField<String> getEmail();

}
