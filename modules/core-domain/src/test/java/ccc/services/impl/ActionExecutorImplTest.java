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
import ccc.domain.Action;
import ccc.domain.Page;
import ccc.domain.RevisionMetadata;
import ccc.domain.UnlockedException;
import ccc.domain.User;
import ccc.rest.RestException;
import ccc.rest.extensions.ResourcesExt;
import ccc.types.ActionStatus;
import ccc.types.CommandType;
import ccc.types.FailureCode;
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
    public void testActionIsFailedIfMethodThrowsException()
                                                 throws RestException {

        // ARRANGE
        final Page p =  new Page(new ResourceName("foo"), "foo", null, _rm);
        final User u = new User(new Username("user"), "password");
        final Action a =
            new Action(
                CommandType.RESOURCE_PUBLISH,
                new Date(),
                u,
                p,
                new HashMap<String, String>(){{
                    put("MAJOR", Boolean.FALSE.toString());
                    put("COMMENT", "");
                }});

        _resourcesExt.publish(
            eq(p.id()),
            eq(u.id()),
            isA(Date.class));
        expectLastCall().andThrow(new UnlockedException(p).toRemoteException());
        replay(_resourcesExt);

        // ACT
        _ea.executeAction(a);

        // ASSERT
        verify(_resourcesExt);
        assertEquals(ActionStatus.FAILED, a.status());
        assertEquals(
            FailureCode.UNLOCKED,
            a.getCode());
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
