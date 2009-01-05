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

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageElement {

    private String _id;
    private String _type;

    private FCKEditor _editor;
    private DateField _dateField;
    private Field<String> _field;

    /**
     * Constructor.
     *
     * @param _id
     */
    public PageElement(final String _id) {
        super();
        this._id = _id;
    }


    public String id() {
        return _id;
    }

    public void id(final String id) {
        _id = id;
    }

    public String type() {
        return _type;
    }

    public void type(final String type) {
        _type = type;
    }

    public FCKEditor editor() {
        return _editor;
    }

    public void editor(final FCKEditor editor) {
        _editor = editor;
    }

    public DateField dateField() {
        return _dateField;
    }

    public void dateField(final DateField field) {

        _dateField = field;
    }


    public Field<String> field() {
        return _field;
    }

    public void field(final Field<String>field) {
        _field = field;
    }



}
