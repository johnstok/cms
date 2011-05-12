/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
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
package ccc.web.rendering;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import ccc.api.types.MimeType;
import ccc.commons.Resources;
import ccc.plugins.PluginFactory;
import ccc.plugins.scripting.Context;
import ccc.plugins.scripting.ProcessingException;
import ccc.plugins.scripting.Script;
import ccc.plugins.scripting.TextProcessor;
import ccc.web.TmpRenderer;
import ccc.web.exceptions.RequestFailedException;


/**
 * A body implementation for scripts.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptBody
    implements
        Body {

    private final Script _script;


    /**
     * Constructor.
     *
     * @param script The script to invoke.
     */
    public ScriptBody(final Script script) {
        _script = script;
    }



    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final Context context,
                      final PluginFactory plugins) {

        final HttpServletResponse resp =
            context.get("response", HttpServletResponse.class);
        disableCaching(resp);
        final PrintWriter pw =
            new PrintWriter(new OutputStreamWriter(os, charset));

        final List<String> whiteList =
            Resources.readIntoList(
                "/scripting_whitelist.txt",
                Charset.forName(TmpRenderer.DEFAULT_CHARSET));

        if (MimeType.JAVASCRIPT.equals(_script.getType())) {
            final TextProcessor scriptRunner = plugins.createScripting();
            scriptRunner.setWhitelist(whiteList);
            try {
                scriptRunner.render(_script, pw, context);
            } catch (final ProcessingException e) {
                throw new RequestFailedException(e);
            }
        } else {
            throw new RuntimeException(
                "Unsupported scripting language: "+_script.getType());
        }
    }



    private void disableCaching(final HttpServletResponse resp) {
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-store, must-revalidate, max-age=0");
        resp.setDateHeader("Expires", new Date(0).getTime());
    }
}
