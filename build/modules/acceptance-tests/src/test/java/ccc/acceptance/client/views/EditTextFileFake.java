/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.acceptance.client.views;

import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.views.EditTextFile;


/**
 * Fake implementation of the {@link EditTextFile} view.
 *
 * @author Civic Computing Ltd.
 */
public class EditTextFileFake implements EditTextFile {


    private final boolean _majorEdit;
    private String _text;
    private String _subMime;
    private String _primaryMime;
    private final String _comment;
    private Object _presenter;
    private boolean _showing;
    private final ValidationResult _validationResult = new ValidationResult();

    /**
     * Constructor.
     *
     * @param text The file text
     * @param primaryMime The primary mime
     * @param subMime The sub mime
     * @param comment The comment
     * @param majorEdit Major edit
     */
    public EditTextFileFake(final String text,
                              final String primaryMime,
                              final String subMime,
                              final String comment,
                              final boolean majorEdit) {
        _majorEdit = majorEdit;
        _text = text;
        _subMime = subMime;
        _primaryMime = primaryMime;
        _comment = comment;
    }

    @Override
    public String getComment() {
        return _comment;
    }

    @Override
    public String getPrimaryMime() {
        return _primaryMime;
    }

    @Override
    public String getSubMime() {
        return _subMime;
    }

    @Override
    public String getText() {
        return _text;
    }

    @Override
    public boolean isMajorEdit() {
        return _majorEdit;
    }

    @Override
    public void hide() {
        _presenter = null;
        _showing   = false;
    }

    @Override
    public void show(final Editable presenter) {
        _presenter = presenter;
        _showing   = true;
    }

    @Override
    public ValidationResult getValidationResult() {
        return _validationResult;
    }

    @Override
    public void setPrimaryMime(final String primary) {
        _primaryMime = primary;
    }

    @Override
    public void setSubMime(final String sub) {
        _subMime = sub;
    }

    @Override
    public void setText(final String text) {
        _text = text;
    }

}
