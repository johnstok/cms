/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import static ccc.commons.jee.DBC.require;

import java.io.Serializable;

/**
 * A paragraph of HTML.
 *
 * @author Civic Computing Ltd
 */
public final class Paragraph implements Serializable {

    private String body = "";

    private Paragraph() { // NO-OP
        // for hibernate
    }
    
    /**
     * Constructor.
     *
     * @param body The HTML body for this paragraph - as a string.
     */
    public Paragraph(final String body) {

        require().notEmpty(body);

        this.body = body;
    }

    /**
     * Accessor for the paragraph body.
     *
     * @return The HTML as a string.
     */
    public String body() {
        return body;
    }

}
