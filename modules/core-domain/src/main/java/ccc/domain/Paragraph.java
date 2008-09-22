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

import static ccc.commons.DBC.*;

import java.io.Serializable;

/**
 * A paragraph of HTML.
 *
 * @author Civic Computing Ltd
 */
public final class Paragraph implements Serializable {


    private String _body = "";

    @SuppressWarnings("unused")
    private Paragraph() { /* NO-OP */ }

    /**
     * Constructor.
     *
     * @param bodyString The HTML body for this paragraph - as a string.
     */
    public Paragraph(final String bodyString) {
        require().notEmpty(bodyString);
        _body = bodyString;
    }

    /**
     * Accessor for the paragraph body.
     *
     * @return The HTML as a string.
     */
    public String body() {
        return _body;
    }
}
