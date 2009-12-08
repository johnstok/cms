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
package ccc.contentcreator.dialogs;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.rest.dto.ResourceSummary;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
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
    private final CheckBox _openInNew = new CheckBox();
    private final CheckBoxGroup _cbg = new CheckBoxGroup();

    private final String _elementid;

    private ResourceSummaryModelData _md = null;
    private String _uuid = null;


    /**
     * Constructor.
     *
     * @param targetRoot ResourceSummary root
     * @param elementid Element ID for FCKEditor
     * @param url URL of selected link
     * @param title Title of the link
     * @param openInNew Boolean  for opening the link in the new  of the window.
     */
    public LinkSelectionDialog(final ResourceSummary targetRoot,
                               final String elementid,
                               final String url,
                               final String title,
                               final boolean openInNew) {
        super(new IGlobalsImpl().uiConstants().selectResource(),
              new IGlobalsImpl());
        _elementid = elementid;

        if (title != null && !title.trim().isEmpty()) {
            _linkName.setValue(title);
        }
        if (url != null && !url.trim().isEmpty()) {
            _linkPath.setValue(url);
        }

        _linkName.setFieldLabel(constants().title());
        _linkName.setId("linkName");
        _linkName.setAllowBlank(false);

        _openInNew.setValue(new Boolean(openInNew));
        _cbg.add(_openInNew);
        _cbg.setFieldLabel(constants().openInNewWindow());

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
                                final String appContext =
                                    new IGlobalsImpl()
                                        .getSetting("application.context");
                                final String path =_md.getAbsolutePath();
                                _linkPath.setValue(appContext+path);
                                _linkName.setValue(_md.getTitle());
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
        addField(_cbg);
        addField(_linkPath);
    }


    private static native String jsniSetUrl(final String selectedUrl,
                                            final String title,
                                            final String uuid,
                                            final String elementID,
                                            final boolean openInNew) /*-{
        if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance == null) {
                return null;
            }

            var selection = instance.Selection;

            if (selection.HasAncestorNode('A')) {
                var link = selection.MoveToAncestorNode( 'A' ) ;
                if ( link )
                    selection.SelectNode( link ) ;
                link.href = selectedUrl;
                link.setAttribute('_fcksavedurl', selectedUrl);
                link.innerHTML = title;
                if (openInNew) {
                    link.target = "_blank";
                }

                link.setAttribute( 'class', "ccc:"+uuid) ;
            } else {
                var linkURL = "<a href='"+selectedUrl+"' title='"+title+"'";
                if (uuid != null) {
                    linkURL = linkURL +" class='ccc:"+uuid+"'";
                }
                if (openInNew) {
                    linkURL = linkURL +" target='_blank'";
                }
                linkURL = linkURL +">"+ title +"</a>";
                return instance.InsertHtml(linkURL);
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
                    jsniSetUrl(
                        _linkPath.getValue(),
                        _linkName.getValue(),
                        _uuid,
                        _elementid,
                        _openInNew.getValue().booleanValue());
                    hide();
                }
            }
        };
    }
}
