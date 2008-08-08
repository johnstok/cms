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
package ccc.view.contentcreator.client;

import com.google.gwt.user.client.ui.TreeItem;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class GWTSupport {


    /**
     * TODO: Add a description of this method.
     *
     * @param treeItem
     * @return
     */
    public static String calculatePathForTreeItem(final TreeItem treeItem) {

        String path = "/";

        if (null!=treeItem.getParentItem()) {
            path = calculatePathForTreeItem(treeItem.getParentItem()) + treeItem.getText()+"/";
        }

        return path;
    }

}
