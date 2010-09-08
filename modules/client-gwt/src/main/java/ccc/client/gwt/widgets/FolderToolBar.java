/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
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

import ccc.api.types.Permission;
import ccc.client.core.I18n;
import ccc.client.gwt.actions.OpenCreateFileAction;
import ccc.client.gwt.actions.OpenCreateFolderAction;
import ccc.client.gwt.actions.OpenCreatePageAction;
import ccc.client.gwt.actions.OpenCreateTemplateAction;
import ccc.client.gwt.actions.OpenCreateTextFileAction;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.i18n.UIConstants;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.KeyListener;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * A toolbar providing actions for a {@link SingleSelectionModel}.
 *
 * @author Civic Computing Ltd.
 */
public class FolderToolBar
    extends
        AbstractToolBar {

    /** ENTER_KEY : int. */
    private static final int ENTER_KEY = 13;

    private final UIConstants _constants = I18n.UI_CONSTANTS;
    private final TextField<String> _filterString = new TextField<String>();
    private final Button _searchButton;

    private final ResourceTable _ssm;

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     */
    FolderToolBar(final ResourceTable ssm) {
        _ssm = ssm;

        addSeparator(null);
        addButton(Permission.FILE_CREATE,
            "uploadFile",
            _constants.uploadFile(),
            new OpenCreateFileAction(ssm));
        addSeparator(Permission.FILE_CREATE);

        addButton(Permission.FOLDER_CREATE,
            "Create folder",
            _constants.createFolder(),
            new OpenCreateFolderAction(ssm));
        addSeparator(Permission.FOLDER_CREATE);

        addButton(Permission.PAGE_CREATE,
            "Create page",
            _constants.createPage(),
            new OpenCreatePageAction(ssm));
        addSeparator(Permission.PAGE_CREATE);

        addButton(Permission.TEMPLATE_CREATE,
            "Create template",
            _constants.createTemplate(),
            new OpenCreateTemplateAction(ssm));
        addSeparator(Permission.TEMPLATE_CREATE);

        addButton(Permission.FILE_CREATE,
            "Create text file",
            _constants.createTextFile(),
            new OpenCreateTextFileAction(ssm));
        addSeparator(Permission.FILE_CREATE);


        _filterString.addKeyListener(new KeyListener() {
            @Override
            public void componentKeyPress(ComponentEvent event) {
                if (event.getKeyCode() == ENTER_KEY) {
                    _ssm.reload();
                }
            }
        });
        add(_filterString);
        _searchButton = new Button(_constants.search());
        _searchButton.addListener(Events.Select, new SearchListener());
        add(_searchButton);
    }

    /**
     * Listener for user search.
     *
     * @author Civic Computing Ltd.
     */
    private final class SearchListener implements Listener<ComponentEvent> {

        public void handleEvent(final ComponentEvent be) {
            _ssm.reload();
        }
    }

    /**
     * Return the filter string value, appended with % if necessary.
     *
     * @return The filter string.
     */
    public String getFilter() {
        String value =  _filterString.getValue();
        if (value == null) {
            return "%";
        }
        if (value.endsWith("%")) {
            return value;
        }
        return value+"%";
    }
}
