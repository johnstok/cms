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
import ccc.contentcreator.client.IGlobalsImpl;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
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
    private final String _elementid;

    private ResourceSummaryModelData _md = null;
    private String _uuid = null;


    /**
     * Constructor.
     *
     * @param targetRoot ResourceSummary root
     * @param elementid Element ID for FCKEditor
     */
    public LinkSelectionDialog(final ResourceSummary targetRoot,
                               final String elementid) {
        super(new IGlobalsImpl().uiConstants().selectResource(),
              new IGlobalsImpl());
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
                    folderSelect.addListener(Events.Hide,
                        new Listener<ComponentEvent>() {
                        public void handleEvent(final ComponentEvent ce) {
                            _md = folderSelect.selectedResource();
                            if (_md != null) {
                                _linkPath.setValue(
                                    _md.getAbsolutePath().substring(1));
                                _linkName.setValue(_md.getName());
                                _uuid =_md.getId().toString();
                            }
                        }});
                    folderSelect.show();
                }});

        _linkPath.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyUp(final ComponentEvent event) {
                _uuid = null;
            }
        });
        addField(_linkName);
        addField(_linkPath);
    }


    private static native String jsniSetUrl(final String selectedUrl,
                                            final String title,
                                            final String uuid,
                                            final String elementID) /*-{
        if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance != null && uuid == null) {
                return instance.InsertHtml("<a href='"+selectedUrl+"'>"
                +title+"</a>");
            } else if (instance != null && uuid != null) {
                return instance.InsertHtml("<a href='"+selectedUrl
                +"' class='ccc:"+uuid+"'>"
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
                        _uuid,
                        _elementid);
                    hide();
                }
            }
        };
    }
}
