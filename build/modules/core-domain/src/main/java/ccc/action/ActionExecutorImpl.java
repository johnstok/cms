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
package ccc.action;

import java.util.Date;

import org.apache.log4j.Logger;

import ccc.domain.Action;
import ccc.rest.CommandFailedException;
import ccc.rest.Commands;
import ccc.types.DBC;
import ccc.types.ID;


/**
 * Implementation of the {@link ActionExecutor} interface.
 *
 * @author Civic Computing Ltd.
 */
public class ActionExecutorImpl implements ActionExecutor {
    private static final Logger LOG =
        Logger.getLogger(ActionExecutorImpl.class.getName());

    private final Commands _commands;

    /**
     * Constructor.
     *
     * @param commands The commands implementation.
     */
    public ActionExecutorImpl(final Commands commands) {
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
            Boolean.valueOf(action.parameters().get("MAJOR")).booleanValue(),
            action.parameters().get("COMMENT"));
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
