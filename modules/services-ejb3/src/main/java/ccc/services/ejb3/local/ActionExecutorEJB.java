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

import java.util.Date;

import org.apache.log4j.Logger;

import ccc.commons.DBC;
import ccc.domain.Action;
import ccc.services.ActionExecutor;
import ccc.services.api.CommandFailedException;
import ccc.services.api.Commands;
import ccc.services.api.ID;


/**
 * Implementation of the {@link ActionExecutor} interface.
 *
 * @author Civic Computing Ltd.
 */
public class ActionExecutorEJB implements ActionExecutor {
    private static final Logger LOG =
        Logger.getLogger(ActionExecutorEJB.class.getName());

    private final Commands _commands;

    /**
     * Constructor.
     *
     * @param commands The commands implementation.
     */
    public ActionExecutorEJB(final Commands commands) {
        DBC.require().notNull(commands);
        _commands = commands;
    }


    /** {@inheritDoc} */
    @Override
    public void executeAction(final Action action) {
        try{
            switch (action.type()) {

                case RESOURCE_UNPUBLISH:
                    executeUnpublish(action);
                    break;

                case RESOURCE_PUBLISH:
                    executePublish(action);
                    break;

                case PAGE_UPDATE:
                    executeUpdate(action);
                    break;

                default:
                    throw new UnsupportedOperationException(
                        "Unsupported action type: "+action.type());

            }
            action.complete();
            LOG.info("Completed action: "+action.id());

        } catch (final CommandFailedException e) {
            fail(action, e);
        }
    }


    private void fail(final Action action, final CommandFailedException e) {
        action.fail(e);
        LOG.info(
            "Failed action: "+action.id()
            +" [CommandFailedException was "
            +e.getFailure().getExceptionId()+"]");
    }


    private void executeUpdate(final Action action)
                                                 throws CommandFailedException {
        _commands.applyWorkingCopy(
            new ID(action.subject().id().toString()),
            new ID(action.actor().id().toString()),
            new Date(),
            action.isMajorEdit(),
            action.getComment());
    }


    private void executePublish(final Action action)
                                                 throws CommandFailedException {
        _commands.publish(
            new ID(action.subject().id().toString()),
            new ID(action.actor().id().toString()),
            new Date());
    }


    private void executeUnpublish(final Action action)
                                                 throws CommandFailedException {
        _commands.unpublish(
            new ID(action.subject().id().toString()),
            new ID(action.actor().id().toString()),
            new Date());
    }
}
