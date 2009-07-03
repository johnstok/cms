package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.api.ResourceType;
import ccc.api.TemplateSummary;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.dialogs.ChooseTemplateDialog;

/**
 * Chooses template for the resource.
 *
 * @author Civic Computing Ltd.
 */
public final class ChooseTemplateAction
    implements
        Action {

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
            GLOBALS.alert(UI_CONSTANTS.noFolderSelected());
            return;
        }

        if (ResourceType.PAGE==item.getType()
            || ResourceType.FOLDER==item.getType()
            || ResourceType.SEARCH==item.getType()) {
            _qs.templates(
                new ErrorReportingCallback<Collection<TemplateSummary>>(UI_CONSTANTS.chooseTemplate()) {
                    public void onSuccess(final Collection<TemplateSummary> templates) {
                        new ChooseTemplateDialog(
                            item,
                            templates,
                            _selectionModel
                        ).show();
                    }
                }
            );
        } else {
            GLOBALS.alert(UI_CONSTANTS.templateCannotBeChosen());

        }
    }
}
