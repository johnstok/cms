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
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.remoting.GetChildrenPagedAction;
import ccc.client.gwt.remoting.GetRootsAction;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
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
    protected RpcProxy<List<BeanModel>> createProxy() {
        final RpcProxy<List<BeanModel>> proxy =
            new RpcProxy<List<BeanModel>>() {

            @Override
            protected void load(
                final Object loadConfig,
                final AsyncCallback<List<BeanModel>> callback) {

                if (null==loadConfig || !(loadConfig instanceof BeanModel)) {
                    // TODO: UseGetResourceForPathAction instead.
                    new GetRootsAction() {
                        @Override
                        protected void onSuccess(
                                     final Collection<ResourceSummary> roots) {
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
                        ((BeanModel) loadConfig).<ResourceSummary>getBean(),
                        1,
                        Globals.MAX_FETCH,
                        "name",
                        SortOrder.ASC,
                        ResourceType.FOLDER) {

                        /** {@inheritDoc} */
                        @Override protected void onFailure(final Throwable t) {
                            InternalServices.EX_HANDLER.unexpectedError(
                                t, I18n.USER_ACTIONS.unknownAction());
                            callback.onFailure(t);
                        }

                        @Override
                        protected void execute(
                                   final Collection<ResourceSummary> children,
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
    protected BaseTreeLoader<BeanModel> createLoader() {
        return new BaseTreeLoader<BeanModel>(createProxy()) {
            @Override
            public boolean hasChildren(final BeanModel parent) {
                final int folderCount =
                    parent.<ResourceSummary>getBean().getFolderCount();
                return folderCount > 0;
            }
        };
    }
}
