package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.api.ResourceType;
import ccc.api.TemplateSummary;
import ccc.contentcreator.api.UIConstants;
import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.IGlobals;
import ccc.contentcreator.client.IGlobalsImpl;
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

    private IGlobals _globals = new IGlobalsImpl();
    private final UIConstants _constants = _globals.uiConstants();

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
            _globals.alert(_constants.noFolderSelected());
            return;
        }

        if (ResourceType.PAGE==item.getType()
            || ResourceType.FOLDER==item.getType()
            || ResourceType.SEARCH==item.getType()) {
            QUERIES_SERVICE.templates(
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
            _globals.alert(_constants.templateCannotBeChosen());

        }
    }
}
