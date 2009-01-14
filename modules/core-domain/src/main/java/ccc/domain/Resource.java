/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */

package ccc.domain;

import static ccc.commons.DBC.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ccc.commons.DBC;
import ccc.commons.serialisation.Serializer;

/**
 * An abstract superclass that contains shared behaviour for the different types
 * of CCC resource.
 * TODO: Should _tags be a linked hash set?
 *
 * @author Civic Computing Ltd
 */
public abstract class Resource extends VersionedEntity {

    private static final int MAXIMUM_TITLE_LENGTH = 256;

    private String       _title             = id().toString();
    private ResourceName _name              = ResourceName.escape(_title);
    private Template     _template          = null;
    private Folder       _parent            = null;
    private User         _lockedBy          = null;
    private List<String> _tags              = new ArrayList<String>();
    private User         _publishedBy       = null;
    private boolean      _includeInMainMenu = false;

    /** Constructor: for persistence only. */
    protected Resource() { super(); }

    /**
     * Constructor.
     *
     * @param name The name for this resource.
     * @param title The title of this resource, as a string.
     */
    protected Resource(final ResourceName name,
                       final String title) {
        require().notNull(name);

        name(name);
        title(title);
    }

    /**
     * Constructor.
     * The title parameter is escaped via {@link ResourceName#escape(String)} to
     * determine the resource's name.
     *
     * @param title The title of this resource, as a string.
     */
    public Resource(final String title) {
        title(title);
        name(ResourceName.escape(title));
    }

    /**
     * Query the type of this resource.
     *
     * @return The ResourceType that describes this resource.
     */
    public abstract ResourceType type();

    /**
     * Type-safe helper method to convert an instance of {@link Resource} to a
     * subclass.
     *
     * @param <T> The type that this resource should be converted to.
     * @param resourceType The class representing the type that this resource
     *      should be converted to.
     * @return This resource as a Page.
     */
    public final <T extends Resource> T as(final Class<T> resourceType) {
        return resourceType.cast(this);
    }

    /**
     * Determine the template for this resource. Iterates up the parent
     * hierarchy if necessary.
     *
     * @param def The default template to use if we cannot compute one.
     * @return The template or null if none is found.
     */
    public final Template computeTemplate(final Template def) {
        return
        (null!=_template)
        ? template()
            : (null!=_parent)
            ? _parent.computeTemplate(def)
                : def;
    }

    /**
     * Determine the absolute path for a resource.
     *
     * @return The absolute path as a {@link ResourcePath}.
     */
    public final ResourcePath absolutePath() {
        return
        (null==parent())
        ? new ResourcePath(name())
        : parent().absolutePath().append(name());
    }

    /**
     * Accessor for name.
     *
     * @return The name for this resource, as a {@link ResourceName}.
     */
    public ResourceName name() {
        return _name;
    }

    /**
     * Mutator for the name field.
     *
     * @param resourceName The new resource name.
     */
    public void name(final ResourceName resourceName) {
        require().notNull(resourceName);
        _name = resourceName;
    }

    /**
     * Accessor for the title.
     *
     * @return The content's title, as a string.
     */
    public String title() {
        return _title;
    }

    /**
     * Sets the title of the resource.
     *
     * @param titleString The new title for this resource.
     */
    public void title(final String titleString) {
        require().notEmpty(titleString);
        require().maxLength(titleString, MAXIMUM_TITLE_LENGTH);
        _title = titleString;
    }

    /**
     * Accessor for the template.
     *
     * @return The {@link Template}.
     */
    public Template template() {
        return _template;
    }

    /**
     * Sets the template for this resource.
     *
     * @param template The new template.
     */
    public void template(final Template template) {
        _template = template;
    }

    /**
     * Accessor for the resource's parent.
     *
     * @return The folder containing this resource.
     */
    public Folder parent() {
        return _parent;
    }

    /**
     * Mutator for the resource's parent. <i>This method should only be called
     * by the {@link Folder} class.</i>
     *
     * @param parent The folder containing this resource.
     */
    void parent(final Folder parent) {
        _parent = parent;
    }

    /**
     * Query method to determine whether a resource is locked.
     *
     * @return True if the resource is locked, false otherwise.
     */
    public boolean isLocked() {
        return null != _lockedBy;
    }

    /**
     * Lock a resource.
     *
     * @param u The user who is locking the resource.
     */
    public void lock(final User u) {
        require().notNull(u);
        if (isLocked()) {
            throw new CCCException("Resource is already locked."); // TODO: Use better exception.
        }
        _lockedBy = u;
    }

