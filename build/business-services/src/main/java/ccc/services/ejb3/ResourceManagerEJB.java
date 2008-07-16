/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see SubVersion log
 *-----------------------------------------------------------------------------
 */
package ccc.services.ejb3;

import static javax.ejb.TransactionAttributeType.REQUIRED;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.Content;
import ccc.domain.Resource;
import ccc.services.ResourceManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
@Stateless
@TransactionAttribute(REQUIRED)
public class ResourceManagerEJB implements ResourceManager {
   
   @PersistenceContext(unitName="ccc-persistence") private EntityManager em;
   
   /**
    * Constructor.
    *
    * @param em2
    */
   public ResourceManagerEJB(EntityManager entityManager) {
      em = entityManager;
   }
   
   /**
    * @see ccc.services.ResourceManager#create()
    */
   @Override
   public void create() {
      Resource r = null;
      em.persist(r);
      System.out.println("created");      
   }
   
   /**
    * @see ccc.services.ResourceManager#lookup(java.lang.String)
    */
   @Override
   public Resource lookup(String absoulteURI) {
      return Resource.class.cast(em.createQuery(null).getSingleResult());
   }

}
