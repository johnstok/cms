/*-----------------------------------------------------------------------------
 * Copyright (c) 2009 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dialogs;

import java.util.Map;

import ccc.contentcreator.actions.GetPropertyAction;
import ccc.contentcreator.client.IGlobalsImpl;

import com.google.gwt.http.client.Response;


/**
 * About dialog for version information.
 *
 * @author Civic Computing Ltd.
 */
public class AboutDialog extends AbstractBaseDialog{

    private String _version = "unknown";
    private String _build = "unknown";
    private String _application = "unknown";

    /**
     * Constructor.
     *
     */
    public AboutDialog() {
        super(new IGlobalsImpl().uiConstants().about(),
            new IGlobalsImpl());

        setWidth(220);
        setMinWidth(220);
        setHeight(150);

        final GetPropertyAction action = new GetPropertyAction() {
            /** {@inheritDoc} */
            @Override
            protected void onOK(final Response response) {
                final Map<String, String> map = parseMapString(response);

                _version = map.get("ccc-version");
                _build = map.get("buildNumber");
                _application = map.get("application.name");
                writeAboutText();
            }
        };
        action.execute();
    }

    private void writeAboutText() {

        final StringBuilder sb = new StringBuilder();
        sb.append("Civic Content Control<br/>");
        sb.append("Application: ");
        sb.append(_application);
        sb.append("<br/>");
        sb.append("Version: ");
        sb.append(_version);
        sb.append("<br/>");
        sb.append("Build: ");
        sb.append(_build);
        sb.append("<br/>");
        sb.append("<br/>");
        sb.append("(c) Civic Computing Limited 2008-2009.<br/>");
        sb.append("All rights reserved.");

        addText(sb.toString());
        layout();
    }
}
