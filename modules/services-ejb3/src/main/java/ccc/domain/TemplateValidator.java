package ccc.domain;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;

import ccc.api.types.DBC;

/**
 * Validates the XML structure of the templates against a specific schema.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateValidator {

    /**
     * @param definition The XML as text
     *
     * @return Null if XML is valid, error message if not.
     */
    public String validate(final String definition) {
        try {
            DBC.require().notEmpty(definition);
        } catch (final IllegalArgumentException e) {
            return "Null or empty definition";
        }

        final StreamSource definitionSource =
            new StreamSource(new StringReader(definition));

        return validate(definitionSource);
    }


    private String validate(final Source definition) {
        String result = null;

        try {
            final String schemaLang = "http://www.w3.org/2001/XMLSchema";
            final SchemaFactory schemaFactory =
                SchemaFactory.newInstance(schemaLang);

            final Schema schema = schemaFactory.newSchema(new StreamSource(
                getClass().getResourceAsStream("/ccc/domain/template.xsd")));

            final Validator validator = schema.newValidator();

            try {
                validator.validate(definition);
            } catch (final SAXException e) {
                result = e.getMessage();
            } catch (final IOException e) {
                throw new RuntimeException(e);
            }
        } catch (final SAXException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
