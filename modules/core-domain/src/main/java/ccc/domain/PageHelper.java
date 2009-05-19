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

package ccc.domain;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ccc.api.PageDelta;
import ccc.api.Paragraph;
import ccc.api.ParagraphType;
import ccc.commons.WordCharFixer;


/**
 * Helper class for manipulating pages.
 * TODO: these methods could be moved to the {@link Page} class.
 *
 * @author Civic Computing Ltd.
 */
public class PageHelper {

    public void validateFieldsForPage(final Set<Paragraph> delta,
                                      final String t) {
        final List<String> errors = validateFields(delta, t);
        if (!errors.isEmpty()) {
            final StringBuffer sb = new StringBuffer();
            for (final String error : errors) {
                sb.append(error);
                sb.append(" ");
            }
            throw new CCCException("Field validation failed: " + sb.toString());
        }
    }

    public List<String> validateFields(final Collection<Paragraph> delta,
                                       final String t) {
        Document document;
        final List<String> errors = new ArrayList<String>();

        final DocumentBuilderFactory factory =
            DocumentBuilderFactory.newInstance();
        try {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final InputSource s = new InputSource(new StringReader(t));
            document = builder.parse(s);
            final NodeList nl = document.getElementsByTagName("field");
            for (int n = 0; n < nl.getLength(); n++) {
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
        } catch (final ParserConfigurationException e) {
            throw new CCCException("Error with XML parsing ", e);
        } catch (final SAXException e) {
            throw new CCCException("Error with XML parsing ", e);
        } catch (final IOException e) {
            throw new CCCException("Error with XML parsing ", e);
        }
        return errors;
    }


    public void assignParagraphs(final Page page, final PageDelta delta) {

        page.deleteAllParagraphs();

        for (final Paragraph para : delta.getParagraphs()) {

            if (ParagraphType.TEXT==para.type()) {
                    final WordCharFixer fixer = new WordCharFixer();
                    final Paragraph p =
                        Paragraph.fromText(para.name(), fixer.fix(para.text()));
                    page.addParagraph(para);
            } else {
                page.addParagraph(para);
            }
        }
    }
}
