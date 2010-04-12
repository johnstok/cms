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
package ccc.api.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.log4j.Logger;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import ccc.api.ActionScheduler;
import ccc.api.Actions;
import ccc.api.Aliases;
import ccc.api.Comments;
import ccc.api.Files;
import ccc.api.Folders;
import ccc.api.Groups;
import ccc.api.Pages;
import ccc.api.Resources;
import ccc.api.SearchEngine;
import ccc.api.Security;
import ccc.api.ServiceLocator;
import ccc.api.Templates;
import ccc.api.Users;
import ccc.api.jaxrs.ActionsImpl;
import ccc.api.jaxrs.AliasesImpl;
import ccc.api.jaxrs.CommentsImpl;
import ccc.api.jaxrs.FilesImpl;
import ccc.api.jaxrs.FoldersImpl;
import ccc.api.jaxrs.GroupsImpl;
import ccc.api.jaxrs.PagesImpl;
import ccc.api.jaxrs.SearchImpl;
import ccc.api.jaxrs.SecurityImpl2;
import ccc.api.jaxrs.TemplatesImpl;
import ccc.api.jaxrs.UsersImpl;
import ccc.api.jaxrs.providers.AclReader;
import ccc.api.jaxrs.providers.ActionSummaryCollectionReader;
import ccc.api.jaxrs.providers.ActionSummaryReader;
import ccc.api.jaxrs.providers.AliasDeltaReader;
import ccc.api.jaxrs.providers.BooleanProvider;
import ccc.api.jaxrs.providers.CommentReader;
import ccc.api.jaxrs.providers.DtoCollectionReader;
import ccc.api.jaxrs.providers.DurationReader;
import ccc.api.jaxrs.providers.FailureWriter;
import ccc.api.jaxrs.providers.GroupCollectionReader;
import ccc.api.jaxrs.providers.GroupReader;
import ccc.api.jaxrs.providers.JsonReader;
import ccc.api.jaxrs.providers.Jsonable2Reader;
import ccc.api.jaxrs.providers.JsonableWriter;
import ccc.api.jaxrs.providers.MetadataWriter;
import ccc.api.jaxrs.providers.PageDeltaReader;
import ccc.api.jaxrs.providers.ResSummaryReader;
import ccc.api.jaxrs.providers.ResourceSummaryCollectionReader;
import ccc.api.jaxrs.providers.RevisionSummaryCollectionReader;
import ccc.api.jaxrs.providers.StringCollectionWriter;
import ccc.api.jaxrs.providers.TemplateDeltaReader;
import ccc.api.jaxrs.providers.TemplateSummaryCollectionReader;
import ccc.api.jaxrs.providers.UUIDProvider;
import ccc.api.jaxrs.providers.UserSummaryCollectionReader;
import ccc.api.jaxrs.providers.UserSummaryReader;
import ccc.api.jaxrs.providers.UuidCollectionWriter;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ProxyServiceLocator implements ServiceLocator {

    private static final Logger LOG =
        Logger.getLogger(ProxyServiceLocator.class);

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
        pFactory.addMessageBodyReader(DtoCollectionReader.class);
        pFactory.addMessageBodyReader(Jsonable2Reader.class);
        pFactory.addMessageBodyReader(TemplateSummaryCollectionReader.class);

        // String Converters
        pFactory.addStringConverter(UUIDProvider.class);
    }

    private final Resources _commands;
    private final Users _users;
    private final ActionsImpl _actions;
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


    public ProxyServiceLocator(final HttpClient http, final String hostUrl) {
        _httpClient = http;
        _hostUrl = hostUrl;
        _secure = _hostUrl+"/ccc/api/secure";
        _public = _hostUrl+"/ccc/api/public";
        _upload = _hostUrl+"/ccc/upload";

        LOG.debug("Secure URL: "+_secure);
        LOG.debug("Public URL: "+_public);
        LOG.debug("Upload URL: "+_upload);

        _commands  =
            new ResourcesDecorator(
                ProxyFactory.create(
                    Resources.class, _secure+"/resources", http),
                    _secure,
                    http);
        _users =
            new UsersImpl(
                ProxyFactory.create(
                    Users.class, _secure+"/users", http));
        _actions  =
            new ActionsImpl(
                ProxyFactory.create(
                    Actions.class, _secure+"/actions", http),
                ProxyFactory.create(
                    ActionScheduler.class, _secure+"/actions", http));
        _folders =
            new FoldersImpl(
                ProxyFactory.create(
                    Folders.class, _secure+"/folders", http));
        _pages =
            new PagesImpl(
                ProxyFactory.create(
                    Pages.class, _secure+"/pages", http));
        _security =
            new SecurityImpl2(
                ProxyFactory.create(
                    Security.class, _public, http));
        _templates =
            new TemplatesImpl(
                ProxyFactory.create(
                    Templates.class, _secure+"/templates", http));
        _comments =
            new CommentsImpl(
                ProxyFactory.create(Comments.class, _secure+"/comments", http));
        _files =
            new FilesImpl(
                ProxyFactory.create(
                    Files.class, _secure+"/files", http));
        _groups =
            new GroupsImpl(
                ProxyFactory.create(
                    Groups.class, _secure+"/groups", http));
        _aliases =
            new AliasesImpl(
                ProxyFactory.create(
                    Aliases.class, _secure+"/aliases", http));
        _search =
            new SearchImpl(
                ProxyFactory.create(
                    SearchEngine.class, _secure+"/search", http));
    }


    public ProxyServiceLocator(final String hostUrl) {
        this(new HttpClient(), hostUrl);
    }

    /** {@inheritDoc} */
    @Override
    public Actions getActions() { return _actions; }

    /** {@inheritDoc} */
    @Override
    public ActionScheduler lookupActionScheduler() { return _actions; }

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

    @Deprecated
    public IFileUploader getFileUploader() {
        return new FileUploader(_httpClient, _upload);
    }
}
