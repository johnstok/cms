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
package ccc.contentcreator.actions;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.PageDelta;
import ccc.rest.ResourceSummary;
import ccc.serialization.JsonKeys;
import ccc.types.ID;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Create a page.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreatePageAction
    extends
        RemotingAction {

    private final ID _parentFolder;
    private final PageDelta _page;
    private final String _name;
    private final ID _template;
    private final String _title;
    private final String _comment;
    private final boolean _majorChange;


    /**
     * Constructor.
     *
     * @param parentFolder
     * @param page
     * @param name
     * @param template
     * @param title The page's title.
     * @param comment A comment describing the update.
     * @param majorChange Is this update a major change.
     *
     */
    public CreatePageAction(final ID parentFolder,
                             final PageDelta page,
                             final String name,
                             final ID template,
                             final String title,
                             final String comment,
                             final boolean majorChange) {
        super(GLOBALS.uiConstants().createPage(), RequestBuilder.POST);
        _parentFolder = parentFolder;
        _page = page;
        _name = name;
        _template = template;
        _title = title;
        _comment = comment;
        _majorChange = majorChange;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/pages";
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final ResourceSummary rs = parseResourceSummary(response);
        execute(rs);
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.PARENT_ID, _parentFolder);
        json.set(JsonKeys.DELTA, _page);
        json.set(JsonKeys.NAME, _name);
        json.set(JsonKeys.TEMPLATE_ID, _template);
        json.set(JsonKeys.TITLE, _title);
        json.set(JsonKeys.COMMENT, _comment);
        json.set(JsonKeys.MAJOR_CHANGE, _majorChange);
        return json.toString();
    }


    protected abstract void execute(ResourceSummary rs);
}
