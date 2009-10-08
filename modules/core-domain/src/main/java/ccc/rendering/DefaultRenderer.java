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

package ccc.rendering;

import ccc.domain.Resource;
import ccc.snapshots.ResourceSnapshot;

/**
 * Default implementation of the {@link Renderer} interface.
 *
 * @author Civic Computing Ltd.
 */
public class DefaultRenderer
    implements
        Renderer {

    private final boolean _respectVisibility;

    /**
     * Constructor.
     *
     * @param respectVisiblity Should we check a resource's visibility?
     */
    public DefaultRenderer(final boolean respectVisiblity) {
        _respectVisibility = respectVisiblity;
    }


    /** {@inheritDoc} */
    @Override
    public Response render(final Resource resource,
                           final Context context) {
        if (resource == null) {
            throw new NotFoundException();
        }
        return renderResourceSnaphot(resource.forCurrentRevision(), context);
    }


    private Response renderResourceSnaphot(final ResourceSnapshot resource,
                                           final Context context) {

        if (resource == null) {
            throw new NotFoundException();
        } else if (_respectVisibility && !resource.isVisible()) {
            throw new NotFoundException();
        }

        context.add("resource", resource);

        return resource.render();
    }


    /** {@inheritDoc} */
    @Override
    public Response renderWorkingCopy(final Resource resource,
                                      final Context context) {
        if (resource == null) {
            throw new NotFoundException();
        }

        final ResourceSnapshot r =
            (_respectVisibility)
                ? resource.forCurrentRevision()
                : resource.forWorkingCopy();

        return renderResourceSnaphot(r, context);
    }


    /** {@inheritDoc} */
    @Override
    public Response renderHistoricalVersion(
                                    final Resource resource,
                                    final String version,
                                    final Context context) {
        if (resource == null) {
            throw new NotFoundException();
        }

        ResourceSnapshot snapshot = resource.forCurrentRevision();

        if (!_respectVisibility) {
            try {
                final long v = new Long(version).longValue();
                if (v<0) {
                    throw new NotFoundException();
                }

                snapshot = resource.forSpecificRevision((int) v);

            } catch (final NumberFormatException e) {
                throw new NotFoundException();
            }
        }

        return renderResourceSnaphot(snapshot, context);
    }
}
