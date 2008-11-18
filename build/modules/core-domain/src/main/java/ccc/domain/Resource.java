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
import ccc.commons.serialisation.Serializer;

/**
 * An abstract superclass that contains shared behaviour for the different types
 * of CCC resource.
 *
 * @author Civic Computing Ltd
 */
public abstract class Resource extends VersionedEntity {

    private static final int MAXIMUM_TITLE_LENGTH = 256;

    private String       _title    = id().toString();
    private ResourceName _name     = ResourceName.escape(_title);
    private Template     _template = null;
    private Folder       _parent   = null;
    private User         _lockedBy = null;

    /** Constructor: for persistence only. */
    protected Resource() { super(); }

    /**
     * Constructor.
     * Sets the resource's title to be the same as the name.
     *
     * @param name The name for this resource.
     */
    protected Resource(final ResourceName name) {
        require().notNull(name);
        _name = name;
        _title = name.toString();
    }

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
        ? displayTemplateName()
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
    void name(final ResourceName resourceName) {
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
    public Template displayTemplateName() {
        return _template;
    }

    /**
     * Sets the template for this resource.
     *
     * @param template The new template.
     */
    public void displayTemplateName(final Template template) {
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
     * TODO: Should check that isLocked == false
     *
     * @param u The user who is locking the resource.
     */
    public void lock(final User u) {
        require().notNull(u);
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
     * TODO: Should call canUnlock?
     */
    public void unlock() {
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

    /** {@inheritDoc} */
    @Override
    public void serialize(final Serializer s) {
        super.serialize(s);
        s.string("title", title());
        s.string("name", name().toString());
        s.string("locked", (isLocked()) ? lockedBy().username() : "");
        s.uuid("parent", (null==_parent) ? null : _parent.id());
        s.uuid("template", (null==_template) ? null : _template.id());
    }
}
