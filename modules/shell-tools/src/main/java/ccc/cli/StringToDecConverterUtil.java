/*-----------------------------------------------------------------------------
 * Copyright © 2011 Civic Computing Ltd.
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
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see the subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.cli;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;


/**
 * TODO: Add a description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class StringToDecConverterUtil {
    private static final Logger LOG =
        Logger.getLogger(StringToDecConverterUtil.class);

    private static final String DECIMAL_PATTERN = "[+,0-9eE.-]+";
    private static final BigDecimal MAX_VALUE = new BigDecimal("1E+14");
    //private static final BigDecimal MIN_VALUE = new BigDecimal("-9E-7");

    private static final int MAX_SCALE = 6;
    private static final int MAX_PRECISION = 19;

    private static final String GET_PARAGRAPHS_QUERY =
        "select page_revision_id, value_text "
        + "from page_revision_paragraphs where type = 'NUMBER'";

    private static final String UPDATE_PARAGRAPH =
        "update page_revision_paragraphs "
        + "set value_decimal = ? "
        + "where page_revision_id = ? and value_decimal is null";

    /**
     *
     * Converts a number represented as text to a decimal number.
     *
     * @param textNumber text representation of a number
     * @return BigDecimal
     */
    public BigDecimal convert(final String textNumber) {
        BigDecimal decNumber = null;
        if(textNumber != null && textNumber.matches(DECIMAL_PATTERN)) {
            // BigNumber(String) constructor does not allow commas
            decNumber = new BigDecimal(textNumber.replaceAll(",", ""));

            if(decNumber.precision() > MAX_PRECISION
                || decNumber.scale() < 0
                   && Math.abs(decNumber.scale()) + decNumber.precision()
                   > MAX_PRECISION - MAX_SCALE
                || decNumber.compareTo(MAX_VALUE) == 1) {

                LOG.warn("Value is larger than maximum precision allowed "
                        +"and was rejected.");
                return null;
            }

            if(decNumber.scale() > MAX_SCALE) {
                LOG.trace("Scale is bigger than maximum allowed: "
                    +decNumber.scale()+". Resetting scale.");
                decNumber = decNumber.setScale(MAX_SCALE,
                    RoundingMode.DOWN);
            }
        }
        return decNumber;
    }

    /**
     * Generates and stores decimal values for the values of the paragraphs
     * of type 'NUMBER'. The new values are stored in the value_decimal column.

     * @param connection The DB connection.
     */
    public void convertParagraphs(final Connection connection) {
        LOG.info("Convert Paragraphs Started");
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            stmt = connection.createStatement();
            LOG.trace("Getting the paragraphs where type is 'NUMBER'");
            rs = stmt.executeQuery(GET_PARAGRAPHS_QUERY);

            if(rs != null && rs.next()) {
                LOG.debug("Processing result set");
                pstmt = connection.prepareStatement(UPDATE_PARAGRAPH);
                int totalparagraphs = 0;
                do {
                    final String textNumber = rs.getString("value_text");
                    final String pageId = rs.getString("page_revision_id");

                    LOG.trace("Found "+textNumber+" for id "+pageId);

                    // Conversion
                    final BigDecimal decNumber = convert(textNumber);
                    if(decNumber == null) {
                        LOG.warn("Failed to convert paragraph "+pageId
                            +" with value "+textNumber);
                    }

                    pstmt.setBigDecimal(1, decNumber);
                    pstmt.setString(2, pageId);
                    pstmt.addBatch();
                    totalparagraphs++;
                } while(rs.next());

                LOG.debug("Executing updates");
                pstmt.executeBatch();
                LOG.info("Processed "+totalparagraphs+" paragraphs");
                LOG.info("Finished");
            }
        } catch (final SQLException e) {
            LOG.fatal(
                "An error occured while converting Strings to Decimal", e);
        } finally {
            LOG.info("Releasing the resources");
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(pstmt);
            DbUtils.closeQuietly(stmt);
        }
    }
}
