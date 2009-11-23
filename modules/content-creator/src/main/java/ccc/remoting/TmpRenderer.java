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

import ccc.domain.NotFoundException;
import ccc.rendering.FileBody;
import ccc.rendering.PageBody;
import ccc.rendering.RedirectRequiredException;
import ccc.rendering.Response;
import ccc.rendering.SearchBody;
import ccc.rest.Resources;
import ccc.rest.RestException;
import ccc.rest.Templates;
import ccc.rest.dto.AliasDto;
import ccc.rest.dto.FileDto;
import ccc.rest.dto.FolderDto;
import ccc.rest.dto.PageDelta;
import ccc.rest.dto.TemplateDelta;
import ccc.rest.snapshots.ResourceSnapshot;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class TmpRenderer {

    private final Templates _templates;
    private final Resources _resources;


    /**
     * Constructor.
     *
     * @param templates The templates API.
     * @param resources The resources API.
     */
    public TmpRenderer(final Templates templates, final Resources resources) {
        _templates = templates;
        _resources = resources;
    }


    /**
     * Render the resource, as a response.
     *
     * @param s The
     *
     * @return A response representing the resource.
     */
    public Response renderSearch(final ResourceSnapshot s) {
        try {
            final UUID tId = s.getTemplate();
            final TemplateDelta t = _templates.templateDelta(tId);
            final Response r = new Response(new SearchBody(t.getBody()));
            r.setCharSet("UTF-8");
            r.setMimeType(
                t.getMimeType().getPrimaryType(), t.getMimeType().getSubType());
            r.setExpiry(s.getCacheDuration());

            return r;
        } catch (final RestException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
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
                return render((FileDto) s);
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


    /**
     * Render the resource, as a response.
     *
     * @return A response representing the resource.
     */
    public Response render(final PageDelta s) {
        try {
            final UUID tId = s.getTemplate();
            final TemplateDelta t = _templates.templateDelta(tId);
            final Response r =
                new Response(new PageBody(t.getBody()));
            r.setCharSet("UTF-8");
            r.setMimeType(
                t.getMimeType().getPrimaryType(), t.getMimeType().getSubType());
            r.setExpiry(s.getCacheDuration());

            return r;
        } catch (final RestException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }


    /**
     * Render the resource, as a response.
     *
     * @return A response representing the resource.
     */
    public Response render(final FolderDto s) {
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


    /**
     * Render the resource, as a response.
     *
     * @return A response representing the resource.
     */
    public Response render(final FileDto s) {
        final Response r = new Response(new FileBody(s.getData()));
        r.setDescription(s.getDescription());
        r.setDisposition("inline; filename=\""+s.getName()+"\"");
        r.setMimeType(
            s.getMimeType().getPrimaryType(), s.getMimeType().getSubType());
        r.setLength(s.getSize());
        r.setExpiry(s.getCacheDuration());

        return r;
    }


    /**
     * Render the resource, as a response.
     *
     * @return A response representing the resource.
     */
    public Response render(final AliasDto s) {
        if (null==s.getTargetPath()) {
            throw new NotFoundException();
        }
        throw new RedirectRequiredException(
            s.getTargetPath());
    }
}
