package ccc.migration.ccc6.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ccc.migration.ResourceBean;

/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public final class ResourceSelector
    implements
        SqlQuery<List<ResourceBean>> {

    /** {@inheritDoc} */
    @Override
    public List<ResourceBean> handle(final ResultSet rs) throws SQLException {
        final List<ResourceBean> resultList =
            new ArrayList<ResourceBean>();

        while (rs.next()) {
            final int contentId = rs.getInt("CONTENT_ID");
            final String type = rs.getString("CONTENT_TYPE");
            final String name = rs.getString("NAME");
            final String displayTemplate = rs.getString("PAGE");
            final boolean published =
                "PUBLISHED".equals(rs.getString("STATUS"));
            final int legacyVersion = rs.getInt("VERSION_ID");

            resultList.add(new ResourceBean(contentId,
                                            type,
                                            name,
                                            displayTemplate,
                                            published,
                                            legacyVersion));
        }
        return resultList;
    }

    /** {@inheritDoc} */
    @Override
    public String getSql() {
        return
            "SELECT content_id, content_type, name, page, "
            + "status, version_id "
            + "FROM "
            + "c3_content, c3_display_templates WHERE "
            + "c3_content.parent_id = ? AND version_id = 0 AND "
            + "(status = 'PUBLISHED' OR status = 'NEW') AND "
            + "c3_content.display_template_id = "
            + "c3_display_templates.template_id(+)";
    }
}
