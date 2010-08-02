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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ccc.api.core.File;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Template;
import ccc.api.types.Link;
import ccc.api.types.MimeType;
import ccc.api.types.Paragraph;
import ccc.api.types.ResourceName;
import ccc.client.core.I18n;
import ccc.client.gwt.binding.DataBinding;
import ccc.client.gwt.core.GWTTemplateEncoder;
import ccc.client.gwt.remoting.GetAbsolutePathAction;
import ccc.client.gwt.widgets.PageElement.FieldType;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
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
public class EditPagePanel extends FormPanel { // TODO: Should extend CCC class

    private static final int LABEL_LENGTH = 13;
    private TextField<String> _name = new TextField<String>();
    private final List<PageElement> _pageElements =
        new ArrayList<PageElement>();
    private final Template _template;


    /**
     * Constructor.
     *
     * @param template The template for the page.
     */
    public EditPagePanel(final Template template) {
        _template = template;
        setLayout(new FormLayout());
        setBorders(false);
        setBodyBorder(false);
        setHeaderVisible(false);
        if (null!=template) { createFields(template.getDefinition()); }
        setScrollMode(Scroll.AUTOY);
    }


    /**
     * Populates fields for editing.
     *
     * @param paras The current paragraph data.
     * @param pageName The name of the page.
     */
    public void populateFields(final Set<Paragraph> paras,
                               final String pageName) {
        _name.setValue(pageName);
        _name.setReadOnly(true);
        _name.disable();

        for (final PageElement c : pageElements()) {
            for (final Paragraph para : paras) {
                if (c.id().equals(para.getName())) {
                    if (FieldType.TEXT == c.fieldType()) {
                        final Field<String> f = c.field();
                        f.setValue(para.getText());
                    } else if (FieldType.DATE == c.fieldType()) {
                        final DateField f = c.dateField();
                        f.setValue(para.getDate());
                    } else if (FieldType.HTML == c.fieldType()) {
                        populateHtml(c, para);
                    } else if (FieldType.CHECKBOX == c.fieldType()) {
                        populateCheckbox(c, para);
                    } else if (FieldType.RADIO == c.fieldType()) {
                        populateRadio(c, para);
                    } else if (FieldType.COMBOBOX == c.fieldType()) {
                        populateComboBox(c, para);
                    }  else if (FieldType.LIST == c.fieldType()) {
                        populateList(c, para);
                    } else if (FieldType.IMAGE == c.fieldType()) {
                        populateImage(c, para);
                    } else if (FieldType.NUMBER == c.fieldType()) {
                        final NumberField f = c.number();
                        f.setValue(para.getNumber());
                    }
                }
            }
        }
    }


    private void populateHtml(final PageElement c, final Paragraph para) {
        final int editorIndex = indexOf(c.editor());
        remove(c.editor());
        final FCKEditor fck =
            new FCKEditor(para.getText(), "250px", c.editor().getToolTip2());
        insert(fck, editorIndex, new FormData("95%"));
        c.editor(fck);
    }


    private void populateCheckbox(final PageElement c,
                                  final Paragraph para) {

        final CheckBoxGroup cbg = c.checkBoxGroup();
        final List<String> valueList = para.getList();

        for (final Field<?> f : cbg.getAll()) {
            if (f instanceof CheckBox) {
                final CheckBox box = (CheckBox) f;
                if (valueList.contains(box.getId())) {
                    box.setValue(Boolean.valueOf(true));
                } else {
                    box.setValue(Boolean.valueOf(false));
                }
            }
        }
    }


    private void populateRadio(final PageElement c, final Paragraph para) {

        final RadioGroup rg = c.radioGroup();
        final String value = para.getText();

        for (final Field<?> f : rg.getAll()) {
            if (f instanceof Radio) {
                final Radio radio = (Radio) f;
                if (radio.getId().equals(value)) {
                    radio.setValue(Boolean.valueOf(true));
                } else {
                    radio.setValue(Boolean.valueOf(false));
                }
            }
        }
    }


