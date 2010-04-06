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

package ccc.persistence;

import javax.persistence.EntityManager;

/**
 * API for creating repositories.
 *
 * @author Civic Computing Ltd.
 */
public interface IRepositoryFactory {

    /**
     * Create an instance of the {@link LogEntryRepository} interface.
     *
     * @return A repository instance.
     */
    LogEntryRepository createLogEntryRepo();


    /**
     * Create an instance of the {@link UserRepository} interface.
     *
     * @return A repository instance.
     */
    UserRepository createUserRepo();


    /**
     * Create an instance of the {@link ResourceRepository} interface.
     *
     * @return A repository instance.
     */
    ResourceRepository createResourceRepository();


    /**
     * Create an instance of the {@link DataRepository} interface.
     *
     * @return A repository instance.
     */
    DataRepository createDataRepository();


    /**
     * Create an instance of the {@link ActionRepository} interface.
     *
     * @return A repository instance.
     */
    ActionRepository createActionRepository();


    /**
     * Create an instance of the {@link CommentRepository} interface.
     *
     * @return A repository instance.
     */
    CommentRepository createCommentRepo();


    /**
     * Create an instance of the {@link GroupRepository} interface.
     *
     * @return A repository instance.
     */
    GroupRepository createGroupRepo();


    /**
     * Factory for {@link RepositoryFactory} objects.
     *
     * @author Civic Computing Ltd.
     */
    public static final class DEFAULT {

        private DEFAULT() { super(); }

        /**
         * Create a repository factory.
         *
         * @param em The entity manager to use.
         *
         * @return A new repository factory.
         */
        public static IRepositoryFactory create(final EntityManager em) {
            return new RepositoryFactory(em);
        }
    }
}
