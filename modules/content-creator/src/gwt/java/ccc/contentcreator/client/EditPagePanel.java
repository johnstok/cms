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

import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.client.ui.FCKEditor;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ParagraphType;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
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
    
    private final static String CHECKBOX = "CHECKBOX";
    private final static String RADIO = "RADIO";
    private final static String HTML = "HTML";
    private final static String TEXT = "TEXT";
    private final static String DATE = "DATE";

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
     */
    public void populateFields(final PageDelta resourceSummary, final String pageName) {
        _name.setValue(pageName);
        _name.setReadOnly(true);
        _name.disable();
        _title.setValue(resourceSummary.getTitle());

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
                        Map<String, String> valueMap = fillValueMap(para);
                        
                        List<Radio> radios = rg.getAll();
                        for (Radio radio : radios) {
                            if ("true".equals(valueMap.get(radio.getId()))) {
                                radio.setValue(true);
                            } else {
                                radio.setValue(false);
                            }
                        }
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
        
        Map<String,String> valueMap = new HashMap<String, String>();
        
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
     * TODO: Add a description of this method.
     *
     * @param definitions
     * @param paragraphs
     */
    public void extractValues(final List<PageElement> definitions,
                               final List<ParagraphDelta> paragraphs) {

        for (final PageElement c : definitions) {
            if (TEXT.equals(c.type())) {
                final Field<String> f = c.field();
                final ParagraphDelta p =
                    new ParagraphDelta(
                        c.id(),
                        ParagraphType.TEXT,
                        null,
                        f.getValue(),
                        null,
                        null);
                paragraphs.add(p);
            } else if (DATE.equals(c.type())) {
                final DateField f = c.dateField();
                final ParagraphDelta p =
                    new ParagraphDelta(
                        c.id(),
                        ParagraphType.DATE,
                        f.getRawValue(),
                        null,
                        f.getValue(),
                        null);
                paragraphs.add(p);
            } else if (HTML.equals(c.type())) {
                final FCKEditor f = c.editor();
                final ParagraphDelta p =
                    new ParagraphDelta(
                        c.id(),
                        ParagraphType.TEXT,
                        null,
                        f.getHTML(),
                        null,
                        null);
                paragraphs.add(p);
            } else if (CHECKBOX.equals(c.type())) {
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
                
                final ParagraphDelta p =
                    new ParagraphDelta(
                        c.id(),
                        ParagraphType.TEXT,
                        null,
                        sb.toString(),
                        null,
                        null);
                paragraphs.add(p);
            } else if (RADIO.equals(c.type())) {
                final RadioGroup rg = c.radioGroup();
                StringBuilder sb = new StringBuilder();
                for (Radio radio : rg.getAll()) {
                    if (sb.length() > 0) {
                        sb.append("\n");
                    }
                    sb.append(radio.getId());
                    sb.append("=");
                    sb.append(radio.getValue().toString());
                }
                
                final ParagraphDelta p =
                    new ParagraphDelta(
                        c.id(),
                        ParagraphType.TEXT,
                        null,
                        sb.toString(),
                        null,
                        null);
                paragraphs.add(p);
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
        _pageElements.clear();
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
            }
        }
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param name
     * @param field
     */
    private void addElementForCheckbox(String name, Element field) {
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
    private void addElementForRadio(String name, Element field) {
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
    private void addElementForTextField(final String name, final String regexp) {

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
}
