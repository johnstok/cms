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

import java.util.Collection;

import ccc.contentcreator.actions.CreateFileAction;
import ccc.contentcreator.actions.CreateFolderAction;
import ccc.contentcreator.api.QueriesService;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.dialogs.ChooseTemplateDialog;
import ccc.contentcreator.dialogs.CreatePageDialog;
import ccc.contentcreator.dialogs.EditTemplateDialog;
import ccc.services.api.ResourceDelta;
import ccc.services.api.TemplateDelta;

import com.extjs.gxt.ui.client.Events;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.toolbar.AdapterToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.LabelToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.TextToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.core.client.GWT;


/**
 * A toolbar providing actions for a {@link ResourceTable}.
 *
 * @author Civic Computing Ltd.
 */
public class FolderToolBar
    extends
        ToolBar {

    protected final QueriesServiceAsync qs = GWT.create(QueriesService.class);

    private ResourceTable _table;

    FolderToolBar(final ResourceTable table) {
        _table = table;


        addSeparator();
        addButton("uploadFile", "Upload File", new CreateFileAction(_table));  // TODO: I18n
        addSeparator();
        addButton("Create Folder", "Create Folder", new CreateFolderAction(_table));  // TODO: I18n
        addSeparator();


        final TextToolItem createPage = new TextToolItem("Create Page"); // TODO: I18n
        createPage.setId("Create Page");
        createPage.addListener(Events.Select, new Listener<ComponentEvent>(){

            public void handleEvent(final ComponentEvent be) {
                final ModelData item = _table.getSelectedFolder();
                Globals.queriesService().templates(
                    new ErrorReportingCallback<Collection<TemplateDelta>>(){
                        public void onSuccess(
                                      final Collection<TemplateDelta> list) {
                            new CreatePageDialog(list, item, _table).show(); // Need deltas here...
                        }});
            }
        });
        add(createPage);


        addSeparator();
        final TextToolItem createTemplate = new TextToolItem("Create Template"); // TODO: I18n
        createTemplate.setId("Create Template");
        createTemplate.addListener(Events.Select, new Listener<ComponentEvent>(){
            public void handleEvent(final ComponentEvent be) {
                final ModelData item = _table.getSelectedFolder();
                new EditTemplateDialog(
                    item.<String>get("id"),
                    _table.detailsStore())
                .show();
            }
        });
        add(createTemplate);


        addSeparator();
        final TextToolItem chooseTemplate = new TextToolItem("Choose Template"); // TODO: I18n
        chooseTemplate.setId("Choose Template");
        chooseTemplate.addListener(Events.Select, new Listener<ComponentEvent>(){
            public void handleEvent(final ComponentEvent be) {
                final ModelData item = _table.getSelectedFolder();
                qs.folderDelta(item.<String>get("id"), new ErrorReportingCallback<ResourceDelta>(){
                    public void onSuccess(final ResourceDelta delta) {
                        qs.templates(new ErrorReportingCallback<Collection<TemplateDelta>>(){
                            public void onSuccess(final Collection<TemplateDelta> templates) {
                                new ChooseTemplateDialog(delta, templates).show();
                            }
                        });
                    }
                });
            }
        });
        add(chooseTemplate);


        addSeparator();
        add(new LabelToolItem("Sort order")); // TODO: I18n
        final ComboBox<ModelData> sortCombo = new ComboBox<ModelData>();
        sortCombo.setWidth(200);
        sortCombo.setDisplayField("name");
        sortCombo.setEditable(false);
        sortCombo.setAutoWidth(false);

        final ListStore<ModelData> sortStore = new ListStore<ModelData>();
        final ModelData nameAlphanumAsc = new BaseModelData();
        nameAlphanumAsc.set("name", "Name - alphanumeric, ascending");
        nameAlphanumAsc.set("value", "NAME_ALPHANUM_ASC");
        sortStore.add(nameAlphanumAsc);
        final ModelData manual = new BaseModelData();
        manual.set("name", "Manual");
        manual.set("value", "MANUAL");
        sortStore.add(manual);

        sortCombo.setStore(sortStore);
        sortCombo.addListener(Events.Select, new Listener<ComponentEvent>(){
            public void handleEvent(final ComponentEvent be) {
                Globals.commandService().updateFolderSortOrder(
                    _table.getSelectedFolder().<String>get("id"),
                    sortCombo.getValue().<String>get("value"),
                    new ErrorReportingCallback<Void>(){
                        public void onSuccess(final Void result) {
                            _table.refreshTable();
                        }});
            }
        });
        add(new AdapterToolItem(sortCombo));

        addSeparator();
    }

    private void addSeparator() {
        add(new SeparatorToolItem());
    }

    private void addButton(final String id,
                           final String text,
                           final Action action) {
        final TextToolItem toolItem = new TextToolItem(text);
        toolItem.setId(id);
        toolItem.addListener(Events.Select, new ListenerAction(action));
        add(toolItem);
    }
}
