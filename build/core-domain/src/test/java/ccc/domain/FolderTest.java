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

import java.util.Collections;

import junit.framework.TestCase;


/**
 * Tests for the {@link Folder} class.
 *
 * @author Civic Computing Ltd
 */
public class FolderTest extends TestCase {

   /**
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
   }

   /**
    * @see junit.framework.TestCase#tearDown()
    */
   protected void tearDown() throws Exception {
      super.tearDown();
   }

   
   public void testFolderTypeIsFolder() {
      
      // ACT
      Resource resource = new Folder();
      
      // ASSERT
      assertEquals(ResourceType.FOLDER, resource.type());
   }
   
   
   public void testResourceCanCastToFolder() {
      
      // ACT
      Resource resource = new Folder();
      
      // ASSERT
      assertEquals(Folder.class, resource.asFolder().getClass());
   }
   
   
   public void testEmptyFolderHasSizeZero() {
      
      // ACT
      int size = new Folder().size();
      
      // ASSERT
      assertEquals(0, size);
   }
   
   public void testAddContentToFolder() {
      
      // ARRANGE
      Folder folder = new Folder();
      final Content content = new Content("Name");
      
      // ACT
      folder.add(content);
      
      // ASSERT
      assertEquals(1, folder.size());
      assertEquals(Collections.singletonList(content), folder.entries());
   }
   
   public void testAddFolderToFolder() {
      
      // ARRANGE
      Folder folder = new Folder();
      final Folder entry = new Folder();
      
      // ACT
      folder.add(entry);
      
      // ASSERT
      assertEquals(1, folder.size());
      assertEquals(Collections.singletonList(entry), folder.entries());
   }
}
