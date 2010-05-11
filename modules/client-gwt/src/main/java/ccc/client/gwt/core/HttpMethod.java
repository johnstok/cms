/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.core;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public enum HttpMethod {

    /** GET : HttpMethod. */
    GET,

    /** POST : HttpMethod. */
    POST,

    /** PUT : HttpMethod. */
    PUT,

    /** DELETE : HttpMethod. */
    DELETE;

    /** OVERRIDE_HEADER : String. */
    public static final String OVERRIDE_HEADER = "X-HTTP-Method-Override";
}
