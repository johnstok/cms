/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import ccc.api.ResourceSummary;
import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.ResourceTree;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class LinkSelectionDialog extends LayoutContainer {

    private final ResourceTree _tree;
    /** _qs : QueriesServiceAsync. */
    private final QueriesServiceAsync _qs = GWT.create(QueriesService.class);


    /**
     * Constructor.
     *
     * @param targetRoot ResourceSummary root
     */
    public LinkSelectionDialog(final ResourceSummary targetRoot) {
        final ContentPanel panel = new ContentPanel();

        panel.setBodyStyle("backgroundColor: white;");
        panel.setScrollMode(Scroll.AUTOY);
        panel.setHeaderVisible(false);
        panel.setWidth(620);
        panel.setHeight(460);
        panel.setLayout(new FitLayout());

        _tree = new ResourceTree(targetRoot);
        panel.add(_tree);

        final Button save = new Button(
            Globals.uiConstants().save(),
            new SelectionListener<ComponentEvent>() {
                @Override
                public void componentSelected(final ComponentEvent ce) {
                    final ResourceSummaryModelData target = selectedResource();
                    if (target == null) {
                        closeWindow();
                    } else {
                        _qs.getAbsolutePath(
                            target.getId(),
                            new ErrorReportingCallback<String>() {
                                public void onSuccess(final String path) {
                                    jsniSetUrl(path, target.getTitle());
                                    closeWindow();
                                }
                            });
                    }
                }
            }
        );
        save.setId("ResourceSelectSave");
        panel.addButton(save);
        add(panel);

    }

    /**
     * Accessor for selected folder.
     *
     * @return Returns the selected folder as {@link FolderDTO}
     */
    public ResourceSummaryModelData selectedResource() {
        return
            (null==_tree.getSelectedItem())
                ? null
                : (ResourceSummaryModelData) _tree.getSelectedItem().getModel();
    }

    private static native String jsniSetUrl(String selectedUrl, String title) /*-{
    $wnd.opener.FCK.InsertHtml("<a href='"+selectedUrl+"'>"+title+"</a>");
    }-*/;

    private static native String closeWindow() /*-{
    $wnd.close() ;
    }-*/;
}
