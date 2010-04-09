/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
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
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.widgets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.api.dto.ResourceSummary;
import ccc.api.dto.UserDto;
import ccc.api.types.Permission;
import ccc.client.gwt.core.Globals;
import ccc.client.gwt.core.GlobalsImpl;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;

/**
 * Accordion control for selecting a resource root.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceNavigator extends ContentPanel {

    private final Globals _globals = new GlobalsImpl();
    private final LeftRightPane _view;
    private final Tree _usersTree;
    private final Tree _actionTree;
    private final CommentTree _commentTree;
    private final List<EnhancedResourceTree> _rootTrees =
        new ArrayList<EnhancedResourceTree>();

    /**
     * Constructor.
     *
     * @param view LeftRightPane of the surrounding view.
     * @param roots Collection of the resource roots.
     * @param user UserSummary of the currently logged in user.
     */
    public ResourceNavigator(final LeftRightPane view,
                      final Collection<ResourceSummary> roots,
                      final UserDto user) {
        setId("resource-navigator");

        _view = view;

        setLayout(new AccordionLayout());
        setBodyBorder(false);
        setHeading("Navigator");

        for (final ResourceSummary root : roots) {
            final EnhancedResourceTree enhancedResourceTree =
                new EnhancedResourceTree(root, _view, user, _globals);
            _rootTrees.add(enhancedResourceTree);
            final ContentPanel contentPanel = new ContentPanel();
            contentPanel.getHeader().setId(root.getName()+"-navigator");
            contentPanel.setAnimCollapse(false);
            contentPanel.setScrollMode(Scroll.AUTO);
            contentPanel.setHeading(root.getCappedName());

            contentPanel.add(enhancedResourceTree.treePanel());
            add(contentPanel);
            contentPanel.addListener(
                Events.Expand,
                new Listener<ComponentEvent>(){
                    public void handleEvent(final ComponentEvent bce) {
                        enhancedResourceTree.showTable();
                    }
                }
            );
        }

        _usersTree = new UserTree(_view);
        if (user.hasPermission(Permission.USER_READ)) {
            final ContentPanel usersPanel = new ContentPanel();
            setPanel(usersPanel, "user-navigator",
                "Users", _usersTree);
        }

        _actionTree = new ActionTree(_view);
        final ContentPanel actionPanel = new ContentPanel();
        setPanel(actionPanel, "action-navigator",
            "Actions", _actionTree);

        _commentTree = new CommentTree(_view);
        final ContentPanel commentsPanel = new ContentPanel();
        setPanel(commentsPanel, "comment-navigator",
            _globals.uiConstants().comments(), _commentTree);

        _rootTrees.get(0).showTable();
    }

    private void setPanel(final ContentPanel actionPanel,
                          final String id,
                          final String text,
                          final Tree actionTree) {

        actionPanel.getHeader().setId(id);
        actionPanel.setAnimCollapse(false);
        actionPanel.setScrollMode(Scroll.AUTO);
        actionPanel.setHeading(text);
        actionPanel.add(actionTree.getTree());
        add(actionPanel);
        actionPanel.addListener(
            Events.Expand,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent bce) {
                    actionTree.showTable();
                }
            }
        );
    }
}
