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

import java.util.HashMap;
import java.util.UUID;

import ccc.api.core.File;
import ccc.api.core.ResourceSummary;
import ccc.api.types.Link;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.client.actions.GetAbsolutePathAction;
import ccc.client.core.DefaultCallback;
import ccc.client.core.I18n;
import ccc.client.core.InternalServices;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.widgets.PageElement;

import com.extjs.gxt.ui.client.data.BeanModel;


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
        final BeanModel model = image.getFSModel();
        if (model != null) {
            id = model.<File>getBean().getId().toString();
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
            final ResourceSummary s = new ResourceSummary();
            s.addLink(
                "absolute-path",
                new Link(ccc.api.synchronous.ResourceIdentifiers.Resource.PATH)
                .build("id", id, InternalServices.ENCODER));

            new GetAbsolutePathAction(I18n.UI_CONSTANTS.selectImage(), s)
            .execute(
                new DefaultCallback<String>(I18n.UI_CONSTANTS.selectImage()) {
                    @Override public void onSuccess(final String path) {
                        final File fs = new File(
                            new MimeType("image", "*"),
                            path,
                            UUID.fromString(id),
                            new ResourceName("img"),
                            "",
                            new HashMap<String, String>());
                        final BeanModel model = DataBinding.bindFileSummary(fs);
                        image.setValue(path);
                        image.setFSModel(model);
                    }});
        }
    }
}
