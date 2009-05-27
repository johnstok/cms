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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ccc.api.FileSummary;
import ccc.api.ID;
import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.FileSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.PageElement.FieldType;
import ccc.contentcreator.client.ui.FCKEditor;
import ccc.contentcreator.dialogs.ImageChooserDialog;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.core.client.GWT;
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
    private final List<PageElement> _pageElements =
        new ArrayList<PageElement>();
    private String _definition;

    // used to retrieve FileSummary for UUID
    private final QueriesServiceAsync _qs = GWT.create(QueriesService.class);

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
        _title.setValue(resourceSummary.getTitle());

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
                    }
                }
            }
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @param para
     */
    private void populateHtml(final PageElement c, final Paragraph para) {

        remove(c.editor());
        remove(c.editorLabel());
        final FCKEditor fck =
            new FCKEditor(para.text(), "250px");
        add(c.editorLabel());
        add(fck, new FormData("95%"));
        c.editor(fck);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @param para
     */
    private void populateCheckbox(final PageElement c,
                                  final Paragraph para) {

        final CheckBoxGroup cbg = c.checkBoxGroup();
        final Map<String, String> valueMap = fillValueMap(para);

        final List<CheckBox> boxes = cbg.getAll();
        for (final CheckBox box : boxes) {
            if ("true".equals(valueMap.get(box.getId()))) {
                box.setValue(true);
            } else {
                box.setValue(false);
            }
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @param para
     */
    private void populateRadio(final PageElement c, final Paragraph para) {

        final RadioGroup rg = c.radioGroup();
        final String value = para.text();

        final List<Radio> radios = rg.getAll();
        for (final Radio radio : radios) {
            if (radio.getId().equals(value)) {
                radio.setValue(true);
            } else {
                radio.setValue(false);
            }
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @param para
     */
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

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @param para
     */
    private void populateList(final PageElement c, final Paragraph para) {

        final ListField<BaseModelData> list = c.list();
        final Map<String, String> valueMap = fillValueMap(para);
        final List<BaseModelData> selection =
            new ArrayList<BaseModelData>();

        final ListStore<BaseModelData> items = list.getStore();
        for (final BaseModelData item : items.getModels()) {
            if (valueMap.containsKey(item.get("value"))) {
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

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @param para
     */
    private void populateImage(final PageElement c, final Paragraph para) {

        final ImageTriggerField image = c.image();
        final String id = para.text();
        if (id != null && !id.trim().equals("")) {
            final ID resourceId = new ID(id);
            _qs.getAbsolutePath(resourceId,
                new ErrorReportingCallback<String>(_constants.updateContent()) { // FIXME: Could also be 'create page'.

                @Override
                public void onSuccess(final String path) {
                    final FileSummary fs =
                        new FileSummary("image", path, resourceId, "", "");
                    final FileSummaryModelData model =
                        new FileSummaryModelData(fs);
                    image.setValue(path);
                    image.setFSModel(model);
                }
            });
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param para
     * @return
     */
    private Map<String, String> fillValueMap(final Paragraph para) {

        final String text = para.text();

        final Map<String, String> valueMap = new HashMap<String, String>();

        final String[] lines = text.split("\n");
        for (final String line : lines) {
            if (line.trim().length() > 0) {
                final String key = line.substring(0, line.indexOf("="));
                final String value = line.substring(line.indexOf("=")+1);
                valueMap.put(key, value);
            }
        }
        return valueMap;
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
            }
            if (p != null) {
                paragraphs.add(p);
            }
        }
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
        final StringBuilder sb = new StringBuilder();
        for (final BaseModelData item : list.getSelection()) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(item.get("value"));
            sb.append("=");
            sb.append(item.get("title"));
        }

        final Paragraph p =
            Paragraph.fromText(c.id(), sb.toString());
        return p;
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
        final StringBuilder sb = new StringBuilder();
        for (final CheckBox cb : cbg.getAll()) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(cb.getId());
            sb.append("=");
            sb.append(cb.getValue().toString());
        }

        final Paragraph p =
            Paragraph.fromText(c.id(), sb.toString());
        return p;
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
                addElementForImage(name, field);
            }
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param name
     * @param field
     */
    private void addElementForImage(final String name, final Element field) {
        final ImageTriggerField image =
            new ImageTriggerField();
        image.setFieldLabel(name);
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
     * TODO: Add a description of this method.
     *
     * @param name
     * @param field
     */
    private void addElementForList(final String name, final Element field) {
        final ListField<BaseModelData> list = new ListField<BaseModelData>();
        list.setFieldLabel(name);
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
     * TODO: Add a description of this method.
     *
     * @param name
     * @param field
     */
    private void addElementForCombobox(final String name, final Element field) {
        final ComboBox<BaseModelData> cb = new ComboBox<BaseModelData>();
        cb.setFieldLabel(name);
        cb.setData("type", FieldType.COMBOBOX);
        cb.setDisplayField("title");
        cb.setValueField("value");
        cb.setId(name);

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
     * TODO: Add a description of this method.
     *
     * @param name
     * @param field
     */
    private void addElementForCheckbox(final String name, final Element field) {
        final CheckBoxGroup cbg =  new  CheckBoxGroup();
        cbg.setFieldLabel(name);
        cbg.setData("type", FieldType.CHECKBOX);
        cbg.setId(name);
        cbg.setOrientation(Orientation.VERTICAL);

        final NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));
            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            final CheckBox cb = new CheckBox();
            cb.setBoxLabel(title);
            cb.setId(value);
            cb.setValue("true".equals(def));

            cbg.add(cb);
        }
        add(cbg, new FormData("95%"));

        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.CHECKBOX);
        pe.checkBoxGroup(cbg);
        _pageElements.add(pe);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param name
     * @param field
     */
    private void addElementForRadio(final String name, final Element field) {
        final RadioGroup rg =  new  RadioGroup();
        rg.setFieldLabel(name);
        rg.setData("type", FieldType.RADIO);
        rg.setId(name);
        rg.setOrientation(Orientation.VERTICAL);

        final NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));
            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            final Radio r = new Radio();
            r.setBoxLabel(title);
            r.setId(value);
            r.setValue("true".equals(def));

            rg.add(r);
        }
        add(rg, new FormData("95%"));

        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.RADIO);
        pe.radioGroup(rg);
        _pageElements.add(pe);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param name
     */
    private void addElementForHtml(final String name) {

        final Text fieldName = new Text(name);
        add(fieldName);
        final FCKEditor fck = new FCKEditor("", "250px");
        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.HTML);
        pe.editorLabel(fieldName);
        pe.editor(fck);
        add(fck, new FormData("95%"));
        _pageElements.add(pe);
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param name
     */
    private void addElementForDate(final String name) {

        final DateField df = new DateField();
        df.setFieldLabel(name);
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
     * TODO: Add a description of this method.
     *
     * @param name
     * @param regexp
     */
    private void addElementForTextArea(final String name, final String regexp) {

        final TextArea ta = new TextArea();
        ta.setData("type", FieldType.TEXT);
        ta.setId(name);
        ta.setFieldLabel(name);
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
     * TODO: Add a description of this method.
     *
     * @param name
     * @param regexp
     */
    private void addElementForTextField(final String name,
                                        final String regexp) {

        final TextField<String> tf = new TextField<String>();
        tf.setData("type", FieldType.TEXT);
        tf.setId(name);
        tf.setFieldLabel(name);
        if (regexp != null) {
            tf.setRegex(regexp);
        }
        add(tf, new FormData("95%"));
        final PageElement pe = new PageElement(name);
        pe.fieldType(FieldType.TEXT);
        pe.field(tf);

        _pageElements.add(pe);
    }

    private void addStaticFields() {

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
}
