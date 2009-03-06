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

    /**
     * Constructor.
     *
     * @param selectionModel
     */
    public ChooseTemplateAction(final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ModelData item = _selectionModel.treeSelection();

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
