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
package ccc.content.response;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ccc.api.DBC;
import ccc.api.MimeType;
import ccc.commons.Resources;
import ccc.content.velocity.VelocityProcessor;
import ccc.domain.Page;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
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
    private final Map<String, String[]> _params;
    private final StatefulReader _reader;
    private final Template _template;

    /**
     * Constructor.
     *
     * @param p The page to render.
     * @param reader A stateful reader to access other resources.
     * @param parameters Additional parameters to control rendering.
     * @param t The template to use for this body.
     */
    public PageBody(final Page p,
                    final StatefulReader reader,
                    final Template t,
                    final Map<String, String[]> parameters) {
        DBC.require().notNull(p);
        DBC.require().notNull(reader);
        DBC.require().notNull(parameters);
        DBC.require().notNull(t);

        _page = p;
        _reader = reader;
        _params = parameters;
        _template = t;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset) {
        final String t = _template.body();
        final Writer w = new OutputStreamWriter(os, charset);
        final Map<String, Object> values = new HashMap<String, Object>();
        values.put("reader", _reader);
        values.put("resource", _page);
        values.put("parameters", _params);

        new VelocityProcessor().render(t, w, values);
    }


    /** BUILT_IN_PAGE_TEMPLATE : Template. */
    static final Template BUILT_IN_PAGE_TEMPLATE =
        new Template(
            "BUILT_IN_PAGE_TEMPLATE",
            "BUILT_IN_PAGE_TEMPLATE",
            Resources.readIntoString(
                PageBody.class.getResource(
                    "/ccc/content/server/default-page-template.txt"),
                Charset.forName("UTF-8")),
            "<fields/>",
            MimeType.HTML,
            new RevisionMetadata(
                new Date(),
                User.SYSTEM_USER,
                true,
                "Created."));
}
