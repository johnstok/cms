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
package ccc.contentcreator.dto;

import java.util.ArrayList;
import java.util.List;


/**
 * A dto for a CCC template.
 * TODO: should extend {@link ResourceDTO}.
 * TODO: Pull validation methods up to {@link ResourceDTO}?
 *
 * @author Civic Computing Ltd.
 */
public class TemplateDTO implements DTO {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 7676780306991591780L;

    private String _title = "";
    private String _description = "";
    private String _body = "";
    private String _id = null;

    @SuppressWarnings("unused") // Required for GWT
    private TemplateDTO() { super(); }

    /**
     * Constructor.
     *
     * @param title The title of the resource.
     * @param body The body of the display template.
     * @param description The description of the template.
     */
    public TemplateDTO(final String title,
                       final String description,
                       final String body) {
        _title = title;
        _description = description;
        _body = body;
    }

    /**
     * Accessor for the title.
     *
     * @return The string representation of the title.
     */
    public String getTitle() {
        return _title;
    }

    /**
     * Accessor for the description.
     *
     * @return The description as a string.
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Accessor for the body.
     *
     * @return The body as a string.
     */
    public String getBody() {
        return _body;
    }

    /**
     * Determine whether the dto is valid.
     *
     * @return True if valid, false otherwise.
     */
    public boolean isValid() {
        return validate().size()<1;
    }

    /**
     * Valid the dto.
     *
     * @return A list of strings representing validation errors.
     */
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

    /**
     * Accessor for the id property.
     *
     * @return The id as a string.
     */
    public String getId() {
        return _id;
    }

    /**
     * Mutator for the id property.
     *
     * @param id The new id.
     */
    public void setId(final String id) {
        _id = id;
    }
}
