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

import java.util.Date;
import java.util.List;
import java.util.UUID;

import ccc.domain.Action;
import ccc.domain.EntityNotFoundException;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public interface ActionRepository {

    /**
     * TODO: Add a description for this method.
     *
     * @param since
     *
     * @return
     */
    List<Action> latest(Date since);

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    List<Action> pending();

    /**
     * TODO: Add a description for this method.
     *
     * @return
     */
    List<Action> completed();

    /**
     * TODO: Add a description for this method.
     *
     * @param actionId
     * @throws EntityNotFoundException
     * @return
     */
    Action find(UUID actionId) throws EntityNotFoundException;

    /**
     * TODO: Add a description for this method.
     *
     * @param action
     */
    void create(Action action);

}
