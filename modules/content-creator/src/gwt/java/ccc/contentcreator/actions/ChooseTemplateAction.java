package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.ChooseTemplateDialog;
import ccc.services.api.ResourceDelta;
import ccc.services.api.TemplateDelta;

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

    private final SingleSelectionModel<ResourceSummaryModelData> _selectionModel;

    /**
     * Constructor.
     *
     * @param selectionModel The selection model.
     */
    public ChooseTemplateAction(
          final SingleSelectionModel<ResourceSummaryModelData> selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();

        if (item == null) {
            Globals.alert(Globals.uiConstants().noFolderSelected());
            return;
        }

        if ("PAGE".equals(item.getType())
            || "FOLDER".equals(item.getType())
            || "SEARCH".equals(item.getType())) {
            _queries.resourceDelta(
                item.getId().toString(),
                new ErrorReportingCallback<ResourceDelta>(){
                    public void onSuccess(final ResourceDelta delta) {
                        _queries.templates(
                        new ErrorReportingCallback<Collection<TemplateDelta>>(){
                            public void onSuccess(
                                final Collection<TemplateDelta> templates) {
                                new ChooseTemplateDialog(
                                    delta.getId().toString(),
                                    delta.getTemplateId(),
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
