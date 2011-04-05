/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.api.core.ResourceCriteria;
import ccc.api.core.ResourceSummary;
import ccc.api.types.ResourceType;
import ccc.api.types.SortOrder;
import ccc.client.actions.GetResourcesPagedAction;
import ccc.client.gwt.binding.DataBinding;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.data.BaseFilterPagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.FilterConfig;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.google.gwt.user.client.rpc.AsyncCallback;

final class ResourceProxy extends RpcProxy<PagingLoadResult<BeanModel>> {

    private ResourceSummary _folder;
    private final ResourceType _type;


    /**
     * Constructor.
     *
     * @param folder The parent folder
     * @param type The resource type
     */
    ResourceProxy(final ResourceSummary folder, final ResourceType type) {
        _folder = folder;
        _type = type;
    }


    void setFolder(final ResourceSummary folder) {
        _folder = folder;
    }


    @Override
    protected void load(final Object loadConfig,
                        final AsyncCallback<PagingLoadResult
                        <BeanModel>> callback) {
        if ((_folder != null && _folder.getType() == ResourceType.RANGE_FOLDER)
            || null==loadConfig
            || !(loadConfig instanceof BaseFilterPagingLoadConfig)
            ) {
            final PagingLoadResult<BeanModel> plr =
               new BasePagingLoadResult<BeanModel>(null);
            callback.onSuccess(plr);
        } else {
            final BaseFilterPagingLoadConfig config =
                (BaseFilterPagingLoadConfig) loadConfig;

            final int page =  config.getOffset()/ config.getLimit()+1;
            final SortOrder order =
                (config.getSortDir() == Style.SortDir.ASC)
                    ? SortOrder.ASC
                    : SortOrder.DESC;

            String name = null;
            if (config.getFilterConfigs() != null) {
            for (final FilterConfig cc : config.getFilterConfigs()) {
                if (cc.getField().equals("name") && cc.getValue() != null) {
                    name = (String) cc.getValue()+"%";
                }
            }
            }
            final ResourceCriteria criteria = new ResourceCriteria();
            criteria.setParent((_folder == null) ? null :_folder.getId());
            criteria.setName(name);
            criteria.setPublished(null);
            criteria.setSortField(config.getSortField());
            criteria.setSortOrder(order);
            criteria.setType(_type);

            new GetResourcesPagedAction(criteria,
                page,
                config.getLimit()) {

                /** {@inheritDoc} */
                @Override protected void onFailure(final Throwable t) {
                    callback.onFailure(t);
                }

                /** {@inheritDoc} */
                @Override protected void execute(
                         final Collection<ResourceSummary> children,
                         final long totalCount) {
                    final List<BeanModel> results =
                        DataBinding.bindResourceSummary(children);

                    final PagingLoadResult<BeanModel> plr =
                        new BasePagingLoadResult<BeanModel>(
                    results, config.getOffset(), (int) totalCount);
                    callback.onSuccess(plr);
                }
            }.execute();
        }
    }





    /**
     * Accessor.
     *
     * @return The current folder for this proxy.
     */
    public ResourceSummary getFolder() {
        return _folder;
    }


    /**
     * Check if this proxy is displaying a specified folder.
     *
     * @param id The folder's ID.
     *
     * @return True if the proxy is displaying the folder, false otherwise.
     */
    public boolean isDisplaying(final UUID id) {
        return null!=getFolder() && getFolder().getId().equals(id);
    }
}
