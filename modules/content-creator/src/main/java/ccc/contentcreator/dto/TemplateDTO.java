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
 * TODO: Pull validation methods up to {@link ResourceDTO}?
 * TODO: Constructor params are in wrong order.
 * TODO: Should have a proper name field - just copies title.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateDTO extends ResourceDTO {


    @SuppressWarnings("unused") // Required for GWT
    private TemplateDTO() { super(null, null, null, null, null); }

    /**
     * Constructor.
     *
     * @param id The uuid for the resource.
     * @param version The version of the resource.
     * @param name The name of the resource.
     * @param title The title of the resource.
     * @param description The description of the template.
     * @param body The body of the display template.
     * @param definition The definition for the template.
     */
    public TemplateDTO(final String id,
                       final int version,
                       final String name,
                       final String title,
                       final String description,
                       final String body,
                       final String definition,
                       final String locked,
                       final String published,
                       final String tags) {
        super(id, version, "TEMPLATE", name, title, locked, published, tags);
        set("description", description);
        set("body", body);
        set("definition", definition);
    }

    /**
     * Accessor for the description.
     *
     * @return The description as a string.
     */
    public String getDescription() {
        return get("description");
    }

    /**
     * Accessor for the body.
     *
     * @return The body as a string.
     */
    public String getBody() {
        return get("body");
    }

    /**
     * Accessor for the definition.
     *
     * @return The definition as a string.
     */
    public String getDefinition() {
        return get("definition");
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
        if (null==getTitle() || getTitle().length()<1) {
            errors.add("Title may not be empty.");
        }
        if (null==getDescription() || getDescription().length()<1) {
            errors.add("Description may not be empty.");
        }
        if (null==getBody() || getBody().length()<1) {
            errors.add("Body may not be empty.");
        }
        if (null==getDefinition() || getDefinition().length()<1) {
            errors.add("Definition may not be empty.");
        }
        return errors;
    }
}
