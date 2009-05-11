/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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

import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.persistence.jpa.BaseDao;
import ccc.services.AuditLog;
import ccc.services.AuditLogEJB;
import ccc.services.Dao;
import ccc.services.ResourceDao;
import ccc.services.ResourceDaoImpl;
import ccc.services.TemplateDao;
import ccc.services.api.TemplateDelta;


/**
 * DAO with methods specific to a template.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=TemplateDao.NAME)
@TransactionAttribute(REQUIRED)
@Local(TemplateDao.class)
public class TemplateDaoImpl implements TemplateDao {

    @PersistenceContext private EntityManager _em;
    private ResourceDao _dao;


    /** Constructor. */
    public TemplateDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public TemplateDaoImpl(final ResourceDao dao) {
        _dao = dao;
    }


    /** {@inheritDoc} */
    @Override
    public Template update(final User actor,
                           final UUID templateId,
                           final TemplateDelta delta) {

        final Template current =
            _dao.findLocked(Template.class, templateId, actor);

        current.title(delta.getTitle());
        current.description(delta.getDescription());
        current.definition(delta.getDefinition());
        current.body(delta.getBody());

        _dao.update(actor, current);

        return current;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Template> allTemplates() {
        return _dao.list("allTemplates", Template.class);
    }

    /** {@inheritDoc} */
    @Override
    public boolean nameExists(final ResourceName templateName) {
        return null!=_dao.find("templateByName", Template.class, templateName);
    }

    @PostConstruct @SuppressWarnings("unused")
    private void configureCoreData() {
        final Dao bdao = new BaseDao(_em);
        final AuditLog audit = new AuditLogEJB(bdao);
        _dao = new ResourceDaoImpl(audit, bdao);
    }
}
