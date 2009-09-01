/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rest.dto;

import ccc.serialization.Json;
import ccc.serialization.JsonKeys;
import ccc.serialization.Jsonable;
import ccc.types.ID;


/**
 * A new template.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateNew implements Jsonable {

    private final ID            _parentId;
    private final TemplateDelta _delta;
    private final String        _title;
    private final String        _description;
    private final String        _name;


    /**
     * Constructor.
     *
     * @param parentId    The template's parent.
     * @param delta       The template's details.
     * @param title       The template's title.
     * @param description The template's description.
     * @param name        The template's name.
     */
    public TemplateNew(final ID parentId,
                       final TemplateDelta delta,
                       final String title,
                       final String description,
                       final String name) {
        _parentId = parentId;
        _delta = delta;
        _title = title;
        _description = description;
        _name = name;
    }


    /**
     * Accessor.
     *
     * @return Returns the parentId.
     */
    public final ID getParentId() {
        return _parentId;
    }


    /**
     * Accessor.
     *
     * @return Returns the delta.
     */
    public final TemplateDelta getDelta() {
        return _delta;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    public final String getTitle() {
        return _title;
    }


    /**
     * Accessor.
     *
     * @return Returns the description.
     */
    public final String getDescription() {
        return _description;
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public final String getName() {
        return _name;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, _parentId);
        json.set(JsonKeys.DELTA, _delta);
        json.set(JsonKeys.TITLE, _title);
        json.set(JsonKeys.DESCRIPTION, _description);
        json.set(JsonKeys.NAME, _name);
    }
}
