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



/**
 * An alias resource. Models a symbolic link that points to another
 * {@link Resource}.
 *
 * TODO: Add circular reference detection?
 *
 * @author Civic Computing Ltd
 */
public class Alias extends Resource {

    private Resource _target;


    /**
     * Constructor.
     * N.B. This constructor should only be used for persistence.
     */
    @SuppressWarnings("unused")
    protected Alias() { super(); }

    /**
     * Constructor.
     *
     * @param resourceName The resource name for the alias.
     * @param target The target for the alias.
     */
    public Alias(final ResourceName resourceName, final Resource target) {
        super(resourceName);
        target(target);
    }

    /**
     * Constructor.
     *
     * @param title The title for the alias.
     * @param target The target for the alias.
     */
    public Alias(final String title, final Resource target) {
        super(title);
        target(target);
    }


    /** {@inheritDoc} */
    @Override
    public ResourceType type() {
        return ResourceType.ALIAS;
    }

    /**
     * Mutator for the target field.
     *
     * @param target
     */
    private void target(final Resource target) {
        DBC.require().notNull(target);
        _target = target;
    }

    /**
     * Accessor for the target field.
     *
     * @return The current target for this alias.
     */
    public Resource target() {
        return _target;
    }
}
