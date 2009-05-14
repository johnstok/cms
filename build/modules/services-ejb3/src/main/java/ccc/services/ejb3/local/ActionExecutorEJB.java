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

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.domain.Action;
import ccc.domain.CCCException;
import ccc.domain.Resource;
import ccc.persistence.jpa.BaseDao;
import ccc.services.ActionExecutor;
import ccc.services.api.CommandFailedException;
import ccc.services.api.Commands;
import ccc.services.api.ID;
import ccc.services.api.ResourceType;


/**
 * EJB implementation of the {@link ActionExecutor} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=ActionExecutor.NAME)
@TransactionAttribute(REQUIRED)
@Local(ActionExecutor.class)
@RunAs("CONTENT_CREATOR")
@RolesAllowed({"ADMINISTRATOR"})
public class ActionExecutorEJB implements ActionExecutor {
    private static final Logger LOG =
        Logger.getLogger(ActionExecutorEJB.class.getName());

    @PersistenceContext private EntityManager _em;
    @EJB(name=Commands.NAME) private transient Commands _commands;

    private BaseDao _bdao;

    /** Constructor. */
    public ActionExecutorEJB() { super(); }


    /**
     * Constructor.
     *
     * @param em The entityManager for this action executor.
     * @param commands The commands implementation.
     */
    public ActionExecutorEJB(final EntityManager em, final Commands commands) {
        _em = em;
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
        final Resource r = action.subject();
        if (ResourceType.PAGE.equals(r.type())) {
//            new ApplyWorkingCopyCommand(_bdao, _audit).execute(
//                action.actor(),
//                new Date(),
//                r.id(),
//                action.getComment(),
//                action.isMajorEdit());

        } else {
            throw new CCCException(
                "Only pages can be updated via the scheduler.");
        }
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
//        _commands.unpublish(
//            new ID(action.subject().id().toString()),
//            new ID(action.actor().id().toString()),
//            new Date());
    }


    /**
     * TODO: Add a description of this method.
     * TODO: Rename this method.
     */
    @PostConstruct
    void configureCoreData() {
        _bdao = new BaseDao(_em);
    }
}
