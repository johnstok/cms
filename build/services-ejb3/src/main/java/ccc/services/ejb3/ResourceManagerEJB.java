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

import static ccc.domain.Queries.RESOURCES_ROR_PATH;
import static javax.ejb.TransactionAttributeType.REQUIRED;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ccc.domain.Resource;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
@Stateless
@TransactionAttribute(REQUIRED)
public class ResourceManagerEJB implements ResourceManager {

   @PersistenceContext(unitName="ccc-persistence")
       private final EntityManager em;

   /**
    * Constructor.
    *
    * @param entityManager A JPA entity manager.
    */
   public ResourceManagerEJB(final EntityManager entityManager) {
      em = entityManager;
   }

   /**
    * @see ResourceManager#lookup(java.lang.String)
    */
   @Override
   public Resource lookup(final ResourcePath absoluteURI) {
      return
          Resource.class.cast(
              em.createNamedQuery(RESOURCES_ROR_PATH)
                 .setParameter("path", absoluteURI)
                 .getSingleResult());
   }

}
