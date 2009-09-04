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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ccc.contentcreator.actions.GetAbsolutePathAction;
import ccc.contentcreator.binding.FileSummaryModelData;
import ccc.contentcreator.client.PageElement.FieldType;
import ccc.contentcreator.dialogs.ImageChooserDialog;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.PageDelta;
import ccc.types.Paragraph;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
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
    private String _definition;
    private final IGlobals _globals = new IGlobalsImpl();
    private int _fckCount = 0;

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

    /**
     * Return number of FCK editor instances.
     *
     * @return Number of instances.
     */
    public int getFCKCount() {
        return _fckCount;
    }



    /**
     * Populates fields for editing.
     *
     * @param resourceSummary PageDTO of the original page.
     * @param pageName The name of the page.
     */
    public void populateFields(final PageDelta resourceSummary,
                               final String pageName) {
        _name.setValue(pageName);
        _name.setReadOnly(true);
        _name.disable();

        for (final PageElement c : pageElements()) {
            for (final Paragraph para : resourceSummary.getParagraphs()) {
                if (c.id().equals(para.name())) {
                    if (FieldType.TEXT == c.fieldType()) {
                        final Field<String> f = c.field();
                        f.setValue(para.text());
                    } else if (FieldType.DATE == c.fieldType()) {
                        final DateField f = c.dateField();
                        f.setValue(para.date());
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
                        f.setValue(para.number());
                    }
                }
            }
        }
    }

    private void populateHtml(final PageElement c, final Paragraph para) {
        final int editorIndex = indexOf(c.editor());
        remove(c.editor());
        final FCKEditor fck =
            new FCKEditor(para.text(), "250px", _globals);
        insert(fck, editorIndex, new FormData("95%"));
        c.editor(fck);
    }

    private void populateCheckbox(final PageElement c,
                                  final Paragraph para) {

        final CheckBoxGroup cbg = c.checkBoxGroup();
        final List<String> valueList = para.list();

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
        final String value = para.text();

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
        final String value = para.text();

        final ListStore<BaseModelData> store = cb.getStore();
        for (final BaseModelData model : store.getModels()) {
            if (model.get("value").equals(value)) {
                cb.setValue(model);
            }
        }
    }

    private void populateList(final PageElement c, final Paragraph para) {

        final ListField<BaseModelData> list = c.list();
        final List<String> valueList = para.list();
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

    private void populateImage(final PageElement c, final Paragraph para) {

        final ImageTriggerField image = c.image();
        final String id = para.text();
        if (id != null && !id.trim().equals("")) {
            final UUID resourceId = UUID.fromString(id);

            new GetAbsolutePathAction(_globals.uiConstants().selectImage(),
                                      resourceId) {
                @Override protected void execute(final String path) {
                    final FileDto fs =
                        new FileDto("image", path, resourceId, "", "");
                    final FileSummaryModelData model =
                        new FileSummaryModelData(fs);
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
        final FileSummaryModelData model = image.getFSModel();
        if (model != null) {
            id = model.getId().toString();
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
        _pageElements.clear();
        removeAll();
        if (definition == null || definition.trim().equals("")) {
            return;
        }
        addStaticFields();

        final Document def = XMLParser.parse(definition);

        final NodeList fields = def.getElementsByTagName("field");
        for (int i=0; i<fields.getLength(); i++) {
            final Element field = ((Element) fields.item(i));
            final String type = field.getAttribute("type");
            final String name = field.getAttribute("name");
            final String regexp = field.getAttribute("regexp");

            if ("text_field".equals(type)) {
                addElementForTextField(name, regexp);
            } else if ("text_area".equals(type)) {
                addElementForTextArea(name, regexp);
            } else if ("date".equals(type)) {
                addElementForDate(name);
            } else if ("html".equals(type)) {
                addElementForHtml(name);
            } else if ("checkbox".equals(type)) {
                addElementForCheckbox(name, field);
            } else if ("radio".equals(type)) {
                addElementForRadio(name, field);
            } else if ("combobox".equals(type)) {
                addElementForCombobox(name, field);
            } else if ("list".equals(type)) {
                addElementForList(name, field);
            } else if ("image".equals(type)) {
                addElementForImage(name);
            } else if ("number".equals(type)) {
                addElementForNumber(name);
            }
        }
    }

    private void addElementForNumber(final String name) {
        final NumberField nf = new NumberField();
        nf.setPropertyEditor(new BigDecimalPropertyEditor(BigDecimal.class));
        nf.setFieldLabel(createLabel(name));
        nf.setData("type", FieldType.NUMBER);
        nf.setId(name);

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
     */
    private void addElementForImage(final String name) {
        final ImageTriggerField image =
            new ImageTriggerField();
        image.setFieldLabel(createLabel(name));
        image.setToolTip(name);
        image.setData("type", FieldType.IMAGE);
        image.setId(name);
        image.setReadOnly(true);

        image.addListener(
            Events.TriggerClick,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent be) {
                    final ImageChooserDialog imageChooser =
                        new ImageChooserDialog(image);
                    imageChooser.show();
                }});

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
     */
    private void addElementForList(final String name, final Element field) {
        final ListField<BaseModelData> list = new ListField<BaseModelData>();
        list.setFieldLabel(createLabel(name));
        list.setToolTip(name);
        list.setData("type", FieldType.LIST);
        list.setDisplayField("title");
        list.setValueField("value");
        list.setId(name);

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
     */
    private void addElementForCombobox(final String name, final Element field) {
        final ComboBox<BaseModelData> cb = new ComboBox<BaseModelData>();
        cb.setFieldLabel(createLabel(name));
        cb.setToolTip(name);
        cb.setData("type", FieldType.COMBOBOX);
        cb.setDisplayField("title");
        cb.setValueField("value");
        cb.setId(name);
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
     */
    private void addElementForCheckbox(final String name, final Element field) {
        final CheckBoxGroup cbg =  new  CheckBoxGroup();
        cbg.setFieldLabel(createLabel(name));
        cbg.setToolTip(name);
        cbg.setData("type", FieldType.CHECKBOX);
        cbg.setId(name);
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
     */
    private void addElementForRadio(final String name, final Element field) {
        final RadioGroup rg = new RadioGroup();
        rg.setFieldLabel(createLabel(name));
        rg.setToolTip(name);
        rg.setData("type", FieldType.RADIO);
        rg.setId(name);
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
     */
    private void addElementForHtml(final String name) {

        final Text fieldName = new Text(name+":");
        fieldName.setStyleName("x-form-item");
        add(fieldName);
        final FCKEditor fck = new FCKEditor("", "250px", _globals);
        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.HTML);
        pe.editorLabel(fieldName);
        pe.editor(fck);
        add(fck, new FormData("95%"));
        _pageElements.add(pe);
        _fckCount++;
    }

    /**
     * Adds {@link DateField} to the panel and to _pageElements list.
     *
     * @param name The name of the field.
     */
    private void addElementForDate(final String name) {

        final DateField df = new DateField();
        df.setFieldLabel(createLabel(name));
        df.setData("type", FieldType.DATE);
        df.setId(name);
        df.setEditable(false);

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
     */
    private void addElementForTextArea(final String name, final String regexp) {

        final TextArea ta = new TextArea();
        ta.setData("type", FieldType.TEXT);
        ta.setId(name);
        ta.setFieldLabel(createLabel(name));
        ta.setToolTip(name);
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
     */
    private void addElementForTextField(final String name,
                                        final String regexp) {

        final TextField<String> tf = new TextField<String>();
        tf.setData("type", FieldType.TEXT);
        tf.setId(name);

        tf.setFieldLabel(createLabel(name));
        tf.setToolTip(name);
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
     * @return Label - shortened if necessary.
     */
    private String createLabel(final String name) {
        if (name.length() > LABEL_LENGTH) {
            return name.substring(0, LABEL_LENGTH) + "...";
        }
        return name;
    }

    private void addStaticFields() {

        _name = new TextField<String>();
        _name.setFieldLabel(_globals.uiConstants().name());
        _name.setAllowBlank(false);
        _name.setId(_globals.uiConstants().name());
        add(_name, new FormData("95%"));
    }
}
