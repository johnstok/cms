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
import org.jboss.resteasy.client.core.executors.ApacheHttpClientExecutor;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import ccc.api.jaxrs.ActionsImpl;
import ccc.api.jaxrs.AliasesImpl;
import ccc.api.jaxrs.CommentsImpl;
import ccc.api.jaxrs.FilesImpl;
import ccc.api.jaxrs.FoldersImpl;
import ccc.api.jaxrs.GroupsImpl;
import ccc.api.jaxrs.PagesImpl;
import ccc.api.jaxrs.ResourcesImpl;
import ccc.api.jaxrs.SearchImpl;
import ccc.api.jaxrs.SecurityImpl2;
import ccc.api.jaxrs.TemplatesImpl;
import ccc.api.jaxrs.UsersImpl;
import ccc.api.jaxrs.providers.FileProvider;
import ccc.api.jaxrs.providers.MetadataProvider;
import ccc.api.jaxrs.providers.S11nProvider;
import ccc.api.jaxrs.providers.StringCollectionProvider;
import ccc.api.jaxrs.providers.UUIDProvider;
import ccc.api.jaxrs.providers.UuidCollectionProvider;
import ccc.api.synchronous.Actions;
import ccc.api.synchronous.Aliases;
import ccc.api.synchronous.Comments;
import ccc.api.synchronous.Files;
import ccc.api.synchronous.Folders;
import ccc.api.synchronous.Groups;
import ccc.api.synchronous.Pages;
import ccc.api.synchronous.Resources;
import ccc.api.synchronous.SearchEngine;
import ccc.api.synchronous.Security;
import ccc.api.synchronous.ServiceLocator;
import ccc.api.synchronous.Templates;
import ccc.api.synchronous.Users;


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
        pFactory.addMessageBodyWriter(StringCollectionProvider.class);
        pFactory.addMessageBodyWriter(MetadataProvider.class);
        pFactory.addMessageBodyWriter(UUIDProvider.class);
        pFactory.addMessageBodyWriter(UuidCollectionProvider.class);
        pFactory.addMessageBodyWriter(FileProvider.class);
        pFactory.addMessageBodyWriter(S11nProvider.class);

        // Readers
        pFactory.addMessageBodyReader(UUIDProvider.class);
        pFactory.addMessageBodyReader(StringCollectionProvider.class);
        pFactory.addMessageBodyReader(MetadataProvider.class);
        pFactory.addMessageBodyReader(UuidCollectionProvider.class);
        pFactory.addMessageBodyReader(S11nProvider.class);

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

        _commands = new ResourcesImpl(createProxy(Resources.class, _api));
        _users = new UsersImpl(createProxy(Users.class, _api));
        _actions  =
            new ActionsImpl(
                createProxy(
                    Actions.class,
                    _api + ccc.api.synchronous.ResourceIdentifiers
                                              .Action
                                              .COLLECTION));
        _folders = new FoldersImpl(createProxy(Folders.class, _api));
        _pages = new PagesImpl(createProxy(Pages.class, _api));
        _security = new SecurityImpl2(createProxy(Security.class, _api));
        _templates = new TemplatesImpl(createProxy(Templates.class, _api));
        _comments = new CommentsImpl(createProxy(Comments.class, _api));
        _files = new FilesImpl(createProxy(Files.class, _api));
        _groups = new GroupsImpl(createProxy(Groups.class, _api));
        _aliases = new AliasesImpl(createProxy(Aliases.class, _api));
        _search =
            new SearchImpl(
                createProxy(
                    SearchEngine.class,
                    _api
                        + ccc.api.synchronous.ResourceIdentifiers
                                             .SearchEngine
                                             .COLLECTION));
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


    private <T> T createProxy(final Class<T> clazz, final String uri) {
        return
            ProxyFactory.create(
                clazz,
                ProxyFactory.createUri(uri),
                new ApacheHttpClientExecutor(_httpClient),
                ResteasyProviderFactory.getInstance(),
                new EnhancedEntityExtractorFactory());
    }
}
