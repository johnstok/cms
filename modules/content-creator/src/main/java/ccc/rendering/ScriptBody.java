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
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
        final PrintWriter pw =
            new PrintWriter(new OutputStreamWriter(os, charset));

        final List<String> whiteList = new ArrayList<String>();
        whiteList.add("java.util");
        whiteList.add("ccc.types");
        whiteList.add("ccc.rest.dto.UserDto");
        whiteList.add("ccc.mail.Mailer");
        whiteList.add("java.lang.String");
        whiteList.add("java.util.ArrayList");
        whiteList.add("java.lang.Object");
        whiteList.add("org.apache.catalina.core.ApplicationDispatcher");
        new ScriptRunner(whiteList)
            .eval(_script, context, pw);
    }



    private void disableCaching(final HttpServletResponse resp) {
        resp.setHeader("Pragma", "no-cache");
        resp.setHeader("Cache-Control", "no-store, must-revalidate, max-age=0");
        resp.setDateHeader("Expires", new Date(0).getTime());
    }
}
