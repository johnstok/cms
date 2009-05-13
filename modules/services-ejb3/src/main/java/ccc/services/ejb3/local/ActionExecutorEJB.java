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

import ccc.actions.Action;
import ccc.actions.ApplyWorkingCopyCommand;
import ccc.domain.CCCException;
import ccc.domain.LockMismatchException;
import ccc.domain.Resource;
import ccc.domain.UnlockedException;
import ccc.persistence.jpa.BaseDao;
import ccc.services.ActionExecutor;
import ccc.services.AuditLogEJB;
import ccc.services.ResourceDao;
import ccc.services.ResourceDaoImpl;
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

    private ResourceDao        _resources;

    @PersistenceContext private EntityManager _em;

    private BaseDao _bdao;

    private AuditLogEJB _audit;

    /** Constructor. */
    public ActionExecutorEJB() { super(); }


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
        } catch (final UnlockedException e) {
            fail(action, e);
        } catch (final LockMismatchException e) {
            fail(action, e);
        }
    }


    private void fail(final Action action, final Exception e) {
        LOG.warn("Failing action.", e);
        action.fail(e);
    }


    private void executeUpdate(final Action action)
                               throws UnlockedException, LockMismatchException {
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
                               throws UnlockedException, LockMismatchException {
        _resources.publish(
            action.subject().id(),
            action.actor().id(),
            new Date());
    }


    private void executeUnpublish(final Action action)
                               throws UnlockedException, LockMismatchException {
        _resources.unpublish(
            action.subject().id(),
            action.actor().id(),
            new Date());
    }


    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        _bdao = new BaseDao(_em);
        _audit = new AuditLogEJB(_bdao);
        _resources = new ResourceDaoImpl(_audit, _bdao);
    }
}
