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

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

import ccc.api.core.ACL;
import ccc.api.core.AccessController;


/**
 * Lucene filter that applies access control for a search result.
 *
 * @author Civic Computing Ltd.
 */
public class AclFilter
    extends
        Filter {

    private final AccessController _ac;
    private final String           _field;


    /**
     * Constructor.
     *
     * @param perms The permissions for the user invoking the query.
     * @param field The lucene field containing the doc's permissions.
     */
    public AclFilter(final String field, final ACL perms) {
        _field = field;
        _ac = new AccessController(perms);
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
                  && _ac.canRead(deserialise(aclField.getBinaryValue()))) {
                docs.set(i);
            }
        }

        return docs;
    }


    /**
     * Serialise a collection of ACLs..
     *
     * @param acl The collection of ACLs to serialise.
     *
     * @return The serialised collection, as a byte array.
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
     * De-serialise a collection of ACL objects.
     *
     * @param bytes The serialised representation.
     *
     * @return The de-serialised collection.
     */
    @SuppressWarnings("unchecked") // API doesn't support generics.
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
