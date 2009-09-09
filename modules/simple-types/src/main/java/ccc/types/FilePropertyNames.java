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
package ccc.types;


/**
 * A list of possible file property names.
 *
 * @author Civic Computing Ltd.
 */
public final class FilePropertyNames {

    private FilePropertyNames() {
        super();
    }

    /** CHARSET : String. */
    public static final String CHARSET = "text.charset";
    /** WIDTH : String. */
    public static final String WIDTH = "image.width";
    /** HEIGHT : String. */
    public static final String HEIGHT = "image.height";
}
