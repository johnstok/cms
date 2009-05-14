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
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;

import ccc.commands.ApplyWorkingCopyCommand;
import ccc.commands.PublishCommand;
import ccc.commands.UnpublishResourceCommand;
import ccc.domain.Action;
import ccc.domain.CCCException;
import ccc.domain.RemoteExceptionSupport;
import ccc.domain.Resource;
import ccc.persistence.jpa.BaseDao;
import ccc.services.ActionExecutor;
import ccc.services.AuditLog;
import ccc.services.AuditLogEJB;
import ccc.services.api.ResourceType;


/**
 * EJB implementation of the {@link ActionExecutor} interface.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=ActionExecutor.NAME)
@TransactionAttribute(REQUIRED)
@Local(ActionExecutor.class)
public class ActionExecutorEJB implements ActionExecutor {
    private static final Logger LOG =
        Logger.getLogger(ActionExecutorEJB.class.getName());

    @PersistenceContext private EntityManager _em;

    private BaseDao _bdao;
    private AuditLog _audit;

    /** Constructor. */
    public ActionExecutorEJB() { super(); }


    /**
     * Constructor.
     *
     * @param em The entityManager for this action executor.
     */
    public ActionExecutorEJB(final EntityManager em) {
        _em = em;
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

        } catch (final RuntimeException e) {
            fail(action, e);
        } catch (final RemoteExceptionSupport e) {
            fail(action, e);
        }
    }


    private void fail(final Action action, final Exception e) {
        LOG.warn("Failing action.", e);
        action.fail(e);
    }


    private void executeUpdate(final Action action)
                                                 throws RemoteExceptionSupport {
        final Resource r = action.subject();
        if (ResourceType.PAGE.equals(r.type())) {
            new ApplyWorkingCopyCommand(_bdao, _audit).execute(
                action.actor(),
                new Date(),
                r.id(),
                action.getComment(),
                action.isMajorEdit());

        } else {
            throw new CCCException(
                "Only pages can be updated via the scheduler.");
        }
    }


    private void executePublish(final Action action)
                                                 throws RemoteExceptionSupport {
        new PublishCommand(_audit).execute(action, new Date());
    }


    private void executeUnpublish(final Action action)
                                                 throws RemoteExceptionSupport {
        new UnpublishResourceCommand(_bdao, _audit).execute(
            action.actor(),
            new Date(),
            action.subject().id());
    }


    /**
     * TODO: Add a description of this method.
     * TODO: Rename this method.
     */
    @PostConstruct
    void configureCoreData() {
        _bdao = new BaseDao(_em);
        _audit = new AuditLogEJB(_bdao);
    }
}
