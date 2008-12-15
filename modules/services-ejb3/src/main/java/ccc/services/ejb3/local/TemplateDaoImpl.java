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

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;

import ccc.domain.Resource;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.services.AuditLog;
import ccc.services.TemplateDao;
import ccc.services.ejb3.support.BaseResourceDao;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="TemplateDao")
@TransactionAttribute(REQUIRED)
@Local(TemplateDao.class)
public class TemplateDaoImpl extends BaseResourceDao implements TemplateDao {

    /** Constructor. */
    @SuppressWarnings("unused") private TemplateDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param _em
     * @param _al
     */
    public TemplateDaoImpl(final EntityManager em, final AuditLog al) {
        _em = em;
        _audit = al;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final UUID parentId,
                       final Template template) {
        create(parentId, (Resource) template);
    }

    /** {@inheritDoc} */
    @Override
    public Template update(final UUID templateId,
                       final long templateVersion,
                       final String title,
                       final String description,
                       final String definition,
                       final String body) {

        final Template current = find(Template.class,
                                      templateId,
                                      templateVersion);

        current.title(title);
        current.description(description);
        current.definition(definition);
        current.body(body);
        _audit.recordUpdate(current);
        return current;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Template> allTemplates() {
        return list("allTemplates", Template.class);
    }

    /** {@inheritDoc} */
    @Override
    public boolean nameExists(final ResourceName templateName) {
        return exists(find("templateByName", Template.class, templateName));
    }
}
