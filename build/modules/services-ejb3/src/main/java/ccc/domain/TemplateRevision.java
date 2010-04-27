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
package ccc.domain;

import java.util.Date;

import ccc.api.core.TemplateDto;
import ccc.api.types.DBC;
import ccc.api.types.MimeType;


/**
 * The content of a template at a specific point in time.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateRevision
    extends
        RevisionEntity<TemplateDto> {

    private String _body;
    private String _definition;
    private MimeType _mimeType;

    /** Constructor: for persistence only. */
    protected TemplateRevision() { super(); }

    /**
     * Constructor.
     *
     * @param timestamp The timestamp of the revision.
     * @param actor The user causing the change.
     * @param majorChange The boolean flag of the major change.
     * @param comment The comment of the revision.
     * @param body The body of the template.
     * @param definition The XML definition of the template.
     * @param mimeType The mime type for the template.
     */
    TemplateRevision(final Date timestamp,
                     final UserEntity actor,
                     final boolean majorChange,
                     final String comment,
                     final String body,
                     final String definition,
                     final MimeType mimeType) {
        super(timestamp, actor, majorChange, comment);

        DBC.require().notEmpty(body);
        DBC.require().notEmpty(definition);
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
    protected TemplateDto delta() {
        final TemplateDto t = new TemplateDto();
        t.setBody(_body);
        t.setDefinition(_definition);
        t.setMimeType(_mimeType); // FIXME: Make a copy.
        return t;
    }
}
