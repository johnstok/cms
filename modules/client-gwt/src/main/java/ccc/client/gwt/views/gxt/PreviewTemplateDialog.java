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
package ccc.client.gwt.views.gxt;

import ccc.api.core.Template;
import ccc.api.types.Paragraph;
import ccc.client.core.I18n;
import ccc.client.gwt.core.GlobalsImpl;

import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PreviewTemplateDialog extends AbstractBaseDialog {

    /**
     * Constructor.
     *
     * @param template Template to display.
     */
    public PreviewTemplateDialog(final Template template) {

        super(I18n.UI_CONSTANTS.template(), new GlobalsImpl());
        TabPanel folder = new TabPanel();
        folder.setWidth(450);
        folder.setAutoHeight(true);

        TabItem definition = new TabItem(I18n.UI_CONSTANTS.definitionXML());
        definition.addStyleName("pad-text");
        // TODO add codemirror text area
        definition.addText(Paragraph.escape(template.getDefinition()));
        folder.add(definition);

        TabItem body = new TabItem(I18n.UI_CONSTANTS.body());
        body.addStyleName("pad-text");
        // TODO add codemirror text area
        body.addText(Paragraph.escape(template.getBody()));
        folder.add(body);
        add(folder);
    }

}
