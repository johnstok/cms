/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.client.gwt.widgets;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ccc.api.core.ResourceSummary;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.remoting.GetChildrenPagedAction;
import ccc.client.gwt.remoting.GetRootsAction;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Renders a tree of folders.
 *
 * @author Civic Computing Ltd.
 */
public class FolderResourceTree extends AbstractResourceTree {

    private final Globals _globals;

    /**
     * Constructor.
     *
     * @param globals IGlobals globals.
     */
    public FolderResourceTree(final Globals globals) {
        _globals = globals;
        load();
    }

    /** {@inheritDoc} */
    @Override
    protected RpcProxy<List<ResourceSummaryModelData>> createProxy() {
        final RpcProxy<List<ResourceSummaryModelData>> proxy =
            new RpcProxy<List<ResourceSummaryModelData>>() {

            @Override
            protected void load(
                final Object loadConfig,
                final AsyncCallback<List<ResourceSummaryModelData>> callback) {

                if (null==loadConfig
                    || !(loadConfig instanceof ResourceSummaryModelData)) {
                    new GetRootsAction() { // TODO: UseGetResourceForPathAction instead.
                        @Override
                        protected void onSuccess(final Collection<ResourceSummary> roots) {
                            for (final ResourceSummary rr : roots) {
                                if (rr.getName().equals("content")) {
                                    callback.onSuccess(
                                        DataBinding.bindResourceSummary(
                                            Collections.singletonList(rr)));
                                }
                            }
                        }

                    }.execute();
                } else {
                    new GetChildrenPagedAction(
                        ((ResourceSummaryModelData) loadConfig).getId(),
                        1,
                        1000,
                        "name",
                        "ASC") {

                        /** {@inheritDoc} */
                        @Override protected void onFailure(final Throwable t) {
                            GLOBALS.unexpectedError(
                                t, GLOBALS.userActions().unknownAction());
                            callback.onFailure(t);
                        }

                        /** {@inheritDoc} */
                            @Override
                            protected String getPath() {
                                return "/resources/list?parent="
                                +((ResourceSummaryModelData) loadConfig).getId()
                                +"&page=1&count=1000&sort="
                                +"name&order=ASC&type=folder";
                            }

                        @Override
                        protected void execute(final Collection<ResourceSummary> children,
                                               final int totalCount) {
                            callback.onSuccess(
                                DataBinding.bindResourceSummary(children));
                        }
                    }.execute();
                }
            }
        };
        return proxy;
    }

    /** {@inheritDoc} */
    @Override
    protected BaseTreeLoader<ResourceSummaryModelData> createLoader() {
        return new BaseTreeLoader<ResourceSummaryModelData>(createProxy()) {
            @Override
            public boolean hasChildren(final ResourceSummaryModelData parent) {
                final int folderCount = parent.getFolderCount();
                return folderCount > 0;
            }
        };
    }
}
