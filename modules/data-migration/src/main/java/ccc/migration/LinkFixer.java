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
package ccc.migration;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

import ccc.commons.XHTML;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class LinkFixer {
    private static Logger log = Logger.getLogger(LinkFixer.class);

    private void extractURLs(final Map<String, StringBuffer> map) {

        for (final Map.Entry<String, StringBuffer> para : map.entrySet()) {
            final String html =
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<head><title>Title goes here</title></head>"
                + "<body>"
                + para.getValue()
                + "</body>"
                + "</html>";
            log.debug(para.getValue());
            try {
                final NodeList l =
                    XHTML.evaluateXPathToNodeList(
                        new ByteArrayInputStream(html.getBytes()),
                    "//xhtml:a");
                for(int i=0; i<l.getLength(); i++) {
                    log.error(l.item(i).getAttributes().getNamedItem("href"));
                }
            } catch (final RuntimeException e) {
                log.error("Error parsing page.");
            }
        }
    }
}
