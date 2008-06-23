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
package ccc.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import ccc.domain.Resource;


/**
 * A very simple persistence engine to get started.
 *
 * @author Civic Computing Ltd
 */
public class TrivialPersistence {

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

   public static SessionFactory getSessionFactory() {
       return sessionFactory;
   }

   public static String save() {
      Session s = TrivialPersistence.getSessionFactory().getCurrentSession();
      Transaction t = s.beginTransaction();
      Resource r = new Resource();
      s.save(r);
      t.commit();
      return r.toString();
   }
}
