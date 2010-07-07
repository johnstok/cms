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
import java.util.List;

import ccc.api.core.ResourceSummary;
import ccc.api.types.SortOrder;
import ccc.client.core.Globals;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.remoting.GetChildrenPagedAction;

import com.extjs.gxt.ui.client.data.BaseTreeLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Renders a tree of resources.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTree extends AbstractResourceTree {

    private final Globals _globals;
    private final ResourceSummary _root;

    /**
     * Constructor.
     *
     * @param root The root of the tree.
     * @param globals IGlobals implementation.
     */
    public ResourceTree(final ResourceSummary root, final Globals globals) {

        _root = root;
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

                final ResourceSummary parent =
                    (null==loadConfig || !(loadConfig instanceof BeanModel))
                        ? _root
                        : ((BeanModel) loadConfig).<ResourceSummary>getBean();

                    new GetChildrenPagedAction(
                        parent,
                        1,
                        Globals.MAX_FETCH,
                        "name",
                        SortOrder.ASC,
                        null) {

                        /** {@inheritDoc} */
                        @Override protected void onFailure(final Throwable t) {
                            callback.onFailure(t);
                        }

                        /** {@inheritDoc} */
                        @Override protected void execute(
                                   final Collection<ResourceSummary> children,
                                   final int count) {
                            callback.onSuccess(
                                DataBinding.bindResourceSummary(children));
                        }
                    }.execute();
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
                final int childCount =
                    parent.<ResourceSummary>getBean().getChildCount();
                return childCount > 0;
            }
        };
    }
}
