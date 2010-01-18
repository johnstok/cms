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
 * Modified by   $Author: petteri $
 * Modified on   $Date: 2009-01-06 16:14:14 +0000 (Tue, 06 Jan 2009) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import ccc.contentcreator.binding.ImageSummaryModelData;
import ccc.contentcreator.client.IGlobalsImpl;
import ccc.types.Paragraph;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;

/**
 * Image selection dialog for FCKEditor use.
 *
 * @author Civic Computing Ltd.
 */
public class ImageSelectionDialog extends AbstractImageSelectionDialog {

    private final String _elementid;

    /**
     * Constructor.
     *
     * @param elementid Element ID for FCKEditor
     */
    public ImageSelectionDialog(final String elementid) {
        super(new IGlobalsImpl().uiConstants().selectImage(),
              new IGlobalsImpl());
        _elementid = elementid;
    }

    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override
            public void componentSelected(final ButtonEvent ce) {
                final ImageSummaryModelData md =
                    getView().getSelectionModel().getSelectedItem();
                if (md != null) {
                    final String path = Paragraph.escape(md.getPath());
                    final String appContext =
                        new IGlobalsImpl().getSetting("application.context");
                    jsniSetUrl(
                        appContext+path,
                        Paragraph.escape(md.getTitle()),
                        md.getId().toString(),
                        _elementid);
                    hide();
                }
            }
        };
    }


    private static native String jsniSetUrl(final String selectedUrl,
                                            final String title,
                                            final String uuid,
                                            final String elementID) /*-{
     if ($wnd.FCKeditorAPI) {
            var instance = $wnd.FCKeditorAPI.GetInstance(elementID);
            if (instance != null) {
                return instance.InsertHtml("<img title='"+title+"' alt='"
                +title+"' class='ccc:"+uuid+"' src='"+selectedUrl+"'/>");
            }
        }
        return null;
    }-*/;

}
