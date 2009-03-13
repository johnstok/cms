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
import java.util.Date;
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
import ccc.domain.User;
import ccc.services.ISearch;
import ccc.services.PageDao;
import ccc.services.ResourceDao;
import ccc.services.UserManager;


/**
 * DAO with methods specific to a page.
 *
 * @author Civic Computing Ltd.
 */
@Stateless(name=PageDao.NAME)
@TransactionAttribute(REQUIRED)
@Local(PageDao.class)
public class PageDaoImpl implements PageDao {

    @EJB(name=ResourceDao.NAME)  private ResourceDao  _dao;
    @EJB(name=ISearch.NAME)      private ISearch      _search;
    @EJB(name=UserManager.NAME)  private UserManager  _users;


    /** Constructor. */
    @SuppressWarnings("unused") public PageDaoImpl() { super(); }

    /**
     * Constructor.
     *
     * @param dao The ResourceDao used for CRUD operations, etc.
     * @param se The search engine to use.
     * @param _um
     */
    public PageDaoImpl(final ResourceDao dao, final ISearch se, final UserManager um) {
        _dao = dao;
        _search = se;
        _users = um;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void update(final UUID id,
                       final String newTitle,
                       final Set<Paragraph> newParagraphs,
                       final String comment,
                       final boolean isMajorEdit) {

        final Page page = _dao.findLocked(Page.class, id);

        page.title(newTitle);
        page.deleteAllParagraphs();

        for (final Paragraph paragraph : newParagraphs) {
            page.addParagraph(paragraph);
        }

        update(comment, isMajorEdit, page, _users.loggedInUser(), new Date());
    }

    private void update(final String comment,
                        final boolean isMajorEdit,
                        final Page page,
                        final User actor,
                        final Date happenedOn) {

        // TODO: check domain model
        if (page.workingCopy() != null) {
            page.clearWorkingCopy();
        }

        final Template template = page.computeTemplate(null);

        if (template != null) {
            validateFieldsForPage(page.paragraphs(), template.definition());
        }
        _dao.update(page, comment, isMajorEdit, actor, happenedOn);
        _search.update(page);
    }

    /** {@inheritDoc} */
    @Override
    public void create(final UUID id, final Page page) {
        final Template template = page.computeTemplate(null);

        if (template != null) {
            validateFieldsForPage(page.paragraphs(), template.definition());

        }
        _dao.create(id, page);
        _search.add(page);
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
        final Page temp = new Page(newTitle);
        for (final Paragraph paragraph : newParagraphs) {
            temp.addParagraph(paragraph);
        }
        final Snapshot workingCopy = temp.createSnapshot();
        updateWorkingCopy(id, workingCopy);
    }


    /** {@inheritDoc} */
    @Override
    public void updateWorkingCopy(final UUID id, final Snapshot workingCopy) {
        final Page page = _dao.findLocked(Page.class, id);
        if (null==page.workingCopy()) { // FIXME: This is just dumb.
            page.createWorkingCopy();
        }
        page.workingCopy(workingCopy);
    }



    /** {@inheritDoc} */
    @Override
    public void clearWorkingCopy(final UUID id) {
        final Page page = _dao.findLocked(Page.class, id);
        page.clearWorkingCopy();
    }

    /** {@inheritDoc} */
    @Override
    public void applyWorkingCopy(final UUID id,
                                 final String comment,
                                 final boolean isMajorEdit,
                                 final User actor,
                                 final Date happenedOn) {
        final Page page = _dao.findLocked(Page.class, id, actor);
        page.applySnapshot(page.workingCopy());
        update(comment, isMajorEdit, page, actor, happenedOn);
    }
}
