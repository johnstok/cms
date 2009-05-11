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
import ccc.domain.CCCException;
import ccc.domain.Resource;
import ccc.persistence.jpa.BaseDao;
import ccc.services.ActionExecutor;
import ccc.services.AuditLog;
import ccc.services.AuditLogEJB;
import ccc.services.Dao;
import ccc.services.ResourceDao;
import ccc.services.ResourceDaoImpl;
import ccc.services.WorkingCopyManager;
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
    private WorkingCopyManager _wcMgr;

    @PersistenceContext private EntityManager _em;

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

                case UNPUBLISH:
                    executeUnpublish(action);
                    break;

                case PUBLISH:
                    executePublish(action);
                    break;

                case UPDATE:
                    executeUpdate(action);
                    break;

                default:
                    throw new UnsupportedOperationException(
                        "Unsupported action type: "+action.type());

            }
            action.complete();
        } catch (final RuntimeException e) {
            LOG.warn("Failing action.", e);
            action.fail(e);
        }
    }


    private void executeUpdate(final Action action) {
        final Resource r = action.subject();
        if (ResourceType.PAGE.equals(r.type())) {
            _wcMgr.applyWorkingCopy(
                r.id(),
                action.parameters().getString("comment"),
                action.parameters().getBool("majorEdit"),
                action.actor(),
                new Date());  // TODO: Should we use action._executeAfter?
        } else {
            throw new CCCException(
                "Only pages can be updated via the scheduler.");
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

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        final Dao bdao = new BaseDao(_em);
        final AuditLog audit = new AuditLogEJB(bdao);
        _resources = new ResourceDaoImpl(audit, bdao);
        _wcMgr = new WorkingCopyManager(_resources);
    }
}
