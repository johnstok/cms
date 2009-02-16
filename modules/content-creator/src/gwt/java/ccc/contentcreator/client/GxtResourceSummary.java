/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import static ccc.contentcreator.binding.DataBinding.*;

import com.extjs.gxt.ui.client.data.BaseModelData;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class GxtResourceSummary
    extends
        BaseModelData {

    /**
     * Constructor.
     *
     * @param summary The resource summary as a JSON string.
     */
    public GxtResourceSummary(final JSONValue summary) {

        final JSONObject summaryObject = summary.isObject();

        set(ID,
            summaryObject.get("id").isString().stringValue());
        set(PARENT_ID,
            summaryObject.get("parentId").isString().stringValue());
        set(NAME,
            summaryObject.get("name").isString().stringValue());
        set(PUBLISHED,
            (null==summaryObject.get("publishedBy"))
                ? null
                :summaryObject.get("publishedBy").isString().stringValue());
        set(TITLE,
            summaryObject.get("title").isString().stringValue());
        set(LOCKED,
            (null==summaryObject.get("lockedBy"))
                ? null
                :summaryObject.get("lockedBy").isString().stringValue());
        set(TYPE,
            summaryObject.get("type").isString().stringValue());
        set(CHILD_COUNT,
            (int) summaryObject.get("childCount").isNumber().doubleValue());
        set(FOLDER_COUNT,
            (int) summaryObject.get("folderCount").isNumber().doubleValue());
        set(MM_INCLUDE,
            summaryObject.get("includeInMainMenu").isBoolean().booleanValue());
        set(SORT_ORDER,
            (null==summaryObject.get("sortOrder"))
                ? null
                :summaryObject.get("lockedBy").isString().stringValue());
    }
}
