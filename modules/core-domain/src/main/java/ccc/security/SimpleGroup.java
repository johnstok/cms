/*-----------------------------------------------------------------------------
 * Copyright © 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.security;

import static java.util.Collections.*;

import java.security.Principal;
import java.security.acl.Group;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;


/**
 * Simple group.
 *
 * @author Civic Computing Ltd.
 */
public class SimpleGroup
    extends
        SimplePrincipal
    implements
        Group {

    // Should be a set.
    private final Set<Principal> _members = new HashSet<Principal>();

    /**
     * Constructor.
     *
     * @param name The group's name.
     */
    public SimpleGroup(final String name) {
        super(name);
    }

    /** {@inheritDoc} */
    @Override
    public boolean addMember(final Principal user) {
        if (_members.contains(user)) {
            return false;
        }
        _members.add(user);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isMember(final Principal member) {
        if (null==member) { return false; }

        for (final Principal p : _members) {
            if (p instanceof Group) {
                final Group nestedGroup = (Group) p;
                if (nestedGroup.isMember(member)) { return true; }
            } else if (member.equals(p)) { return true; }
        }

        return false;
    }

    /** {@inheritDoc} */
    @Override
    public Enumeration<? extends Principal> members() {
        return enumeration(unmodifiableSet(_members));
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeMember(final Principal user) {
        if (!_members.contains(user)) {
            return false;
        }
        _members.remove(user);
        return true;
    }
}
