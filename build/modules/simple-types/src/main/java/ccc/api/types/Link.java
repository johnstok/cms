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
package ccc.api.types;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Helper class for building URIs.
 * <p>This class implements the URI template specification version 03:
 * http://bitworking.org/projects/URI-Templates/spec/
 *
 * @author Civic Computing Ltd.
 */
public class Link {

    private String _uri;


    /**
     * Constructor.
     *
     * @param uri The URI as a string.
     */
    public Link(final String uri) {
        _uri     = DBC.require().notNull(uri);
    }


    /**
     * Build a URI with the specified parameter.
     *
     * @param name The parameter name.
     * @param value The parameter value.
     * @param encoder The encoder to use when building a string.
     *
     * @return The URI, as a string.
     */
    public String build(final String name,
                        final String value,
                        final Encoder encoder) {
        return build(
            Collections.singletonMap(name, new String[] {value}),
            encoder);
    }


    /** {@inheritDoc} */
    @Override
    public String toString() {
        return _uri;
    }


    /**
     * Build a URI with no specified parameters.
     *
     * @param encoder The encoder to use when building a string.
     *
     * @return The URI, as a string.
     */
    public String build(final Encoder encoder) {
        return build(new HashMap<String, String[]>(), encoder);
    }


    /**
     * Build a URI with the specified parameters.
     *
     * @param params The template parameters.
     * @param encoder The encoder to use when building a string.
     *
     * @return The URI, as a string.
     */
    public String build(final Map<String, String[]> params,
                        final Encoder encoder) {

        final StringBuffer sb = new StringBuffer();
        StringBuffer pattern = new StringBuffer();

        boolean inPattern = false;
        for (int i=0; i<_uri.length(); i++) {
            final char c = _uri.charAt(i);
            if ('{'==c) {
                inPattern=true;
                continue;
            } else if ('}'==c) {
                inPattern=false;
                sb.append(evaluate(pattern.toString(), params, encoder));
                pattern = new StringBuffer();
                continue;
            }
            if (!inPattern) {
                sb.append(c);
            } else {
                pattern.append(c);
            }
        }
        if (inPattern) {
            throw new IllegalStateException(
                "URI ended with incomplete expression.");
        }
        return sb.toString();
    }


    private String evaluate(final String template,
                            final Map<String, String[]> params,
                            final Encoder encoder) {

      String op = "var";
      String arg = null;

      final String[] parts = template.split("\\|");
      final String varString = parts[parts.length-1];
      if (3==parts.length) {
          op = parts[0].substring(1);
          arg = parts[1];
      }

      final Map<String, String[]> varRes =
          new LinkedHashMap<String, String[]>();
      final String[] vars = varString.split(",");
      for (final String var : vars) {
          final String[] varDef = var.split("=");
          varRes.put(
              varDef[0],
              (varDef.length>1)?new String[] {varDef[1]}:null);
      }

        switch (Operation.valueOf(op.toUpperCase())) {
            case VAR:
                return var(encoder, varRes, params);
            case JOIN:
                return join(encoder, varRes, params, arg);
            case OPT:
                return opt(encoder, varRes, params, arg);
            case NEG:
                return neg(encoder, varRes, params, arg);
            case SUFFIX:
                return suffix(encoder, varRes, params, arg);
            case PREFIX:
                return prefix(encoder, varRes, params, arg);
            case LIST:
                return list(encoder, varRes, params, arg);
            default:
                throw new UnsupportedOperationException(
                    "Unsupported operation: " + op);
        }
    }


    private String neg(final Encoder encoder,
                       final Map<String, String[]> varRes,
                       final Map<String, String[]> params,
                       final String arg) {
        final StringBuffer result = new StringBuffer();
        for (final String var : varRes.keySet()) {
            final String[] param = params.get(var);
            if (null!=param&&param.length>0) {
                /* NO OP */
            } else if (null!=param&&param.length>0) {
                /* NO OP */
            } else {
                result.append(arg);
            }
        }

        return result.toString();
    }


