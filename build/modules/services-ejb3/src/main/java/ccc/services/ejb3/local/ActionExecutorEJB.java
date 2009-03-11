/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3.local;

import static javax.ejb.TransactionAttributeType.*;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.actions.Action;
import ccc.services.ActionExecutor;
import ccc.services.ResourceDao;


/**
 * EJB implementation of the {@link ActionExecutor} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="ActionExecutor")
@TransactionAttribute(REQUIRED)
@Local(ActionExecutor.class)
public class ActionExecutorEJB implements ActionExecutor {

    @EJB(name="ResourceDao") private ResourceDao _resources;

    /** Constructor. */
    @SuppressWarnings("unused") public ActionExecutorEJB() { super(); }


    /**
     * Constructor.
     *
     * @param rdao The resource DAO for this executor.
     */
    public ActionExecutorEJB(final ResourceDao rdao) {
        _resources = rdao;
    }


    /** {@inheritDoc} */
    @Override
    public void executeAction(final Action action) {
        try{
            switch (action.type()) {

                case UNPUBLISH:
                    executeUnpublish(action);
                    break;

                case PUBLISH:
                    executePublish(action);
                    break;

                default:
                    throw new UnsupportedOperationException(
                        "Unsupported action type: "+action.type());

            }
            action.complete();
        } catch (final RuntimeException e) {
            action.fail(e);
        }
    }


    private void executePublish(final Action action) {
        _resources.publish(
            action.subject().id(),
            action.actor().id(),
            new Date());  // TODO: Should we use action._executeAfter?
    }


    private void executeUnpublish(final Action action) {
        _resources.unpublish(
            action.subject().id(),
            action.actor().id(),
            new Date()); // TODO: Should we use action._executeAfter?
    }
}
