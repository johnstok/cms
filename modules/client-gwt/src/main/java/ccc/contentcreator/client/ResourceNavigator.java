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
package ccc.contentcreator.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ccc.rest.dto.ResourceSummary;
import ccc.rest.dto.UserDto;

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

    private final IGlobals _globals = new IGlobalsImpl();
    private final LeftRightPane _view;
    private final UserTree _usersTree;
    private final ActionTree _actionTree;
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
            if ("assets".equals(root.getName())) {
                if (!user.getRoles().contains(IGlobals.ADMINISTRATOR)
                    && !user.getRoles().contains(IGlobals.SITE_BUILDER)) {
                    continue;
                }
            }

            final EnhancedResourceTree tree =
                new EnhancedResourceTree(root, _view, user, _globals);
            _rootTrees.add(tree);

            final ContentPanel contentPanel = new ContentPanel();
            contentPanel.getHeader().setId(root.getName()+"-navigator");
            contentPanel.setAnimCollapse(false);
            contentPanel.setScrollMode(Scroll.AUTO);
            contentPanel.setHeading(root.getCappedName());
            contentPanel.add(tree);
            add(contentPanel);
            contentPanel.addListener(
                Events.Expand,
                new Listener<ComponentEvent>(){
                    public void handleEvent(final ComponentEvent bce) {
                        tree.showTable();
                    }
                }
            );
        }


        _usersTree = new UserTree(_view);
        if (user.getRoles().contains(IGlobals.ADMINISTRATOR)) {
            final ContentPanel usersPanel = new ContentPanel();
            usersPanel.setAnimCollapse(false);
            usersPanel.getHeader().setId("user-navigator");
            usersPanel.setScrollMode(Scroll.AUTO);
            usersPanel.setHeading("Users");
            usersPanel.add(_usersTree.getTree());
            add(usersPanel);
            usersPanel.addListener(
                Events.Expand,
                new Listener<ComponentEvent>(){
                    public void handleEvent(final ComponentEvent bce) {
                        _usersTree.showTable();
                    }
                }
            );
        }

        _actionTree = new ActionTree(_view);
        final ContentPanel actionPanel = new ContentPanel();
        actionPanel.getHeader().setId("action-navigator");
        actionPanel.setAnimCollapse(false);
        actionPanel.setScrollMode(Scroll.AUTO);
        actionPanel.setHeading("Actions");
        actionPanel.add(_actionTree);
        add(actionPanel);
        actionPanel.addListener(
            Events.Expand,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent bce) {
                    _actionTree.showTable();
                }
            }
        );

        _rootTrees.get(0).showTable();
    }
}
