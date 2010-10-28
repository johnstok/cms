/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.plugins.search.lucene;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

import ccc.api.core.ACL;
import ccc.api.core.ACL.Entry;
import ccc.api.types.DBC;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class AclFilter
    extends
        Filter {

    private static final Logger LOG  = Logger.getLogger(AclFilter.class);

    private final ACL _perms;
    private final String _field;


    /**
     * Constructor.
     *
     * @param perms The permissions for the user invoking the query.
     * @param field The lucene field containing the doc's permissions.
     */
    public AclFilter(final String field, final ACL perms) {
        _field = field;
        _perms = DBC.require().notNull(perms);
    }


    /** {@inheritDoc} */
    @Override
    public DocIdSet getDocIdSet(final IndexReader reader) throws IOException {

        // Assume all documents are invalid.
        final OpenBitSet docs = new OpenBitSet(reader.maxDoc());

        // Validate accessible documents.
        for (int i=0; i<reader.maxDoc(); i++) {
            final Document d = reader.document(i);
            final Field aclField = d.getField(_field);
            if (null!=aclField
                  && isAccessible(deserialise(aclField.getBinaryValue()))) {
                docs.set(i);
            }
        }

        return docs;
    }


    private boolean isAccessible(final Collection<ACL> docAcl) {
        for (final ACL acl : docAcl) {
            if (!isReadable(acl)) { return false; }
        }
        return true;
    }


    private boolean isReadable(final ACL acl) {
        if (acl.getGroups().isEmpty() && acl.getUsers().isEmpty()) {
            return true;
        }

        for (final Entry group : acl.getGroups()) {
            if (group.isReadable() && permsIncludeGroup(group.getPrincipal())) {
                return true;
            }
        }
        for (final Entry user : acl.getUsers()) {
            if (user.isReadable() && permsIncludeUser(user.getPrincipal())) {
                return true;
            }
        }

        return false;
    }


    private boolean permsIncludeUser(final UUID principal) {
        for (final Entry user : _perms.getUsers()) {
            if (principal.equals(user.getPrincipal())) { return true; }
        }
        return false;
    }


    private boolean permsIncludeGroup(final UUID principal) {
        for (final Entry group : _perms.getGroups()) {
            if (principal.equals(group.getPrincipal())) { return true; }
        }
        return false;
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param acl
     * @return
     */
    static byte[] serialise(final Collection<ACL> acl) {
        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            final ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(acl);
            return os.toByteArray();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * TODO: Add a description for this method.
     *
     * @param acl
     * @return
     */
    static Collection<ACL> deserialise(final byte[] bytes) {
        try {
            final ByteArrayInputStream is = new ByteArrayInputStream(bytes);
            final ObjectInputStream ois = new ObjectInputStream(is);
            return (Collection<ACL>) ois.readObject();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
