/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
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

import ccc.api.Paragraph;
import ccc.contentcreator.client.GwtJson;

import com.google.gwt.http.client.RequestBuilder;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ValidateFieldAction_
    extends
        RemotingAction {

    private final String _definition;
    private final Set<Paragraph> _paragraphs;


    /**
     * Constructor.
     * @param definition Template definition used to validate the paragraphs.
     * @param paragraphs The paragraphs to validate.
     */
    public ValidateFieldAction_(final Set<Paragraph> paragraphs,
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
        json.set("definition", _definition);
        json.set("paragraphs", _paragraphs);
        return json.toString();
    }
}
