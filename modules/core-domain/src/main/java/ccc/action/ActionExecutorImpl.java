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
import java.util.Map;
import java.util.UUID;

import ccc.rest.RestException;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.CommandType;
import ccc.types.DBC;


/**
 * Implementation of the {@link ActionExecutor} interface.
 *
 * @author Civic Computing Ltd.
 */
public class ActionExecutorImpl implements ActionExecutor {

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
    public void executeAction(final UUID subjectId,
                              final UUID actorId,
                              final CommandType command,
                              final Map<String, String> params)
                                                        throws RestException {
        switch (command) {

            case RESOURCE_UNPUBLISH:
                executeUnpublish(subjectId, actorId, params);
                break;

            case RESOURCE_PUBLISH:
                executePublish(subjectId, actorId, params);
                break;

            case PAGE_UPDATE:
                executeUpdate(subjectId, actorId, params);
                break;

            case RESOURCE_DELETE:
                executeDelete(subjectId, actorId, params);
                break;

            default:
                throw new UnsupportedOperationException(
                    "Unsupported action type: "+command);

        }
    }


    private void executeDelete(final UUID subjectId,
                               final UUID actorId,
                               final Map<String, String> params)
                                                        throws RestException {
        _resourcesExt.delete(subjectId, actorId, new Date());
    }


    private void executeUpdate(final UUID subjectId,
                               final UUID actorId,
                               final Map<String, String> params)
                                                         throws RestException {
        _resourcesExt.applyWorkingCopy(
            subjectId,
            actorId,
            new Date(),
            Boolean.valueOf(params.get("MAJOR")).booleanValue(),
            params.get("COMMENT"));
    }


    private void executePublish(final UUID subjectId,
                                final UUID actorId,
                                final Map<String, String> params)
                                                         throws RestException {
        _resourcesExt.publish(subjectId, actorId, new Date());
    }


    private void executeUnpublish(final UUID subjectId,
                                  final UUID actorId,
                                  final Map<String, String> params)
                                                         throws RestException {
        _resourcesExt.unpublish(subjectId, actorId, new Date());
    }
}
