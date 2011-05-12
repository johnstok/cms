package ccc.domain;

import junit.framework.TestCase;


/**
 * Tests for the {@link TemplateValidator} class.
 *
 * @author Civic Computing Ltd.
 */
public class TemplateValidatorTest extends TestCase {

    /**
     * Test.
     */
    public void testValidSchema() {

        // ARRANGE
        final TemplateValidator tv = new TemplateValidator();

        // ACT
        final String result = tv.validate(VALID_XML_VALID_SCHEMA);

        // ASSERT
        assertNull(result);
    }

    /**
     * Test.
     */
    public void testInvalidSchema() {

        // ARRANGE
        final TemplateValidator tv = new TemplateValidator();

        // ACT
        final String result = tv.validate(VALID_XML_INVALID_SCHEMA);

        // ASSERT
        assertNotNull(result);
    }

    /**
     * Test.
     */
    public void testInvalidXML() {

        // ARRANGE
        final TemplateValidator tv = new TemplateValidator();

        // ACT
        final String result = tv.validate(INVALID_XML);

        // ASSERT
        assertNotNull(result);
    }

    /**
     * Test.
     */
    public void testEmptyDefinition() {

        // ARRANGE
        final TemplateValidator tv = new TemplateValidator();

        // ACT
        final String result = tv.validate("");

        // ASSERT
        final String expected = "Null or empty definition";
        assertEquals(expected, result);
    }

    /**
     * Test.
     */
    public void testMoreThan32Fields() {
     // ARRANGE
        final TemplateValidator tv = new TemplateValidator();

        // ACT
        final String result = tv.validate(MORE_THAN_32_FIELDS);

        // ASSERT
        assertNotNull(result);
    }

    private static final String VALID_XML_VALID_SCHEMA =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        +"<fields>"
        +"\n   <field name=\"dateField\" type=\"date\" "
                + "description=\"birthday\"/>"
        +"\n   <field name=\"oneLineWithValidation\" type=\"text_field\" "
                + "regexp=\"\\d{1,3}\"/>"
        +"\n   <field name=\"manyLines\" type=\"text_area\" "
                + "title=\"Longer text\" />"
        +"\n   <field name=\"richText\" type=\"html\" />"
        +"\n   <field name=\"checkBoxes\" type=\"checkbox\">"
        +"\n       <option default=\"true\" title=\"My Value\" "
                + "value=\"my_value\"/>"
        +"\n       <option title=\"Other Value\" value=\"other_value\"/>"
        +"\n   </field>"
        +"\n   <field name=\"radio\" type=\"radio\">"
        +"\n       <option default=\"true\" title=\"My Value\" "
                + "value=\"my_value\"/>"
        +"\n       <option title=\"Other Value\" value=\"other_value\"/>"
        +"\n   </field>"
        +"\n   <field name=\"combos\" type=\"combobox\">"
        +"\n       <option default=\"true\" title=\"My Value\" "
                + "value=\"my_value\"/>"
        +"\n       <option title=\"Other Value\" value=\"other_value\"/>"
        +"\n   </field>"
        +"\n   <field name=\"list\" type=\"list\">"
        +"\n       <option default=\"true\" title=\"My Value\" "
                + "value=\"my_value\"/>"
        +"\n       <option title=\"Other Value\" value=\"other_value\"/>"
        +"\n   </field>"
        +"\n   <field name=\"photo\" type=\"image\"/>"
        +"\n   <field name=\"number\" type=\"number\"/>"
        +"\n</fields>";

    private static final String VALID_XML_INVALID_SCHEMA =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        +"<fields><garbage />"
        +"\n   <field name=\"oneLineWithValidation\" type=\"text_field\" "
                + "regexp=\"\\d{1,3}\">invalid text</field>"
        +"\n   <field name=\"checkBoxes\" type=\"checkbox\">"
        +"\n       <option title=\"Other Value\" value=\"other_value\"/>"
        +"\n   </field>"
        +"\n   <field name=\"radio\" type=\"radio\">"
        +"\n       <option default=\"true\" title=\"My Value\" "
                + "value=\"my_value\"/>"
        +"\n   </field>"
        +"\n</fields>";

    private static final String INVALID_XML =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        +"<fields <garbage />"
        +"\n   <field name= type=\"text_field\" "
                + "regexp=\"\\d{1,3}\">invalid text</field>";

    private static final String MORE_THAN_32_FIELDS =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        +"<fields>"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" /><field name=\"f\" type=\"html\" />"
        +"<field name=\"f\" type=\"html\" />"
        +"</fields>";
}
