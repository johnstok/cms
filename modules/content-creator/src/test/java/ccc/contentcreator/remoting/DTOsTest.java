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
package ccc.contentcreator.remoting;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import junit.framework.TestCase;
import ccc.contentcreator.dto.AliasDTO;
import ccc.contentcreator.dto.FileDTO;
import ccc.contentcreator.dto.TemplateDTO;
import ccc.contentcreator.dto.UserDTO;
import ccc.domain.Alias;
import ccc.domain.CreatorRoles;
import ccc.domain.Data;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.ResourceName;
import ccc.domain.Template;
import ccc.domain.User;


/**
 * Tests for the {@link DTOs} class.
 *
 * @author Civic Computing Ltd
 */
public class DTOsTest extends TestCase {

    /**
     * Test.
     */
    public void testUserFromNewDto() {

        // ARRANGE
        final UserDTO dto = new UserDTO();
        dto.setUsername("dummy");
        dto.setEmail("abc@def.com");

        // ACT
        final User actual = DTOs.userFrom(dto);

        // ASSERT
        assertNotNull(actual.id());
        assertEquals(-1, actual.version());
        assertEquals("dummy", actual.username());
        assertEquals("abc@def.com", actual.email());
        assertEquals(EnumSet.noneOf(CreatorRoles.class), actual.roles());
    }

    /**
     * Test.
     */
    public void testUserFromExistingDto() {

        // ARRANGE
        final User expected = new User("user1");
        expected.version(1);
        expected.email("abc@def.com");
        expected.addRole(CreatorRoles.ADMINISTRATOR);

        final UserDTO dto = DTOs.dtoFrom(expected);

        // ACT
        final User actual = DTOs.userFrom(dto);

        // ASSERT
        assertEquals(expected, actual);
        assertEquals(expected.id(), actual.id());
        assertEquals(expected.version(), actual.version());
        assertEquals(expected.username(), actual.username());
        assertEquals(expected.email(), actual.email());
        assertEquals(expected.roles(), actual.roles());
    }

    /**
     * Test.
     */
    public void testTemplateFromDto() {

        // ARRANGE
        final UUID id = UUID.randomUUID();
        final TemplateDTO dto =
            new TemplateDTO(
                id.toString(),
                0,
                "name",
                "title",
                "description",
                "body",
                "def");

        // ACT
        final Template actual = DTOs.templateFrom(dto);

        // ASSERT
        assertEquals("title", actual.title());
        assertEquals("description", actual.description());
        assertEquals("def", actual.definition());
        assertEquals("body", actual.body());
    }

    /**
     * Test.
     */
    public void testDtoFromAlias() {

        // ARRANGE
        final Folder target = new Folder(new ResourceName("target"));
        final Alias alias = new Alias(new ResourceName("alias"), target);

        // ACT
        final AliasDTO actual = DTOs.dtoFrom(alias);

        // ASSERT
        assertEquals("alias", actual.getName());
        assertEquals(target.id().toString(), actual.getTargetId());
    }

    /**
     * Test.
     */
    public void testDtoFromFile() {

        // ARRANGE
        final File f = new File(new ResourceName("a"), "b", "c", new Data(), 0);

        // ACT
        final FileDTO actual = DTOs.dtoFrom(f);

        // ASSERT
        assertEquals("a", actual.getName().toString());
        assertEquals(Long.valueOf(0), actual.getSize());
        assertEquals("application/octet-stream", actual.getMimeType());
        assertEquals("FILE", actual.getType());
        assertEquals("c", actual.getDescription());
    }

    /**
     * Test.
     */
    public void testDtoFromUser() {

        // ARRANGE
        final User u = new User("username");
        u.email("test@test.com");
        u.addRole(CreatorRoles.CONTENT_CREATOR);
        u.addRole(CreatorRoles.SITE_BUILDER);

        // ACT
        final UserDTO userDTO = DTOs.dtoFrom(u);

        // ASSERT
        assertEquals(u.id().toString(), userDTO.getId());
        assertEquals((Integer) u.version(), userDTO.getVersion());
        assertEquals(u.email(), userDTO.getEmail());
        assertTrue(userDTO.getRoles().contains(
            CreatorRoles.CONTENT_CREATOR.name()));
        assertTrue(userDTO.getRoles().contains(
            CreatorRoles.SITE_BUILDER.name()));

    }

    /**
     * Test.
     */
    public void testDtoFromUserList() {

        // ARRANGE
        final List<User> userList = new ArrayList<User>();
        userList.add(new User("username1"));
        userList.add(new User("username2"));

        // ACT
        final List<UserDTO> userDtos = DTOs.dtoFrom(userList);

        // ASSERT
        assertEquals(userList.size(), userDtos.size());
        assertEquals("username1", userDtos.get(0).getUsername());
        assertEquals("username2", userDtos.get(1).getUsername());

    }
}
