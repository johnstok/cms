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
package ccc.types;

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

    /** PATH_PATTERN : Pattern. */
    public static final Pattern PATH_PATTERN =
        Pattern.compile("(/"+ResourceName.VALID_CHARACTERS+")*");
    /** TOKEN_PATTERN : String. */
    public static final String TOKEN_PATTERN =
        "/("+ResourceName.VALID_CHARACTERS+")";
    private final List<ResourceName> _elements;

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
        DBC.require().notNull(pathString);

        Matcher m = PATH_PATTERN.matcher(pathString);

        if (!m.matches()) {
            throw new IllegalArgumentException(
                pathString
                +" does not match the regular expression: "
                +PATH_PATTERN);
        }

        final List<ResourceName> parts = new ArrayList<ResourceName>();

        m = Pattern.compile(TOKEN_PATTERN).matcher(pathString);
        while (m.find()) {
            parts.add(
                new ResourceName(pathString.substring(m.start()+1, m.end())));
        }
        _elements = unmodifiableList(parts);
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
        parts.addAll(stem._elements);
        parts.add(newElement);

        _elements = unmodifiableList(parts);
    }

    /**
     * Constructor.
     */
    public ResourcePath() {
        _elements = unmodifiableList(new ArrayList<ResourceName>());
    }

    /**
     * Constructor.
     * Constructs a path with a single element.
     *
     * @param name The name of the path element.
     */
    public ResourcePath(final ResourceName name) {
        DBC.require().notNull(name);

        final List<ResourceName> parts = new ArrayList<ResourceName>();
        parts.add(name);
        _elements = unmodifiableList(parts);
    }

    /**
     * Constructor.
     *
     * @param elements A list of resource names.
     */
    public ResourcePath(final List<ResourceName> elements) {
        DBC.require().notNull(elements);
        _elements = unmodifiableList(new ArrayList<ResourceName>(elements));
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

        for (final ResourceName element : _elements) {
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
        return _elements; // Already unmodifiable.
    }

    /**
     * Returns list of all but topmost elements. In case of
     * root/empty path empty list is returned.
     *
     * @return A list of resource names.
     */
    public List<ResourceName> elementsToTop() {
        final List<ResourceName> limitedElements =
            new ArrayList<ResourceName>(_elements);

        if (limitedElements.size() > 1) {
            limitedElements.remove(limitedElements.size()-1);
        }

        return limitedElements;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result =
            prime
            * result
            + ((_elements == null) ? 0 : _elements.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ResourcePath other = (ResourcePath) obj;
        if (_elements == null) {
            if (other._elements != null) {
                return false;
            }
        } else if (!_elements.equals(other._elements)) {
            return false;
        }
        return true;
    }

    /**
     * Return a path minus the top most (i.e. root) element.
     *
     * @return Resource path.
     */
    public ResourcePath removeTop() {
        final List<ResourceName> limitedElements =
            new ArrayList<ResourceName>(_elements);
        limitedElements.remove(0);
        return new ResourcePath(limitedElements);
    }

    /**
     * Return the top.
     *
     * @return The name for the resource at the top of the path.
     */
    public ResourceName top() { // Rename to 'start()'
        return (0==_elements.size()) ? null : _elements.get(0);
    }
}
