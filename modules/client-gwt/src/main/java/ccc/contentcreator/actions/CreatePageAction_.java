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

import ccc.api.ID;
import ccc.api.JsonKeys;
import ccc.api.PageDelta;
import ccc.api.ResourceSummary;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * Create a page.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreatePageAction_
    extends
        RemotingAction {

    private final ID _parentFolder;
    private final PageDelta _page;
    private final String _name;
    private final ID _template;
    private final String _title;


    /**
     * Constructor.
     *
     * @param title The page's title.
     * @param template
     * @param name
     * @param page
     * @param parentFolder
     */
    public CreatePageAction_(final ID parentFolder,
                             final PageDelta page,
                             final String name,
                             final ID template,
                             final String title) {
        super(GLOBALS.uiConstants().createPage(), RequestBuilder.POST);
        _parentFolder = parentFolder;
        _page = page;
        _name = name;
        _template = template;
        _title = title;
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
        return json.toString();
    }


    protected abstract void execute(ResourceSummary rs);
}
