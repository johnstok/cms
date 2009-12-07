/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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
package ccc.contentcreator.actions;

import java.util.Set;

import ccc.contentcreator.client.GwtJson;
import ccc.serialization.JsonKeys;
import ccc.types.Paragraph;

import com.google.gwt.http.client.RequestBuilder;


/**
 * Remote action for field validation.
 *
 * @author Civic Computing Ltd.
 */
public class ValidateFieldAction
    extends
        RemotingAction {

    private final String _definition;
    private final Set<Paragraph> _paragraphs;


    /**
     * Constructor.
     * @param definition Template definition used to validate the paragraphs.
     * @param paragraphs The paragraphs to validate.
     */
    public ValidateFieldAction(final Set<Paragraph> paragraphs,
                                final String definition) {
        super(USER_ACTIONS.validatePageFields(), RequestBuilder.POST);
        _definition = definition;
        _paragraphs = paragraphs;
    }


    /** {@inheritDoc} */
    @Override
    protected String getPath() {
        return "/page-validator";
    }


    /** {@inheritDoc} */
    @Override
    protected String getBody() {
        final GwtJson json = new GwtJson();
        json.set(JsonKeys.DEFINITION, _definition);
        json.set(JsonKeys.PARAGRAPHS, _paragraphs);
        return json.toString();
    }
}
