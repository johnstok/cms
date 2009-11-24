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
package ccc.remoting;

import java.util.UUID;

import ccc.rendering.FileBody;
import ccc.rendering.NotFoundException;
import ccc.rendering.PageBody;
import ccc.rendering.RedirectRequiredException;
import ccc.rendering.Response;
import ccc.rendering.ScriptBody;
import ccc.rendering.SearchBody;
import ccc.rest.Files;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.Templates;
import ccc.rest.dto.AliasDto;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.FolderDto;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.dto.TextFileDelta;
import ccc.rest.snapshots.ResourceSnapshot;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class TmpRenderer {

    private final Templates _templates;
    private final Resources _resources;
    private final Files     _files;


    /**
     * Constructor.
     *
     * @param files     The files API.
     * @param templates The templates API.
     * @param resources The resources API.
     */
    public TmpRenderer(final Files files,
                       final Templates templates,
                       final Resources resources) {
        _templates = templates;
        _resources = resources;
        _files = files;
    }


    /**
     * Render the resource, as a response.
     *
     * @param s The snapshot to render.
     *
     * @return A response representing the resource.
     */
    public Response render(final ResourceSnapshot s) {
        switch (s.getType()) {
            case ALIAS:
                return render((AliasDto) s);
            case FILE:
                final FileDto f = (FileDto) s;
                if (f.isText() && f.isExecutable()) {
                    return invoke(f);
                }
                return render(f);
            case FOLDER:
                return render((FolderDto) s);
            case PAGE:
                return render((PageDelta) s);
            case SEARCH:
                return renderSearch(s);
            default:
                throw new NotFoundException();
        }
    }


    private Response renderSearch(final ResourceSnapshot s) {
        try {
            final UUID tId = s.getTemplate();
            final TemplateDelta t = _templates.templateDelta(tId);
            final Response r = new Response(new SearchBody(t.getBody()));
            r.setCharSet("UTF-8");
            r.setMimeType(t.getMimeType());
            r.setExpiry(s.getCacheDuration());
            return r;

        } catch (final RestException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }


    private Response invoke(final FileDto f) {

        try {
            final TextFileDelta tf = _files.get(f.getId());
            return
                new Response(
                    new ScriptBody(tf.getContent()));
        } catch (final RestException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }


    private Response render(final PageDelta s) {
        try {
            final UUID tId = s.getTemplate();
            final TemplateDelta t = _templates.templateDelta(tId);
            final Response r = new Response(new PageBody(t.getBody()));
            r.setCharSet("UTF-8");
            r.setMimeType(t.getMimeType());
            r.setExpiry(s.getCacheDuration());
            return r;

        } catch (final RestException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }


    private Response render(final FolderDto s) {
        try {
            if (null!= s.getIndexPage()) {
                throw new RedirectRequiredException(// FIXME: Broken for /assets
                    _resources.getAbsolutePath(s.getIndexPage()));
            } else if (null != s.getDefaultPage()) {
                throw new RedirectRequiredException(// FIXME: Broken for /assets
                    _resources.getAbsolutePath(s.getIndexPage()));
            }

        } catch (final RestException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }

        throw new NotFoundException();
    }


    private Response render(final FileDto s) {
        final Response r = new Response(new FileBody(s));
        r.setDescription(s.getDescription());
        r.setDisposition("inline; filename=\""+s.getName()+"\"");
        r.setMimeType(s.getMimeType());
        r.setLength(s.getSize());
        r.setExpiry(s.getCacheDuration());
        return r;
    }


    private Response render(final AliasDto s) {
        if (null==s.getTargetPath()) {
            throw new NotFoundException();
        }
        throw new RedirectRequiredException(
            s.getTargetPath());
    }
}
