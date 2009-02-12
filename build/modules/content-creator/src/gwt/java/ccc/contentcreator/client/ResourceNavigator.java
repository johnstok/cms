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

import ccc.services.api.ResourceSummary;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceNavigator extends ContentPanel {

    private final LeftRightPane _view;
    private final UserTree _usersTree;
    private final List<EnhancedResourceTree> _rootTrees =
        new ArrayList<EnhancedResourceTree>();

    /**
     * Constructor.
     *
     * @param view LeftRightPane of the surrounding view.
     * @param roots Collection of the resource roots.
     */
    ResourceNavigator(final LeftRightPane view,
                      final Collection<ResourceSummary> roots) {
        setId("resource-navigator");

        _view = view;

        setLayout(new AccordionLayout());
        setBodyBorder(false);
        setHeading("Navigator");

        for (final ResourceSummary root : roots) {
            final EnhancedResourceTree tree =
                new EnhancedResourceTree(root, _view);
            _rootTrees.add(tree);

            final ContentPanel contentPanel = new ContentPanel();
            contentPanel.getHeader().setId(root._name+"-navigator");
            contentPanel.setScrollMode(Scroll.AUTO);
            contentPanel.setHeading(root._name);
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
        final ContentPanel usersPanel = new ContentPanel();
        usersPanel.getHeader().setId("user-navigator");
        usersPanel.setScrollMode(Scroll.AUTO);
        usersPanel.setHeading("Users");
        usersPanel.add(_usersTree);
        add(usersPanel);
        usersPanel.addListener(
            Events.Expand,
            new Listener<ComponentEvent>(){
                public void handleEvent(final ComponentEvent bce) {
                    _usersTree.showTable();
                }
            }
        );

        _rootTrees.get(0).showTable();
    }
}
