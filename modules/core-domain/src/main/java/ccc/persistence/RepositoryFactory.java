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
     * TODO: Add a description for this method.
     *
     * @return
     */
    public LogEntryRepository createLogEntryRepo() {
        return new LogEntryRepositoryImpl(_em);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public UserRepository createUserRepo() {
        return new UserRepositoryImpl(_em);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public ResourceRepository createResourceRepository() {
        return new ResourceRepositoryImpl(_em);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public DataRepository createDataRepository() {
        return DataRepositoryImpl.onFileSystem(_em);
    }

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    public ActionRepository createActionRepository() {
        return new ActionRepositoryImpl(_em);
    }

}
