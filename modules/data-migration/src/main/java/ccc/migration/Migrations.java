/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd
 * All rights reserved.
 *
 * Revision      $Rev: 220 $
 * Modified by   $Author: keith $
 * Modified on   $Date: 2008-08-07 15:22:12 +0100 (Thu, 07 Aug 2008) $
 *
 * Changes: see subversion log
 *-----------------------------------------------------------------------------
 */
package ccc.migration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

import ccc.commons.JNDI;
import ccc.commons.Registry;
import ccc.commons.XHTML;
import ccc.domain.PredefinedResourceNames;
import ccc.domain.ResourceName;
import ccc.services.api.Commands;
import ccc.services.api.FileDelta;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.Queries;
import ccc.services.api.ResourceSummary;
import ccc.services.api.ServiceNames;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;

/**
 * Data migration from CCC6 to CCC7.
 * TODO: Factor out duplicate template creation code.
 *
 * @author Civic Computing Ltd
 */
public class Migrations {

    private final LegacyDBQueries _queries;
    private final Registry _registry = new JNDI();
    /** _templates : Map. */
    private final Map<String, ResourceSummary> _templates =
        new HashMap<String, ResourceSummary>();

    private final Map<Integer, UserSummary> _users =
        new HashMap<Integer, UserSummary>();

    private static Logger log =
        Logger.getLogger(ccc.migration.Migrations.class);
    private ResourceSummary _assetRoot;
    private ResourceSummary _templateFolder;
    private ResourceSummary _contentRoot;
    private String _targetURL = "http://localhost:8080/creator/upload";

    /**
     * Constructor.
     *
     * @param manager
     * @param queries Queries
     */
    public Migrations(final LegacyDBQueries queries) {
        _queries = queries;
    }

    /**
     * Creates root and migrates all children under it.
     *
     */
    public void migrate() {
        createDefaultFolderStructure();

        // Migrate users
        migrateUsers();

        // Walk the tree migrating each resource
        migrateChildren(_contentRoot._id, 0);

        // Migrate files
        migrateFilesAndImages();
    }

    /**
     * TODO: Add a description of this method.
     *
     */
    private void migrateFilesAndImages() {
        final ResourceSummary filesResource =
            commands().createFolder(_contentRoot._id, "files");
        final ResourceSummary imagesResource =
            commands().createFolder(_contentRoot._id, "images");

        final HttpClient client = new HttpClient();

        authenticate(client);

        final List<FileDelta> files =_queries.selectFiles(LegacyDBQueries.FILE);
        for (final FileDelta legacyFile : files) {
            uploadFile(filesResource, client, legacyFile, "files/");
        }

        final List<FileDelta> images =
            _queries.selectFiles(LegacyDBQueries.IMAGE);
        for (final FileDelta legacyFile : images) {
            uploadFile(imagesResource, client, legacyFile, "images/");
        }
    }

    /**
     * Authenticate using login form.
     *
     * @param client
     */
    private void authenticate(final HttpClient client) {

        final GetMethod get = new GetMethod("http://localhost:8080/creator");
        try {
            client.executeMethod(get);
        } catch (final Exception e) {
            log.error("initial get method failed ", e);
        }
        get.releaseConnection();

        final PostMethod authpost =
            new PostMethod("http://localhost:8080/creator/j_security_check");
        final NameValuePair userid   =
            new NameValuePair("j_username", "migration");
        final NameValuePair password =
            new NameValuePair("j_password", "migration");
        authpost.setRequestBody(
            new NameValuePair[] {userid, password});

        try {
            client.executeMethod(authpost);
        } catch (final Exception e) {
            log.error("Authentication failed ", e);
        }
        authpost.releaseConnection();

        try {
            final int status = client.executeMethod(get);
            if (status != HttpStatus.SC_OK) {
                log.error("Page get request did not return 200");
            }
        } catch (final Exception e) {
            log.error("Authentication failed ", e);
        }

        get.releaseConnection();
    }

