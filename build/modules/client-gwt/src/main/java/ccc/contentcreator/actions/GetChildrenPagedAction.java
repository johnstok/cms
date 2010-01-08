/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import ccc.contentcreator.client.GwtJson;
import ccc.rest.dto.PagingDto;
import ccc.rest.dto.ResourceSummary;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public abstract class GetChildrenPagedAction
extends
RemotingAction{

    private final PagingDto _pagingDto;
    private final UUID _parentId;

    /**
     * Constructor.
     *
     */
    public GetChildrenPagedAction(final UUID parentId,
                                  final PagingDto pagingDto) {
        super("get children paged", RequestBuilder.POST); // FIXME i18n
        _pagingDto = pagingDto;
        _parentId = parentId;
    }

    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/folders/"+_parentId+"/children-paged";
    }


    /** {@inheritDoc} */
    @Override
    protected void onOK(final Response response) {
        final JSONArray result = JSONParser.parse(response.getText()).isArray();
        final Collection<ResourceSummary> children =
            new ArrayList<ResourceSummary>();
        for (int i=0; i<result.size(); i++) {
            children.add(
                new ResourceSummary(new GwtJson(result.get(i).isObject())));
        }

        execute(children);
    }

    /** {@inheritDoc} */
    @Override protected String getBody() {
        final GwtJson json = new GwtJson();
        _pagingDto.toJson(json);
        return json.toString();
    }

    /**
     * Handle the result of a successful call.
     *
     * @param children The collection of folder children.
     */
    protected abstract void execute(Collection<ResourceSummary> children);
}
