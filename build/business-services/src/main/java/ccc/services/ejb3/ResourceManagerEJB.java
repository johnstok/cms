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
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
   
   private static final SessionFactory sessionFactory;
   
   static {
      try {
          // Create the SessionFactory from hibernate.cfg.xml
          sessionFactory = new Configuration().configure().buildSessionFactory();
      } catch (Throwable ex) {
          // Make sure you log the exception, as it might be swallowed
          System.err.println("Initial SessionFactory creation failed." + ex);
          throw new ExceptionInInitializerError(ex);
      }
  }

   /**
    * @see ccc.services.ResourceManager#create()
    */
   @Override
   public void create() {
      Session s;
      try {
         s = ((SessionFactory) new InitialContext().lookup("ccc-persistence")).getCurrentSession();
      } catch (HibernateException e) {
         // TODO Auto-generated catch block
         throw new RuntimeException(e);
      } catch (NamingException e) {
         // TODO Auto-generated catch block
         throw new RuntimeException(e);
      }
      Resource r = new Resource();
      s.save(r);
      System.out.println("created");      
   }

}
