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
import ccc.api.PageDelta;
import ccc.api.ResourceSummary;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class CreatePageAction_
    extends
        RemotingAction {

    private final ID _parentFolder;
    private final PageDelta _page;
    private final String _name;
    private final boolean _publish;
    private final ID _template;
    private final String _title;


    /**
     * Constructor.
     * @param title The page's title.
     * @param template
     * @param publish
     * @param name
     * @param page
     * @param parentFolder
     */
    public CreatePageAction_(final ID parentFolder,
                             final PageDelta page,
                             final String name,
                             final boolean publish,
                             final ID template,
                             final String title) {
        super(GLOBALS.uiConstants().createPage(), RequestBuilder.POST);
        _parentFolder = parentFolder;
        _page = page;
        _name = name;
        _publish = publish;
        _template = template;
        _title = title;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() { // FIXME: Escape!!
        return "/pages"
            + "?id="+_parentFolder
            + "&n="+_name
            + "&p="+_publish
            + "&m="+_template
            + "&t="+_title;
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
        _page.toJson(json);
        return json.toString();
    }


    protected abstract void execute(ResourceSummary rs);
}
