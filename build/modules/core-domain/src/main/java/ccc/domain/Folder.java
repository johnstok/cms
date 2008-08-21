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

import ccc.commons.DBC;
import ccc.commons.JSON;


/**
 * A folder that can contain other resources.
 *
 * @author Civic Computing Ltd
 */
public final class Folder extends Resource {

    /** serialVersionUID : long. */
    private static final long serialVersionUID = 2800863951865644693L;
    private List<Resource> _entries = new ArrayList<Resource>();

    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    protected Folder() { super(); }

    /**
     * Constructor.
     *
     * @param name The unique name for this resource.
     */
    public Folder(final ResourceName name) {
        super(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType type() {
        return ResourceType.FOLDER;
    }

    /**
     * Accessor for size.
     *
     * @return The number of resources contained by this folder.
     */
    public int size() {
        return _entries.size();
    }

    /**
     * Add a resource to this folder.
     *
     * @param resource The resource to add.
     */
    public void add(final Resource resource) {
        DBC.require().notNull(resource);
        if (hasEntryWithName(resource.name())) {
            throw new CCCException(
                "Folder already contains a resource with name '"
                + resource.name()
                + "'.");
        }
        _entries.add(resource);
    }

    /**
     * Accessor for entries.
     *
     * @return A list of all the resources in this folder.
     */
    public List<Resource> entries() {
        return unmodifiableList(_entries);
    }

    /**
     * Navigate from this folder to another resource described by the
     * specified path.
     *
     * @param <T> The type of the resource at the specified path.
     * @param path The path to a resource, relative to this folder.
     * @return The resource at the specified path.
     */
    @SuppressWarnings("unchecked")
    public <T extends Resource> T navigateTo(final ResourcePath path) {

        Resource currentPosition = this;

        for (final ResourceName name : path.elements()) {

            if (ResourceType.FOLDER != currentPosition.type()) {
                throw new CCCException(
                    currentPosition.name()
                        +" in path "+path
                        +" is not a folder.");
            }

            currentPosition =
                currentPosition.as(Folder.class).findEntryByName(name);

        }
        return (T) currentPosition; // TODO: Work out why we need a cast here...
    }

    /**
     * Find the entry in this folder with the specified name.
     * Throws a CCCException if no resource exists with the specified name.
     *
     * @param name The name of the resource.
     * @return The resource with the specified name.
     */
    public Resource findEntryByName(final ResourceName name) {
        for (final Resource entry : _entries) {
            if (entry.name().equals(name)) {
                return entry;
            }
        }
        throw new CCCException("No entry '"+name+"' in folder '"+name()+"'");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toJSON() {
        return
            JSON.object()
                .add("name", name().toString())
                .add("displayTemplateName",
                     (null==displayTemplateName())
                         ? null : displayTemplateName().name().toString())
                .add("entries", entryReferences())
                .toString();
    }

    /**
     * Retrieve a list of references, one for each entry in this folder.
     *
     * @return A list of resource references.
     */
    public List<ResourceRef> entryReferences() {

        final List<ResourceRef> resourceRefs = new ArrayList<ResourceRef>();
        for (final Resource entry : _entries) {
            final ResourceRef ref =
                new ResourceRef(entry.name(), entry.id(), entry.type());
            if(entry.type() == ResourceType.FOLDER) {
                ref.addMetadata(
                    "folder-count",
                    String.valueOf(entry.as(Folder.class).folderCount()));
            }
            resourceRefs.add(ref);
        }
        return resourceRefs;
    }

    /**
     * Query method - returns the number of folders that are children of this
     * folder.
     *
     * @return The number of folders that are children of this folder, as an
     *      integer.
     */
    public int folderCount() {
        int count = 0;
        for (final Resource entry : _entries) {
            if (entry.type()==ResourceType.FOLDER) { count++; }
        }
        return count;
    }
//  /**
//  * TODO: Add a description of this method.
//  *
//  * @param rootFolder
//  */
// private static void prettyPrint(Folder rootFolder) {
//     int indent = 0;
//     System.out.println(rootFolder.name());
//     prettyPrint(rootFolder.entries(), indent+2);
// }
//
// /**
//  * TODO: Add a description of this method.
//  *
//  * @param entries
//  * @param i
//  */
// private static void prettyPrint(List<Resource> entries, int i) {
//     for (Resource entry : entries) {
//         for (int a=0 ; a < i ; a++) {
//             System.out.print(" ");
//         }
//         System.out.println(entry.name());
//         if (entry.type() == ResourceType.FOLDER) {
//             prettyPrint(entry.asFolder().entries(), i+2);
//         }
//     }
// }

    /**
     * Query whether this folder has an entry with the specified name.
     *
     * @param resourceName The name of the resource.
     * @return True if an entry exists, false otherwise.
     */
    public boolean hasEntryWithName(final ResourceName resourceName) {
        for (final Resource entry : entries()) {
            if (entry.name().equals(resourceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieve the entries in this folder in a type-safe list.
     *
     * @param resourceType A class representing the type of resources in this
     *      folder.
     * @param <T> The type of the resources in this folder.
     * @return The entries in this folder as a type-safe list.
     */
    public <T extends Resource> List<T> entries(final Class<T> resourceType) {
        final List<T> entries = new ArrayList<T>();
        for (final Resource entry : _entries) {
            entries.add(entry.as(resourceType));
        }
        return entries;
    }

    /**
     * Query method to determine if this folder contains any pages.
     *
     * @return true if the folder contains any pages, false otherwise.
     */
    public boolean hasPages() {
        for (final Resource r : _entries) {
            if (r.type().equals(ResourceType.PAGE)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Accessor method for the first page in this folder.
     *
     * @return The first page in the list of entries.
     */
    public Page firstPage() {
        for (final Resource r : _entries) {
            if (r.type().equals(ResourceType.PAGE)) {
                return r.as(Page.class);
            }
        }
        throw new CCCException("No pages in this folder.");
    }
}
