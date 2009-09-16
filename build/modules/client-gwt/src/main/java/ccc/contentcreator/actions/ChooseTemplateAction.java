package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.contentcreator.binding.ResourceSummaryModelData;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.SelectionModelEventBus;
import ccc.contentcreator.client.SingleSelectionModel;
import ccc.contentcreator.controllers.ChangeResourceTemplatePresenter;
import ccc.contentcreator.views.gxt.ChooseTemplateDialog;
import ccc.rest.dto.TemplateSummary;
import ccc.types.ResourceType;

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
            new GetTemplatesAction(UI_CONSTANTS.chooseTemplate()){
                @Override protected void execute(
                                 final Collection<TemplateSummary> templates) {
                    new ChangeResourceTemplatePresenter(
                        GLOBALS,
                        new SelectionModelEventBus(_selectionModel),
                        new ChooseTemplateDialog(),
                        item,
                        templates);
                }
            }.execute();
        } else {
            GLOBALS.alert(UI_CONSTANTS.templateCannotBeChosen());

        }
    }
}
