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
import ccc.rest.RestException;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.DBC;


/**
 * Implementation of the {@link ActionExecutor} interface.
 *
 * @author Civic Computing Ltd.
 */
public class ActionExecutorImpl implements ActionExecutor {
    private static final Logger LOG =
        Logger.getLogger(ActionExecutorImpl.class.getName());

    private final ResourcesExt _resourcesExt;

    /**
     * Constructor.
     *
     * @param resourcesExt The commands implementation.
     */
    public ActionExecutorImpl(final ResourcesExt resourcesExt) {
        DBC.require().notNull(resourcesExt);
        _resourcesExt = resourcesExt;
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

                case RESOURCE_DELETE:
                    executeDelete(action);
                    break;

                default:
                    throw new UnsupportedOperationException(
                        "Unsupported action type: "+action.type());

            }
            action.complete();
            LOG.info("Completed action: "+action.id());

        } catch (final RestException e) {
            fail(action, e);
        }
    }


    private void fail(final Action action, final RestException e) {
        action.fail(e.getFailure());
        LOG.info(
            "Failed action: "+action.id()
            +" [CommandFailedException was "
            +e.getFailure().getExceptionId()+"]");
    }


    private void executeDelete(final Action action) throws RestException {
        _resourcesExt.delete(action.subject().id(),
                             action.actor().id(),
                             new Date());
    }


    private void executeUpdate(final Action action)
                                                 throws RestException {
        _resourcesExt.applyWorkingCopy(
            action.subject().id(),
            action.actor().id(),
            new Date(),
            Boolean.valueOf(action.parameters().get("MAJOR")).booleanValue(),
            action.parameters().get("COMMENT"));
    }


    private void executePublish(final Action action)
                                                 throws RestException {
        _resourcesExt.publish(
            action.subject().id(),
            action.actor().id(),
            new Date());
    }


    private void executeUnpublish(final Action action)
                                                 throws RestException {
        _resourcesExt.unpublish(
            action.subject().id(),
            action.actor().id(),
            new Date());
    }
}
