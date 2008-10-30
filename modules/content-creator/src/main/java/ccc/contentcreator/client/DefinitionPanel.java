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
package ccc.contentcreator.client;

import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class DefinitionPanel
    extends
        LayoutContainer {

    public DefinitionPanel() {
        setLayout(new FormLayout());
    }

    public void renderFields(final String definition) {
        removeAll();
        String xml =
            "<fields><field name=\"foo\" type=\"text_area\"/><field name=\"foo2\" type=\"text_field\"/></fields>";

        if (definition != null) {
            xml = definition;
        }

        final Document def = XMLParser.parse(xml);

        final NodeList fields = def.getElementsByTagName("field");
        for (int i=0; i<fields.getLength(); i++) {
            final Element field = ((Element) fields.item(i));
            final String type = field.getAttribute("type");
            final String name = field.getAttribute("name");
            if ("text_field".equals(type)) {
                final TextField<String> tf = new TextField<String>();
                tf.setId(name);
                tf.setFieldLabel(name);
                add(tf);
            } else if ("text_area".equals(type)) {
                final TextArea ta = new TextArea();
                ta.setId(name);
                ta.setFieldLabel(name);
                add(ta);

            } else if ("date".equals(type)) {
                final DateField df = new DateField();
                df.setId(name);
                df.setFieldLabel(name);
                add(df);
            }
        }
    }
}
