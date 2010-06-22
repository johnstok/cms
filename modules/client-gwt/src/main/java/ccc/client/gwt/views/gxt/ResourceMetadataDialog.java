/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * This file is part of Content Control.
 *
 * Content Control is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Content Control is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Content Control.  If not, see http://www.gnu.org/licenses/.
 *
 * Revision      $Rev: 2472 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2010-02-18 15:56:04 +0000 (Thu, 18 Feb 2010) $
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.client.gwt.views.gxt;

import static ccc.client.core.InternalServices.*;

import java.util.Collection;
import java.util.Map;

import ccc.api.core.Resource;
import ccc.client.core.I18n;
import ccc.client.core.Response;
import ccc.client.core.ValidationResult;
import ccc.client.gwt.binding.ResourceSummaryModelData;
import ccc.client.gwt.core.GlobalsImpl;
import ccc.client.gwt.core.SingleSelectionModel;
import ccc.client.gwt.remoting.UpdateMetadataAction;
import ccc.client.gwt.widgets.ContentCreator;
import ccc.client.gwt.widgets.MetadataGrid;

import com.extjs.gxt.ui.client.event.BoxComponentEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.form.TextField;


/**
 * Editable view of a resource's metadata.
 *
 * @author Civic Computing Ltd.
 */
public class ResourceMetadataDialog extends AbstractEditDialog {


    private ResourceSummaryModelData _resource;
    private SingleSelectionModel _ssm;

    private final TextField<String> _title = new TextField<String>();
    private final TextField<String> _description = new TextField<String>();
    private final TextField<String> _tags = new TextField<String>();

    private final MetadataGrid _metadataPanel;

    /**
     * Constructor.
     *
     * @param resource The model data of the resource.
     * @param data The metadata.
     * @param ssm The selection model.
     */
    public ResourceMetadataDialog(final ResourceSummaryModelData resource,
                          final Collection<Map.Entry<String, String>> data,
                          final SingleSelectionModel ssm) {
        super(I18n.UI_CONSTANTS.metadata(), new GlobalsImpl());

        _ssm = ssm;
        _resource = resource;

        _title.setFieldLabel(constants().title());
        _title.setAllowBlank(false);
        _title.setId("title");
        _title.setValue(resource.getTitle());

        _description.setFieldLabel(constants().description());
        _description.setAllowBlank(true);
        _description.setId("description");
        _description.setValue(resource.getDescription());

        _tags.setFieldLabel(constants().tags());
        _tags.setAllowBlank(true);
        _tags.setId("tags");
        _tags.setValue(resource.getTags());

        addField(_title);
        addField(_description);
        addField(_tags);

        final Text fieldName = new Text("Values:");
        fieldName.setStyleName("x-form-item");
        addField(fieldName);

        _metadataPanel = new MetadataGrid(data);

        addField(_metadataPanel);

        addListener(Events.Resize,
            new Listener<BoxComponentEvent>() {
            @Override
            public void handleEvent(final BoxComponentEvent be) {
                _metadataPanel.handleResize(be);
            }
        });
    }


    /** {@inheritDoc} */
    @Override
    protected SelectionListener<ButtonEvent> saveAction() {
        return new SelectionListener<ButtonEvent>() {
            @Override public void componentSelected(final ButtonEvent ce) {

                final ValidationResult vr = new ValidationResult();
                if (!_title.getValue().matches("[^<^>]*")) {
                    vr.addError(constants().titlesMustNotContainBrackets());
                }
                vr.addError(
                    VALIDATOR.notEmpty(
                        _title.getValue(), _title.getFieldLabel()));
                vr.addError(
                    VALIDATOR.validateMetadataValues(
                        _metadataPanel.currentMetadata()));

                if (!vr.isValid()) {
                    ContentCreator.WINDOW.alert(vr.getErrorText());
                    return;
                }

                updateMetaData();
            }
        };
    }

    private void updateMetaData() {
        final Map<String, String> metadata =
            _metadataPanel.currentMetadata();
        final String tags =
            (null==_tags.getValue()) ? "" : _tags.getValue();
        final String title = _title.getValue();
        final String description = _description.getValue();

        final Resource r = new Resource();
        r.setId(_resource.getId());
        r.setTitle(title);
        r.setDescription(description);
        r.setMetadata(metadata);
        r.setTags(ResourceSummaryModelData.parseTagString(tags));
        r.addLink(
            Resource.METADATA,
            _resource.getDelegate().uriMetadata().toString());

        new UpdateMetadataAction(r) {
                /** {@inheritDoc} */
                @Override protected void onNoContent(
                                             final Response response) {
                    _resource.setTags(tags);
                    _resource.setTitle(title);
                    _resource.setDescription(description);
                    _ssm.update(_resource);
                    ResourceMetadataDialog.this.hide();
                }
        }.execute();
    }
}
