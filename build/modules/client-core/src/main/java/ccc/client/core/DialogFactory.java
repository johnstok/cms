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
package ccc.client.core;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import ccc.api.core.ACL;
import ccc.api.core.Group;
import ccc.api.core.ResourceSummary;
import ccc.api.core.Revision;
import ccc.api.core.Template;
import ccc.api.core.User;
import ccc.api.types.Duration;
import ccc.api.types.ResourceType;
import ccc.client.views.ChangeResourceTemplate;
import ccc.client.views.CreateAction;
import ccc.client.views.CreateAlias;
import ccc.client.views.CreateFolder;
import ccc.client.views.CreatePage;
import ccc.client.views.CreateTextFile;
import ccc.client.views.CreateUser;
import ccc.client.views.UpdateFolder;
import ccc.client.views.EditTextFile;
import ccc.client.views.RenameResource;


/**
 * Factory for UI dialogs.
 *
 * @author Civic Computing Ltd.
 */
public interface DialogFactory {

    /**
     * Update a dialog.
     *
     * @return The required dialog.
     */
    UpdateFolder updateFolder();


    /**
     * Create a dialog.
     *
     * @return The required dialog.
     */
    LegacyView updateCurrentUser();


    /**
     * Create a dialog.
     *
     * @return The required dialog.
     */
    RenameResource renameResource();


    /**
     * Create a dialog.
     *
     * @param item The Resource item to move.
     * @param ssm The selection model.
     * @param root Resource root for the selection dialog.
     *
     * @return The required dialog.
     */
    LegacyView moveResource(ResourceSummary item,
                            SingleSelectionModel ssm,
                            ResourceSummary root);


    /**
     * Create a dialog.
     *
     * @param groups The list of all groups.
     *
     * @return The required dialog.
     */
    CreateUser createUser(List<Group> groups);


    /**
     * Create a dialog.
     *
     * @return The required dialog.
     */
    CreateTextFile creatTextFile();


    /**
     * Create a dialog.
     *
     * @param id The id of the parent folder.
     * @param ssm The selection model.
     *
     * @return The required dialog.
     */
    LegacyView createTemplate(UUID id, SingleSelectionModel ssm);


    /**
     * Create a dialog.
     *
     * @param templates The templates for the page.
     * @param root The root folder used when selecting resource links.
     *
     * @return The required dialog.
     */
    CreatePage createPage(Collection<Template> templates, ResourceSummary root);


    /**
     * Create a dialog.
     *
     * @return The required dialog.
     */
    CreateFolder createFolder();


    /**
     * Create a dialog.
     *
     * @param parent The folder in which this file should be saved.
     * @param ssm The selection model.
     *
     * @return The required dialog.
     */
    LegacyView uploadFile(ResourceSummary parent,
                          SingleSelectionModel ssm);


    /**
     * Create a dialog.
     *
     * @return The required dialog.
     */
    CreateAlias createAlias();


    /**
     * Create a dialog.
     *
     * @return The required dialog.
     */
    CreateAction createAction();


    /**
     * Create a dialog.
     *
     * @return The required dialog.
     */
    LegacyView about();


    /**
     * Create a dialog.
     *
     * @return The required dialog.
     */
    ChangeResourceTemplate chooseTemplate();


    /**
     * Create a dialog.
     *
     * @param data The history to display, as a collection of {@link Revision}s.
     * @param type The type of the resource.
     * @param ssm The selection model.
     *
     * @return The required dialog.
     */
    LegacyView viewHistory(List<Revision> data,
                           ResourceType type,
                           SingleSelectionModel ssm);


    /**
     * Create a dialog.
     *
     * @param resource The resource whose ACL will be updated.
     * @param acl The access control list for the resource.
     * @param groups A list of all groups available in the system.
     *
     * @return The required dialog.
     */
    LegacyView updateAcl(ResourceSummary resource,
                         ACL acl,
                         Collection<Group> groups);


    /**
     * Create a dialog.
     *
     * @param alias The alias being edited.
     * @param targetName The name of the target resource.
     * @param targetRoot The root of the target resource.
     *
     * @return The required dialog.
     */
    LegacyView updateAlias(ResourceSummary alias,
                           String targetName,
                           ResourceSummary targetRoot);


    /**
     * Create a dialog.
     *
     * @param template Template to display.
     *
     * @return The required dialog.
     */
    LegacyView previewTemplate(Template template);


    /**
     * Create a dialog.
     *
     * @param item The resource to rename.
     * @param ds The Duration summary of the resource.
     *
     * @return The required dialog.
     */
    LegacyView editCaching(ResourceSummary item, Duration ds);


    /**
     * Create a dialog.
     *
     * @return The required dialog.
     */
    LegacyView login();


    /**
     * Create a dialog.
     *
     * @param model The template to update.
     * @param proxy The resource model.
     * @param ssm The selection model.
     *
     * @return The required dialog.
     */
    LegacyView editTemplate(Template model,
                            ResourceSummary proxy,
                            SingleSelectionModel ssm);


    /**
     * Create a dialog.
     *
     * @return The required dialog.
     */
    EditTextFile editTextFile();


    /**
     * Create a the main window.
     *
     * @param user The currently logged in user.
     */
    void mainWindow(User user);


    /**
     * Create a dialog.
     *
     * @param delta The user to edit.
     * @param groups The list of all groups.
     *
     * @return The required dialog.
     */
    LegacyView editUser(User delta, Collection<Group> groups);


    /**
     * Create a dialog.
     *
     * @param resource The model data of the resource.
     * @param data The metadata.
     * @param ssm The selection model.
     *
     * @return The required dialog.
     */
    LegacyView updateMetadata(ResourceSummary resource,
                              Set<Entry<String, String>> data,
                              SingleSelectionModel ssm);
}
