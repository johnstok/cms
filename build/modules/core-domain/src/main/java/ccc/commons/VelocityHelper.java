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
package ccc.commons;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import ccc.domain.Resource;
import ccc.domain.ResourcePath;


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
    public List<Resource> selectPathElements(final Resource resource) {

        final List<Resource> elements = new ArrayList<Resource>();

        Resource current = resource;

        elements.add(current);
        while (current.parent() != null) {
            current = current.parent();
            elements.add(current);
        }
        Collections.reverse(elements);
        return elements;
    }



}
