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
package ccc.services.impl;

import static org.easymock.EasyMock.*;

import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;
import ccc.action.ActionExecutorImpl;
import ccc.domain.Page;
import ccc.domain.RevisionMetadata;
import ccc.domain.User;
import ccc.rest.RestException;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.CommandType;
import ccc.types.ResourceName;
import ccc.types.Username;


/**
 * Tests for the {@link ActionExecutorImpl} class.
 *
 * @author Civic Computing Ltd.
 */
public class ActionExecutorImplTest
    extends
        TestCase {

    /**
     * Test.
     * @throws RestException From the Commands API.
     */
    public void testPublishResourceIsSupported()
                                                 throws RestException {

        // ARRANGE
        final Page p =  new Page(new ResourceName("foo"), "foo", null, _rm);
        final User u = new User(new Username("user"), "password");

        _resourcesExt.publish(
            eq(p.id()),
            eq(u.id()),
            isA(Date.class));
        replay(_resourcesExt);

        // ACT
        _ea.executeAction(
            p.id(),
            u.id(),
            CommandType.RESOURCE_PUBLISH,
            new HashMap<String, String>(){{
                put("MAJOR", Boolean.FALSE.toString());
                put("COMMENT", "");
            }});

        // ASSERT
        verify(_resourcesExt);
    }
    
    /**
     * Test.
     * @throws RestException From the Commands API.
     */
    public void testResourceUnpublishIsSupported()
                                                 throws RestException {

        // ARRANGE
        final Page p =  new Page(new ResourceName("foo"), "foo", null, _rm);
        final User u = new User(new Username("user"), "password");

        _resourcesExt.unpublish(
            eq(p.id()),
            eq(u.id()),
            isA(Date.class));
        replay(_resourcesExt);

        // ACT
        _ea.executeAction(
            p.id(),
            u.id(),
            CommandType.RESOURCE_UNPUBLISH ,
            new HashMap<String, String>(){{
                put("MAJOR", Boolean.FALSE.toString());
                put("COMMENT", "");
            }});

        // ASSERT
        verify(_resourcesExt);
    }


    /** {@inheritDoc} */
    @Override
    protected void setUp() {
        _resourcesExt = createStrictMock(ResourcesExt.class);
        _ea = new ActionExecutorImpl(_resourcesExt);
    }
    /** {@inheritDoc} */
    @Override
    protected void tearDown() {
        _ea = null;
        _resourcesExt = null;
    }

    private ActionExecutorImpl _ea;
    private ResourcesExt _resourcesExt;
    private final RevisionMetadata _rm =
        new RevisionMetadata(new Date(), User.SYSTEM_USER, true, "Created.");
}
