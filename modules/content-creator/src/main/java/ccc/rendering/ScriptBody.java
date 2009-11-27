/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rendering;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import ccc.commons.Context;
import ccc.commons.ScriptRunner;


/**
 * A body implementation for scripts.
 *
 * @author Civic Computing Ltd.
 */
public class ScriptBody
    implements
        Body {

    private final String _script;


    /**
     * Constructor.
     *
     * @param script The script to invoke.
     */
    public ScriptBody(final String script) {
        _script = script;
    }



    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final Context context,
                      final TextProcessor processor) throws IOException {

        final HttpServletResponse resp =
            context.get("response", HttpServletResponse.class);
        disableCaching(resp);

        // FIXME: Add a white-list.
        new ScriptRunner().eval(_script, context, resp.getWriter());
    }



    private void disableCaching(final HttpServletResponse resp) {
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-store, must-revalidate, max-age=0");
        resp.setDateHeader("Expires", new Date(0).getTime());
    }
}
