/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.web.jaxrs;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import ccc.api.core.ServiceLocator;
import ccc.api.jaxrs.ActionsImpl;
import ccc.api.jaxrs.AliasesImpl;
import ccc.api.jaxrs.CommentsImpl;
import ccc.api.jaxrs.FilesImpl;
import ccc.api.jaxrs.FoldersImpl;
import ccc.api.jaxrs.GroupsImpl;
import ccc.api.jaxrs.PagesImpl;
import ccc.api.jaxrs.RegistryServiceLocator;
import ccc.api.jaxrs.ResourcesImpl;
import ccc.api.jaxrs.SearchImpl;
import ccc.api.jaxrs.TemplatesImpl;
import ccc.api.jaxrs.UsersImpl;
import ccc.api.jaxrs.providers.FileReader;
import ccc.api.jaxrs.providers.MetadataWriter;
import ccc.api.jaxrs.providers.RestExceptionMapper;
import ccc.api.jaxrs.providers.S11nProvider;
import ccc.api.jaxrs.providers.StringCollectionWriter;
import ccc.api.jaxrs.providers.UUIDProvider;
import ccc.api.jaxrs.providers.UuidCollectionWriter;
import ccc.commons.JNDI;


/**
 * Application def'n for JAX-RS.
 *
 * @author Civic Computing Ltd.
 */
public class JaxrsConfiguration
    extends
        javax.ws.rs.core.Application {

    private final Set<Object> _resources;


    /**
     * Constructor.
     */
    public JaxrsConfiguration() {
        final Set<Object> resources = new HashSet<Object>();
        final ServiceLocator sl =
            new RegistryServiceLocator(CCCProperties.getAppName(), new JNDI());

        resources.add(new FoldersImpl(sl.getFolders()));
        resources.add(new AliasesImpl(sl.getAliases()));
        resources.add(new SearchImpl(sl.getSearch()));
        resources.add(new ResourcesImpl(sl.getResources()));
        resources.add(new PagesImpl(sl.getPages()));
        resources.add(new CommentsImpl(sl.getComments()));
        resources.add(new UsersImpl(sl.getUsers()));
        resources.add(new GroupsImpl(sl.getGroups()));
        resources.add(new TemplatesImpl(sl.getTemplates()));
        resources.add(new FilesImpl(sl.getFiles()));
        resources.add(new ActionsImpl(sl.getActions()));

        _resources = Collections.unmodifiableSet(resources);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> providers = new HashSet<Class<?>>();

        providers.add(RestExceptionMapper.class);
        providers.add(EJBExceptionMapper.class);

        providers.add(SecurityImpl.class);
        providers.add(ServicesImpl.class);

        providers.add(UUIDProvider.class);
        providers.add(MetadataWriter.class);
        providers.add(StringCollectionWriter.class);
        providers.add(UuidCollectionWriter.class);
        providers.add(FileReader.class);
        providers.add(S11nProvider.class);

        return providers;
    }

    /** {@inheritDoc} */
    @Override
    public Set<Object> getSingletons() { return _resources; }
}
