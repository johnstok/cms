package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.ChooseTemplateDialog;
import ccc.services.api.ResourceDelta;
import ccc.services.api.TemplateDelta;

import com.extjs.gxt.ui.client.data.ModelData;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class ChooseTemplateAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();
    private final UIConstants _constants = Globals.uiConstants();

    private final SingleSelectionModel _selectionModel;
    private final boolean _isTreeSelection;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     * @param isTreeSelection The boolean for tree/table selection.
     */
    public ChooseTemplateAction(final SingleSelectionModel selectionModel,
                                final boolean isTreeSelection) {
        _selectionModel = selectionModel;
        _isTreeSelection = isTreeSelection;
    }

    /** {@inheritDoc} */
    public void execute() {
        ModelData item = null;
        if (_isTreeSelection) {
            item = _selectionModel.treeSelection();
        } else {
            item = _selectionModel.tableSelection();
        }

        if ("PAGE".equals(item.<String>get("type"))
            || "FOLDER".equals(item.<String>get("type"))
            || "SEARCH".equals(item.<String>get("type"))) {
            _queries.resourceDelta(
                item.<String>get("id"),
                new ErrorReportingCallback<ResourceDelta>(){
                    public void onSuccess(final ResourceDelta delta) {
                        _queries.templates(
                        new ErrorReportingCallback<Collection<TemplateDelta>>(){
                            public void onSuccess(
                                final Collection<TemplateDelta> templates) {
                                new ChooseTemplateDialog(
                                    delta._id,
                                    delta._templateId,
                                    templates)
                                .show();
                            }
                        });
                    }
                }
            );

        } else {
            Globals.alert(_constants.templateCannotBeChosen());

        }
    }
}
