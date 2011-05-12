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
package ccc.web.rendering;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import ccc.api.types.DBC;
import ccc.api.types.MimeType;
import ccc.plugins.PluginFactory;
import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.ProcessingException;
import ccc.plugins.scripting.Script;
import ccc.web.exceptions.RequestFailedException;


/**
 * The class can write a Page object as the body of a {@link Response}. The
 * body will be written as html text, with the specified character set.
 *
 * @author Civic Computing Ltd.
 */
public class PageBody
    implements
        Body {

    private final Script _template;


    /**
     * Constructor.
     *
     * @param t The template to use for this body.
     */
    public PageBody(final Script t) {
        DBC.require().notNull(t);
        _template = t;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final Context context,
                      final PluginFactory plugins) {
        final Script t = _template;
        final Writer w = new OutputStreamWriter(os, charset);

        try {
            if (MimeType.JAVASCRIPT.equals(t.getType())) {
                plugins.createScripting().render(t, w, context);
            } else {
                plugins.createTemplating().render(t, w, context);
            }
        } catch (final ProcessingException e) {
            throw new RequestFailedException(e);
        }
    }
}
