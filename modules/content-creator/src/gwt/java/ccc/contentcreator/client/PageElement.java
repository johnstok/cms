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


import ccc.contentcreator.client.ui.FCKEditor;

import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;


/**
 * Class for an element of the page.
 *
 * @author Civic Computing Ltd.
 */
public class PageElement {

    private String _id;
    private String _type;

    private FCKEditor _editor;
    private Text _editorLabel;
    private DateField _dateField;
    private Field<String> _field;

    /**
     * Constructor.
     *
     * @param id Id of the page element.
     */
    public PageElement(final String id) {
        super();
        this._id = id;
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
    public String type() {
        return _type;
    }

    /**
     * Mutator for the type of the page element.
     *
     * @param type The type of the page element.
     */
    public void type(final String type) {
        _type = type;
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

}
