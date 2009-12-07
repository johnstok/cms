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
package ccc.contentcreator.client;



import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.CheckBoxGroup;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.ListField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;


/**
 * Class for an element of the page.
 *
 * @author Civic Computing Ltd.
 */
public class PageElement {

    private String _id;
    private FieldType _fieldType;

    private FCKEditor _editor;
    private Text _editorLabel;
    private DateField _dateField;
    private Field<String> _field;

    private CheckBoxGroup _checkBoxGroup;
    private RadioGroup _radioGroup;
    private ComboBox<BaseModelData> _combobox;
    private ListField<BaseModelData> _list;
    private ImageTriggerField _image;
    private NumberField _number;


    /**
     * Possible types of paragraph fields.
     *
     * @author Civic Computing Ltd.
     */
    public enum FieldType {
        /** RADIO : FieldType. */
        RADIO,
        /** HTML : FieldType. */
        HTML,
        /** TEXT : FieldType. */
        TEXT,
        /** DATE : FieldType. */
        DATE,
        /** CHECKBOX : FieldType. */
        CHECKBOX,
        /** COMBOBOX : FieldType. */
        COMBOBOX,
        /** LIST : FieldType. */
        LIST,
        /** IMAGE : FieldType. */
        IMAGE,
        /** NUMBER : FieldType. */
        NUMBER;
    }

    /**
     * Constructor.
     *
     * @param id Id of the page element.
     */
    public PageElement(final String id) {
        super();
        _id = id;
    }

    /**
     * Accessor for the id of the page element.
     *
     * @return The id.
     */
    public String id() {
        return _id;
    }

    /**
     * Mutator for the id of the page element.
     *
     * @param id The id of the page element.
     */
    public void id(final String id) {
        _id = id;
    }

    /**
     * Accessor for the type of the page element.
     *
     * @return The type.
     */
    public FieldType fieldType() {
        return _fieldType;
    }

    /**
     * Mutator for the type of the page element.
     *
     * @param fieldType The type of the page element.
     */
    public void fieldType(final FieldType fieldType) {
        _fieldType = fieldType;
    }

    /**
     * Accessor for the FCKEditor field.
     *
     * @return The field.
     */
    public FCKEditor editor() {
        return _editor;
    }

    /**
     * Mutator for the FCKEditor field.
     *
     * @param editor The value of the field.
     */
    public void editor(final FCKEditor editor) {
        _editor = editor;
    }

    /**
     * Accessor for the Date field.
     *
     * @return The field.
     */
    public DateField dateField() {
        return _dateField;
    }

    /**
     * Mutator for the Date field.
     *
     * @param field The value of the field.
     */
    public void dateField(final DateField field) {
        _dateField = field;
    }

    /**
     * Accessor for the String field.
     *
     * @return The field.
     */
    public Field<String> field() {
        return _field;
    }

    /**
     * Mutator for the String field.
     *
     * @param field The value of the field.
     */
    public void field(final Field<String>field) {
        _field = field;
    }

    /**
     * Accessor for the editorLabel.
     *
     * @return The label.
     */
    public Text editorLabel() {
        return _editorLabel;
    }

    /**
     * Mutator for the editorLabel.
     *
     * @param editorLabel The value of the field.
     */
    public void editorLabel(final Text editorLabel) {
        _editorLabel = editorLabel;
    }

    /**
     * Mutator.
     *
     * @param checkBoxGroup The checkBoxGroup to set.
     */
    public void checkBoxGroup(final CheckBoxGroup checkBoxGroup) {

        _checkBoxGroup = checkBoxGroup;
    }

    /**
     * Accessor.
     *
     * @return Returns the checkBoxGroup.
     */
    public CheckBoxGroup checkBoxGroup() {

        return _checkBoxGroup;
    }

    /**
     * Mutator.
     *
     * @param radioGroup The radioGroup to set.
     */
    public void radioGroup(final RadioGroup radioGroup) {
        _radioGroup = radioGroup;
    }

    /**
     * Accessor.
     *
     * @return Returns the radioGroup.
     */
    public RadioGroup radioGroup() {

        return _radioGroup;
    }

    /**
     * Mutator.
     *
     * @param combobox The combobox to set.
     */
    public void combobox(final ComboBox<BaseModelData> combobox) {

        _combobox = combobox;
    }

    /**
     * Accessor.
     *
     * @return Returns the combobox.
     */
    public ComboBox<BaseModelData> combobox() {

        return _combobox;
    }

    /**
     * Mutator.
     *
     * @param list The list to set.
     */
    public void list(final ListField<BaseModelData> list) {

        _list = list;
    }

    /**
     * Accessor.
     *
     * @return Returns the list.
     */
    public ListField<BaseModelData> list() {

        return _list;
    }

    /**
     * Mutator.
     *
     * @param image The field for the image selector.
     */
    public void image(final ImageTriggerField image) {
        _image = image;
    }

    /**
     * Accessor.
     *
     * @return  The field of the image selector.
     */
    public ImageTriggerField image() {
        return _image;
    }

    /**
     * Accessor.
     *
     * @return Returns the number.
     */
    public final NumberField number() {
        return _number;
    }

    /**
     * Mutator.
     *
     * @param number The number to set.
     */
    public final void number(final NumberField number) {
        _number = number;
    }

}
