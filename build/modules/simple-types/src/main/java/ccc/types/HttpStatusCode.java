/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.types;


/**
 * A list of HTTP status codes.
 *
 * @author Civic Computing Ltd.
 */
public final class HttpStatusCode {


    /** OK : int. */
    public static final int OK = 200;


    /** IM_A_TEAPOT : int. */
    public static final int IM_A_TEAPOT = 418;


    /** NOT_FOUND : int. */
    public static final int NOT_FOUND = 404;


    /** ERROR : int. */
    public static final int ERROR = 500;


    /**
     * Constructor.
     */
    private HttpStatusCode() {
        super();
    }
}
