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
package ccc.rendering;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;

import ccc.api.DBC;
import ccc.api.MimeType;
import ccc.api.template.StatefulReader;
import ccc.commons.Resources;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.snapshots.PageSnapshot;


/**
 * The class can write a Page object as the body of a {@link Response}. The
 * body will be written as html text, with the specified character set.
 *
 * @author Civic Computing Ltd.
 */
public class PageBody
    implements
        Body {

    private final PageSnapshot    _page;
    private final Map<String, String[]> _params;
    private final StatefulReader _reader;
    private final Template _template;

    /**
     * Constructor.
     *
     * @param page The page to render.
     * @param reader A stateful reader to access other resources.
     * @param parameters Additional parameters to control rendering.
     * @param t The template to use for this body.
     */
    public PageBody(final PageSnapshot page,
                    final StatefulReader reader,
                    final Template t,
                    final Map<String, String[]> parameters) {
        DBC.require().notNull(page);
        DBC.require().notNull(reader);
        DBC.require().notNull(parameters);
        DBC.require().notNull(t);

        _page = page;
        _reader = reader;
        _params = parameters;
        _template = t;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final TextProcessor processor) {
        final String t = _template.body();
        final Writer w = new OutputStreamWriter(os, charset);
        final Context context = new Context(_reader, _page, _params);

        processor.render(t, w, context);
    }


    /** BUILT_IN_PAGE_TEMPLATE : Template. */
    public static final Template BUILT_IN_PAGE_TEMPLATE =
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

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public PageSnapshot getPage() {
        return _page;
    }
}
