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

import ccc.api.FileSummary;
import ccc.api.ID;
import ccc.api.PageDelta;
import ccc.api.ParagraphDelta;
import ccc.api.ParagraphType;
import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.FileSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
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

    private static final String CHECKBOX = "CHECKBOX";
    private static final String RADIO = "RADIO";
    private static final String HTML = "HTML";
    private static final String TEXT = "TEXT";
    private static final String DATE = "DATE";
    private static final String COMBOBOX = "COMBOBOX";
    private static final String LIST = "LIST";
    private static final String IMAGE = "IMAGE";


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

        // TODO: Refactor
        for (final PageElement c : pageElements()) {
            for (final ParagraphDelta para : resourceSummary.getParagraphs()) {
                if (c.id().equals(para.getName())) {
                    if (TEXT.equals(c.type())) {
                        final Field<String> f = c.field();
                        f.setValue(para.getTextValue());
                    } else if (DATE.equals(c.type())) {
                        final DateField f = c.dateField();
                        f.setValue(para.getDateValue());
                    } else if (HTML.equals(c.type())) {
                        remove(c.editor());
                        remove(c.editorLabel());
                        final FCKEditor fck =
                            new FCKEditor(para.getTextValue(), "250px");
                        add(c.editorLabel());
                        add(fck, new FormData("95%"));
                        c.editor(fck);
                    } else if (CHECKBOX.equals(c.type())) {
                        final CheckBoxGroup cbg = c.checkBoxGroup();
                        Map<String, String> valueMap = fillValueMap(para);

                        List<CheckBox> boxes = cbg.getAll();
                        for (CheckBox box : boxes) {
                            if ("true".equals(valueMap.get(box.getId()))) {
                                box.setValue(true);
                            } else {
                                box.setValue(false);
                            }
                        }
                    } else if (RADIO.equals(c.type())) {
                        final RadioGroup rg = c.radioGroup();
                        String value = para.getTextValue();

                        List<Radio> radios = rg.getAll();
                        for (Radio radio : radios) {
                            if (radio.getId().equals(value)) {
                                radio.setValue(true);
                            } else {
                                radio.setValue(false);
                            }
                        }
                    } else if (COMBOBOX.equals(c.type())) {
                        ComboBox<BaseModelData> cb = c.combobox();
                        String value = para.getTextValue();

                        ListStore<BaseModelData> store = cb.getStore();
                        for (BaseModelData model : store.getModels()) {
                            if (model.get("value").equals(value)) {
                                cb.setValue(model);
                            }
                        }
                    }  else if (LIST.equals(c.type())) {
                        final ListField<BaseModelData> list = c.list();
                        Map<String, String> valueMap = fillValueMap(para);
                        final List<BaseModelData> selection =
                            new ArrayList<BaseModelData>();

                        ListStore<BaseModelData> items = list.getStore();
                        for (BaseModelData item : items.getModels()) {
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
                    } else if (IMAGE.equals(c.type())) {
                        final ImageTriggerField image = c.image();
                        String id = para.getTextValue();
                        final ID resourceId = new ID(id);
                        _qs.getAbsolutePath(resourceId, new ErrorReportingCallback<String>() {

                            @Override
                            public void onSuccess(String path) {
                                FileSummary fs = new FileSummary("image", path, resourceId, "", "");
                                FileSummaryModelData model = new FileSummaryModelData(fs);
                                image.setValue(path);
                                image.setFSModel(model);
                            }
                        });
                    }
                }
            }
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param para
     * @return
     */
    private Map<String, String> fillValueMap(final ParagraphDelta para) {

        String text = para.getTextValue();

        Map<String, String> valueMap = new HashMap<String, String>();

        String[] lines = text.split("\n");
        for (String line : lines) {
            if (line.trim().length() > 0) {
                String key = line.substring(0, line.indexOf("="));
                String value = line.substring(line.indexOf("=")+1);
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
                              final List<ParagraphDelta> paragraphs) {
        ParagraphDelta p = null;

        for (final PageElement c : definitions) {
            if (TEXT.equals(c.type())) {
                p = extractText(c);
            } else if (DATE.equals(c.type())) {
                p = extractDate(c);
            } else if (HTML.equals(c.type())) {
                p = extractHtml(c);
            } else if (CHECKBOX.equals(c.type())) {
                p = extractCheckBox(c);
            } else if (RADIO.equals(c.type())) {
                p = extractRadio(c);
            } else if (COMBOBOX.equals(c.type())) {
                p = extractComboBox(c);
            } else if (LIST.equals(c.type())) {
                p = extractList(c);
            } else if (IMAGE.equals(c.type())) {
                p = extractImage(c);
            }
            if (p != null) {
                paragraphs.add(p);
            }
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @return
     */
    private ParagraphDelta extractImage(PageElement c) {
        ImageTriggerField image = c.image();
        String id = "";
        FileSummaryModelData model = image.getFSModel();
        if (model != null) {
            id = model.getId().toString();
        }

        ParagraphDelta p = new ParagraphDelta(
            c.id(),
            ParagraphType.TEXT,
            null,
            id,
            null,
            null);
        return p;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @return
     */
    private ParagraphDelta extractList(PageElement c) {

        final ListField<BaseModelData> list = c.list();
        StringBuilder sb = new StringBuilder();
        for (BaseModelData item : list.getSelection()) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(item.get("value"));
            sb.append("=");
            sb.append(item.get("title"));
        }
        ParagraphDelta p = new ParagraphDelta(
            c.id(),
            ParagraphType.TEXT,
            null,
            sb.toString(),
            null,
            null);
        return p;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @return
     */
    private ParagraphDelta extractComboBox(PageElement c) {

        final ComboBox<BaseModelData> cb = c.combobox();
        String selected = "";
        if (cb.getValue() != null) {
            selected = cb.getValue().get("value");
        }

        ParagraphDelta p = new ParagraphDelta(
            c.id(),
            ParagraphType.TEXT,
            null,
            selected,
            null,
            null);
        return p;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @return
     */
    private ParagraphDelta extractRadio(PageElement c) {

        final RadioGroup rg = c.radioGroup();
        String selected = "";
        if (rg.getValue() != null) {
            selected = rg.getValue().getId();
        }

        ParagraphDelta p = new ParagraphDelta(
            c.id(),
            ParagraphType.TEXT,
            null,
            selected,
            null,
            null);
        return p;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @return
     */
    private ParagraphDelta extractCheckBox(PageElement c) {

        final CheckBoxGroup cbg = c.checkBoxGroup();
        StringBuilder sb = new StringBuilder();
        for (CheckBox cb : cbg.getAll()) {
            if (sb.length() > 0) {
                sb.append("\n");
            }
            sb.append(cb.getId());
            sb.append("=");
            sb.append(cb.getValue().toString());
        }

        ParagraphDelta p = new ParagraphDelta(
            c.id(),
            ParagraphType.TEXT,
            null,
            sb.toString(),
            null,
            null);
        return p;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @return
     */
    private ParagraphDelta extractHtml(PageElement c) {

        final FCKEditor f = c.editor();
        ParagraphDelta p = new ParagraphDelta(
            c.id(),
            ParagraphType.TEXT,
            null,
            f.getHTML(),
            null,
            null);
        return p;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @return
     */
    private ParagraphDelta extractDate(PageElement c) {

        final DateField f = c.dateField();
        ParagraphDelta p = new ParagraphDelta(
            c.id(),
            ParagraphType.DATE,
            f.getRawValue(),
            null,
            f.getValue(),
            null);
        return p;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param c
     * @return
     */
    private ParagraphDelta extractText(PageElement c) {

        final Field<String> f = c.field();
        ParagraphDelta p = new ParagraphDelta(
            c.id(),
            ParagraphType.TEXT,
            null,
            f.getValue(),
            null,
            null);
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
        image.setData("type", IMAGE);
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
        pe.type(IMAGE);
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
        list.setData("type", LIST);
        list.setDisplayField("title");
        list.setValueField("value");
        list.setId(name);

        final ListStore<BaseModelData> store =  new ListStore<BaseModelData>();
        final List<BaseModelData> selection = new ArrayList<BaseModelData>();

        NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));
            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            BaseModelData model = new BaseModelData();
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
        pe.type(LIST);
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
        cb.setData("type", COMBOBOX);
        cb.setDisplayField("title");
        cb.setValueField("value");
        cb.setId(name);

        ListStore<BaseModelData> store =  new ListStore<BaseModelData>();
        NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));
            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            BaseModelData model = new BaseModelData();
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
        pe.type(COMBOBOX);
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
        cbg.setData("type", CHECKBOX);
        cbg.setId(name);
        cbg.setOrientation(Orientation.VERTICAL);

        NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));
            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            CheckBox cb = new CheckBox();
            cb.setBoxLabel(title);
            cb.setId(value);
            cb.setValue("true".equals(def));

            cbg.add(cb);
        }
        add(cbg, new FormData("95%"));

        final PageElement pe = new PageElement(name);
        pe.type(CHECKBOX);
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
        rg.setData("type", RADIO);
        rg.setId(name);
        rg.setOrientation(Orientation.VERTICAL);

        NodeList nl = field.getElementsByTagName("option");
        for (int i=0; i<nl.getLength(); i++) {
            final Element option = ((Element) nl.item(i));
            final String def  = option.getAttribute("default");
            final String title = option.getAttribute("title");
            final String value = option.getAttribute("value");

            Radio r = new Radio();
            r.setBoxLabel(title);
            r.setId(value);
            r.setValue("true".equals(def));

            rg.add(r);
        }
        add(rg, new FormData("95%"));

        final PageElement pe = new PageElement(name);
        pe.type(RADIO);
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
        pe.type(HTML);
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
        df.setData("type", DATE);
        df.setId(name);

        final PageElement pe = new PageElement(name);
        pe.type(DATE);
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
        ta.setData("type", TEXT);
        ta.setId(name);
        ta.setFieldLabel(name);
        if (regexp != null) {
            ta.setRegex(regexp);
        }
        add(ta, new FormData("95%"));
        final PageElement pe = new PageElement(name);
        pe.type(TEXT);
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
        tf.setData("type", TEXT);
        tf.setId(name);
        tf.setFieldLabel(name);
        if (regexp != null) {
            tf.setRegex(regexp);
        }
        add(tf, new FormData("95%"));
        final PageElement pe = new PageElement(name);
        pe.type(TEXT);
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
