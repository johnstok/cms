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
package ccc.migration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ccc.domain.CCCException;
import ccc.domain.Paragraph;
import ccc.domain.ResourceName;
import ccc.domain.ResourcePath;
import ccc.services.ResourceManager;


/**
 * TODO Add Description for this type.
 *
 * @author Civic Computing Ltd
 */
public class MigrationsEJB {

    private final ResourceManager manager;
    private final Queries queries;

    /**
     * Constructor.
     *
     * @param manager
     * @param queries 
     */
    public MigrationsEJB(final ResourceManager manager, Queries queries) {
        this.manager = manager;
        this.queries = queries;
    }
    
    public void migrate() {

        // Create a root content folder.
        createContentRoot();
        ResourcePath rootFolder = new ResourcePath(new ResourceName("Root"));
        manager.createFolder("/Root");
        
        // Walk the tree migrating each resource
        migrateChildren(rootFolder, 0, queries);
    }
    
    private void migrateChildren(ResourcePath path, Integer parent, Queries queries) {
        try {
            ResultSet rs = queries.selectResources(parent);
            while (rs.next()) {
                String type = rs.getString("CONTENT_TYPE");
                if (type.equals("FOLDER")) {
                    System.out.println(path.toString()+"/"+rs.getString("NAME"));
                    
                    ResourcePath childFolder =
                        path.append(ResourceName.escape(rs.getString("NAME")));
                    manager.createFolder(childFolder.toString());
                    migrateChildren(childFolder, rs.getInt("CONTENT_ID"), queries);
                } 
                else if (type.equals("PAGE")) {
                    System.out.println(">"+path.toString()+"/"+rs.getString("NAME"));
                    ResourcePath childContent =
                        path.append(ResourceName.escape(rs.getString("NAME")));
                    try {
                        manager.createContent(childContent.toString());
                        migrateParagraphs(childContent, rs.getInt("CONTENT_ID"));
                        
                    } catch (javax.ejb.EJBException e) {
                        if (e.getCausedByException().getClass().equals(CCCException.class)) {
                            System.err.print(">>>>>> "+e.getMessage());
                        }
                        else {
                            throw e;
                        }
                    }
                }
                else {
                    System.out.println("Unkown resource type");
                }
            }
        }
        catch (SQLException e) {
            throw new CCCException("Migration failed.", e);
        }
    }

    /**
     * Merges paragraphs, creates Paragraph objects and calls manager.
     *
     * @param childContent
     * @param pageId
     * @throws SQLException 
     */
    private void migrateParagraphs(final ResourcePath path, int pageId) throws SQLException {
        System.out.println("#### migrating paragraphs for "+pageId );
        Map<String, Paragraph> map = new HashMap<String, Paragraph>();

        ResultSet rs = queries.selectParagraphs(pageId);
        // populate map
        while (rs.next()) {
            String key = rs.getString("PARA_TYPE");
            String text = rs.getString("TEXT");
            // ignore empty/null texts
            if (text == null) {
                continue;
            }
            if (map.containsKey(key)) {
                // merge
                String existingText = map.get(key).body();
                String newText = existingText + text;
                map.put(key, new Paragraph(newText));
                System.out.println("#### merged texts for Paragraph "+key );
            } else {
                // new item
                map.put(key, new Paragraph(text));
                System.out.println("#### Created Paragraph "+key );
            }
        }
        manager.createParagraphsForContent(path.toString(), map);
    }

    /**
     * @see ccc.migration.Migrations#createContentRoot()
     */
    public void createContentRoot() {
        manager.createRoot();
    }

}
