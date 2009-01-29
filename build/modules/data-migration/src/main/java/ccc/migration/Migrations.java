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
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
    private static final boolean DEBUG = false;
    private static Logger log = Logger.getLogger(Migrations.class);

    private final LegacyDBQueries _queries;
    private final Registry _registry = new JNDI();
    /** _templates : Map. */
    private final Map<String, ResourceSummary> _templates =
        new HashMap<String, ResourceSummary>();

    private final Map<Integer, UserSummary> _users =
        new HashMap<Integer, UserSummary>();

    private ResourceSummary _assetRoot;
    private ResourceSummary _templateFolder;
    private ResourceSummary _contentRoot;
    private Properties _props;

    /**
     * Constructor.
     *
     * @param queries Queries
     * @param props Properties of the migration
     */
    public Migrations(final LegacyDBQueries queries, final Properties props) {
        _queries = queries;
        _props = props;
    }

    /**
     * Creates root and migrates all children under it.
     *
     */
    public void migrate() {
        createDefaultFolderStructure();
        migrateUsers();
        migrateResources(_contentRoot._id, 0);
        migrateFilesAndImages();
    }

    /**
     * TODO: Add a description of this method.
     *
     */
    private void migrateFilesAndImages() {
        final InputStream mimes =
            Thread.currentThread().
            getContextClassLoader().
            getResourceAsStream("ccc7mime.types");

        final MimetypesFileTypeMap mimemap = new MimetypesFileTypeMap(mimes);

        final ResourceSummary filesResource =
            commands().createFolder(_contentRoot._id, "files");  // FIXME: Specify actor & date
        final ResourceSummary imagesResource =
            commands().createFolder(_contentRoot._id, "images");  // FIXME: Specify actor & date

        final HttpClient client = new HttpClient();

        authenticateForUpload(
            client, _props.getProperty("targetApplicationURL"));

        final List<FileDelta> files =_queries.selectFiles();
        for (final FileDelta legacyFile : files) {
            uploadFile(filesResource,
                client, legacyFile,
                _props.getProperty("filesSourcePath"), mimemap);
        }

        final List<FileDelta> images =
            _queries.selectImages();
        for (final FileDelta legacyFile : images) {
            uploadFile(imagesResource,
                client, legacyFile,
                _props.getProperty("imagesSourcePath"), mimemap);
        }
    }

    /**
     * Authenticate using login form.
     *
     * @param client
     */
    public void authenticateForUpload(final HttpClient client,
                                      final String appURL) {

        final GetMethod get =
            new GetMethod(appURL+"/upload");
        try {
            client.executeMethod(get);
        } catch (final Exception e) {
            log.error("initial get method failed ", e);
        }
        get.releaseConnection();

        final PostMethod authpost = new PostMethod(appURL+"/j_security_check");
        final NameValuePair userid   =
            new NameValuePair("j_username", "migration");
        final NameValuePair password =
            new NameValuePair("j_password", "migration");
        authpost.setRequestBody(
            new NameValuePair[] {userid, password});

        try {
            final int status = client.executeMethod(authpost);
            log.debug(status);
        } catch (final Exception e) {
            log.error("Authentication failed ", e);
        }
        authpost.releaseConnection();

        // in order to prevent 'not a multipart post error' for the first upload
        try {
            client.executeMethod(get);
        } catch (final Exception e) {
            log.error("get method failed ", e);
        }
        get.releaseConnection();
    }

    private void uploadFile(final ResourceSummary filesResource,
                            final HttpClient client,
                            final FileDelta legacyFile,
                            final String directory,
                            final MimetypesFileTypeMap mimemap) {

        final File file = new File(directory+legacyFile._name);
        if (!file.exists()) {
            log.debug("File not found: "+legacyFile._name);
        } else {
            try {
                final PostMethod filePost =
                    new PostMethod(_props.getProperty("targetUploadURL"));
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
                fp.setContentType(mimemap.getContentType(file));
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
        _assetRoot = commands().createRoot(PredefinedResourceNames.ASSETS);  // FIXME: Specify actor & date
        _templateFolder =
            commands().createFolder(_assetRoot._id,
                PredefinedResourceNames.TEMPLATES);  // FIXME: Specify actor & date
        _contentRoot = commands().createRoot(PredefinedResourceNames.CONTENT);  // FIXME: Specify actor & date
        commands().lock(_contentRoot._id);  // FIXME: Specify actor & date
        commands().publish(_contentRoot._id);  // FIXME: Specify actor & date
        commands().unlock(_contentRoot._id);  // FIXME: Specify actor & date
        log.info("Created default folder structure.");
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
        log.info("Migrated users.");
    }

    private void migrateResources(final String parentFolderId,
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
            final ResourceSummary rs =
                commands().createFolder(parentFolderId, r.name());   // FIXME: Specify actor & date

            migrateTemplate(r, rs);

            publish(r, rs);

            migrateResources(rs._id, r.contentId());

        } catch (final Exception e) {
            log.error("Unexpected error", e);
        }
    }

    private void migratePage(final String parentFolderId,
                             final ResourceBean r) {

        try {
            // Query the versions of a page
            final List<Integer> paragraphVersions = determinePageVersions(r);

            // Create the page
            final ResourceSummary rs =
                createPage(parentFolderId, r, paragraphVersions);

            // Apply all updates
//            for (final Integer version : paragraphVersions) {
//                updatePage(r, rs, version);
//            }

            // Set the template
            migrateTemplate(r, rs);

            // Publish, if necessary
            publish(r, rs);

        } catch (final Exception e) {
            log.error("Unexpected error.", e);
        }
    }

    private List<Integer> determinePageVersions(final ResourceBean r) {

        final List<Integer> paragraphVersions =
            _queries.selectParagraphVersions(r.contentId());
        log.debug("Page versions available: "+paragraphVersions);

        if (-1 == paragraphVersions.get(0)) { // Discard working version
            paragraphVersions.remove(0);
            log.debug("Ignoring working copy for page: "+r.contentId());
        }

        if (0 != paragraphVersions.get(0)) { // Do version 0 last
            throw new MigrationException(
                "No 'current version' for page "+r.contentId());
        } else {
            paragraphVersions.remove(0);
            paragraphVersions.add(0);
        }
        log.info(
            "Migrating page "+r.contentId()
            +", "+paragraphVersions.size()+" versions.");
        return paragraphVersions;
    }

    private void updatePage(final ResourceBean r,
                            final ResourceSummary rs,
                            final Integer version) {

        commands().lock(rs._id);// FIXME: Specify actor & date
//        final String userId =
//            determineActor(r.contentId(), version, "%", "MADE LIVE");
        final PageDelta d = migratePageVersion(r, rs._id, version);
        commands().updatePage(d); // FIXME: Specify actor & date
        commands().unlock(rs._id);  // FIXME: Specify actor & date
        log.debug("Updated page: "+r.contentId());
    }

    private ResourceSummary createPage(final String parentFolderId,
                                       final ResourceBean r,
                                       final List<Integer> paragraphVersions) {

        final PageDelta delta =
            migratePageVersion(r, null, paragraphVersions.remove(0));
        final ResourceSummary rs =
            commands().createPage(parentFolderId, delta, null);  // FIXME: Specify actor & date
        log.debug("Created page: "+r.contentId());
        return rs;
    }

    private void publish(final ResourceBean r, final ResourceSummary rs) {
        if (r.isPublished()) {
            final String actor =
                determineActor(r.contentId(),
                               r.legacyVersion(),
                               "Changed Status to  PUBLISHED",
                               "CHANGE STATUS");
            commands().lock(rs._id);  // FIXME: Specify actor & date
            commands().publish(rs._id); // FIXME: Specify actor & date
            commands().unlock(rs._id);  // FIXME: Specify actor & date
        }
    }

    private PageDelta migratePageVersion(final ResourceBean r,
                                         final String id,
                                         final int version) {

        final PageDelta delta = new PageDelta();

        delta._name = r.name();
        delta._title = r.name();
        delta._id = id;

        delta._paragraphs.clear();
        final Map<String, StringBuffer> paragraphs =
            migrateParagraphs(r.contentId(), version);
        for (final Map.Entry<String, StringBuffer> para
                : paragraphs.entrySet()) {
            final ParagraphDelta pd = new ParagraphDelta();
            pd._name = para.getKey();
            pd._textValue = para.getValue().toString();
            pd._type = "TEXT";
            delta._paragraphs.add(pd);
        }
        return delta;
    }

    private void migrateTemplate(final ResourceBean r,
                                 final ResourceSummary rs) {

        final String templateName = r.displayTemplate();

        if (null == templateName) { // Resource has no template
            return;
        }

        if (!_templates.containsKey(templateName)) { // Not yet migrated
            final TemplateDelta t = new TemplateDelta();
            t._body = "Empty template!";
            t._definition = "<fields/>";
            t._description = "No description.";
            t._name = r.displayTemplate();
            t._title = r.displayTemplate();

            final ResourceSummary ts =
                commands().createTemplate(_templateFolder._id, t);  // FIXME: Specify actor & date

            _templates.put(templateName, ts);
        }

        final String templateId = _templates.get(templateName)._id;
        commands().lock(rs._id);  // FIXME: Specify actor & date
        commands().updateResourceTemplate(rs._id, templateId);   // FIXME: Specify actor & date
        commands().unlock(rs._id);  // FIXME: Specify actor & date
    }

    private Map<String, StringBuffer> migrateParagraphs(final int pageId,
                                                        final int version) {
        log.debug("Assembling paragraphs for "+pageId+" v."+version);

        final Map<String, StringBuffer> map =
            new HashMap<String, StringBuffer>();
        final List<ParagraphBean> paragraphs =
            _queries.selectParagraphs(pageId, version);

        for (final ParagraphBean p : paragraphs) {
            if (p.text() == null) { // ignore empty/null texts
                log.debug("Ignoring empty part for paragraph "+p.key());

            } else if (map.containsKey(p.key())) { // merge
                final StringBuffer sb = map.get(p.key());
                map.put(p.key(), sb.append(p.text()));
                log.debug("Appended to paragraph "+p.key());

            } else { // new item
                map.put(p.key(), new StringBuffer(p.text()));
                log.debug("Created paragraph "+p.key());
            }
        }
        log.debug("Assembly done.");
        return map;
    }

    private String determineActor(final int id,
                                  final int version,
                                  final String comment,
                                  final String action) {

        final Integer userId =
            _queries.selectUserFromLog(id, version, action, comment);

        log.debug("Actor for "+action+" on "+id+" v."+version+" is "+userId);

        final UserSummary user =_users.get(userId);

        if (null==user) {
            log.warn("Unable to determine user for action "+action);
            // TODO: select 'unknown' user.
            return null;
        }

        return user._id;
    }

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
