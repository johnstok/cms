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

import ccc.api.FileSummary;
import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.DataBinding;
import ccc.contentcreator.binding.FileSummaryModelData;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.data.ModelReader;
import com.extjs.gxt.ui.client.data.RpcProxy;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.ListView;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Image selection dialog for FCKEditor use.
 *
 * @author Civic Computing Ltd.
 */
public class ImageSelectionDialog extends LayoutContainer {

    private static final int PANEL_HEIGHT = 460;
    private static final int PANEL_WIDTH = 620;

    private final QueriesServiceAsync _qs = GWT.create(QueriesService.class);


    /**
     * Constructor.
     */
    public ImageSelectionDialog() {
        setLayout(new FlowLayout(10));
    }

    /** {@inheritDoc} */
    @Override
    protected void onRender(final Element parent, final int index) {
        super.onRender(parent, index);

        final RpcProxy<FileSummaryModelData, List<FileSummaryModelData>> proxy =
            new RpcProxy<FileSummaryModelData, List<FileSummaryModelData>>() {
            @Override
            protected void load(final FileSummaryModelData loadConfig,
                                final AsyncCallback<List<FileSummaryModelData>> callback) {

                _qs.getAllContentImages(
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

        final ListLoader loader =
            new BaseListLoader(proxy, new ModelReader<FileSummaryModelData>());
        final ListStore<FileSummaryModelData> store =
            new ListStore<FileSummaryModelData>(loader);
        loader.load();

        final ContentPanel panel = new ContentPanel();
        panel.setCollapsible(false);
        panel.setAnimCollapse(false);
        panel.setFrame(true);
        panel.setId("images-view");
        panel.setHeaderVisible(false);
        panel.setWidth(PANEL_WIDTH);
        panel.setHeight(PANEL_HEIGHT);
        panel.setLayout(new FitLayout());

        panel.setBodyBorder(false);

        final ListView<FileSummaryModelData> view =
                new ListView<FileSummaryModelData>();

        view.setTemplate(getTemplate());
        view.setStore(store);
        view.setItemSelector("div.thumb-wrap");
        view.getSelectionModel().addListener(
            Events.SelectionChange,
            new ImageSelectionListener());

        panel.add(view);
        add(panel);
    }

    /**
     * Listener for image selection.
     *
     * @author Civic Computing Ltd.
     */
    private static final class ImageSelectionListener
        implements
            Listener<SelectionChangedEvent<FileSummaryModelData>> {

        public void handleEvent(
                        final SelectionChangedEvent<FileSummaryModelData> be) {
            if (null!=be.getSelectedItem()) {
                jsniSetUrl(
                    be.getSelectedItem().getPath(),
                    be.getSelectedItem().getTitle());
            }
        }
    }

    // TODO: Property names aren't type safe.
    private native String getTemplate() /*-{
        return ['<tpl for=".">',
         '<div class="thumb-wrap" id="{NAME}" style="border: 1px solid white">',
         '<div class="thumb"><img src="{PATH}" title="{TITLE}"></div>',
         '<span class="x-editable">{SHORT_NAME}</span></div>',
         '</tpl>',
         '<div class="x-clear"></div>'].join("");

         }-*/;

    private static native String jsniSetUrl(String selectedUrl, String title) /*-{
    $wnd.opener.FCK.InsertHtml("<img title='"+title+"' alt='"+title+"' src='"+selectedUrl+"'/>");
    $wnd.close() ;
    }-*/;

}
