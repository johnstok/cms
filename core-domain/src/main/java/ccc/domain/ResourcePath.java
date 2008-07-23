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

import static java.util.Collections.*;

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

        final List<ResourceName> parts = new ArrayList<ResourceName>();

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
        final List<ResourceName> parts = new ArrayList<ResourceName>();
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
     * Constructs a path with a single element.
     *
     * @param name The name of the path element.
     */
    public ResourcePath(final ResourceName name) {
        final List<ResourceName> parts = new ArrayList<ResourceName>();
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
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder toString = new StringBuilder();

        for (final ResourceName element : elements) {
            toString.append('/');
            toString.append(element.toString());
        }

        return toString.toString();
    }

    /**
     * Query method.
     * Returns the elements of this path as a list of
     * {@link ResourceName names}.
     *
     * @return A list of resource names, representing the path.
     */
    public List<ResourceName> elements() {
        return elements; // Already unmodifiable.
    }

    /**
     * Returns list of all but topmost elements. In case of
     * root/empty path empty list is returned.
     *
     * @return A list of resource names.
     */
    public List<ResourceName> elementsToTop() {
        final List<ResourceName> limitedElements =
            new ArrayList<ResourceName>(elements);

        if (limitedElements.size() > 1) {
            limitedElements.remove(limitedElements.size()-1);
        }

        return limitedElements;
    }


}
