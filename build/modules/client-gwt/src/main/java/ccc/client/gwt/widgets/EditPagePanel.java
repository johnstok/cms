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
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ccc.api.core.ResourceSummary;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.client.core.I18n;
import ccc.client.core.Validatable;
import ccc.client.core.ValidationResult;
import ccc.client.widgets.Option;
import ccc.client.widgets.PageElement;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;


/**
 * Panel containing fields for page content editing.
 *
 * @author Civic Computing Ltd.
 */
public class EditPagePanel
    extends
        FormPanel // TODO: Should extend CCC class
    implements
        Validatable {

    private TextField<String> _name = new TextField<String>();
    private final List<PageElement<? extends Component>> _pageElements =
        new ArrayList<PageElement<? extends Component>>();

    private final String _definition;
    private final ResourceSummary _targetRoot;


    /**
     * Constructor.
     *
     * @param definition The definition for the page.
     * @param targetRoot The root resource containing resources.
     */
    public EditPagePanel(final String definition,
                         final ResourceSummary targetRoot) {
        _definition = definition;
        _targetRoot = targetRoot;

        setLayout(new FormLayout());
        setBorders(false);
        setBodyBorder(false);
        setHeaderVisible(false);
        if (null!=_definition) { createFields(_definition); }
        setScrollMode(Scroll.AUTOY);
    }


    /**
     * Mutator.
     *
     * @param pageName The name of the page.
     */
    public void setName(final ResourceName pageName) {
        _name.setValue(pageName.toString());
        _name.setReadOnly(true);
        _name.disable();
    }


    /**
     * Populates fields for editing.
     *
     * @param paras The current paragraph data.
     */
    public void setValues(final Set<Paragraph> paras) {
        for (final PageElement<? extends Component> c : _pageElements) {
            for (final Paragraph para : paras) {
                if (c.getName().equals(para.getName())) {
                    if (c instanceof CCHtmlField) {
                        final int editorIndex = indexOf(c.getUI());
                        remove(c.getUI());
                        c.setValue(para);
                        insert(c.getUI(), editorIndex, new FormData("95%"));
                    } else {
                        c.setValue(para);
                    }
                }
            }
        }
    }


    /**
     * Accessor of the name field.
     *
     * @return TextField for name.
     */
    public TextField<String> getName() {
        return _name;
    }


    /** {@inheritDoc} */
    @Override
    public ValidationResult getValidationResult() {
        final ValidationResult vResult = new ValidationResult();

        for (final PageElement<? extends Component> c : _pageElements) {
            c.validate(vResult);
        }

        return vResult;
    }


    /**
     * Get the current values for this page.
     *
     * @return A set of paragraphs representing the current page state.
     */
    public Set<Paragraph> getValues() { return extractValues(_pageElements); }


    /**
     * Reads values from the form and stores them to the {@link ParagraphDelta}
     * objects of the paragraphs list.
     *
     * @param definitions List of form elements
     *
     * @return A set of paragraphs representing the content of the supplied
     *  definitions.
     */
    private Set<Paragraph> extractValues(
                     final List<PageElement<? extends Component>> definitions) {
        final Set<Paragraph> paragraphs = new HashSet<Paragraph>();
        Paragraph p = null;

        for (final PageElement<? extends Component> c : definitions) {
            p = c.getValue();
            if (p != null) {
                paragraphs.add(p);
            }
        }
        return paragraphs;
    }


    /**
     * Adds necessary fields for the panel.
     *
     * @param definition XML of the definition.
     */
    private void createFields(final String definition) {
        if (definition == null || definition.trim().equals("")) {
            return;
        }
        addStaticFields();

        final Document def = XMLParser.parse(definition);

        final NodeList fields = def.getElementsByTagName("field");

        for (int i=0; i<fields.getLength(); i++) {

            final Element field        = ((Element) fields.item(i));
            final String type          = field.getAttribute("type");
            final String name          = field.getAttribute("name");
            final String title         = field.getAttribute("title");
            final String desc          = field.getAttribute("description");
            final String regexp        = field.getAttribute("regexp");
            final List<Option> options = parseOptions(field);

            PageElement<? extends Component> pe = null;

            if ("text_field".equals(type)) {
                pe = new CCTextField(name, regexp, title, desc);
            } else if ("text_area".equals(type)) {
                pe = new CCTextAreaField(name, regexp, title, desc);
            } else if ("date".equals(type)) {
                pe = new CCDateField(name, title, desc);
            } else if ("html".equals(type)) {
                pe = new CCHtmlField(name, title, desc);
            } else if ("checkbox".equals(type)) {
                pe = new CCCheckBoxField(name, title, desc, options);
            } else if ("radio".equals(type)) {
                pe = new CCRadioField(name, title, desc, options);
            } else if ("combobox".equals(type)) {
                pe = new CCComboBoxField(name, title, desc, options);
            } else if ("list".equals(type)) {
                pe = new CCListField(name, title, desc, options);
            } else if ("image".equals(type)) {
                pe = new CCImageField(name, title, desc);
            } else if ("resource".equals(type)) {
                pe = new CCResourceField(name, title, desc, _targetRoot);
            } else if ("number".equals(type)) {
                pe = new CCNumberField(name, title, desc);
            }

            if (null!=pe) {
                add(pe.getUI(), new FormData("95%"));
                _pageElements.add(pe);
            }
        }
    }


    private void addStaticFields() {
        _name = new TextField<String>();
        _name.setFieldLabel(I18n.UI_CONSTANTS.name());
        _name.setAllowBlank(false);
        add(_name, new FormData("95%"));
    }


    private List<Option> parseOptions(final Element field) {
        final List<Option> options = new ArrayList<Option>();
        final NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));

            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            options.add(
                new Option(title, value, Boolean.valueOf("true".equals(def))));
        }
        return options;
    }
}
