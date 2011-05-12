/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import java.util.UUID;

import ccc.api.core.ResourceSummary;
import ccc.api.types.Paragraph;
import ccc.client.actions.FindResourceAction;
import ccc.client.widgets.PageElement;


/**
 * An image chooser on a template field.
 *
 * @author Civic Computing Ltd.
 */
public class CCImageField
    extends
        PageElement<ImageTriggerField> {

    private ImageTriggerField _image;


    /**
     * Constructor.
     *
     * @param name   The field's name.
     * @param title  The field's title.
     * @param desc   The field's description.
     */
    public CCImageField(final String name,
                        final String title,
                        final String desc) {
        super(name);

        final ImageTriggerField image = new ImageTriggerField();
        image.setFieldLabel(createLabel(name, title));
        image.setToolTip(createTooltip(name, title, desc));

        _image = image;
    }


    /** {@inheritDoc} */
    @Override
    public Paragraph getValue() {
        final ImageTriggerField image = _image;
        String id = "";
        final ResourceSummary model = image.getFSModel();
        if (model != null) {
            id = model.getId().toString();
        }

        final Paragraph p =
            Paragraph.fromText(getName(), id);
        return p;
    }


    /** {@inheritDoc} */
    @Override
    public ImageTriggerField getUI() { return _image; }


    /** {@inheritDoc} */
    @Override
    public void setValue(final Paragraph para) {
        // FIXME: Dodgy - just get the FileDTO for the specified Id.
        final ImageTriggerField image = _image;
        final String id = para.getText();
        if (id != null && !id.trim().equals("")) {
            new FindResourceAction(UUID.fromString(id)) {
                @Override
                protected void execute(final ResourceSummary resource) {
                    image.setValue(resource.getAbsolutePath());
                    image.setFSModel(resource);
                }
            }.execute();
        }
    }
}
