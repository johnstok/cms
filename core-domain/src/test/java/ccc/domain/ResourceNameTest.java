/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.domain;

import junit.framework.TestCase;


/**
 * Tests for the {@link ResourceName} class.
 *
 * @author Civic Computing Ltd
 */
public class ResourceNameTest extends TestCase {
   
   private final String LOWERCASE_ALPHABET = "abcdefghijklmnopqrstuvwxyz";
   private final String UPPERCASE_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   private final String DIGITS = "0123456789";
   private final String UNDERSCORE = "_";
   private final String WORD_CHARACTERS = 
      LOWERCASE_ALPHABET
    + UPPERCASE_ALPHABET
    + DIGITS
    + UNDERSCORE;

   public void testWordCharactersAreAccepted() {

      // ACT
      new ResourceName(LOWERCASE_ALPHABET);
      new ResourceName(UPPERCASE_ALPHABET);
      new ResourceName(DIGITS);
      new ResourceName(UNDERSCORE);
      new ResourceName(WORD_CHARACTERS);
   }
   
   public void testZeroLengthStringIsRejected() {
      
      // ARRANGE
      String zeroLength = "";
      
      // ACT
      try {
         new ResourceName(zeroLength);
         fail("ResourceName failed to reject a zero length string.");
      } catch (RuntimeException e) {
         
      // ASSERT
         assertEquals("A resource name must be longer than zero characters.", e.getMessage());
      }
   }
   
   public void testNullIsRejected() {

      // ACT
      try {
         new ResourceName(null);
         fail("ResourceName failed to reject a NULL string.");
      } catch (RuntimeException e) {
         
      //ASSERT
         assertEquals("A resource name may not be NULL.", e.getMessage());
      }
   }
   
   public void testToStringReturnsStringRepresentation() {
      
      // ARRANGE
      ResourceName name = new ResourceName(WORD_CHARACTERS);
      
      // ACT
      String stringRepresentation = name.toString();
      
      // ASSERT
      assertEquals(WORD_CHARACTERS, stringRepresentation);
   }
   
   public void testSpaceIsRejected() {
      
      // ARRANGE
      String whitespace = " ";
      
      // ACT
      try {
         new ResourceName(whitespace);
         fail("ResourceName failed to reject whitespace.");
      } catch (RuntimeException e) {
         
      //ASSERT
         assertEquals("  does not match the java.util.regex pattern '\\w+'.", e.getMessage());
      }
   }
   
   public void testPeriodIsRejected() {
      
      // ARRANGE
      String period = ".";
      
      // ACT
      try {
         new ResourceName(period);
         fail("ResourceName failed to reject period.");
      } catch (RuntimeException e) {
         
      //ASSERT
         assertEquals(". does not match the java.util.regex pattern '\\w+'.", e.getMessage());
      }
   }
   
   public void testTildeIsRejected() {
      
      // ARRANGE
      String tilde = "~";
      
      // ACT
      try {
         new ResourceName(tilde);
         fail("ResourceName failed to reject tilde.");
      } catch (RuntimeException e) {
         
      //ASSERT
         assertEquals("~ does not match the java.util.regex pattern '\\w+'.", e.getMessage());
      }
   }
   
   public void testDashIsRejected() {
      
      // ARRANGE
      String dash = "-";
      
      // ACT
      try {
         new ResourceName(dash);
         fail("ResourceName failed to reject dash.");
      } catch (RuntimeException e) {
         
      //ASSERT
         assertEquals("- does not match the java.util.regex pattern '\\w+'.", e.getMessage());
      }
   }
   
   public void testRfc3986ReservedCharactersAreRejected() {
      
      // ARRANGE
      String[] reservedChars = 
         new String[] {"!", "*", "'", "(", ")", ";", ":", "@", "&", "=",
                       "+", "$", ",", "/", "?", "%", "#", "[", "]"};
      
      for (String reservedChar : reservedChars){
         // ACT
         try {
            new ResourceName(reservedChar);
            fail("ResourceName failed to reject reserved.");
         } catch (RuntimeException e) {
            
         //ASSERT
            assertEquals(reservedChar+" does not match the java.util.regex pattern '\\w+'.", e.getMessage());
         }
      }
   }
   
   public void testEquals() {
      
      // ARRANGE
      ResourceName foo = new ResourceName("fooBAR_123");
      ResourceName bar = new ResourceName("fooBAR_123");
      
      // ASSERT
      assertEquals(foo, bar);
      assertEquals(bar, foo);
   }
   
   public void testHashCode() {
      
      // ARRANGE
      ResourceName foo = new ResourceName("fooBAR_123");
      ResourceName bar = new ResourceName("fooBAR_123");
      
      // ASSERT
      assertEquals(foo.hashCode(), bar.hashCode());
   }
   
   public void testEscapeMethod() {
      
      // ARRANGE
      String invalidCharacters = "!*'();:@&=+$,/\\?%#[]foo BAR_123.~-";
      String expectedName      = "____________________foo_BAR_123___";
      
      // ACT
      ResourceName validCharacters = ResourceName.escape(invalidCharacters);
      
      // ASSERT
      assertEquals(new ResourceName(expectedName), validCharacters);
   }
}
