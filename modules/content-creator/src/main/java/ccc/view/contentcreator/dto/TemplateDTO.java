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
package ccc.view.contentcreator.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class TemplateDTO implements Serializable {

    private String _title = "";
    private String _description = "";
    private String _body = "";

    private TemplateDTO() { super(); }

    /**
     * Constructor.
     *
     * @param title
     * @param description
     * @param body
     */
    public TemplateDTO(final String title, final String description, final String body) {
        _title = title;
        _description = description;
        _body = body;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getTitle() {
        return _title;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getDescription() {
        return _description;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public String getBody() {
        return _body;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public boolean isValid() {
        return validate().size()<1;
    }

    public List<String> validate() {
        final List<String> errors = new ArrayList<String>();
        if (null==_title || _title.length()<1) {
            errors.add("Title may not be empty.");
        }
        if (null==_description || _description.length()<1) {
            errors.add("Description may not be empty.");
        }
        if (null==_body || _body.length()<1) {
            errors.add("Body may not be empty.");
        }
        return errors;
    }
}
