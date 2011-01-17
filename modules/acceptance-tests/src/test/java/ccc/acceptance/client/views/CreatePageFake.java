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

import java.util.Set;

import ccc.api.core.Template;
import ccc.api.types.Paragraph;
import ccc.client.core.Editable;
import ccc.client.core.ValidationResult;
import ccc.client.views.CreatePage;


/**
 * Fake implementation of the {@link CreatePage} view.
 *
 * @author Civic Computing Ltd.
 */
public class CreatePageFake implements CreatePage {

    private final String _comment;
    private final boolean _majorEdit;
    private final boolean _publish;
    private final String _name;
    private final Set<Paragraph> _paragraphs;
    private final Template _template;
    private Object _presenter;
    private boolean _showing;
    private final ValidationResult _validationResult = new ValidationResult();

    /**
     * Constructor.
     *
     * @param name The name
     * @param majorEdit Major edit.
     * @param publish Publish.
     * @param comment Comment.
     * @param paragraphs Paragraph of the page.
     * @param template Template.
     */
    public CreatePageFake(final String name,
                          final boolean majorEdit,
                          final boolean publish,
                          final String comment,
                          final Set<Paragraph> paragraphs,
                          final Template template) {
        _comment = comment;
        _name = name;
        _majorEdit = majorEdit;
        _publish = publish;
        _paragraphs = paragraphs;
        _template = template;
    }

    @Override
    public void alert(final String message) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    @Override
    public String getComment() {
        return _comment;
    }

    @Override
    public boolean getMajorEdit() {
        return _majorEdit;
    }
    
    @Override
    public boolean getPublish() {
        return _publish;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public Set<Paragraph> getParagraphs() {
        return _paragraphs;
    }

    @Override
    public Template getSelectedTemplate() {
        return _template;
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

}