    private void uploadFile(final ResourceSummary filesResource,
                            final HttpClient client,
                            final FileDelta legacyFile,
                            final String directory) {

        final File file = new File(directory+legacyFile._name);
        if (!file.exists()) {
            log.debug("File not found: "+legacyFile._name);
        } else {
            try {
                final PostMethod filePost = new PostMethod(_targetURL);
                log.debug("Migrating file: "+legacyFile._name);
                final String name =
                    ResourceName.escape(legacyFile._name).toString();

                String title = legacyFile._title;
                if (title == null) {
                    title = legacyFile._name;
                }
                final String description =
                    legacyFile._description == null ? "" :legacyFile._description;

                final FilePart fp = new FilePart("file", file.getName(), file);
                fp.setContentType(
                    new MimetypesFileTypeMap().getContentType(file));
                final Part[] parts = {
                        new StringPart("fileName", name),
                        new StringPart("title", legacyFile._title),
                        new StringPart("description", description),
                        new StringPart("path", filesResource._id),
                        fp
                };
                filePost.setRequestEntity(
                    new MultipartRequestEntity(parts, filePost.getParams())
                );

                client.getHttpConnectionManager().
                    getParams().setConnectionTimeout(5000);


                final int status = client.executeMethod(filePost);
                if (status == HttpStatus.SC_OK) {
                    log.debug(
                        "Upload complete, response="
                        + filePost.getResponseBodyAsString()
                    );
                } else {
                    log.error(
                        "Upload failed, response="
                        + HttpStatus.getStatusText(status)
                    );
                }
            } catch (final Exception e) {
                log.error("File migration failed ", e);
            }
        }
    }

    private void createDefaultFolderStructure() {
        _assetRoot = commands().createRoot(PredefinedResourceNames.ASSETS);
        _templateFolder =
            commands().createFolder(_assetRoot._id,
                PredefinedResourceNames.TEMPLATES);
        _contentRoot = commands().createRoot(PredefinedResourceNames.CONTENT);
        commands().lock(_contentRoot._id);
        commands().publish(_contentRoot._id);
        commands().unlock(_contentRoot._id);
    }

    private void migrateUsers() {
        final Map<Integer, UserDelta> mus = _queries.selectUsers();
        for (final Map.Entry<Integer, UserDelta> mu : mus.entrySet()) {
            try {
                final UserSummary u = commands().createUser(mu.getValue());
                _users.put(mu.getKey(), u);
            } catch (final RuntimeException e) {
                log.warn("Failed to create user: "+e.getMessage());
            }
        }
    }

    private void migrateChildren(final String parentFolderId,
                                 final Integer parent) {

        final List<ResourceBean> resources = _queries.selectResources(parent);

        for (final ResourceBean r : resources) {

            log.debug("type "+r.type());
            log.debug("name "+r.name());

            // ignore null/empty name
            if (r.name() == null || r.name().trim().equals("")) {
                log.debug("NO NAME");
                continue;
            }

            if (r.type().equals("FOLDER")) {
                migrateFolder(parentFolderId, r);
            } else if (r.type().equals("PAGE")) {
                migratePage(parentFolderId, r);
            } else {
                log.debug("Unkown resource type");
            }
        }
    }

    private void migrateFolder(final String parentFolderId,
                               final ResourceBean r) {

        try {
            log.debug("FOLDER");

            final ResourceSummary rs =
                commands().createFolder(parentFolderId, r.name());

            final String templateId = migrateTemplate(r);
            if (null!=templateId) {
                commands().lock(rs._id);
                commands().updateResourceTemplate(rs._id, templateId);
                commands().unlock(rs._id);
            }

            final String publishedBy = migratePublish(r);
            if (null!=publishedBy) {
                commands().lock(rs._id);
                commands().publish(rs._id); // FIXME: Publisher is wrong.
                commands().unlock(rs._id);
            }

            migrateChildren(rs._id, r.contentId());

        } catch (final Exception e) {
            log.error("Unexpected error", e);
        }
    }

