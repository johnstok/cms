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

import ccc.contentcreator.api.ResourceServiceAsync;
import ccc.contentcreator.api.Root;

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
    private final Tree _contentTree;
    private final Tree _assetsTree;
    private final Tree _usersTree;

    /**
     * Constructor.
     *
     * @param view LeftRightPane of the surrounding view.
     * @param rsa ResourceServiceAsync.
     */
    ResourceNavigator(final LeftRightPane view,
                      final ResourceServiceAsync rsa) {

        _view = view;

        _contentTree = new EnhancedResourceTree(rsa, Root.CONTENT, _view);
        _assetsTree = new EnhancedResourceTree(rsa, Root.ASSETS, _view);
        _usersTree = new UserTree(_view);

        setLayout(new AccordionLayout());
        setBodyBorder(false);
        setHeading("Resource Navigator");

        final ContentPanel contentPanel = new ContentPanel();
        contentPanel.getHeader().setId("content-navigator");
        contentPanel.setScrollMode(Scroll.AUTO);
        contentPanel.setHeading("Content");
        contentPanel.add(_contentTree);
        add(contentPanel);

        final ContentPanel assetsPanel = new ContentPanel();
        assetsPanel.getHeader().setId("assets-navigator");
        assetsPanel.setScrollMode(Scroll.AUTO);
        assetsPanel.setHeading("Assets");
        assetsPanel.add(_assetsTree);
        add(assetsPanel);

        final ContentPanel usersPanel = new ContentPanel();
        usersPanel.getHeader().setId("user-navigator");
        usersPanel.setScrollMode(Scroll.AUTO);
        usersPanel.setHeading("Users");
        usersPanel.add(_usersTree);
        add(usersPanel);

    }
}
