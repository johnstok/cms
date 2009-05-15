package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.api.ResourceType;
import ccc.api.TemplateSummary;
import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.ChooseTemplateDialog;

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
     * @param selectionModel The selection model.
     */
    public ChooseTemplateAction(
          final SingleSelectionModel selectionModel) {
        _selectionModel = selectionModel;
    }

    /** {@inheritDoc} */
    public void execute() {
        final ResourceSummaryModelData item = _selectionModel.tableSelection();

        if (item == null) {
            Globals.alert(Globals.uiConstants().noFolderSelected());
            return;
        }

        if (ResourceType.PAGE==item.getType()
            || ResourceType.FOLDER==item.getType()
            || ResourceType.SEARCH==item.getType()) {
            _queries.templates(
                new ErrorReportingCallback<Collection<TemplateSummary>>() {
                    public void onSuccess(final Collection<TemplateSummary> templates) {
                        new ChooseTemplateDialog(
                            item.getId(),
                            item.getTemplateId(),
                            templates
                        ).show();
                    }
                }
            );
        } else {
            Globals.alert(_constants.templateCannotBeChosen());

        }
    }
}
