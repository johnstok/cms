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
package ccc.content.velocity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ccc.api.Paragraph;
import ccc.api.ResourceType;
import ccc.commons.XHTML;
import ccc.domain.Page;
import ccc.domain.ResourcePath;
import ccc.snapshots.FolderSnapshot;
import ccc.snapshots.ResourceSnapshot;


/**
 * Collection of helper methods for velocity templates.
 *
 * @author Civic Computing Ltd.
 */
public class VelocityHelper {

    /**
     * Returns instance of Random initialised with current time.
     *
     * @return Instance of Random.
     */
    public Random random() {
        return new Random(new Date().getTime());
    }

    /**
     * Create ResourcePath object from the path.
     *
     * @param path The path as a String.
     * @return ResourcePath
     */
    public ResourcePath path(final String path) {
        return new ResourcePath(path);
    }

    /**
     * Returns current year.
     *
     * @return Current year as a String.
     */
    public String currentYear() {
       final Calendar cal = Calendar.getInstance();
       return ""+cal.get(Calendar.YEAR);
    }

    /**
     * Creates a list of resources from root till given resource.
     *
     * @param resource The resource.
     * @return A list of resources.
     */
    public List<ResourceSnapshot> selectPathElements(
        final ResourceSnapshot resource) {

        final List<ResourceSnapshot> elements =
            new ArrayList<ResourceSnapshot>();

        ResourceSnapshot current = resource;

        elements.add(current);
        while (current.parent() != null) {
            current = current.parent().forCurrentRevision();
            elements.add(current);
        }
        Collections.reverse(elements);
        return elements;
    }

    /**
     * Helper method for content index displaying.
     *
     * @param folder The folder the get pages from.
     * @param displayLimit Number of pages to display. -1 for no limit.
     * @param contentElements A list of paragraph names to load.
     * @return A list of maps containing paragraph name and text for each page.
     */
    public List<Map<String, String>> selectPagesForContentIndex(
                                           final ResourceSnapshot folder,
                                           final int displayLimit,
                                           final List<String> contentElements) {
        final List<Map<String, String>> elements =
            new ArrayList<Map<String, String>>();

        if (folder.type() !=  ResourceType.FOLDER) {
            return null;
        }

        final FolderSnapshot f  = (FolderSnapshot) folder;

        int c = 0;
        for (final Page page : f.pages()) {
            if (displayLimit == -1 || c < displayLimit) {
                final Map<String, String> map = new HashMap<String, String>();
                for (final Paragraph para : page.currentRevision().paragraphs()) {
                    if (contentElements.contains(para.name())) {
                        map.put(para.name(), para.text());
                    }
                }
                elements.add(map);
                c++;
            }
        }
        return elements;
    }

    /**
     * Escape strings that will be rendered in the main body of an HTML
     * document.
     *
     * @param unescaped The un-escaped string.
     * @return The escaped string.
     */
    public String escape(final String unescaped) {
        if (null==unescaped) {
            return null;
        }
        return XHTML.escape(unescaped);
    }
}
