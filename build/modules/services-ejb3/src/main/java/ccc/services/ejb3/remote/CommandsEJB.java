/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3.remote;

import static javax.ejb.TransactionAttributeType.*;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.services.ResourceDAOLocal;
import ccc.services.api.Commands;
import ccc.services.api.PageDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="PublicCommands")
@TransactionAttribute(REQUIRED)
@Remote(Commands.class)
public class CommandsEJB
    extends
        ModelTranslation
    implements
        Commands {

    @EJB(name="ResourceDAO", beanInterface=ResourceDAOLocal.class)
    private ResourceDAOLocal _resources;

    /** {@inheritDoc} */
    @Override
    public void createAlias(final String parentId,
                            final String name,
                            final String targetId) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary createFolder(final String parentId,
                                      final String name) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void createPage(final String parentId,
                           final PageDelta delta,
                           final String templateId) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void createTemplate(final String parentId,
                               final TemplateDelta delta) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void createUser(final UserDelta delta) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary lock(final String resourceId) {
        return map(_resources.lock(resourceId));
    }

    /** {@inheritDoc} */
    @Override
    public void move(final String resourceId,
                     final long version,
                     final String newParentId) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary publish(final String resourceId) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void rename(final String resourceId,
                       final long version,
                       final String name) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary unlock(final String resourceId) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary unpublish(final String resourceId) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void updateAlias(final String aliasId,
                            final long version,
                            final String targetId) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void updatePage(final String pageId,
                           final long version,
                           final PageDelta delta) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void updateResourceTemplate(final String resourceId,
                                       final long version,
                                       final String templateId) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void updateTags(final String resourceId,
                           final long version,
                           final String tags) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public ResourceSummary updateTemplate(final String templateId,
                               final long version,
                               final TemplateDelta delta) {

        throw new UnsupportedOperationException("Method not implemented.");
    }

    /** {@inheritDoc} */
    @Override
    public void updateUser(final String userId,
                           final long version,
                           final UserDelta delta) {

        throw new UnsupportedOperationException("Method not implemented.");
    }
}
