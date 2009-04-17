package ccc.contentcreator.actions;

import java.util.Collection;

import ccc.contentcreator.api.QueriesServiceAsync;
import ccc.contentcreator.callbacks.ErrorReportingCallback;
import ccc.contentcreator.client.Action;
import ccc.contentcreator.client.Globals;
import ccc.contentcreator.dialogs.ChooseTemplateDialog;
import ccc.services.api.ResourceDelta;
import ccc.services.api.TemplateDelta;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class ChooseRootTemplateAction
    implements
        Action {

    private final QueriesServiceAsync _queries = Globals.queriesService();
    private final String _id;

    /**
     * Constructor.
     *
     * @param id The root id.
     */
    public ChooseRootTemplateAction(final String id) {
        _id = id;
    }

    /** {@inheritDoc} */
    public void execute() {
        _queries.resourceDelta(
            _id,
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
    }
}
