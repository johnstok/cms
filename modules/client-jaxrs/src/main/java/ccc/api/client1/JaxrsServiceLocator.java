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
package ccc.api.client1;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import ccc.rest.Actions;
import ccc.rest.Aliases;
import ccc.rest.Comments;
import ccc.rest.Files;
import ccc.rest.Folders;
import ccc.rest.Groups;
import ccc.rest.Pages;
import ccc.rest.Resources;
import ccc.rest.SearchEngine;
import ccc.rest.Security;
import ccc.rest.ServiceLocator;
import ccc.rest.Templates;
import ccc.rest.Users;
import ccc.rest.providers.AclReader;
import ccc.rest.providers.ActionSummaryCollectionReader;
import ccc.rest.providers.ActionSummaryReader;
import ccc.rest.providers.AliasDeltaReader;
import ccc.rest.providers.BooleanProvider;
import ccc.rest.providers.CommentReader;
import ccc.rest.providers.DurationReader;
import ccc.rest.providers.FailureWriter;
import ccc.rest.providers.GroupCollectionReader;
import ccc.rest.providers.GroupReader;
import ccc.rest.providers.JsonReader;
import ccc.rest.providers.JsonableWriter;
import ccc.rest.providers.MetadataWriter;
import ccc.rest.providers.PageDeltaReader;
import ccc.rest.providers.ResSummaryReader;
import ccc.rest.providers.ResourceSummaryCollectionReader;
import ccc.rest.providers.RevisionSummaryCollectionReader;
import ccc.rest.providers.StringCollectionWriter;
import ccc.rest.providers.TemplateDeltaReader;
import ccc.rest.providers.UUIDProvider;
import ccc.rest.providers.UserSummaryCollectionReader;
import ccc.rest.providers.UserSummaryReader;
import ccc.rest.providers.UuidCollectionWriter;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class JaxrsServiceLocator implements ServiceLocator {

    private static final Logger LOG =
        Logger.getLogger(JaxrsServiceLocator.class);

    static {
        final ResteasyProviderFactory pFactory =
            ResteasyProviderFactory.getInstance();
        RegisterBuiltin.register(pFactory);

        // Writers
        pFactory.addMessageBodyWriter(JsonableWriter.class);
        pFactory.addMessageBodyWriter(StringCollectionWriter.class);
        pFactory.addMessageBodyWriter(MetadataWriter.class);
        pFactory.addMessageBodyWriter(JsonReader.class);
        pFactory.addMessageBodyWriter(UUIDProvider.class);
        pFactory.addMessageBodyWriter(UuidCollectionWriter.class);

        // Readers
        pFactory.addMessageBodyReader(ResourceSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(ResSummaryReader.class);
        pFactory.addMessageBodyReader(DurationReader.class);
        pFactory.addMessageBodyReader(UserSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(UserSummaryReader.class);
        pFactory.addMessageBodyReader(BooleanProvider.class);
        pFactory.addMessageBodyReader(FailureWriter.class);
        pFactory.addMessageBodyReader(StringCollectionWriter.class);
        pFactory.addMessageBodyReader(MetadataWriter.class);
        pFactory.addMessageBodyReader(ActionSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(AliasDeltaReader.class);
        pFactory.addMessageBodyReader(RevisionSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(UUIDProvider.class);
        pFactory.addMessageBodyReader(TemplateDeltaReader.class);
        pFactory.addMessageBodyReader(PageDeltaReader.class);
        pFactory.addMessageBodyReader(ActionSummaryReader.class);
        pFactory.addMessageBodyReader(CommentReader.class);
        pFactory.addMessageBodyReader(GroupCollectionReader.class);
        pFactory.addMessageBodyReader(GroupReader.class);
        pFactory.addMessageBodyReader(AclReader.class);
        pFactory.addMessageBodyReader(UuidCollectionWriter.class);

        // String Converters
        pFactory.addStringConverter(UUIDProvider.class);
    }

    private final Resources _commands;
    private final Users _users;
    private final Actions _actions;
    private final Folders _folders;
    private final Pages _pages;
    private final Security _security;
    private final Templates _templates;
    private final Comments  _comments;
    private final Files _files;
    private final Aliases _aliases;
    private final Groups _groups;
    private final SearchEngine _search;

    private final String     _secure;
    private final String     _public;
    private final String     _hostUrl;
    private final String     _upload;
    private final HttpClient _httpClient;


    public JaxrsServiceLocator(final HttpClient _http, final String hostUrl) {
        _httpClient = _http;
        _hostUrl = hostUrl;
        _secure = _hostUrl+"/ccc/api/secure";
        _public = _hostUrl+"/ccc/api/public";
        _upload = _hostUrl+"/ccc/upload";

        LOG.debug("Secure URL: "+_secure);
        LOG.debug("Public URL: "+_public);
        LOG.debug("Upload URL: "+_upload);

        _commands  = ProxyFactory.create(Resources.class, _secure, _http);
        _users     = ProxyFactory.create(Users.class, _secure, _http);
        _actions   = ProxyFactory.create(Actions.class, _secure, _http);
        _folders   = ProxyFactory.create(Folders.class, _secure, _http);
        _pages     = ProxyFactory.create(Pages.class, _secure, _http);
        _security  = ProxyFactory.create(Security.class, _public, _http);
        _templates = ProxyFactory.create(Templates.class, _secure, _http);
        _comments  = ProxyFactory.create(Comments.class, _secure, _http);
        _files     = ProxyFactory.create(Files.class, _secure+"/files", _http);
        _groups    = ProxyFactory.create(Groups.class, _secure, _http);
        _aliases =
            ProxyFactory.create(Aliases.class, _secure+"/aliases", _http);
        _search    =
            ProxyFactory.create(SearchEngine.class, _secure+"/search", _http);
    }


    public JaxrsServiceLocator(final String hostUrl) {
        this(new HttpClient(), hostUrl);
    }

    /** {@inheritDoc} */
    @Override
    public Actions getActions() { return _actions; }

    /** {@inheritDoc} */
    @Override
    public Comments getComments() { return _comments; }

    /** {@inheritDoc} */
    @Override
    public Files getFiles() { return _files; }

    /** {@inheritDoc} */
    @Override
    public Folders getFolders() { return _folders; }

    /** {@inheritDoc} */
    @Override
    public Pages getPages() { return _pages; }

    /** {@inheritDoc} */
    @Override
    public Groups getGroups() { return _groups; }

    /** {@inheritDoc} */
    @Override
    public Resources getResources() { return _commands; }

    /** {@inheritDoc} */
    @Override
    public SearchEngine getSearch() { return _search; }

    /** {@inheritDoc} */
    @Override
    public Templates getTemplates() { return _templates; }

    /** {@inheritDoc} */
    @Override
    public Users getUsers() { return _users; }

    /** {@inheritDoc} */
    @Override
    public Aliases getAliases() { return _aliases; }


    /** {@inheritDoc} */
    @Override
    public Security getSecurity() { return _security; }

    public IFileUploader getFileUploader() {
        return new FileUploader(_httpClient, _upload);
    }
}
