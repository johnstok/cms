/*-----------------------------------------------------------------------------
 * Copyright (c) 2010 Civic Computing Ltd.
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
import java.util.List;

import ccc.api.core.File;
import ccc.api.core.ResourceSummary;
import ccc.client.core.I18n;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.remoting.GetImagesPagedAction;
import ccc.client.gwt.views.gxt.FolderSelectionDialog;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.PagingLoadResult;
import com.extjs.gxt.ui.client.data.PagingLoader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.WindowEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TriggerField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.PagingToolBar;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * Image selection panel.
 *
 * @author Civic Computing Ltd.
 */
public class ImageSelectionPanel extends ContentPanel {

    private final TriggerField<String> _folderField =
        new TriggerField<String>();

    private final PagingToolBar _pagerBar;

    private static final int PANEL_HEIGHT = 478;
    private static final int IMAGES_PER_PAGE = 20;

    private ResourceSummaryModelData _folder = null;
    private  final ListView<BeanModel> _view = new ListView<BeanModel>();

    private ImageTriggerField _image;


    /**
     * Constructor.
     *
     */
    public ImageSelectionPanel() {

        final RpcProxy<PagingLoadResult<BeanModel>> proxy =
            createProxy();

        final PagingLoader<PagingLoadResult<BeanModel>> loader =
           new BasePagingLoader<PagingLoadResult<BeanModel>>(proxy);
        loader.setRemoteSort(true);

        final ToolBar toolBar = new ToolBar();

        _folderField.setId("image-folder");
        _folderField.setValue("");
        _folderField.setEditable(false);
        _folderField.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final FolderSelectionDialog folderSelect =
                        new FolderSelectionDialog();
                    folderSelect.addListener(
                        Events.Hide,
                        new Listener<WindowEvent>() {
                        public void handleEvent(final WindowEvent be2) {
                            final Button b = be2.getButtonClicked();
                            if (null==b) { // 'X' button clicked.
                                return;
                            }
                            _folder = folderSelect.selectedFolder();
                            _folderField.setValue(
                                (null==_folder)
                                    ? null
                                    : _folder.getName());
                            _pagerBar.refresh();
                        }});
                    folderSelect.show();
                }});
        toolBar.add(new Text(I18n.UI_CONSTANTS.folder()));
        toolBar.add(_folderField);
        toolBar.add(new SeparatorToolItem());

        final ListStore<BeanModel> listStore = new ListStore<BeanModel>(loader);

        setCollapsible(false);
        setAnimCollapse(false);
        setId("images-view");
        setHeaderVisible(false);
        setAutoWidth(true);
        setHeight(PANEL_HEIGHT);
        setLayout(new FitLayout());
        setBorders(false);
        setBodyBorder(false);
        setBodyStyleName("backgroundColor: white;");

        _view.setBorders(false);
        _view.setAutoWidth(true);
        _view.setTemplate(getTemplate());
        _view.setStore(listStore);
        _view.setItemSelector("div.thumb-wrap");

        add(_view);

        _pagerBar = new PagingToolBar(IMAGES_PER_PAGE);
        _pagerBar.bind(loader);
        loader.load(0, IMAGES_PER_PAGE);
        setTopComponent(toolBar);
        setBottomComponent(_pagerBar);
    }


    private RpcProxy<PagingLoadResult<BeanModel>> createProxy() {

        return new RpcProxy<PagingLoadResult<BeanModel>>() {

            @Override
            protected void load(final Object loadConfig,
                                final AsyncCallback<PagingLoadResult
                                <BeanModel>> callback) {
                if (null == _folder
                    || null==loadConfig
                    || !(loadConfig instanceof BasePagingLoadConfig)) {
                    final PagingLoadResult<BeanModel> plr =
                        new BasePagingLoadResult<BeanModel>(null);
                    callback.onSuccess(plr);
                } else {
                    final BasePagingLoadConfig config =
                        (BasePagingLoadConfig) loadConfig;

                    new GetImagesPaged(
                        I18n.UI_CONSTANTS.selectImage(),
                        _folder.getDelegate(),
                        config,
                        callback).execute();
                }
            }
        };
    }


    /**
     * Loads up images in the pager.
     *
     * @author Civic Computing Ltd.
     */
    private final class GetImagesPaged
        extends
            GetImagesPagedAction {

        private final BasePagingLoadConfig _config;
        private final AsyncCallback<PagingLoadResult<BeanModel>> _callback;


        private GetImagesPaged(final String actionName,
                               final ResourceSummary parent,
                               final BasePagingLoadConfig config,
                               final AsyncCallback<PagingLoadResult
                                   <BeanModel>> callback) {

            super(actionName, parent,
                config.getOffset()/config.getLimit()+1,
                config.getLimit());
            _config = config;
            _callback = callback;
        }

        @Override
        protected void execute(final Collection<File> images,
                               final int totalCount) {
            final List<BeanModel> results =
                loadModel(_image, images);
            final PagingLoadResult<BeanModel> plr =
                new BasePagingLoadResult<BeanModel>(
                    results, _config.getOffset(), totalCount);
            _callback.onSuccess(plr);
        }
    }

    private List<BeanModel> loadModel(final ImageTriggerField image,
                                      final Collection<File> files) {
        final List<BeanModel> models =
            DataBinding.bindFileSummary(files);
        if (image != null && models != null && models.size() > 0) {

            final BeanModel fs = image.getFSModel();

            if (fs != null) {
                final List<BeanModel> selection =
                    new ArrayList<BeanModel>();
                for (final BeanModel item : models) {
                    if (item.<File>getBean().getId().equals(
                            fs.<File>getBean().getId())) {
                        selection.add(item);
                    }
                }
                _view.getSelectionModel().setSelection(selection);
            }
        }
        return models;
    }

    /**
     * Accessor.
     *
     * @return Returns the list view.
     */
    public ListView<BeanModel> getView() {
        return _view;
    }


    private String getTemplate() {
        return
            "<tpl for=\".\">"
            + "<div class=\"thumb-wrap\" id=\"{"
            + DataBinding.FileBeanModel.NAME
            + "}\" style=\"border: 1px solid white\">"
            + "<div class=\"thumb\">"
            + "<img src=\"preview/{"
            + DataBinding.FileBeanModel.PATH
            + "}?thumb=200\" title=\"{"
            + DataBinding.FileBeanModel.TITLE + "}\"></div>"
            + "<span class=\"x-editable\">{"
            + DataBinding.FileBeanModel.SHORT_NAME + "} {"
            + DataBinding.FileBeanModel.WIDTH + "}x{"
            + DataBinding.FileBeanModel.HEIGHT + "}px</span></div>"
            + "</tpl>"
            + "<div class=\"x-clear\"></div>";
     }



    /**
     * Accessor.
     *
     * @return Returns the image.
     */
    public final ImageTriggerField getImage() {
        return _image;
    }

    /**
     * Mutator.
     *
     * @param image The image to set.
     */
    public final void setImage(final ImageTriggerField image) {
        _image = image;
    }


}
