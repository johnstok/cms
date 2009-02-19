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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ccc.domain.CCCException;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Snapshot;
import ccc.domain.Template;
import ccc.services.PageDao;
import ccc.services.ResourceDao;


/**
 * DAO with methods specific to a page.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name="PageDao")
@TransactionAttribute(REQUIRED)
@Local(PageDao.class)
public class PageDaoImpl implements PageDao {

    @EJB(name="ResourceDao") private ResourceDao _dao;


    /** Constructor. */
    @SuppressWarnings("unused") public PageDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     */
    public PageDaoImpl(final ResourceDao dao) {
        _dao = dao;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final UUID id,
                       final String newTitle,
                       final Set<Paragraph> newParagraphs) {

        final Page page = _dao.findLocked(Page.class, id);

        page.title(newTitle);
        page.deleteAllParagraphs();

        // TODO: check domain model
        if (page.workingCopy() != null) {
            page.clearWorkingCopy();
        }

        for (final Paragraph paragraph : newParagraphs) {
            page.addParagraph(paragraph);
        }

        final Template template = page.computeTemplate(null);

        if (template != null) {
            validateFieldsForPage(newParagraphs, template.definition());
        }
        _dao.update(page);
    }

    /** {@inheritDoc} */
    @Override
    public void create(final UUID id, final Page page) {
        final Template template = page.computeTemplate(null);

        if (template != null) {
            validateFieldsForPage(page.paragraphs(), template.definition());

        }
        _dao.create(id, page);
    }

    private void validateFieldsForPage(final Set<Paragraph> delta,
                                final String t) {
        final List<String>  errors = validateFields(delta, t);
        if (!errors.isEmpty()) {
            final StringBuffer sb = new StringBuffer();
            for (final String error : errors) {
                sb.append(error);
                sb.append(" ");
            }
            throw new CCCException(
                "Field validation failed: "+sb.toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public List<String> validateFields(final Set<Paragraph> delta,
                                       final String t) {

        Document document;
        final List<String> errors = new ArrayList<String>();

        final DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final InputSource s =
                new InputSource(new StringReader(t));
            document = builder.parse(s);
            final NodeList nl = document.getElementsByTagName("field");
            for (int n = 0;  n < nl.getLength(); n++) {
                final NamedNodeMap nnm = nl.item(n).getAttributes();
                final Node regexp = nnm.getNamedItem("regexp");
                final Node name = nnm.getNamedItem("name");
                if (regexp != null && name != null) {
                    for (final Paragraph para : delta) {
                        if (name.getNodeValue().equals(para.name())
                            && !para.text().matches(regexp.getNodeValue())
                            && ("TEXT".equals(para.type().name())
                            || "HTML".equals(para.type().name()))) {
                            errors.add(para.name());
                        }
                    }
                }
            }
        } catch (final Exception e) {
            throw new CCCException("Error with XML parsing ", e);
        }
        return errors;

    }


    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final UUID id,
                                  final String newTitle,
                                  final Set<Paragraph> newParagraphs) {
        final Page page = _dao.findLocked(Page.class, id);
        // TODO: check domain model
        if (page.workingCopy() == null) {
            page.createWorkingCopy();
        }

        final Page temp = new Page(newTitle);
        for (final Paragraph paragraph : newParagraphs) {
            temp.addParagraph(paragraph);
        }
        final Snapshot workingCopy = temp.createSnapshot();
        page.workingCopy(workingCopy);
//        _dao.update(page); Not necessary?
    }


    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final UUID id) {
        final Page page = _dao.findLocked(Page.class, id);
        page.clearWorkingCopy();
    }

}
