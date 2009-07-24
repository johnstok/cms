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
package ccc.snapshots;

import java.util.Map;
import java.util.Set;

import ccc.api.Paragraph;
import ccc.api.template.IPageSnapshot;
import ccc.domain.IPage;
import ccc.domain.Page;
import ccc.domain.Template;
import ccc.rendering.PageBody;
import ccc.rendering.Response;
import ccc.services.DataManager;
import ccc.services.SearchEngine;
import ccc.services.StatefulReader;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class PageSnapshot extends ResourceSnapshot implements IPage, IPageSnapshot {
    private final IPage _delegate;

    /**
     * Constructor.
     *
     * @param p The page this snapshot wraps.
     * @param page The revision or working copy.
     */
    public PageSnapshot(final Page p, final IPage page) {
        super(p);
        _delegate = page;
    }

    /** {@inheritDoc} */
    @Override
    public Set<Paragraph> getContent() {
        return _delegate.getContent();
    }

    /** {@inheritDoc} */
    @Override
    public Paragraph paragraph(final String name) {
        return _delegate.paragraph(name);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Paragraph> paragraphs() {
        return _delegate.paragraphs();
    }

    /** {@inheritDoc} */
    @Override
    public Response render(final Map<String, String[]> parameters,
                           final SearchEngine search,
                           final StatefulReader reader,
                           final DataManager dm) {
        final Template t =
            computeTemplate(PageBody.BUILT_IN_PAGE_TEMPLATE);
        final Response r =
            new Response(new PageBody(this, reader, t, parameters));
        r.setCharSet("UTF-8");
        r.setMimeType(t.mimeType().getPrimaryType(), t.mimeType().getSubType());
        r.setExpiry(computeCache());

        return r;
    }
}
