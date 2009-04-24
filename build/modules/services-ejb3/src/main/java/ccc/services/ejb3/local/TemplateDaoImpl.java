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
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.services.AuditLog;
import ccc.services.ResourceDao;
import ccc.services.TemplateDao;
import ccc.services.UserManager;
import ccc.services.ejb3.support.BaseDao;
import ccc.services.ejb3.support.Dao;


/**
 * DAO with methods specific to a template.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=TemplateDao.NAME)
@TransactionAttribute(REQUIRED)
@Local(TemplateDao.class)
public class TemplateDaoImpl implements TemplateDao {

    @EJB(name=UserManager.NAME) private UserManager    _users;
    @PersistenceContext private EntityManager _em;
    private ResourceDao _dao;


    /** Constructor. */
    @SuppressWarnings("unused") public TemplateDaoImpl() { super(); }

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
    public Template update(final UUID templateId,
                       final String title,
                       final String description,
                       final String definition,
                       final String body) {

        final Template current = _dao.findLocked(Template.class, templateId);

        current.title(title);
        current.description(description);
        current.definition(definition);
        current.body(body);

        _dao.update(current);

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
        final AuditLog audit = new AuditLogEJB(_em);
        _dao = new ResourceDaoImpl(_users, audit, bdao);
    }
}
