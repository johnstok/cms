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

import ccc.contentcreator.actions.ChooseTemplateAction;
import ccc.contentcreator.actions.CreateFileAction;
import ccc.contentcreator.actions.CreateFolderAction;
import ccc.contentcreator.actions.CreatePageAction;
import ccc.contentcreator.actions.CreateTemplateAction;
import ccc.contentcreator.actions.UpdateSortOrderAction;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.toolbar.AdapterToolItem;


/**
 * A toolbar providing actions for a {@link SingleSelectionModel}.
 *
 * @author Civic Computing Ltd.
 */
public class FolderToolBar
    extends
        AbstractToolBar {

    /**
     * Constructor.
     *
     * @param ssm The selection model to use.
     */
    FolderToolBar(final SingleSelectionModel ssm) {

        addSeparator();
        addButton("uploadFile", "Upload File", new CreateFileAction(ssm)); // TODO: I18n
        addSeparator();
        addButton("Create Folder", "Create Folder", new CreateFolderAction(ssm)); // TODO: I18n
        addSeparator();
        addButton("Create Page", "Create Page", new CreatePageAction(ssm)); // TODO: I18n
        addSeparator();
        addButton("Create Template", "Create Template", new CreateTemplateAction(ssm)); // TODO: I18n
        addSeparator();
        addButton("Choose Template", "Choose Template", new ChooseTemplateAction(ssm)); // TODO: I18n
        addSeparator();
        addLabel("Sort order"); // TODO: I18n
        addSortOrderCombo(ssm);
        addSeparator();
    }

    private void addSortOrderCombo(final SingleSelectionModel ssm) {

        final ComboBox<ModelData> sortCombo = new ComboBox<ModelData>();
        sortCombo.setWidth(200);
        sortCombo.setDisplayField("name");
        sortCombo.setEditable(false);
        sortCombo.setAutoWidth(false);

        final ListStore<ModelData> sortStore = new ListStore<ModelData>();
        final ModelData nameAlphanumAsc = new BaseModelData();
        nameAlphanumAsc.set("name", "Name - alphanumeric, ascending"); // TODO: I18n
        nameAlphanumAsc.set("value", "NAME_ALPHANUM_ASC");
        sortStore.add(nameAlphanumAsc);
        final ModelData manual = new BaseModelData();
        manual.set("name", "Manual"); // TODO: I18n
        manual.set("value", "MANUAL");
        sortStore.add(manual);

        sortCombo.setStore(sortStore);
        sortCombo.addListener(
            Events.Select,
            new ListenerAction(new UpdateSortOrderAction(ssm, sortCombo)));
        add(new AdapterToolItem(sortCombo));
    }
}
