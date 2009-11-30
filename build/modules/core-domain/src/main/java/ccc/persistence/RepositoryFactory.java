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
package ccc.persistence;

import javax.persistence.EntityManager;

import ccc.types.DBC;


/**
 * A factory for repository objects.
 *
 * @author Civic Computing Ltd.
 */
public class RepositoryFactory {

    private final EntityManager _em;

    /**
     * Constructor.
     *
     * @param em The entity manager for this factory.
     */
    public RepositoryFactory(final EntityManager em) {
        DBC.require().notNull(em);
        _em = em;
    }

    /**
     * Create an instance of the {@link LogEntryRepository} interface.
     *
     * @return A repository instance.
     */
    public LogEntryRepository createLogEntryRepo() {
        return new LogEntryRepositoryImpl(_em);
    }

    /**
     * Create an instance of the {@link UserRepository} interface.
     *
     * @return A repository instance.
     */
    public UserRepository createUserRepo() {
        return new UserRepositoryImpl(_em);
    }

    /**
     * Create an instance of the {@link ResourceRepository} interface.
     *
     * @return A repository instance.
     */
    public ResourceRepository createResourceRepository() {
        return new ResourceRepositoryImpl(_em);
    }

    /**
     * Create an instance of the {@link DataRepository} interface.
     *
     * @return A repository instance.
     */
    public DataRepository createDataRepository() {
        return DataRepositoryImpl.onFileSystem(_em);
    }

    /**
     * Create an instance of the {@link ActionRepository} interface.
     *
     * @return A repository instance.
     */
    public ActionRepository createActionRepository() {
        return new ActionRepositoryImpl(_em);
    }

}
