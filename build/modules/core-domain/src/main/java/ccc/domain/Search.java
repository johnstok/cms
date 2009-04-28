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
package ccc.domain;


/**
 * A search that can return other results.
 *
 * @author Civic Computing Ltd.
 */
public class Search
    extends
        Resource {

    /** Constructor: for persistence only. */
    protected Search() { super(); }

    /**
     * Constructor.
     *
     * @param title The title for this resource.
     */
    public Search(final String title) {
        super(title);
    }

    /** {@inheritDoc} */
    @Override
    public ResourceType type() {
        return ResourceType.SEARCH;
    }

}