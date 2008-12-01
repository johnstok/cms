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

import java.util.Collection;

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.services.api.FolderSummary;

import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.extjs.gxt.ui.client.widget.tree.Tree;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceNavigator extends ContentPanel {

    private final LeftRightPane _view;
    private final Tree _usersTree;

    /**
     * Constructor.
     *
     * @param view LeftRightPane of the surrounding view.
     * @param rsa ResourceServiceAsync.
     */
    ResourceNavigator(final LeftRightPane view,
                      final Collection<FolderSummary> roots,
                      final ResourceServiceAsync rsa) {

        _view = view;

        setLayout(new AccordionLayout());
        setBodyBorder(false);
        setHeading("Navigator");

        for (final FolderSummary root : roots) {
            final EnhancedResourceTree tree =
                new EnhancedResourceTree(rsa, root, _view);

            final ContentPanel contentPanel = new ContentPanel();
            contentPanel.getHeader().setId(root._name+"-navigator");
            contentPanel.setScrollMode(Scroll.AUTO);
            contentPanel.setHeading(root._name);
            contentPanel.add(tree);
            add(contentPanel);
        }



        _usersTree = new UserTree(_view);
        final ContentPanel usersPanel = new ContentPanel();
        usersPanel.getHeader().setId("user-navigator");
        usersPanel.setScrollMode(Scroll.AUTO);
        usersPanel.setHeading("Users");
        usersPanel.add(_usersTree);
        add(usersPanel);

    }
}