    private String opt(final Encoder encoder,
                       final Map<String, String[]> varRes,
                       final Map<String, String[]> params,
                       final String arg) {
        final StringBuffer result = new StringBuffer();
        for (final String var : varRes.keySet()) {
            final String[] param = params.get(var);
            if (null!=param&&param.length>0) {
                result.append(encoder.encode(arg));
            } else if (null!=param&&param.length>0){
                result.append(encoder.encode(arg));
            }
        }

        return result.toString();
    }


    private String list(final Encoder encoder,
                        final Map<String, String[]> varRes,
                        final Map<String, String[]> params,
                        final String arg) {
        final String result = suffix(encoder, varRes, params, arg);
        return
            (result.length()>0)
                ? result.substring(0, result.length()-arg.length())
                : result;
    }


    // FIXME: Supplying a list variable to the join operator is an error.
    private String join(final Encoder encoder,
                        final Map<String, String[]> varRes,
                        final Map<String, String[]> params,
                        final String arg) {
        final StringBuffer result = new StringBuffer();
        for (final String var : varRes.keySet()) {
            final String[] param = params.get(var);
            final String[] def   = varRes.get(var);
            if (null!=param) {
                for (final String val : param) {
                    if (null==val) { continue; }
                    result.append(var);
                    result.append("=");
                    result.append(encoder.encode(val));
                    result.append(arg);
                }
            } else if (null!=def){
                for (final String val : def) {
                    if (null==val) { continue; }
                    result.append(var);
                    result.append("=");
                    result.append(encoder.encode(val));
                    result.append(arg);
                }
            }
        }
        if (result.length()>0) { result.deleteCharAt(result.length()-1); }

        return result.toString();
    }


    private String prefix(final Encoder encoder,
                          final Map<String, String[]> varRes,
                          final Map<String, String[]> params,
                          final String arg) {
        final StringBuffer result = new StringBuffer();
        for (final String var : varRes.keySet()) {
            final String[] param = params.get(var);
            final String[] def   = varRes.get(var);
            if (null!=param) {
                for (final String val : param) {
                    result.append(arg);
                    result.append(encoder.encode(val));
                }
            } else if (null!=def){
                for (final String val : def) {
                    result.append(arg);
                    result.append(encoder.encode(val));
                }
            }
        }

        return result.toString();
    }


    private String suffix(final Encoder encoder,
                          final Map<String, String[]> varRes,
                          final Map<String, String[]> params,
                          final String arg) {
        final StringBuffer result = new StringBuffer();
        for (final String var : varRes.keySet()) {
            final String[] param = params.get(var);
            final String[] def   = varRes.get(var);
            if (null!=param) {
                for (final String val : param) {
                    result.append(encoder.encode(val));
                    result.append(arg);
                }
            } else if (null!=def){
                for (final String val : def) {
                    result.append(encoder.encode(val));
                    result.append(arg);
                }
            }
        }

        return result.toString();
    }


    private String var(final Encoder encoder,
                       final Map<String, String[]> varRes,
                       final Map<String, String[]> params) {

        final StringBuffer result = new StringBuffer();
        for (final String var : varRes.keySet()) {
            final String[] param = params.get(var);
            final String[] def   = varRes.get(var);
            if (null!=param) {
                for (final String val : param) {
                    result.append(encoder.encode(val));
                }
            } else if (null!=def){
                for (final String val : def) {
                    result.append(encoder.encode(val));
                }
            } else {
                result.append("");
            }
        }
        return result.toString();
    }

    /**
     * Available URI operations.
     *
     * @author Civic Computing Ltd.
     */
    private static enum Operation {
        VAR, JOIN, OPT, NEG, SUFFIX, PREFIX, LIST;
    }

    /**
     * URI encoder API.
     *
     * @author Civic Computing Ltd.
     */
    public static interface Encoder {

        /**
         * Encode a URI.
         *
         * @param uri The URI to encode.
         *
         * @return The encoded URI.
         */
        String encode(String string);
    }
}
