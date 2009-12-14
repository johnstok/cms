/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.contentcreator.client;

import ccc.contentcreator.binding.ImageSummaryModelData;
import ccc.contentcreator.dialogs.ImageChooserDialog;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.form.TriggerField;


/**
 * An extended trigger field storing selected image as
 * {@link ImageSummaryModelData}.
 *
 * @author Civic Computing Ltd.
 */
public class ImageTriggerField extends TriggerField<String> {

    private ImageSummaryModelData _md;

    /**
     * Mutator.
     *
     * @param md FileSummaryModelData to set.
     */
    public void setFSModel(final ImageSummaryModelData md) {
        _md = md;
    }

    /**
     * Accessor.
     *
     * @return FileSummaryModelData of the field.
     */
    public ImageSummaryModelData getFSModel() {
        return _md;
    }


    /**
     * Constructor.
     *
     */
    public ImageTriggerField() {
        super();

        setEditable(true);

        addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final ImageChooserDialog imageChooser =
                        new ImageChooserDialog(ImageTriggerField.this);
                    imageChooser.show();
                }});

        addListener(
            Events.KeyPress,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    clear();
                    _md = null;
                    be.stopEvent();
                }});
    }

}