    private void migratePage(final String parentFolderId,
                             final ResourceBean r) {

        try {
            log.debug("PAGE");

            final PageDelta delta = new PageDelta();

            delta._name = r.name();
            delta._title = r.name();

            final Map<String, StringBuffer> paragraphs =
                migrateParagraphs(r.contentId());
            for (final Map.Entry<String, StringBuffer> para
                    : paragraphs.entrySet()) {
                final ParagraphDelta pd = new ParagraphDelta();
                pd._name = para.getKey();
                pd._textValue = para.getValue().toString();
                pd._type = "TEXT";
                delta._paragraphs.add(pd);
            }

            final ResourceSummary rs =
                commands().createPage(parentFolderId, delta, null);


            final String templateId = migrateTemplate(r);
            if (null!=templateId) {
                commands().lock(rs._id);
                commands().updateResourceTemplate(rs._id, templateId);
                commands().unlock(rs._id);
            }


            final String publishedBy = migratePublish(r);
            if (null!=publishedBy) {
                commands().lock(rs._id);
                commands().publish(rs._id); // FIXME: Publisher is wrong.
                commands().unlock(rs._id);
            }

        } catch (final Exception e) {
            log.error("Unexpected error: "+e.getMessage());
        }
    }

    private String migrateTemplate(final ResourceBean r) {

        final String templateName = r.displayTemplate();

        if (null == templateName) { // Resource has no template
            return null;
        }

        if (_templates.containsKey(templateName)) { // Template already migrated
            return _templates.get(templateName)._id;
        }

        final TemplateDelta t = new TemplateDelta();
        t._body = "Empty template!";
        t._definition = "<fields/>";
        t._description = "No description.";
        t._name = r.displayTemplate();
        t._title = r.displayTemplate();

        final ResourceSummary ts =
            commands().createTemplate(_templateFolder._id, t);

        _templates.put(templateName, ts);

        return ts._id;
    }

    /**
     * Merges paragraphs, returns map of joined paragraph text.
     *
     * @param path
     * @param pageId
     */
    private Map<String, StringBuffer> migrateParagraphs(final int pageId) {

        log.debug("#### migrating paragraphs for "+pageId);
        final Map<String, StringBuffer> map =
            new HashMap<String, StringBuffer>();

        final List<ParagraphBean> paragraphs =
            _queries.selectParagraphs(pageId);
        // populate map
        for (final ParagraphBean p : paragraphs) {
            // ignore empty/null texts
            if (p.text() == null || p.text().trim().equals("")) {
                continue;
            }
            if (map.containsKey(p.key())) {
                // merge
                final StringBuffer sb = map.get(p.key());
                map.put(p.key(), sb.append(p.text()));
                log.debug("#### merged texts for Paragraph "+p.key());
            } else {
                // new item
                map.put(p.key(), new StringBuffer(p.text()));
                log.debug("#### Created Paragraph "+p.key());
            }
        }
        return map;
    }


    private String migratePublish(final ResourceBean r) {

        if (r.isPublished()) {
            final Integer legacyUserId =
                _queries.selectUserFromLog(r.contentId(),
                    r.legacyVersion(),
                    "CHANGE STATUS",
                "Changed Status to  PUBLISHED");

            if (legacyUserId != null) {
                final UserSummary user =_users.get(legacyUserId);
                return (null==user)
                ? null
                    : user._id;
            } else {
                log.warn("Unable to determine publisher for "+r.contentId());
                return null;
            }

        } else {
            return null;
        }

    }

    /**
     * TODO: Add a description of this method.
     *
     * @param map
     */
    private void extractURLs(final Map<String, StringBuffer> map) {

        for (final Map.Entry<String, StringBuffer> para : map.entrySet()) {
            final String html =
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
                + "<head><title>Title goes here</title></head>"
                + "<body>"
                + para.getValue()
                + "</body>"
                + "</html>";
            log.debug(para.getValue());
            try {
                final NodeList l =
                    XHTML.evaluateXPath_(
                        new ByteArrayInputStream(html.getBytes()),
                    "//xhtml:a");
                for(int i=0; i<l.getLength(); i++) {
                    log.error(l.item(i).getAttributes().getNamedItem("href"));
                }
            } catch (final RuntimeException e) {
                log.error("Error parsing page.");
            }
        }
    }

    /**
     * Accessor for the content manager.
     *
     * @return A {@link Commands}.
     */
    Commands commands() {
        return _registry.get(ServiceNames.PUBLIC_COMMANDS);
    }

    /**
     * Accessor for the user manager.
     *
     * @return An {@link Queries}.
     */
    Queries queries() {
        return _registry.get(ServiceNames.PUBLIC_QUERIES);
    }
}
