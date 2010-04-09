/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ccc.api.dto.FolderDto;
import ccc.api.exceptions.CycleDetectedException;
import ccc.api.exceptions.ResourceExistsException;
import ccc.domain.sorting.Sorter;
import ccc.plugins.s11n.Json;
import ccc.plugins.s11n.JsonKeys;
import ccc.plugins.s11n.Jsonable;
import ccc.types.DBC;
import ccc.types.ResourceName;
import ccc.types.ResourceOrder;
import ccc.types.ResourcePath;
import ccc.types.ResourceType;


/**
 * A folder that can contain other resources.
 *
 * @author Civic Computing Ltd
 */
public final class Folder
    extends
        Resource {

    private Set<Resource> _entries = new HashSet<Resource>();
    private ResourceOrder  _order = ResourceOrder.MANUAL;
    private Page _indexPage = null;


    /** Constructor: for persistence only. */
    protected Folder() { super(); }


    /**
     * Constructor.
     *
     * @param title The title for this resource.
     */
    public Folder(final String title) {
        super(title);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ResourceType getType() {
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
        final Resource existingEntry = getEntryWithName(resource.getName());
        if (null!=existingEntry) {
            throw new ResourceExistsException(
                existingEntry.getId(), existingEntry.getName());
        }
        if (resource instanceof Folder) {
            final Folder folder = (Folder) resource;
            if (folder.isAncestorOf(this)) {
                throw new CycleDetectedException();
            }
        }

        final int nextIndex = maxIndex(_entries)+1;
        _entries.add(resource);
        resource.setParent(this, Integer.valueOf(nextIndex));
    }


    private int maxIndex(final Set<Resource> entries) {
        int nextIndex = 0;
        for (final Resource r : entries) {
            final Integer index = r.getIndex();
            if (null==index) { continue; }
            if (index.intValue()>nextIndex) {
                nextIndex = index.intValue();
            }
        }
        return nextIndex;
    }


    /**
     * Determine if this folder is an ancestor of the specified resource.
     * <ul>
     * <li>Returns false if resource is null.
     * <li>Returns true if this equals the resource.
     * <li>Returns true if this equals a parent of the resource.
     * <li>Otherwise return false.
     * </ul>
     *
     * @param resource The potential child.
     * @return Returns true if the resource is a child of this folder.
     */
    public boolean isAncestorOf(final Resource resource) {
        if (null==resource) {
            return false;
        } else if (equals(resource)) {
            return true;
        } else if (isAncestorOf(resource.getParent())) {
            return true;
        }
        return false;
    }


    /**
     * Accessor for entries.
     *
     * @return A list of all the resources in this folder.
     */
    public List<Resource> getEntries() {
        final List<Resource> entries = new ArrayList<Resource>(_entries);
        Sorter.sort(entries, _order);
        return entries;
    }


    /**
     * Accessor for entries.
     * <p>For example, calling entries(10, 3) will return the resources with
     * positions 20..29 in the list.
     *
     * @param count The number of entries to return.
     * @param page The page of entries to return.
     * @param sortOrder The order in which the entries should be sorted.
     *
     * @return A list of all the resources in this folder.
     */
    public List<Resource> getEntries(final int count,
                                  final int page,
                                  final String sortOrder) {
        DBC.require().greaterThan(0, count);
        DBC.require().greaterThan(0, page);

        final int from = count * (page-1);
        int to = from + count;
        to = (to>_entries.size()) ? _entries.size() : to;

        final List<Resource> entries = new ArrayList<Resource>(_entries);
        Sorter.sort(entries, ResourceOrder.valueOf(sortOrder));

        return entries.subList(from, to);
    }


    /**
     * Navigate from this folder to another resource described by the
     * specified path.
     *
     * @param path The path to a resource, relative to this folder.
     * @return The resource at the specified path.
     */
    public Resource navigateTo(final ResourcePath path) {

        Resource currentPosition = this;

        for (final ResourceName name : path.elements()) {

            if (ResourceType.FOLDER != currentPosition.getType()) {
                throw new RuntimeException(
                    currentPosition.getName()
                        +" in path "+path
                        +" is not a folder.");
            }

            currentPosition =
                currentPosition.as(Folder.class).getEntryWithName2(name);

        }
        return currentPosition;
    }


    /**
     * Find the entry in this folder with the specified name.
     * Throws a CCCException if no resource exists with the specified name.
     *
     * @param name The name of the resource.
     * @return The resource with the specified name.
     */
    public Resource getEntryWithName2(final ResourceName name) {
        for (final Resource entry : _entries) {
            if (entry.getName().equals(name)) {
                return entry;
            }
        }
        throw new RuntimeException(
            "No entry '"+name+"' in folder '"+getName()+"'");
    }


    /**
     * Query method - returns the number of folders that are children of this
     * folder.
     *
     * @return The number of folders that are children of this folder, as an
     *      integer.
     */
    public int getFolderCount() {
        int count = 0;
        for (final Resource entry : _entries) {
            if (entry.getType()==ResourceType.FOLDER) { count++; }
        }
        return count;
    }


    /**
     * Query whether this folder has an entry with the specified name.
     *
     * @param resourceName The name of the resource.
     * @return True if an entry exists, false otherwise.
     */
    public boolean hasEntryWithName(final ResourceName resourceName) {
        for (final Resource entry : _entries) {
            if (entry.getName().equals(resourceName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Retrieve an entry from this folder.
     *
     * @param resourceName The name of the resource to retrieve.
     *
     * @return The corresponding resource of NULL if no such resource exists.
     */
    public Resource getEntryWithName(final ResourceName resourceName) {
        for (final Resource entry : _entries) {
            if (entry.getName().equals(resourceName)) {
                return entry;
            }
        }
        return null;
    }


    /**
     * Retrieve the entries in this folder in a type-safe list.
     *
     * @param resourceType A class representing the type of resources in this
     *      folder.
     * @param <T> The type of the resources in this folder.
     * @return The entries in this folder as a type-safe list.
     */
    public <T extends Resource> List<T> getEntries(
                                                final Class<T> resourceType) {
        final List<T> entries = new ArrayList<T>();
        for (final Resource entry : getEntries()) {
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
            if (r.getType().equals(ResourceType.PAGE)) {
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
    public Page getFirstPage() {
        for (final Resource r : getEntries()) {
            if (r.getType().equals(ResourceType.PAGE)) {
                return r.as(Page.class);
            }
        }
        throw new RuntimeException("No pages in this folder.");
    }


    /**
     * Remove a resource from this folder.
     *
     * @param resource The resource to remove.
     */
    public void remove(final Resource resource) {
        DBC.require().notNull(resource);
        _entries.remove(resource);
        resource.setParent(null, null);
    }


    /**
     * Retrieve a list of all the pages in this folder with sort order applied.
     *
     * @return A list of pages.
     */
    public List<Page> getPages() {
        final List<Page> entries = new ArrayList<Page>();
        for (final Resource entry : getEntries()) {
            if (entry.getType()==ResourceType.PAGE) {
                entries.add(entry.as(Page.class));
            }
        }
        return entries;
    }


    /**
     * Retrieve a list of all the folders in this folder.
     *
     * @return A list of folders.
     */
    public List<Folder> getFolders() {
        final List<Folder> entries = new ArrayList<Folder>();
        for (final Resource entry : getEntries()) {
            if (entry.getType()==ResourceType.FOLDER) {
                entries.add(entry.as(Folder.class));
            }
        }
        return entries;
    }


    /**
     * Query method to determine if this folder contains any aliases.
     *
     * @return true if the folder contains any aliases, false otherwise.
     */
    public boolean hasAliases() {
        for (final Resource r : _entries) {
            if (r.getType().equals(ResourceType.ALIAS)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Accessor method for the first alias in this folder.
     *
     * @return The first alias in the list of entries.
     */
    public Alias getFirstAlias() {
        for (final Resource r : getEntries()) {
            if (r.getType().equals(ResourceType.ALIAS)) {
                return r.as(Alias.class);
            }
        }
        throw new RuntimeException("No aliases in this folder.");
    }


    /**
     * Accessor for the sort order property.
     *
     * @return The folder sort order.
     */
    public ResourceOrder getSortOrder() {
        return _order;
    }


    /**
     * Mutator for the sort order property.
     *
     * @param order The new sort order.
     */
    public void setSortOrder(final ResourceOrder order) {
        DBC.require().notNull(order);
        _order = order;
    }


    /** {@inheritDoc} */
    @Override
    public Jsonable createSnapshot() {
        return new Jsonable(){
            /** {@inheritDoc} */
            @Override public void toJson(final Json json) {
                json.set(JsonKeys.TITLE, getTitle());
            }
        };
    }


    /**
     * Accessor for the index page.
     *
     * @return The index page of this folder.
     */
    public Page getIndexPage() {
        return _indexPage;
    }


    /**
     * Mutator for the index page.
     *
     * @param indexPage The index page to set.
     */
    public void setIndexPage(final Page indexPage) {
        _indexPage = indexPage;
    }


    private UUID getDefaultPage() {
        for (final Resource r : getEntries()) {
            if (ResourceType.PAGE.equals(r.getType())
                && r.isPublished()) {
                return r.getId();
            }
        }
        return null;
    }


    /** {@inheritDoc} */
    @Override
    public void delete() {
        super.delete();
        for (final Resource r : getEntries()) {  // Recursive deletion.
            r.delete();
        }
    }


    /** {@inheritDoc} */
    @Override
    public void undelete() {
        super.undelete();
        for (final Resource r : getEntries()) {  // Recursive un-deletion.
            r.undelete();
        }
    }


    /** {@inheritDoc} */
    @Override
    public void toJson(final Json json) {
        super.toJson(json);
        json.set(JsonKeys.SORT_ORDER, getSortOrder().name());
        json.set(
            JsonKeys.INDEX_PAGE_ID,
            (null==getIndexPage()) ? null : getIndexPage().getId().toString());
        // Index folder entries?
    }


    /**
     * Get a child element via its ID.
     *
     * @param id The child's ID.
     *
     * @return The child or NULL if no child exists for the specified ID.
     */
    public Resource getChild(final UUID id) {
        for (final Resource r : _entries) {
            if (r.getId().equals(id)) { return r; }
        }
        return null;
    }




    /* ====================================================================
     * Snapshot support.
     * ================================================================== */


    /** {@inheritDoc} */
    @Override
    public FolderDto forWorkingCopy() {
        return forCurrentRevision();
    }


    /** {@inheritDoc} */
    @Override
    public FolderDto forCurrentRevision() {
        final FolderDto dto = new FolderDto(
            (null==getParent()) ? null : getParent().getId(),
                getName());
        super.setDtoProps(dto);
        dto.setIndexPage((null==_indexPage) ? null : _indexPage.getId());
        dto.setDefaultPage(getDefaultPage());
        return dto;
    }


    /** {@inheritDoc} */
    @Override
    public FolderDto forSpecificRevision(final int revNo) {
        return forCurrentRevision();
    }
}
