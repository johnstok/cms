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
package ccc.client.gwt.views.gxt;

import ccc.api.core.ResourceSummary;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.gwt.widgets.ImageSelectionPanel;
import ccc.client.gwt.widgets.ImageTriggerField;

import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;


/**
 * Image chooser for image type paragraph fields.
 *
 * @author Civic Computing Ltd.
 */
public class ImageChooserDialog
    extends
        AbstractBaseDialog {

    private final ImageSelectionPanel _imagePanel =
        new ImageSelectionPanel(InternalServices.ROOTS.getElements());


    /**
     * Constructor.
     *
     * @param image The trigger field for the image.
     */
    public ImageChooserDialog(final ImageTriggerField image) {
        super(I18n.UI_CONSTANTS.selectImage(), InternalServices.GLOBALS);
        _imagePanel.setImage(image);
        add(_imagePanel);
        addButton(getCancel());
        final Button save = new Button(constants().save(), saveAction());
        addButton(save);
    }


    /** {@inheritDoc} */
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>(){
            @Override
            public void componentSelected(final ButtonEvent ce) {
                final BeanModel md =
                    _imagePanel.getView().getSelectionModel().getSelectedItem();

                if (md != null) {
                    final ResourceSummary rs = md.<ResourceSummary>getBean();
                    _imagePanel.getImage().setValue(rs.getAbsolutePath());
                    _imagePanel.getImage().setFSModel(rs);
                }

                hide();
            }
        };
    }
}
