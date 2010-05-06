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

import ccc.api.core.Actions;
import ccc.api.core.Aliases;
import ccc.api.core.Comments;
import ccc.api.core.Files;
import ccc.api.core.Folders;
import ccc.api.core.Groups;
import ccc.api.core.Pages;
import ccc.api.core.Resources;
import ccc.api.core.SearchEngine;
import ccc.api.core.Security;
import ccc.api.core.ServiceLocator;
import ccc.api.core.Templates;
import ccc.api.core.Users;
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
import ccc.api.jaxrs.providers.ActionSummaryReader;
import ccc.api.jaxrs.providers.BooleanProvider;
import ccc.api.jaxrs.providers.DtoCollectionReader;
import ccc.api.jaxrs.providers.FileReader;
import ccc.api.jaxrs.providers.JsonableCollectionWriter;
import ccc.api.jaxrs.providers.JsonableWriter;
import ccc.api.jaxrs.providers.MetadataWriter;
import ccc.api.jaxrs.providers.StringCollectionWriter;
import ccc.api.jaxrs.providers.UUIDProvider;
import ccc.api.jaxrs.providers.UserSummaryCollectionReader;
import ccc.api.jaxrs.providers.UuidCollectionWriter;


/**
 * {@link ServiceLocator} implementation that uses RESTEasy proxies.
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
        pFactory.addMessageBodyWriter(UUIDProvider.class);
        pFactory.addMessageBodyWriter(UuidCollectionWriter.class);
        pFactory.addMessageBodyWriter(FileReader.class);

        // Readers
        pFactory.addMessageBodyReader(UUIDProvider.class);
        pFactory.addMessageBodyReader(BooleanProvider.class);
        pFactory.addMessageBodyReader(JsonableWriter.class);
        pFactory.addMessageBodyReader(JsonableCollectionWriter.class);

        pFactory.addMessageBodyReader(UserSummaryCollectionReader.class);
        pFactory.addMessageBodyReader(StringCollectionWriter.class);
        pFactory.addMessageBodyReader(MetadataWriter.class);
        pFactory.addMessageBodyReader(ActionSummaryReader.class);
        pFactory.addMessageBodyReader(UuidCollectionWriter.class);
        pFactory.addMessageBodyReader(DtoCollectionReader.class);

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

    private final String     _api;
    private final String     _hostUrl;
    private final HttpClient _httpClient;


    /**
     * Constructor.
     *
     * @param hostUrl The base URL for the CC server.
     */
    public ProxyServiceLocator(final String hostUrl) {
        _httpClient = new HttpClient();
        _hostUrl = hostUrl;
        _api = _hostUrl+"/ccc/api";

        LOG.debug("API URL: "+_api);

        _commands  =
            new ResourcesDecorator(
                ProxyFactory.create(
                    Resources.class,
                    _api, _httpClient),
                _api,
                _httpClient);
        _users =
            new UsersImpl(
                ProxyFactory.create(
                    Users.class,
                    _api, _httpClient));
        _actions  =
            new ActionsImpl(
                ProxyFactory.create(
                    Actions.class,
                    _api, _httpClient));
        _folders =
            new FoldersImpl(
                ProxyFactory.create(
                    Folders.class,
                    _api, _httpClient));
        _pages =
            new PagesImpl(
                ProxyFactory.create(
                    Pages.class,
                    _api, _httpClient));
        _security =
            new SecurityImpl2(
                ProxyFactory.create(
                    Security.class,
                    _api, _httpClient));
        _templates =
            new TemplatesImpl(
                ProxyFactory.create(
                    Templates.class,
                    _api, _httpClient));
        _comments =
            new CommentsImpl(
                ProxyFactory.create(
                    Comments.class,
                    _api, _httpClient));
        _files =
            new FilesImpl(
                ProxyFactory.create(
                    Files.class,
                    _api, _httpClient));
        _groups =
            new GroupsImpl(
                ProxyFactory.create(
                    Groups.class,
                    _api, _httpClient));
        _aliases =
            new AliasesImpl(
                ProxyFactory.create(
                    Aliases.class,
                    _api, _httpClient));
        _search =
            new SearchImpl(
                ProxyFactory.create(
                    SearchEngine.class,
                    _api+ccc.api.core.ResourceIdentifiers.SearchEngine.COLLECTION, _httpClient));
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


    /**
     * Accessor.
     *
     * @return A browser for the site.
     */
    public SiteBrowser getBrowser() {
        return new SiteBrowserImpl(_httpClient, _hostUrl);
    }


    /**
     * Accessor.
     *
     * @return The HTTP client for this service locator.
     */
    public HttpClient getHttpClient() { return _httpClient; }
}
