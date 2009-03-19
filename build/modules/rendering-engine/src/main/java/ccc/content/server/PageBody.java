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
package ccc.content.server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import ccc.commons.DBC;
import ccc.commons.Resources;
import ccc.commons.VelocityProcessor;
import ccc.domain.Page;
import ccc.domain.Template;
import ccc.services.StatefulReader;


/**
 * The class can write a Page object as the body of a {@link Response}. The
 * body will be written as html text, with the specified character set.
 *
 * @author Civic Computing Ltd.
 */
public class PageBody
    implements
        Body {

    private final Page    _page;
    private final Charset _charset;
    private final Map<String, String[]> _params;

    /**
     * Constructor.
     *
     * @param p The page to render.
     * @param charset The character set used when writing the page to an
     *  {@link OutputStream}.
     * @param parameters
     */
    public PageBody(final Page p,
                    final Charset charset,
                    final Map<String, String[]> parameters) {
        DBC.require().notNull(p);
        DBC.require().notNull(charset);

        _page = p;
        _charset = charset;
        _params = parameters;
    }

    /** {@inheritDoc} */
    @Override
    public Page getResource() {
        return _page;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final StatefulReader reader) throws IOException {
        final String t = _page.computeTemplate(BUILT_IN_PAGE_TEMPLATE).body();
        final Writer w = new OutputStreamWriter(os, _charset);
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put("reader", reader);
        values.put("resource", _page);
        values.put("parameters", _params);

        new VelocityProcessor().render(t, w, values);
    }

    private static final Template BUILT_IN_PAGE_TEMPLATE =
        new Template(
            "BUILT_IN_PAGE_TEMPLATE",
            "BUILT_IN_PAGE_TEMPLATE",
            Resources.readIntoString(
                ContentServlet.class.getResource("default-page-template.txt"),
                Charset.forName("UTF-8")),
            "<fields/>");
}
