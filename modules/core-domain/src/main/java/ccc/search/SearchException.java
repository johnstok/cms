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
package ccc.search;


/**
 * Exceptional condition for the search engine.
 *
 * @author Civic Computing Ltd.
 */
public class SearchException
    extends
        Exception {


    /**
     * Constructor.
     */
    public SearchException() {
        super();
    }


    /**
     * Constructor.
     *
     * @param message The message describing this exception.
     * @param cause The root cause of the exception.
     */
    public SearchException(final String message, final Throwable cause) {
        super(message, cause);
    }


    /**
     * Constructor.
     *
     * @param message The message describing this exception.
     */
    public SearchException(final String message) {
        super(message);
    }


    /**
     * Constructor.
     *
     * @param cause The root cause of the exception.
     */
    public SearchException(final Throwable cause) {
        super(cause);
    }
}
