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

package ccc.contentcreator.client;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.contentcreator.actions.GetChildrenAction;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.rest.dto.ResourceSummary;

import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Renders a tree of resources.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceTree extends AbstractResourceTree {

    private final IGlobals _globals;
    private final ResourceSummary _root;

    /**
     * Constructor.
     *
     * @param root The root of the tree.
     * @param globals IGlobals implementation.
     */
    public ResourceTree(final ResourceSummary root, final IGlobals globals) {

        _root = root;
        _globals = globals;

        loader().load(null);
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

                final UUID parentId =
                    (null==loadConfig
                        || !(loadConfig instanceof ResourceSummaryModelData))
                    ? _root.getId()
                    : ((ResourceSummaryModelData) loadConfig).getId();

                    new GetChildrenAction(_globals.userActions().loadData(),
                        parentId) {

                        /** {@inheritDoc} */
                        @Override protected void onFailure(final Throwable t) {
                            callback.onFailure(t);
                        }

                        /** {@inheritDoc} */
                        @Override protected void execute(
                                   final Collection<ResourceSummary> children) {
                            callback.onSuccess(
                                DataBinding.bindResourceSummary(children));
                        }
                    }.execute();
            }
        };
        return proxy;
    }
}
