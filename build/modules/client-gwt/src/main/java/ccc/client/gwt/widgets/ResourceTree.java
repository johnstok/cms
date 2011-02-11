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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceName;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.client.actions.GetResourcesPagedAction;
import ccc.client.core.Globals;
import ccc.client.core.I18n;
import ccc.client.gwt.binding.DataBinding;

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

    private final ResourceType _type;


    /**
     * Constructor.
     *
     * @param root The root of the tree.
     * @param type The resource type to show.
     */
    public ResourceTree(final ResourceSummary root,
                        final ResourceType type) {
        super(root);
        _type = type;
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
                    final List<ResourceSummary> list = new ArrayList<ResourceSummary>();
                    list.add(getRoot());
                    final ResourceSummary search = new ResourceSummary();
                    search.setName(new ResourceName(I18n.uiConstants.search()));
                    search.setTitle(I18n.uiConstants.search());
                    search.setType(ResourceType.SEARCH);
                    list.add(search);
                    callback.onSuccess(
                        DataBinding.bindResourceSummary( Collections.unmodifiableList(list)));
                } else {
                    final ResourceSummary parent =
                        ((BeanModel) loadConfig).<ResourceSummary>getBean();
                    if (getChildCount(parent) > Globals.MAX_FETCH) {
                        final List<ResourceSummary> children =
                            createRangeFolders(getChildCount(parent), parent);
                        callback.onSuccess(
                            DataBinding.bindResourceSummary(children));
                    } else {
                        int page = 1;
                        if (parent.getType() == ResourceType.RANGE_FOLDER) {
                            parent.setId(parent.getParent());
                            page = Integer.decode(parent.getAbsolutePath());
                        }

                        final ResourceCriteria criteria =
                            new ResourceCriteria();
                        criteria.setParent(parent.getId());
                        criteria.setSortField("name");
                        criteria.setSortOrder(SortOrder.ASC);
                        criteria.setType(_type);

                        new GetResourcesPagedAction(criteria,
                                                   page,
                                                   Globals.MAX_FETCH) {

                            /** {@inheritDoc} */
                            @Override protected void onFailure(
                                                           final Throwable t) {
                                callback.onFailure(t);
                            }

                            @Override
                            protected void execute(final Collection<ResourceSummary> children,
                                                   final long totalCount) {

                                callback.onSuccess(
                                    DataBinding.bindResourceSummary(children));
                            }
                        }.execute();
                    }
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
                return getChildCount(parent.<ResourceSummary>getBean()) > 0;
            }
        };
    }


    private int getChildCount(final ResourceSummary parent) {
        if (_type == ResourceType.FOLDER) {
            return parent.getFolderCount();
        }
        return parent.getChildCount();
    }
}
