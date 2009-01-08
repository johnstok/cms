/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author: petteri $
 * Modified on   $Date: 2009-01-06 16:14:14 +0000 (Tue, 06 Jan 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import java.util.Collection;
import java.util.List;

import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.DataBinding;
import ccc.services.api.FileSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.data.ModelReader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Util;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ImageSelectionDialog extends LayoutContainer {
    final QueriesServiceAsync _qs = GWT.create(QueriesService.class);
    final ListView<ModelData> view = null;
    public ImageSelectionDialog() {
        setLayout(new FlowLayout(10));
    }

    @Override
    protected void onRender(final Element parent, final int index) {
        super.onRender(parent, index);

        final RpcProxy<ModelData, List<ModelData>> proxy =
            new RpcProxy<ModelData, List<ModelData>>() {
            @Override
            protected void load(final ModelData loadConfig,
                                final AsyncCallback<List<ModelData>> callback) {

                _qs.getAllImages(
                    new AsyncCallback<Collection<FileSummary>>(){

                        public void onFailure(final Throwable arg0) {
                            callback.onFailure(arg0);
                        }

                        public void onSuccess(final Collection<FileSummary> arg0) {
                            callback.onSuccess(
                                DataBinding.bindFileSummary(arg0));
                        }
                    });
            }
        };

        final ListLoader loader = new BaseListLoader(proxy,
            new ModelReader<ModelData>());
        final ListStore<ModelData> store = new ListStore<ModelData>(loader);
        loader.load();

        final ContentPanel panel = new ContentPanel();
        panel.setCollapsible(true);
        panel.setAnimCollapse(false);
        panel.setFrame(true);
        panel.setId("images-view");
        panel.setHeading("Simple ListView (0 items selected)");
        panel.setWidth(535);
        panel.setAutoHeight(true);
        panel.setLayout(new FitLayout());

        panel.setBodyBorder(false);

        final ListView<ModelData> view = new ListView<ModelData>() {
            @Override
            protected ModelData prepareData(ModelData model) {
                String s = model.get("title");
                model.set("shortName", Util.ellipse(s, 15));
                return model;
            }

        };

        view.setTemplate(getTemplate());
        view.setStore(store);
        view.setItemSelector("div.thumb-wrap");
        view.getSelectionModel().addListener(Events.SelectionChange,
            new Listener<SelectionEvent<ModelData>>() {

            public void handleEvent(final SelectionEvent<ModelData> be) {
                if (be.selection.size() == 1) {
                    jsniSetUrl(
                        "/server"+(String) be.selection.get(0).get("path"),
                        (String) be.selection.get(0).get("title"));
                }
            }

        });
        panel.add(view);
        add(panel);
    }

    private native String getTemplate() /*-{
        return ['<tpl for=".">',
         '<div class="thumb-wrap" id="{name}" style="border: 1px solid white">',
         '<div class="thumb"><img src="/server{path}" title="{title}"></div>',
         '<span class="x-editable">{shortName}</span></div>',
         '</tpl>',
         '<div class="x-clear"></div>'].join("");

         }-*/;

    private static native String jsniSetUrl(String selectedUrl, String alt) /*-{
    $wnd.opener.SetUrl( selectedUrl,null,null,alt ) ;
    $wnd.close() ;
    }-*/;

}
