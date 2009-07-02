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
package ccc.domain;

import java.util.Date;

import ccc.api.DBC;
import ccc.api.MimeType;
import ccc.api.TemplateDelta;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateRevision
    extends
        Revision<TemplateDelta> {

    private String _body;
    private String _definition;
    private MimeType _mimeType;

    /** Constructor: for persistence only. */
    protected TemplateRevision() { super(); }

    /**
     * Constructor.
     *
     * @param majorChange
     * @param comment
     * @param body
     * @param definition
     * @param mimeType
     */
    TemplateRevision(final Date timestamp,
                     final User actor,
                     final boolean majorChange,
                     final String comment,
                     final String body,
                     final String definition,
                     final MimeType mimeType) {
        super(timestamp, actor, majorChange, comment);

        DBC.require().notNull(body);
        DBC.require().notNull(definition);
        DBC.require().notNull(mimeType);

        _body = body;
        _definition = definition;
        _mimeType = mimeType;
    }


    /**
     * Accessor.
     *
     * @return Returns the body.
     */
    public final String getBody() {
        return _body;
    }


    /**
     * Accessor.
     *
     * @return Returns the definition.
     */
    public final String getDefinition() {
        return _definition;
    }


    /**
     * Accessor.
     *
     * @return Returns the mimeType.
     */
    public final MimeType getMimeType() {
        return _mimeType;
    }

    /** {@inheritDoc} */
    @Override
    protected TemplateDelta delta() {
        return new TemplateDelta(_body, _definition, _mimeType);
    }
}
