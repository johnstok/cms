/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.rendering.velocity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import ccc.rest.extensions.ResourcesExt;


/**
 * A Velocity resource loader that delegates to a {@link StatefulReader}.
 *
 * @author Civic Computing Ltd.
 */
public class CCCResourceLoader
    extends
        ResourceLoader {

    /** {@inheritDoc} */
    @Override
    public long getLastModified(final Resource arg0) {
        return new Date().getTime();
    }


    /** {@inheritDoc} */
    @Override
    public InputStream getResourceStream(final String absolutePath) {
        final ResourcesExt resources =
            (ResourcesExt) rsvc.getApplicationAttribute("ccc-reader");

        if (null==resources) {
            return new ByteArrayInputStream(new byte[0]);
        }

        // TODO: Throw resource not found exception?
        final String contents =
            resources.fileContentsFromPath(absolutePath, "UTF8");

        try {
            return new ByteArrayInputStream(contents.getBytes("UTF8"));
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8 encoding not supported!", e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void init(final ExtendedProperties arg0) {
        /* NO-OP */
    }


    /** {@inheritDoc} */
    @Override
    public boolean isSourceModified(final Resource arg0) {
        return true;
    }
}