    private void populateComboBox(final PageElement c,
                                  final Paragraph para) {

        final ComboBox<BaseModelData> cb = c.combobox();
        final String value = para.getText();

        final ListStore<BaseModelData> store = cb.getStore();
        for (final BaseModelData model : store.getModels()) {
            if (model.get("value").equals(value)) {
                cb.setValue(model);
            }
        }
    }


    private void populateList(final PageElement c, final Paragraph para) {

        final ListField<BaseModelData> list = c.list();
        final List<String> valueList = para.getList();
        final List<BaseModelData> selection =
            new ArrayList<BaseModelData>();

        final ListStore<BaseModelData> items = list.getStore();
        for (final BaseModelData item : items.getModels()) {
            if (valueList.contains(item.get("value"))) {
                selection.add(item);
            }
        }
        list.removeAllListeners();
        // ListField bug/feature - http://extjs.com/forum/showthread.php?t=55659
        list.addListener(Events.Render, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent baseEvent) {
                list.setSelection(selection);
            }
        });
    }


    // FIXME: Dodgy - just get the FileDTO for the specified Id.
    private void populateImage(final PageElement c, final Paragraph para) {

        final ImageTriggerField image = c.image();
        final String id = para.getText();
        if (id != null && !id.trim().equals("")) {
            final ResourceSummary s = new ResourceSummary();
            s.addLink(
                "absolute-path",
                new Link(ccc.api.core.ResourceIdentifiers.Resource.PATH)
                .build("id", id, new GWTTemplateEncoder()));

            new GetAbsolutePathAction(I18n.UI_CONSTANTS.selectImage(), s) {
                @Override protected void execute(final String path) {
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
                }
            }.execute();
        }
    }


    /**
     * Reads values from the form and stores them to the {@link ParagraphDelta}
     * objects of the paragraphs list.
     *
     * @param definitions List of form elements
     * @param paragraphs List of paragraphs
     */
    public void extractValues(final List<PageElement> definitions,
                              final Set<Paragraph> paragraphs) {
        Paragraph p = null;

        for (final PageElement c : definitions) {
            if (FieldType.TEXT == c.fieldType()) {
                p = extractText(c);
            } else if (FieldType.DATE == c.fieldType()) {
                p = extractDate(c);
            } else if (FieldType.HTML == c.fieldType()) {
                p = extractHtml(c);
            } else if (FieldType.CHECKBOX == c.fieldType()) {
                p = extractCheckBox(c);
            } else if (FieldType.RADIO == c.fieldType()) {
                p = extractRadio(c);
            } else if (FieldType.COMBOBOX == c.fieldType()) {
                p = extractComboBox(c);
            } else if (FieldType.LIST == c.fieldType()) {
                p = extractList(c);
            } else if (FieldType.IMAGE == c.fieldType()) {
                p = extractImage(c);
            } else if (FieldType.NUMBER == c.fieldType()) {
                p = extractNumber(c);
            }
            if (p != null) {
                paragraphs.add(p);
            }
        }
    }

    private Paragraph extractNumber(final PageElement c) {
        final NumberField f = c.number();
        if (null==f.getValue()) {
            return null;
        }
        final Paragraph p =
            Paragraph.fromNumber(c.id(), (BigDecimal) f.getValue());
        return p;
    }

    private Paragraph extractImage(final PageElement c) {
        final ImageTriggerField image = c.image();
        String id = "";
        final BeanModel model = image.getFSModel();
        if (model != null) {
            id = model.<File>getBean().getId().toString();
        }

        final Paragraph p =
            Paragraph.fromText(c.id(), id);
        return p;
    }


    private Paragraph extractList(final PageElement c) {
        final ListField<BaseModelData> list = c.list();
        final List<String> strings = new ArrayList<String>();

        for (final BaseModelData item : list.getSelection()) {
            strings.add((String) item.get("value"));
        }

        return Paragraph.fromList(c.id(), strings);
    }


    private Paragraph extractComboBox(final PageElement c) {
        final ComboBox<BaseModelData> cb = c.combobox();
        String selected = "";
        if (cb.getValue() != null) {
            selected = cb.getValue().get("value");
        }

        final Paragraph p =
            Paragraph.fromText(c.id(), selected);
        return p;
    }


    private Paragraph extractRadio(final PageElement c) {
        final RadioGroup rg = c.radioGroup();
        String selected = "";
        if (rg.getValue() != null) {
            selected = rg.getValue().getId();
        }

        final Paragraph p =
            Paragraph.fromText(c.id(), selected);
        return p;
    }


    private Paragraph extractCheckBox(final PageElement c) {
        final CheckBoxGroup cbg = c.checkBoxGroup();
        final List<String> strings = new ArrayList<String>();

        for (final CheckBox cb : cbg.getValues()) {
            strings.add(cb.getId());
        }

        return Paragraph.fromList(c.id(), strings);
    }


    private Paragraph extractHtml(final PageElement c) {
        final FCKEditor f = c.editor();
        final Paragraph p =
            Paragraph.fromText(c.id(), f.getHTML());
        return p;
    }


    private Paragraph extractDate(final PageElement c) {
        final DateField f = c.dateField();
        if (null==f.getValue()) {
            return null;
        }
        final Paragraph p =
            Paragraph.fromDate(c.id(), f.getValue());
        return p;
    }


    private Paragraph extractText(final PageElement c) {
        final Field<String> f = c.field();
        final Paragraph p =
            Paragraph.fromText(c.id(), f.getValue());
        return p;
    }


    /**
     * Accessor of the name field.
     *
     * @return TextField for name.
     */
    public TextField<String> name() {
        return _name;
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
    public Template template() {
        return _template;
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
            final Element field = ((Element) fields.item(i));
            final String type   = field.getAttribute("type");
            final String name   = field.getAttribute("name");
            final String title  = field.getAttribute("title");
            final String desc   = field.getAttribute("description");
            final String regexp = field.getAttribute("regexp");

            if ("text_field".equals(type)) {
                addElementForTextField(name, regexp, title, desc);
            } else if ("text_area".equals(type)) {
                addElementForTextArea(name, regexp, title, desc);
            } else if ("date".equals(type)) {
                addElementForDate(name, title, desc);
            } else if ("html".equals(type)) {
                addElementForHtml(name, title, desc);
            } else if ("checkbox".equals(type)) {
                addElementForCheckbox(name, field, title, desc);
            } else if ("radio".equals(type)) {
                addElementForRadio(name, field, title, desc);
            } else if ("combobox".equals(type)) {
                addElementForCombobox(name, field, title, desc);
            } else if ("list".equals(type)) {
                addElementForList(name, field, title, desc);
            } else if ("image".equals(type)) {
                addElementForImage(name, title, desc);
            } else if ("number".equals(type)) {
                addElementForNumber(name, title, desc);
            }
        }
    }


    private void addElementForNumber(final String name,
                                     final String title,
                                     final String desc) {
        final NumberField nf = new NumberField();
        nf.setPropertyEditor(new BigDecimalPropertyEditor(BigDecimal.class));
        nf.setFieldLabel(createLabel(name, title));
        nf.setData("type", FieldType.NUMBER);
        nf.setToolTip(createTooltip(name, title, desc));

        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.NUMBER);
        pe.number(nf);
        add(nf, new FormData("95%"));
        _pageElements.add(pe);
    }


    /**
     * Adds {@link ImageTriggerField} to the panel and to _pageElements list.
     *
     * @param name The name of the field.
     * @param title The field's title.
     * @param desc The field's description.
     */
    private void addElementForImage(final String name,
                                    final String title,
                                    final String desc) {
        final ImageTriggerField image =
            new ImageTriggerField();
        image.setFieldLabel(createLabel(name, title));
        image.setToolTip(createTooltip(name, title, desc));
        image.setData("type", FieldType.IMAGE);

        add(image, new FormData("95%"));

        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.IMAGE);
        pe.image(image);
        _pageElements.add(pe);
    }


    /**
     * Adds {@link ListField} to the panel and to _pageElements list.
     *
     * @param name The name of the field.
     * @param field The XML of field options.
     * @param title2 The field's title.
     * @param desc The field's description.
     */
    private void addElementForList(final String name,
                                   final Element field,
                                   final String title2,
                                   final String desc) {
        final ListField<BaseModelData> list = new ListField<BaseModelData>();
        list.setFieldLabel(createLabel(name, title2));
        list.setToolTip(createTooltip(name, title2, desc));
        list.setData("type", FieldType.LIST);
        list.setDisplayField("title");
        list.setValueField("value");

        final ListStore<BaseModelData> store =  new ListStore<BaseModelData>();
        final List<BaseModelData> selection = new ArrayList<BaseModelData>();

        final NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));
            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            final BaseModelData model = new BaseModelData();
            model.set("title", title);
            model.set("value", value);
            store.add(model);
            if ("true".equals(def)) {
                selection.add(model);
            }
        }
        list.setStore(store);
        add(list, new FormData("95%"));
        // ListField bug/feature - http://extjs.com/forum/showthread.php?t=55659
        list.addListener(Events.Render, new Listener<BaseEvent>() {
            public void handleEvent(final BaseEvent baseEvent) {
                list.setSelection(selection);
            }
        });

        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.LIST);
        pe.list(list);
        _pageElements.add(pe);

    }


    /**
     * Adds {@link ComboBox} to the panel and to _pageElements list.
     *
     * @param name The name of the field.
     * @param field The XML of field options.
     * @param title2 The field's title.
     * @param desc The field's description.
     */
    private void addElementForCombobox(final String name,
                                       final Element field,
                                       final String title2,
                                       final String desc) {
        final ComboBox<BaseModelData> cb = new ComboBox<BaseModelData>();
        cb.setTriggerAction(TriggerAction.ALL);
        cb.setFieldLabel(createLabel(name, title2));
        cb.setToolTip(createTooltip(name, title2, desc));
        cb.setData("type", FieldType.COMBOBOX);
        cb.setDisplayField("title");
        cb.setValueField("value");
        cb.setEditable(false);

        final ListStore<BaseModelData> store =  new ListStore<BaseModelData>();
        final NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));
            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            final BaseModelData model = new BaseModelData();
            model.set("title", title);
            model.set("value", value);
            store.add(model);
            if ("true".equals(def)) {
                cb.setValue(model);
            }
        }
        cb.setStore(store);
        add(cb, new FormData("95%"));

        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.COMBOBOX);
        pe.combobox(cb);
        _pageElements.add(pe);
    }


    /**
     * Adds {@link CheckBoxGroup} to the panel and to _pageElements list.
     *
     * @param name The name of the field.
     * @param field The XML of field options.
     * @param title2 The field's title.
     * @param desc The field's description.
     */
    private void addElementForCheckbox(final String name,
                                       final Element field,
                                       final String title2,
                                       final String desc) {
        final CheckBoxGroup cbg =  new  CheckBoxGroup();
        cbg.setFieldLabel(createLabel(name, title2));
        cbg.setToolTip(createTooltip(name, title2, desc));
        cbg.setData("type", FieldType.CHECKBOX);
        cbg.setOrientation(Orientation.VERTICAL);
        cbg.setStyleAttribute("overflow", "hidden");

        final NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));
            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            final CheckBox cb = new CheckBox();
            cb.setBoxLabel(title);
            cb.setId(value);
            cb.setValue(Boolean.valueOf("true".equals(def)));

            cbg.add(cb);
        }
        add(cbg, new FormData("95%"));

        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.CHECKBOX);
        pe.checkBoxGroup(cbg);
        _pageElements.add(pe);
    }


    /**
     * Adds {@link RadioGroup} to the panel and to _pageElements list.
     *
     * @param name The name of the field.
     * @param field The XML of field options.
     * @param title2 The field's title.
     * @param desc The field's description.
     */
    private void addElementForRadio(final String name,
                                    final Element field,
                                    final String title2,
                                    final String desc) {
        final RadioGroup rg = new RadioGroup();
        rg.setFieldLabel(createLabel(name, title2));
        rg.setToolTip(createTooltip(name, title2, desc));
        rg.setData("type", FieldType.RADIO);
        rg.setOrientation(Orientation.VERTICAL);
        rg.setStyleAttribute("overflow", "hidden");

        final NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));
            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            final Radio r = new Radio();
            r.setBoxLabel(title);
            r.setId(value);
            r.setValue(Boolean.valueOf("true".equals(def)));

            rg.add(r);
        }
        add(rg, new FormData("95%"));

        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.RADIO);
        pe.radioGroup(rg);
        _pageElements.add(pe);
    }


    /**
     * Adds FCKEditor to the panel and to _pageElements list.
     *
     * @param name The name of the field.
     * @param title The field's title.
     * @param desc The field's description.
     */
    private void addElementForHtml(final String name,
                                   final String title,
                                   final String desc) {

        final Text fieldName = new Text(createLabel(name, title)+":");
        fieldName.setStyleName("x-form-item");
        add(fieldName);
        final FCKEditor fck =
            new FCKEditor("", "250px", createTooltip(name, title, desc));
        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.HTML);
        pe.editorLabel(fieldName);
        pe.editor(fck);
        add(fck, new FormData("95%"));
        _pageElements.add(pe);
    }


    /**
     * Adds {@link DateField} to the panel and to _pageElements list.
     *
     * @param name The name of the field.
     * @param title The field's title.
     * @param desc The field's description.
     */
    private void addElementForDate(final String name,
                                   final String title,
                                   final String desc) {

        final DateField df = new DateField();
        df.setFieldLabel(createLabel(name, title));
        df.setData("type", FieldType.DATE);
        df.setEditable(false);
        df.setToolTip(createTooltip(name, title, desc));

        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.DATE);
        pe.dateField(df);
        add(df, new FormData("95%"));
        _pageElements.add(pe);
    }


    /**
     * Adds {@link TextArea} to the panel and to _pageElements list.
     *
     * @param name The name of the field.
     * @param regexp The regexp for field validation.
     * @param title The field's title.
     * @param desc The field's description.
     */
    private void addElementForTextArea(final String name,
                                       final String regexp,
                                       final String title,
                                       final String desc) {

        final TextArea ta = new TextArea();
        ta.setData("type", FieldType.TEXT);
        ta.setFieldLabel(createLabel(name, title));
        ta.setToolTip(createTooltip(name, title, desc));
        if (regexp != null) {
            ta.setRegex(regexp);
        }
        add(ta, new FormData("95%"));
        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.TEXT);
        pe.field(ta);
        _pageElements.add(pe);
    }


    /**
     * Adds {@link TextField} to the panel and to _pageElements list.
     *
     * @param name The name of the field.
     * @param regexp The regexp for field validation.
     * @param title The field's title.
     * @param desc The field's description.
     */
    private void addElementForTextField(final String name,
                                        final String regexp,
                                        final String title,
                                        final String desc) {

        final TextField<String> tf = new TextField<String>();
        tf.setData("type", FieldType.TEXT);

        tf.setFieldLabel(createLabel(name, title));
        tf.setToolTip(createTooltip(name, title, desc));
        if (regexp != null) {
            tf.setRegex(regexp);
        }
        add(tf, new FormData("95%"));
        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.TEXT);
        pe.field(tf);

        _pageElements.add(pe);
    }


    /**
     * Shortens label string.
     *
     * @param name The original string of the label.
     * @param title The field's title.
     * @return Label - shortened if necessary.
     */
    private String createLabel(final String name, final String title) {
        final String label =
            (null!=title && title.trim().length()>0) ? title : name;
        if (label.length() > LABEL_LENGTH) {
            return label.substring(0, LABEL_LENGTH) + "...";
        }
        return label;
    }


    private String createTooltip(final String name,
                                 final String title,
                                 final String description) {
        final String label =
            (null!=title && title.trim().length()>0) ? title : name;
        return
            "<b>"+label+"</b><br>"
            + ((null==description) ? "" : description);
    }


    private void addStaticFields() {
        _name = new TextField<String>();
        _name.setFieldLabel(I18n.UI_CONSTANTS.name());
        _name.setAllowBlank(false);
        add(_name, new FormData("95%"));
    }
}