    /**
     * Query method - determine who has locked this resource.
     *
     * @return The locking user or null if the resource is not locked.
     */
    public User lockedBy() {
        return _lockedBy;
    }

    /**
     * Unlock the resource.
     *
     * @param user The user releasing the lock.
     */
    public void unlock(final User user) {

        if (!isLocked()) {
            throw new UnlockedException(this);
        }

        if (!canUnlock(user)) {
            throw new CCCException("User not allowed to unlock this resource."); // TODO: Use better exception.
        }

        _lockedBy = null;
    }

    /**
     * Determine whether a user can unlock this resource.
     *
     * @param user The user trying to unlock the resource.
     * @return True if the user can unlock the resource, false otherwise.
     */
    public boolean canUnlock(final User user) {
        return user.equals(lockedBy())
        || user.hasRole(CreatorRoles.ADMINISTRATOR);
    }

    /**
     * Set the tags for this resource.
     *
     * @param tagString A string of comma separated values that represents the
     *  tags for this resource.
     */
    public void tags(final String tagString) {
        DBC.require().notNull(tagString);

        final String[] tagArray = tagString.split(",");
        _tags.clear();
        for(final String tag : tagArray) {
            if (tag.trim().length() < 1) {
                continue;
            }
            _tags.add(tag.trim());
        }
    }

    /**
     * Accessor for a resource's tags.
     *
     * @return The tags for this resource as a list.
     */
    public List<String> tags() {
        return Collections.unmodifiableList(_tags);
    }


    /**
     * Return this resource's tags as a comma separated list.
     *
     * @return A string representation of the tags.
     */
    public String tagString() {
        final StringBuilder sb = new StringBuilder();
        for (final String tag : tags()) {
            sb.append(tag);
            sb.append(',');
        }

        String tagString = sb.toString();
        if (tagString.endsWith(",")) {
            tagString = tagString.substring(0, tagString.length()-1);
        }

        return tagString;
    }

    /**
     * Publish the resource.
     *
     * @param user The user.
     */
    public void publish(final User user) {
        require().notNull(user);
        _publishedBy = user;
    }

    /**
     * Query method to determine whether a resource is published.
     *
     * @return True if the resource is published, false otherwise.
     */
    public boolean isPublished() {
        return _publishedBy != null;
    }

    /**
     * Return user who published the resource.
     *
     * @return The user or null if the resource is unpublished.
     */
    public User publishedBy() {
        return _publishedBy;
    }

    /**
     * Unpublish the resource.
     *
     */
    public void unpublish() {
        _publishedBy = null;
    }

    /**
     * Query method to determine whether a resource is visible.
     *
     * A resource is visible if itself and all of its parents are published.
     *
     * @return True if resource's all parents are published.
     */
    public boolean isVisible() {
        final boolean parentVisible =
            (null==_parent) ? true : _parent.isVisible();
        return parentVisible && isPublished();
    }

    /** {@inheritDoc} */
    @Override
    public void serialize(final Serializer s) {
        super.serialize(s);
        s.string("title", title());
        s.string("name", name().toString());
        s.string("locked", (isLocked()) ? lockedBy().username() : "");
        s.string("published", (isPublished()) ? publishedBy().username() : "");
        s.uuid("parent", (null==_parent) ? null : _parent.id());
        s.uuid("template", (null==_template) ? null : _template.id());
        s.array("tags", tags());
    }


    /**
     * Confirm this resource is locked by the specified user.
     *
     * @param user The user who should have the lock.
     */
    public void confirmLock(final User user) {
        if (!isLocked()) {
            throw new UnlockedException(this);
        }
        if (!lockedBy().equals(user)) {
            throw new LockMismatchException(this);
        }
    }

    /**
     * Accessor for 'include in main menu' property.
     *
     * @return True if the resource should be included, false otherwise.
     */
    public boolean includeInMainMenu() {
        return _includeInMainMenu;
    }

    /**
     * Mutator for 'include in main menu' property.
     *
     * @param shouldInclude Should the resource be included?
     */
    public void includeInMainMenu(final boolean shouldInclude) {
            _includeInMainMenu = shouldInclude;
    }

    /**
     * Accessor for the root parent of this resource.
     *
     * @return The root parent of this resource.
     */
    public Resource root() {
        if (null == _parent) {
            return this;
        }
        return _parent.root();
    }
}
