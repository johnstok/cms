/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.remoting.actions;


/**
 * Constants for use with CCC Servlets.
 * We don't use an enum because the Servlet API requires string-based keys.
 *
 * @author Civic Computing Ltd.
 */
public final class SessionKeys {

    private SessionKeys() { super(); }

    /** EXCEPTION_KEY : String. */
    public static final String EXCEPTION_KEY = "ccc.exception";

    /** DAO_KEY : String. */
    public static final String DAO_KEY = "ccc.dao";

    /** CURRENT_USER : String. */
    public static final String CURRENT_USER = "ccc.current_user";

    /** AUDIT_KEY : String. */
    public static final String AUDIT_KEY = "ccc.audit";

    /** DATA_KEY : String. */
    public static final String DATA_KEY = "ccc.data";

    /** EM_KEY : String. */
    public static final String EM_KEY = "ccc.em";

}
