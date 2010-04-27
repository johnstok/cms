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
package ccc.web;

import java.util.UUID;

import ccc.api.core.Alias;
import ccc.api.core.File;
import ccc.api.core.Files;
import ccc.api.core.Folder;
import ccc.api.core.Page;
import ccc.api.core.Resource;
import ccc.api.core.Resources;
import ccc.api.core.Template;
import ccc.api.core.Templates;
import ccc.api.exceptions.CCException;
import ccc.plugins.scripting.Script;
import ccc.web.rendering.FileBody;
import ccc.web.rendering.NotFoundException;
import ccc.web.rendering.PageBody;
import ccc.web.rendering.RedirectRequiredException;
import ccc.web.rendering.Response;
import ccc.web.rendering.ScriptBody;
import ccc.web.rendering.SearchBody;


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
    public Response render(final Resource s) {
        switch (s.getType()) {
            case ALIAS:
                return render((Alias) s);
            case FILE:
                final File f = (File) s;
                if (f.isText() && f.isExecutable()) {
                    return invoke(f);
                }
                return render(f);
            case FOLDER:
                return render((Folder) s);
            case PAGE:
                return render((Page) s);
            case SEARCH:
                return renderSearch(s);
            default:
                throw new NotFoundException();
        }
    }


    private Response renderSearch(final Resource s) {
        try {
            final UUID tId = s.getTemplate();
            final Template t = _templates.templateDelta(tId);
            final Response r =
                new Response(
                    new SearchBody(
                        new Script(t.getBody(), tId.toString())));
            r.setCharSet("UTF-8");
            r.setMimeType(t.getMimeType());
            r.setExpiry(s.getCacheDuration());
            return r;

        } catch (final CCException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }


    private Response invoke(final File f) {
        try {
            final File tf = _files.get(f.getId());
            return
                new Response(
                    new ScriptBody(
                        new Script(tf.getContent(), tf.getId().toString())));
        } catch (final CCException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }


    private Response render(final Page s) {
        try {
            final UUID tId = s.getTemplate();
            final Template t = _templates.templateDelta(tId);
            final Response r =
                new Response(
                    new PageBody(
                        new Script(t.getBody(), tId.toString())));
            r.setCharSet("UTF-8");
            r.setMimeType(t.getMimeType());
            r.setExpiry(s.getCacheDuration());
            if (s.isCacheable()) {
                r.setEtag(s.getId()+"-"+s.getRevision(), true);
            }
            return r;

        } catch (final CCException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }
    }


    private Response render(final Folder s) {
        try {
            if (null!= s.getIndexPage()) {
                throw new RedirectRequiredException(
                    _resources.getAbsolutePath(s.getIndexPage()));
            } else if (null != s.getDefaultPage()) {
                throw new RedirectRequiredException(
                    _resources.getAbsolutePath(s.getDefaultPage()));
            }

        } catch (final CCException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException(e);
        }

        throw new NotFoundException();
    }


    private Response render(final File s) {
        final Response r = new Response(new FileBody(s));
        r.setDescription(s.getDescription());
        r.setDisposition("inline; filename=\""+s.getName()+"\"");
        r.setMimeType(s.getMimeType());
        r.setLength(s.getSize());
        r.setExpiry(s.getCacheDuration());
        if (s.isCacheable()) {
            r.setEtag(s.getId()+"-"+s.getRevision(), false);
            r.setLastModified(s.getDateChanged());
        }
        return r;
    }


    private Response render(final Alias s) {
        if (null==s.getTargetPath()) { throw new NotFoundException(); }
        throw new RedirectRequiredException(s.getTargetPath());
    }
}
