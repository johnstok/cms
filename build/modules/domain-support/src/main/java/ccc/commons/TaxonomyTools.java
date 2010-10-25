package ccc.commons;

import java.io.CharArrayReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


/**
 * Collection of tools for taxonomy support.
 *
 * @author Civic Computing Ltd.
 */
public final class TaxonomyTools {

    /**
     * Resolves vocabulary's term title against term id.
     *
     * @param vocabulary The XML to parse.
     * @param termID The term id to resolve.
     * @return The title of the term.
     */
    public String resolveTermTitle(final String vocabulary,
                                   final String termID) {
        try {
            final DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Reader reader = new CharArrayReader(vocabulary.toCharArray());
            final Document doc = builder.parse(new InputSource(reader));
            final NodeList list = doc.getElementsByTagName("term");
            for (int a=0; a<list.getLength(); a++) {
                if (list.item(a).getNodeType() == Node.ELEMENT_NODE) {
                    final Element e = (Element) list.item(a);
                    if (null != e && e.getAttribute("id").equals(termID)) {
                        return e.getAttribute("title");
                    }
                }
            }
        } catch (final Exception e) {
            return "fault:"+e; // FIXME
        }
        return null;
    }

    /**
     * Resolves id from the template definition XML.
     *
     * @param fieldName The name of the field.
     * @param definitionXML The definition XML of the template.
     * @return The id of the vocabulary for the field.
     */
    public String resolveVocabularyID(final String fieldName,
                                      final String definitionXML) {
        try {
            final DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Reader reader =
                new CharArrayReader(definitionXML.toCharArray());
            final Document doc = builder.parse(new InputSource(reader));

            final NodeList list = doc.getElementsByTagName("field");
            for (int a=0; a<list.getLength(); a++) {
                if (list.item(a).getNodeType() == Node.ELEMENT_NODE) {
                    final Element e = (Element) list.item(a);
                    if (null != e && e.getAttribute("name").equals(fieldName)) {
                        return e.getAttribute("vocabulary");
                    }
                }
            }
        } catch (final Exception e) {
            return "fault:"+e; // FIXME
        }
        return null;
    }

}
