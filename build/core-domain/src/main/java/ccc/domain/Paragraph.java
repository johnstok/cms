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

import static ccc.commons.jee.DBC.*;

/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class Paragraph {

    private String body;

    /**
     * Constructor.
     *
     * @param string
     */
    public Paragraph(String body) {
        
        require().notEmpty(body);
        
        this.body = body;
    }

    /**
     * Accessor for the paragraph body.
     *
     * @return
     */
    public String body() {
        return body;
    }

}
