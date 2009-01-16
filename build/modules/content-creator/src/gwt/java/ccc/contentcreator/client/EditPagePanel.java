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

import java.util.ArrayList;
import java.util.List;

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.client.ui.FCKEditor;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;

import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Text;
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
public class EditPagePanel extends FormPanel { // TODO: Should extend CCC class
    private TextField<String> _title = new TextField<String>();
    private TextField<String> _name = new TextField<String>();
    private List<PageElement> _pageElements = new ArrayList<PageElement>();
    private String _definition;

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
     * @param resourceSummary PageDTO of the original page.
     */
    public void populateFields(final PageDelta resourceSummary) {
        _name.setValue(resourceSummary._name);
        _name.setReadOnly(true);
        _name.disable();
        _title.setValue(resourceSummary._title);

        for (final ParagraphDelta para : resourceSummary._paragraphs) {
            for (final PageElement c : pageElements()) {
                if (c.id().equals(para._name)) {
                    if ("TEXT".equals(c.type())) {
                        final Field<String> f = c.field();
                        f.setValue(para._textValue);
                    } else if ("DATE".equals(c.type())) {
                        final DateField f = c.dateField();
                        f.setValue(para._dateValue);
                    } else if ("HTML".equals(c.type())) {
                        remove(c.editor());
                        final FCKEditor fck = new FCKEditor(para._textValue,
                                                            "",
                                                            "595px",
                                                            "250px");
                        add(fck);
                        c.editor(fck);
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
     * Accessor for the page elements.
     *
     * @return Elements of the page
     */
    public List<PageElement> pageElements() {
        return _pageElements;
    }

    /**
     * Accessor for the definition of page's template.
     *
     * @return The definition
     */
    public String definition() {
        return _definition;
    }

    /**
     * Adds necessary fields for the panel.
     *
     * @param definition XML of the definition.
     */
    public void createFields(final String definition) {
        _definition = definition;
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
            final String regexp = field.getAttribute("regexp");

            if ("text_field".equals(type)) {
                final TextField<String> tf = new TextField<String>();
                tf.setData("type", "TEXT");
                tf.setId(name);
                tf.setFieldLabel(name);
                if (regexp != null) {
                    tf.setRegex(regexp);
                }
                add(tf);
                final PageElement pe = new PageElement(name);
                pe.type("TEXT");
                pe.field(tf);

                _pageElements.add(pe);

            } else if ("text_area".equals(type)) {
                final TextArea ta = new TextArea();
                ta.setData("type", "TEXT");
                ta.setId(name);
                ta.setFieldLabel(name);
                if (regexp != null) {
                    ta.setRegex(regexp);
                }
                add(ta);
                final PageElement pe = new PageElement(name);
                pe.type("TEXT");
                pe.field(ta);
                _pageElements.add(pe);

            } else if ("date".equals(type)) {
                final DateField df = new DateField();
                df.setFieldLabel(name);
                df.setData("type", "DATE");
                df.setId(name);

                final PageElement pe = new PageElement(name);
                pe.type("DATE");
                pe.dateField(df);
                add(df);
                _pageElements.add(pe);
            } else if ("html".equals(type)) {
                final Text fieldName = new Text(name);
                add(fieldName);
                final FCKEditor fck = new FCKEditor("", "", "595px", "250px");
                final PageElement pe = new PageElement(name);
                pe.type("HTML");
                pe.editor(fck);
                add(fck);
                _pageElements.add(pe);
            }
        }
    }
}
