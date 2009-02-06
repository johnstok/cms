package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.contentcreator.api.QueriesServiceAsync;
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
        final ModelData item = _selectionModel.getSelectedFolder();
        _queries.folderDelta(
            item.<String>get("id"),
            new ErrorReportingCallback<ResourceDelta>(){
                public void onSuccess(final ResourceDelta delta) {
                    _queries.templates(
                        new ErrorReportingCallback<Collection<TemplateDelta>>(){
                            public void onSuccess(
                                    final Collection<TemplateDelta> templates) {
                                new ChooseTemplateDialog(
                                    delta,
                                    templates)
                                .show();
                            }
                        }
                    );
                }
            }
        );
    }
}
