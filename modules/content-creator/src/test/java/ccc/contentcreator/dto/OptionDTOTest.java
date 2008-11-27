/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */
package ccc.contentcreator.dto;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class OptionDTOTest extends TestCase {

    /**
     * Test.
     */
    public void testHasChanged() {

        // ARRANGE
        final TemplateDTO a =
            new TemplateDTO(
                null,
                0,
                "a",
                "a",
                "a",
                "a",
                "a",
                "",
                "",
                "foo,bar");
        final TemplateDTO b =
            new TemplateDTO(
                null,
                0,
                "b",
                "b",
                "b",
                "b",
                "b",
                "",
                "",
                "foo,bar");


        OptionDTO<TemplateDTO> option;

        // is NULL; set NULL -> hasChanged: FALSE
        option =
            new OptionDTO<TemplateDTO>(null, null, OptionDTO.Type.CHOICES);
        option.setCurrentValue(null);
        assertEquals(false, option.hasChanged());

        // is NULL; set a -> hasChanged: TRUE
        option =
            new OptionDTO<TemplateDTO>(null, null, OptionDTO.Type.CHOICES);
        option.setCurrentValue(a);
        assertEquals(true, option.hasChanged());

        // is a; set b -> hasChanged: TRUE
        option =
            new OptionDTO<TemplateDTO>(a, null, OptionDTO.Type.CHOICES);
        option.setCurrentValue(b);
        assertEquals(true, option.hasChanged());

        // is b; set b -> hasChanged: FALSE
        option =
            new OptionDTO<TemplateDTO>(b, null, OptionDTO.Type.CHOICES);
        option.setCurrentValue(b);
        assertEquals(false, option.hasChanged());

        // is b; set NULL -> hasChanged: TRUE
        option =
            new OptionDTO<TemplateDTO>(b, null, OptionDTO.Type.CHOICES);
        option.setCurrentValue(null);
        assertEquals(true, option.hasChanged());
    }

    /**
     * Test.
     */
    public void testSetCurrentValue() {

        // ARRANGE
        final TemplateDTO expected =
            new TemplateDTO(
                null,
                0,
                "name",
                "title",
                "description",
                "body",
                "def",
                "",
                "",
                "foo,bar");
        final OptionDTO<TemplateDTO> option =
            new OptionDTO<TemplateDTO>(null, null, OptionDTO.Type.CHOICES);
        assertEquals(false, option.hasChanged());

        // ACT
        option.setCurrentValue(expected);

        // ASSERT
        assertEquals(true, option.hasChanged());
        assertSame(expected, option.getCurrentValue());
    }

    /**
     * Test.
     */
    public void testCurrentValueAccessor() {

        // ARRANGE
        final TemplateDTO current =
            new TemplateDTO(
                null,
                0,
                "name",
                "title",
                "description",
                "body",
                "def",
                "",
                "",
                "foo,bar");
        final OptionDTO<TemplateDTO> option =
            new OptionDTO<TemplateDTO>(current, null, OptionDTO.Type.CHOICES);

        // ACT
        final TemplateDTO actual = option.getCurrentValue();

        // ASSERT
        assertSame(current, actual);
    }

    /**
     * Test.
     */
    public void testType() {

        // ARRANGE
        final OptionDTO<TemplateDTO> option =
            new OptionDTO<TemplateDTO>(null,
                                       null,
                                       OptionDTO.Type.TEXT_SINGLE_LINE);

        // ACT
        final OptionDTO.Type actual = option.getType();

        // ASSERT
        assertEquals(OptionDTO.Type.TEXT_SINGLE_LINE, actual);
    }

    /**
     * Test.
     */
    public void testChoices() {

        // ARRANGE
        final TemplateDTO a =
            new TemplateDTO(
                null,
                0,
                "name",
                "title",
                "description",
                "body",
                "def",
                "",
                "",
                "foo,bar");
        final TemplateDTO b =
            new TemplateDTO(
                null,
                0,
                "name",
                "title",
                "description",
                "body",
                "def",
                "",
                "",
                "foo,bar");
        final List<TemplateDTO> choices =
            Arrays.asList(new TemplateDTO[]{a, b});

        final OptionDTO<TemplateDTO> option =
            new OptionDTO<TemplateDTO>(a, choices, OptionDTO.Type.CHOICES);

        // ACT
        final List<TemplateDTO> actual = option.getChoices();

        // ASSERT
        assertEquals(choices, actual);
    }
}
