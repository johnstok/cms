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
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Globals;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.TriggerField;


/**
 * Dialog for FCKEditor link selector.
 *
 * @author Civic Computing Ltd.
 */
public class LinkSelectionDialog extends AbstractEditDialog {


    private final TriggerField<String> _linkPath =
        new TriggerField<String>();
    private final TextField<String> _linkName = new TextField<String>();

    private ResourceSummaryModelData _md = null;
    private final String _elementid;


    /**
     * Constructor.
     *
     * @param targetRoot ResourceSummary root
     * @param elementid Element ID for FCKEditor
     */
    public LinkSelectionDialog(final ResourceSummary targetRoot,
                               final String elementid) {
        super(Globals.uiConstants().selectResource());
        _elementid = elementid;

        _linkName.setFieldLabel(constants().name());
        _linkName.setId("linkName");
        _linkName.setAllowBlank(false);

        _linkPath.setFieldLabel(constants().path());
        _linkPath.setId("linkPath");
        _linkPath.setAllowBlank(false);
        _linkPath.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final ResourceSelectionDialog folderSelect =
                        new ResourceSelectionDialog(targetRoot);
                    folderSelect.addListener(Events.Close,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent ce) {
                            _md = folderSelect.selectedResource();
                            if (_md != null) {
                                _linkPath.setValue(_md.getAbsolutePath());
                                _linkName.setValue(_md.getName());
                            }
                        }});
                    folderSelect.show();
                }});
        addField(_linkName);
        addField(_linkPath);
    }


    private static native String jsniSetUrl(final String selectedUrl,
                                            final String title,
                                            final String elementID) /*-{
        if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance != null) {
                return instance.InsertHtml("<a href='"+selectedUrl+"'>"
                +title+"</a>");
            }
        }
        return null;

    }-*/;


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {
                if (_linkPath.getValue() != null
                    && _linkName.getValue() != null) {
                    jsniSetUrl(_linkPath.getValue(),
                        _linkName.getValue(),
                        _elementid);
                    close();
                }
            }
        };
    }
}
