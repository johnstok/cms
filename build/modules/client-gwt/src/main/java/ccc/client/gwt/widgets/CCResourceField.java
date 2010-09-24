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
 * A resource chooser on a template field.
 *
 * @author Civic Computing Ltd.
 */
public class CCResourceField
    extends
        PageElement<ResourceTriggerField> {

    private final ResourceTriggerField _uiControl;


    /**
     * Constructor.
     *
     * @param name       The field's name.
     * @param title      The field's title.
     * @param desc       The field's description.
     * @param targetRoot The root resource containing resources.
     */
    public CCResourceField(final String name,
                           final String title,
                           final String desc,
                           final ResourceSummary targetRoot) {
        super(name);

        _uiControl = new ResourceTriggerField(targetRoot);
        _uiControl.setFieldLabel(createLabel(name, title));
        _uiControl.setToolTip(createTooltip(name, title, desc));
    }


    /** {@inheritDoc} */
    @Override
    public Paragraph getValue() {
        final ResourceSummary target = _uiControl.getTarget();
        final String id =
            (null!=target)
                ? target.getId().toString()
                : "";
        return Paragraph.fromText(getName(), id);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceTriggerField getUI() { return _uiControl; }


    /** {@inheritDoc} */
    @Override
    public void setValue(final Paragraph para) {
        final String id = para.getText();
        if (id != null && !id.trim().equals("")) {
            new FindResourceAction(UUID.fromString(id)) {
                @Override
                protected void execute(final ResourceSummary resource) {
                    _uiControl.setTarget(resource);
                }
            }.execute();
        }
    }
}
