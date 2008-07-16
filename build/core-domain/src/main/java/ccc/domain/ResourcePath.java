/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import java.util.ArrayList;
import static java.util.Collections.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class models an absolute path to a resource.
 *
 * @author Civic Computing Ltd
 */
public class ResourcePath {
    
    private final List<ResourceName> elements;
    private final Pattern PATH_PATTERN = Pattern.compile("(/(\\w+))+");
    
    /**
     * Constructor.
     *
     * @param pathString
     */
    public ResourcePath(String pathString) {
        
        Matcher m = PATH_PATTERN.matcher(pathString);
        
        if (!m.matches()) throw new RuntimeException(pathString+" does not match the regular expression: "+PATH_PATTERN);
        
        List<ResourceName> parts = new ArrayList<ResourceName>();

        m = Pattern.compile("/(\\w+)").matcher(pathString);
        while (m.find()) {
            parts.add(new ResourceName(pathString.substring(m.start()+1, m.end())));
        }
        elements = unmodifiableList(parts);
    }
    
    /**
     * Copy Constructor.
     *
     * @param stem
     * @param newElement
     */
    private ResourcePath(ResourcePath stem, ResourceName newElement) {
        List<ResourceName> parts = new ArrayList<ResourceName>();
        parts.addAll(stem.elements);
        parts.add(newElement);
        
        elements = unmodifiableList(parts);
    }

    /**
     * Constructor.
     */
    public ResourcePath() {
        elements = unmodifiableList(new ArrayList<ResourceName>());
    }

    /**
     * Append a resource name to an existing path.
     *
     * @param resourceName
     */
    public ResourcePath append(ResourceName resourceName) {
        return new ResourcePath(this, resourceName);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder toString = new StringBuilder();
        
        for (ResourceName element : elements) {
            toString.append('/');
            toString.append(element.toString());
        }
        
        return toString.toString();
    }

    
}
