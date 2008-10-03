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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import ccc.contentcreator.dto.AliasDTO;
import ccc.contentcreator.dto.FileDTO;
import ccc.contentcreator.dto.FolderDTO;
import ccc.contentcreator.dto.PageDTO;
import ccc.contentcreator.dto.ResourceDTO;
import ccc.contentcreator.dto.TemplateDTO;
import ccc.contentcreator.dto.UserDTO;
import ccc.domain.Alias;
import ccc.domain.CCCException;
import ccc.domain.CreatorRoles;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.Template;
import ccc.domain.User;


/**
 * Helper methods for DTOs.
 *
 * @author Civic Computing Ltd
 */
public final class DTOs {

    private DTOs() { super(); }

    /**
     * Create a {@link Template} from a {@link TemplateDTO}.
     *
     * @param templateDTO The dto from which to create the template.
     * @return A valid template.
     */
    public static Template templateFrom(final TemplateDTO templateDTO) {
        final Template t = new Template(
            templateDTO.getTitle(),
            templateDTO.getDescription(),
            templateDTO.getBody());


        if (null!=templateDTO.getId()) {
            t.id(UUID.fromString(templateDTO.getId()));
        }

        return t;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param <T>
     * @param <U>
     * @param resources
     * @return
     */
    public static <T extends ResourceDTO, U extends Resource> List<T>
        dtoFrom(final Collection<U> resources, final Class<U> resourceType) {
        final List<T> dtos = new ArrayList<T>();
        for (final U resource : resources) {
            dtos.add((T) dtoFrom(resource));
        }
        return dtos;
    }

    /**
     * Convert a resource to a dto.
     *
     * @param resource The resource to convert.
     * @param <T> The type of the dto to return.
     * @return A dto representing the specified resource.
     */
    @SuppressWarnings("unchecked")
    public static <T extends ResourceDTO> T dtoFrom(final Resource resource) {

        switch (resource.type()) {
            case FOLDER:
                return (T) dtoFrom(resource.as(Folder.class));

            case PAGE:
                return (T) dtoFrom(resource.as(Page.class));

            case TEMPLATE:
                return (T) dtoFrom(resource.as(Template.class));

            case ALIAS:
                return (T) dtoFrom(resource.as(Alias.class));

            case FILE:
                return (T) dtoFrom(resource.as(File.class));

            default:
                throw new CCCException(
                    "Cannot convert resource of type "+resource.type());
        }
    }

    /**
     * Create a  {@link TemplateDTO} from a {@link Template}.
     *
     * @param template The template from which to create a dto.
     * @return A dto representing the template.
     */
    public static TemplateDTO dtoFrom(final Template template) {
        if (null==template) {
            return null;
        }

        final TemplateDTO dto =
            new TemplateDTO(
                template.id().toString(),
                template.version(),
                template.name().toString(),
                template.title(),
                template.description(),
                template.body());
        return dto;
    }

    /**
     * Create a DTO for a folder resource.
     *
     * @param f The folder.
     * @return The DTO.
     */
    private static FolderDTO dtoFrom(final Folder f) {

        return new FolderDTO(
            f.id().toString(),
            f.version(),
            f.name().toString(),
            f.title(),
            f.folderCount()
        );
    }

    /**
     * Create a DTO for a file resource.
     *
     * @param f The file.
     * @return The DTO.
     */
    private static FileDTO dtoFrom(final File f) {

        return new FileDTO(
            f.id().toString(),
            f.version(),
            f.name().toString(),
            f.title(),
            f.size(),
            f.mimeType().toString(),
            f.fileData().id().toString(),
            f.description()
        );
    }

    /**
     * Create a DTO for a page resource.
     *
     * @param p The page.
     * @return The DTO.
     */
    private static PageDTO dtoFrom(final Page p) {

        final Map<String, String> paragraphs =
            new HashMap<String, String>();
        for (final Map.Entry<String, Paragraph> para
                : p.paragraphs().entrySet()) {
            paragraphs.put(para.getKey(), para.getValue().body());
        }
        return new PageDTO(
            p.id().toString(),
            p.version(),
            p.name().toString(),
            p.title(),
            paragraphs
        );
    }

    /**
     * Create a DTO for an alias resource.
     *
     * @param a The Alias.
     * @return The DTO.
     */
    private static AliasDTO dtoFrom(final Alias a) {

        return new AliasDTO(
            a.id().toString(),
            a.version(),
            a.name().toString(),
            a.title(),
            a.target().id().toString()
        );
    }

    /**
     * Create a DTO for an user resource.
     *
     * @param u The User.
     * @return The DTO.
     */
    public static UserDTO dtoFrom(final User u) {
        final UserDTO userDTO = new UserDTO();
        userDTO.setId(u.id().toString());
        userDTO.setVersion(u.version());
        userDTO.setUsername(u.username());
        userDTO.setEmail(u.email());
        final Set<String> roles = new HashSet<String>();
        for (final CreatorRoles role : u.roles()) {
            roles.add(role.name());
        }
        userDTO.setRoles(roles);
        return userDTO;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param userList
     * @return
     */
    public static List<UserDTO> dtoFrom(final Collection<User> userList) {
        final ArrayList<UserDTO> dtos = new ArrayList<UserDTO>();
        for (final User u : userList) {
            dtos.add(dtoFrom(u));
        }
        return dtos;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param userDto
     * @return
     */
    public static User userFrom(final UserDTO userDto) {
        final User u = new User(userDto.getUsername());
        u.id(UUID.fromString(userDto.getId()));
        u.version(userDto.getVersion());
        u.email(userDto.getEmail());
        for (final String role : userDto.getRoles()) {
            u.addRole(CreatorRoles.valueOf(role));
        }

        return u;
    }
}
