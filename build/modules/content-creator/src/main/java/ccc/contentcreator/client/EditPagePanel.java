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

import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.dto.PageDTO;
import ccc.contentcreator.dto.ParagraphDTO;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
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
public class EditPagePanel extends FormPanel {
    private TextField<String> _title = new TextField<String>();
    private TextField<String> _name = new TextField<String>();

    /** _constants : UIConstants. */
    private final UIConstants _constants = Globals.uiConstants();

    /**
     * Constructor.
     *
     */
    public EditPagePanel() {
        super();
        setLayout(new FormLayout());
        setBorders(false);
        setBodyBorder(false);
        setHeaderVisible(false);

    }

    private void drawStaticFields() {
        _name = new TextField<String>();
        _name.setFieldLabel(_constants.name());
        _name.setAllowBlank(false);
        _name.setId(_constants.name());
        add(_name, new FormData("95%"));

        _title = new TextField<String>();
        _title.setFieldLabel(_constants.title());
        _title.setAllowBlank(false);
        _title.setId(_constants.title());
        add(_title, new FormData("95%"));
    }

    /**
     * Populates fields for editing.
     *
     * @param page PageDTO of the original page.
     */
    public void populateFields(final PageDTO page) {
        _name.setValue(page.getName());
        _name.setReadOnly(true);
        _name.disable();
        _title.setValue(page.getTitle());

        for (final Entry<String, ParagraphDTO> para : page.getParagraphs().entrySet()) {

            for (final Component c : getItems()) {
                if (c.getId().equals(para.getKey())) {
                    final String value = para.getValue().getValue();
                    if ("TEXT".equals(c.getData("type"))) {
                        final Field<String> f = (Field<String>) c;
                        f.setValue(value);
                    } else if ("DATE".equals(c.getData("type"))) {
                        final DateField f = (DateField) c;
                        final Date d = new Date(new Long(value));
                        f.setValue(d);
                    }
                }
            }
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return TextField for name.
     */
    public TextField<String> name() {
        return _name;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @return TextField for title.
     */
    public TextField<String> title() {
        return _title;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @return List of components in definition panel.
     */
    public List<Component> definitionItems() {
        return getItems();
    }

    /**
     * Adds necessary fields for the panel.
     *
     * @param definition XML of the definition.
     */
    public void createFields(final String definition) {
        removeAll();
        if (definition == null || definition.trim().equals("")) {
            return;
        }
        drawStaticFields();

        final Document def = XMLParser.parse(definition);

        final NodeList fields = def.getElementsByTagName("field");
        for (int i=0; i<fields.getLength(); i++) {
            final Element field = ((Element) fields.item(i));
            final String type = field.getAttribute("type");
            final String name = field.getAttribute("name");

            if ("text_field".equals(type)) {
                final TextField<String> tf = new TextField<String>();
                tf.setData("type", "TEXT");
                tf.setId(name);
                tf.setFieldLabel(name);
                add(tf);
            } else if ("text_area".equals(type)) {
                final TextArea ta = new TextArea();
                ta.setData("type", "TEXT");
                ta.setId(name);
                ta.setFieldLabel(name);
                add(ta);

            } else if ("date".equals(type)) {
                final DateField df = new DateField();
                df.setFieldLabel(name);
                df.setData("type", "DATE");
                df.setId(name);
                add(df);
            }
        }
    }
}
