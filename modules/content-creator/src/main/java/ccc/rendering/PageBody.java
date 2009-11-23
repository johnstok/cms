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

import ccc.commons.Resources;
import ccc.domain.RevisionMetadata;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.types.DBC;
import ccc.types.MimeType;


/**
 * The class can write a Page object as the body of a {@link Response}. The
 * body will be written as html text, with the specified character set.
 *
 * @author Civic Computing Ltd.
 */
public class PageBody
    implements
        Body {

    private final String _template;


    /**
     * Constructor.
     *
     * @param t The template to use for this body.
     */
    public PageBody(final String t) {
        DBC.require().notNull(t);
        _template = t;
    }

    /** {@inheritDoc} */
    @Override
    public void write(final OutputStream os,
                      final Charset charset,
                      final Context context,
                      final TextProcessor processor) {
        final String t = _template;
        final Writer w = new OutputStreamWriter(os, charset);

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
}
