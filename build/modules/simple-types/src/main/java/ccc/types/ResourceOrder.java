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
 * Sort orders for resource lists.
 *
 * @author Civic Computing Ltd.
 */
public enum ResourceOrder {

    /** MANUAL : ResourceOrder. */
    MANUAL,
    /** NAME_ALPHANUM_ASC : ResourceOrder. */
    NAME_ALPHANUM_ASC,
    /** NAME_ALPHANUM_ASC : ResourceOrder. */
    DATE_CREATED_ASC,
    /** NAME_ALPHANUM_ASC : ResourceOrder. */
    DATE_CREATED_DESC,
    /** NAME_ALPHANUM_ASC : ResourceOrder. */
    DATE_CHANGED_ASC,
    /** NAME_ALPHANUM_ASC : ResourceOrder. */
    DATE_CHANGED_DESC;
}
