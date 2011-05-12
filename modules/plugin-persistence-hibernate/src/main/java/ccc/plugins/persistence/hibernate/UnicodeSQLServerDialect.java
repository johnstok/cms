/*-----------------------------------------------------------------------------
 * Copyright © 2010 Civic Computing Ltd.
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
package ccc.plugins.persistence.hibernate;

import java.sql.Types;

import org.hibernate.dialect.SQLServerDialect;


/**
 * Dialect for MS SQLServer handing UTF.
 *
 * @author Civic Computing Ltd.
 */
public class UnicodeSQLServerDialect extends SQLServerDialect{

    /** COLUMN_TYPE_CLOB : String. */
    public static final String COLUMN_TYPE_CLOB    = "nvarchar(max)";
    /** COLUMN_TYPE_VARCHAR : String. */
    public static final String COLUMN_TYPE_VARCHAR = "nvarchar($l)";
    /** COLUMN_TYPE_CHAR : String. */
    public static final String COLUMN_TYPE_CHAR    = "nchar(1)";

    /**
     * Constructor.
     */
    public UnicodeSQLServerDialect() {
      super();
      registerColumnType(Types.CHAR,        COLUMN_TYPE_CHAR);
      registerColumnType(Types.VARCHAR,     COLUMN_TYPE_VARCHAR);
      registerColumnType(Types.LONGVARCHAR, COLUMN_TYPE_VARCHAR);
      registerColumnType(Types.CLOB,        COLUMN_TYPE_CLOB);
    }


}
