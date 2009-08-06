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
package ccc.api.rest;

import ccc.api.ID;
import ccc.api.Json;
import ccc.api.JsonKeys;
import ccc.api.Jsonable;
import ccc.api.PageDelta;


/**
 * A new page.
 *
 * @author Civic Computing Ltd.
 */
public class PageNew implements Jsonable {

    private final ID        _parentId;
    private final PageDelta _delta;
    private final String    _name;
    private final ID        _templateId;
    private final String    _title;


    /**
     * Constructor.
     *
     * @param parentId
     * @param delta
     * @param name
     * @param templateId
     * @param title
     */
    public PageNew(final ID parentId,
                   final PageDelta delta,
                   final String name,
                   final ID templateId,
                   final String title) {
        _parentId = parentId;
        _delta = delta;
        _name = name;
        _templateId = templateId;
        _title = title;
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
    public final PageDelta getDelta() {
        return _delta;
    }


    /**
     * Accessor.
     *
     * @return Returns the name.
     */
    public final String getName() {
        return _name;
    }


    /**
     * Accessor.
     *
     * @return Returns the templateId.
     */
    public final ID getTemplateId() {
        return _templateId;
    }


    /**
     * Accessor.
     *
     * @return Returns the title.
     */
    public final String getTitle() {
        return _title;
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        json.set(JsonKeys.PARENT_ID, _parentId);
        json.set(JsonKeys.DELTA, _delta);
        json.set(JsonKeys.NAME, _name);
        json.set(JsonKeys.TEMPLATE_ID, _templateId);
        json.set(JsonKeys.TITLE, _title);
    }
}
