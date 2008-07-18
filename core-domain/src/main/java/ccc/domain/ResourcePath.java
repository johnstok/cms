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

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class models an absolute path to a resource.
 *
 * @author Civic Computing Ltd
 */
public final class ResourcePath {

    private final List<ResourceName> elements;
    /** PATH_PATTERN : Pattern. */
    public static final Pattern PATH_PATTERN = Pattern.compile("(/(\\w+))+");

    /**
     * Constructor.
     *
     * Converts a string into a ResourcePath, performing validation. A valid
     * path should match the java.util.regex regular expression defined by
     * {@value ResourcePath#PATH_PATTERN}.
     *
     * @param pathString The absolute resource path, represented as a string.
     */
    public ResourcePath(final String pathString) {

        Matcher m = PATH_PATTERN.matcher(pathString);

        if (!m.matches()) {
            throw new RuntimeException(
                pathString
                +" does not match the regular expression: "
                +PATH_PATTERN);
        }

        List<ResourceName> parts = new ArrayList<ResourceName>();

        m = Pattern.compile("/(\\w+)").matcher(pathString);
        while (m.find()) {
            parts.add(
                new ResourceName(pathString.substring(m.start()+1, m.end())));
        }
        elements = unmodifiableList(parts);
    }

    /**
     * Copy Constructor.
     *
     * @param stem
     * @param newElement
     */
    private ResourcePath(final ResourcePath stem,
                         final ResourceName newElement) {
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
     * Constructor.
     *
     * @param name
     */
    public ResourcePath(ResourceName name) {
        List<ResourceName> parts = new ArrayList<ResourceName>();
        parts.add(name);
        elements = unmodifiableList(parts);
    }

    /**
     * Append a resource name to the end of an existing path. For example, with
     * an existing path '/foo' calling append('bar') will return a new path
     * object that represents '/foo/bar'.
     *
     * @param resourceName The name of the resource to append.
     * @return A new resource path.
     */
    public ResourcePath append(final ResourceName resourceName) {
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

    /**
     * TODO: Add a description of this method.
     *
     * @return
     */
    public List<ResourceName> elements() {
        return elements; // Already unmodifiable.
    }


}
