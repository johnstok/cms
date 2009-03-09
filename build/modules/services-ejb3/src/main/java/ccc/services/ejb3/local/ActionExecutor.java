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
import ccc.services.ResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="ActionExecutor")
@TransactionAttribute(REQUIRED)
@Local(IActionExecutor.class)
public class ActionExecutor implements IActionExecutor {

    @EJB(name="ResourceDao") private ResourceDao _resources;

    /** Constructor. */
    @SuppressWarnings("unused") public ActionExecutor() { super(); }


    /** {@inheritDoc} */
    @Override
    public void executeAction(final Action action) {
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
    }


    private void executePublish(final Action action) {
        _resources.publish(
            action.parameters().getUuid("resource"),
            action.actor(),
            new Date());  // TODO: Should we use action._executeAfter?
    }


    private void executeUnpublish(final Action action) {
        _resources.unpublish(
            action.parameters().getUuid("resource"),
            action.actor(),
            new Date()); // TODO: Should we use action._executeAfter?
    }
}
