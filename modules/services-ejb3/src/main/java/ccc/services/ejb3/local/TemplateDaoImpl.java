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

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;

import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.services.ResourceDao;
import ccc.services.TemplateDao;


/**
 * DAO with methods specific to a template.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="TemplateDao")
@TransactionAttribute(REQUIRED)
@Local(TemplateDao.class)
public class TemplateDaoImpl implements TemplateDao {

    @EJB(name="ResourceDao") private ResourceDao _dao;


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
}
