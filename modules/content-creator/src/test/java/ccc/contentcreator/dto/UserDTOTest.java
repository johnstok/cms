/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dto;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;
import ccc.contentcreator.remoting.DTOs;
import ccc.domain.CreatorRoles;
import ccc.domain.User;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class UserDTOTest extends TestCase {

    /**
     * Test.
     */
    public void testEquals() {

        // ARRANGE
        final User u = new User("username");
        final UserDTO dto1 = DTOs.dtoFrom(u);
        final UserDTO dto2 = DTOs.dtoFrom(u);

        // ACT
        final boolean equals = dto1.equals(dto2);

        // ASSERT
        assertFalse("Should use standard equality.", equals);
    }

    /**
     * Test.
     */
    public void testIdProperty() {

        // ARRANGE
        final UserDTO userDTO = new UserDTO();

        // ACT
        userDTO.setId("id");

        // ASSERT
        assertEquals("id", userDTO.getId());
    }

    /**
     * Test.
     */
    public void testVersionProperty() {

        // ARRANGE
        final UserDTO userDTO = new UserDTO();

        // ACT
        userDTO.setVersion(1);

        // ASSERT
        assertEquals((Integer) 1, userDTO.getVersion());
    }

    /**
     * Test.
     */
    public void testUsernameProperty() {

        // ARRANGE
        final UserDTO userDTO = new UserDTO();

        // ACT
        userDTO.setUsername("foo");

        // ASSERT
        assertEquals("foo", userDTO.getUsername());
    }

    /**
     * Test.
     */
    public void testEmailProperty() {

        // ARRANGE
        final UserDTO userDTO = new UserDTO();

        // ACT
        userDTO.setEmail("email");

        // ASSERT
        assertEquals("email", userDTO.getEmail());
    }

    /**
     * Test.
     */
    public void testRolesProperty() {

        // ARRANGE
        final UserDTO userDTO = new UserDTO();
        final Set<String> roles = new HashSet<String>();
        roles.add(CreatorRoles.ADMINISTRATOR.name());
        roles.add(CreatorRoles.SITE_BUILDER.name());

        // ACT
        userDTO.setRoles(roles);

        // ASSERT
        assertEquals(roles, userDTO.getRoles());
    }

}
