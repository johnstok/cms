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
package ccc.contentcreator.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.ImageSummaryModelData;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.core.IGlobals;
import ccc.contentcreator.core.IGlobalsImpl;
import ccc.contentcreator.remoting.GetImagesPagedAction;
import ccc.contentcreator.views.gxt.FolderSelectionDialog;
import ccc.rest.dto.FileDto;

import com.extjs.gxt.ui.client.data.BasePagingLoadConfig;
import com.extjs.gxt.ui.client.data.BasePagingLoadResult;
import com.extjs.gxt.ui.client.data.BasePagingLoader;
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
//    private static final int PANEL_WIDTH = 700;
    private static final int IMAGES_PER_PAGE = 20;

    private ResourceSummaryModelData _folder = null;
    private  final ListView<ImageSummaryModelData> _view =
        new ListView<ImageSummaryModelData>();

    private final IGlobals _globals = new IGlobalsImpl();

    private ImageTriggerField _image;


    /**
     * Constructor.
     *
     */
    public ImageSelectionPanel() {

        final RpcProxy<PagingLoadResult<ImageSummaryModelData>> proxy =
            createProxy();

        final PagingLoader<PagingLoadResult<ImageSummaryModelData>> loader =
           new BasePagingLoader<PagingLoadResult<ImageSummaryModelData>>(proxy);
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
                    folderSelect.addListener(Events.Hide,
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
        toolBar.add(new Text(_globals.uiConstants().folder()));
        toolBar.add(_folderField);
        toolBar.add(new SeparatorToolItem());

        final ListStore<ImageSummaryModelData> listStore =
            new ListStore<ImageSummaryModelData>(loader);

        setCollapsible(false);
        setAnimCollapse(false);
        setId("images-view");
        setHeaderVisible(false);
//        setWidth(PANEL_WIDTH);
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


    private RpcProxy<PagingLoadResult<ImageSummaryModelData>> createProxy() {

        return new RpcProxy<PagingLoadResult<ImageSummaryModelData>>() {

            @Override
            protected void load(final Object loadConfig,
                                final AsyncCallback<PagingLoadResult
                                <ImageSummaryModelData>> callback) {
                if (null == _folder
                    || null==loadConfig
                    || !(loadConfig instanceof BasePagingLoadConfig)) {
                    final PagingLoadResult<ImageSummaryModelData> plr =
                        new BasePagingLoadResult<ImageSummaryModelData>(null);
                    callback.onSuccess(plr);
                } else {
                    final BasePagingLoadConfig config =
                        (BasePagingLoadConfig) loadConfig;

                    new GetImagesPaged(
                        _globals.uiConstants().selectImage(),
                        _folder.getId(),
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
        private final AsyncCallback<PagingLoadResult
        <ImageSummaryModelData>> _callback;

        /**
         * Constructor.
         *
         * @param actionName
         * @param parentId
         * @param image
         * @param config
         * @param callback
         */
        private GetImagesPaged(final String actionName,
                               final UUID parentId,
                               final BasePagingLoadConfig config,
                               final AsyncCallback<PagingLoadResult
                                   <ImageSummaryModelData>> callback) {

            super(actionName, parentId,
                config.getOffset()/config.getLimit()+1,
                config.getLimit());
            _config = config;
            _callback = callback;
        }

        @Override
        protected void execute(final Collection<FileDto> images,
                               final int totalCount) {
            final List<ImageSummaryModelData> results =
                loadModel(_image, images);
            final PagingLoadResult<ImageSummaryModelData> plr =
                new BasePagingLoadResult<ImageSummaryModelData>(
                    results, _config.getOffset(), totalCount);
            _callback.onSuccess(plr);
        }
    }

    private List<ImageSummaryModelData> loadModel(final ImageTriggerField image,
        final Collection<FileDto> arg0) {
        final List<ImageSummaryModelData> models =
            DataBinding.bindFileSummary(arg0);
        if (image != null && models != null && models.size() > 0) {

            final ImageSummaryModelData fs = image.getFSModel();

            if (fs != null) {
                final List<ImageSummaryModelData> selection =
                    new ArrayList<ImageSummaryModelData>();
                for (final ImageSummaryModelData item : models) {
                    if (item.getId().equals(fs.getId())) {
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
    public ListView<ImageSummaryModelData> getView() {
        return _view;
    }


    // TODO: Property names aren't type safe.
    private native String getTemplate() /*-{

    return ['<tpl for=".">',
     '<div class="thumb-wrap" id="{NAME}" style="border: 1px solid white">',
     '<div class="thumb">',
     '<img src="preview/{PATH}?thumb=200" title="{TITLE}"></div>',
     '<span class="x-editable">{SHORT_NAME} {WIDTH}x{HEIGHT}px</span></div>',
     '</tpl>',
     '<div class="x-clear"></div>'].join("");

     }-*/;



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
