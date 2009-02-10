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
package ccc.services.ejb3.remote;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import ccc.domain.Alias;
import ccc.domain.CreatorRoles;
import ccc.domain.File;
import ccc.domain.Folder;
import ccc.domain.LogEntry;
import ccc.domain.Page;
import ccc.domain.Paragraph;
import ccc.domain.Resource;
import ccc.domain.ResourceType;
import ccc.domain.Template;
import ccc.domain.User;
import ccc.services.api.AliasDelta;
import ccc.services.api.FileDelta;
import ccc.services.api.FileSummary;
import ccc.services.api.LogEntrySummary;
import ccc.services.api.PageDelta;
import ccc.services.api.ParagraphDelta;
import ccc.services.api.ResourceDelta;
import ccc.services.api.ResourceSummary;
import ccc.services.api.TemplateDelta;
import ccc.services.api.UserDelta;
import ccc.services.api.UserSummary;


/**
 * TODO: Add Description for this type.
 *
 * @author Civic Computing Ltd.
 */
public class ModelTranslation {

    /**
     * Map a collection of Folder to a collection of FolderSummary.
     *
     * @param folders The collection of folders to map.
     * @return The corresponding collection of FolderSummary.
     */
    protected Collection<ResourceSummary> mapFolders(final Collection<Folder> folders) {
        final Collection<ResourceSummary> mapped = new ArrayList<ResourceSummary>();
        for (final Folder f : folders) {
            mapped.add(map(f));
        }
        return mapped;
    }

    /**
     * Map a collection of Resource to a collection of ResourceSummary.
     *
     * @param resources The collection of resources to map.
     * @return The corresponding collection of ResourceSummary.
     */
    protected Collection<ResourceSummary> mapResources(final Collection<Resource> resources) {
        final Collection<ResourceSummary> mapped = new ArrayList<ResourceSummary>();
        for (final Resource r : resources) {
            mapped.add(map(r));
        }
        return mapped;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param listUsers
     * @return
     */
    protected Collection<UserSummary> mapUsers(final Collection<User> users) {
        final Collection<UserSummary> mapped = new ArrayList<UserSummary>();
        for (final User u : users) {
            mapped.add(map(u));
        }
        return mapped;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param listUsers
     * @return
     */
    protected Collection<FileSummary> mapFiles(final Collection<File> files) {
        final Collection<FileSummary> mapped = new ArrayList<FileSummary>();
        for (final File f : files) {
            mapped.add(map(f));
        }
        return mapped;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param history
     * @return
     */
    protected Collection<LogEntrySummary> mapLogEntries(final List<LogEntry> logEntries) {
        final Collection<LogEntrySummary> mapped =
            new ArrayList<LogEntrySummary>();
        for (final LogEntry le : logEntries) {
            mapped.add(map(le));
        }
        return mapped;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param le
     * @return
     */
    protected LogEntrySummary map(final LogEntry le) {
        final LogEntrySummary les = new LogEntrySummary();
        les._action = le.action().name();
        les._actor = le.actor().username();
        les._happenedOn = le.happenedOn().getTime();
        les._summary = le.summary();
        return les;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param r
     * @return
     */
    protected ResourceSummary map(final Resource r) {
        final ResourceSummary rs = new ResourceSummary();
        rs._id = r.id().toString();
        rs._name = r.name().toString();
        rs._parentId = (null==r.parent()) ? null : r.parent().id().toString();
        rs._type = r.type().name();
        rs._lockedBy = (r.isLocked()) ? r.lockedBy().username() : null;
        rs._title = r.title();
        rs._publishedBy = (r.isPublished()) ? r.publishedBy().username() : null;
        rs._childCount = 0;
        rs._folderCount = 0;
        rs._includeInMainMenu = r.includeInMainMenu();
        if (r.type() == ResourceType.FOLDER) {
            rs._childCount = r.as(Folder.class).entries().size();
            rs._folderCount = r.as(Folder.class).folders().size();
            rs._sortOrder = r.as(Folder.class).sortOrder().name();
        }
        return rs;
    }

    /**
     * Map a Folder to a FolderSummary.
     *
     * @param f
     * @return
     */
    protected ResourceSummary map(final Folder f) {
        final ResourceSummary rs = map((Resource) f);
        rs._childCount = f.entries().size();
        rs._folderCount = f.folders().size();
        rs._sortOrder = f.sortOrder().name();
        return rs;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param loggedInUser
     * @return
     */
    protected UserSummary map(final User user) {
        final UserSummary us = new UserSummary();
        us._email = user.email().getText();
        us._id = user.id().toString();
        us._username = user.username();
        return us;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param loggedInUser
     * @return
     */
    protected FileSummary map(final File file) {
        final FileSummary fs = new FileSummary();
        fs._id = file.id().toString();
        fs._name = file.name().toString();
        fs._title = file.title();
        fs._mimeType = file.mimeType().toString();
        fs._path = file.absolutePath().toString();
        return fs;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param template
     * @return
     */
    protected TemplateDelta delta(final Template template) {
        if (null==template) {
            return null;
        }
        final TemplateDelta delta = new TemplateDelta();
        delta._id = template.id().toString();
        delta._body = template.body();
        delta._definition = template.definition();
        delta._description = template.description();
        delta._name = template.name().toString();
        delta._title = template.title();
        return delta;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param user
     * @return
     */
    protected UserDelta delta(final User user) {
        final UserDelta delta = new UserDelta();
        delta._id = user.id().toString();
        delta._email = user.email().getText();
        delta._username = user.username();
        delta._roles = new HashSet<String>();
        for (final CreatorRoles role : user.roles()) {
            delta._roles.add(role.name());
        }
        return delta;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param alias
     * @return
     */
    protected AliasDelta delta(final Alias alias) {
        final AliasDelta delta = new AliasDelta();
        delta._id = alias.id().toString();
        delta._name = alias.name().toString();
        delta._targetId = alias.target().id().toString();
        delta._targetName = alias.target().name().toString();
        return delta;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param file
     * @return
     */
    protected FileDelta delta(final File file) {
        final FileDelta delta = new FileDelta();
        delta._id = file.id().toString();
        delta._name = file.name().toString();
        delta._title = file.title();
        delta._description = file.description();
        return delta;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param find
     * @return
     */
    protected PageDelta delta(final Page page) {
        final PageDelta delta = new PageDelta();
        delta._id = page.id().toString();
        delta._name = page.name().toString();
        delta._title = page.title();
        final Template t = page.template();
        delta._templateId = (null==t) ? null : t.id().toString();
        final Template ct = page.computeTemplate(null);
        delta._computedTemplate = (null==ct) ? null : delta(ct);
        delta._paragraphs = new ArrayList<ParagraphDelta>();
        for (final Paragraph p : page.paragraphs()) {
            final ParagraphDelta pDelta = new ParagraphDelta();
            pDelta._name = p.name();
            pDelta._type = p.type().name();
            pDelta._textValue = p.text();
            pDelta._dateValue = p.date();
            delta._paragraphs.add(pDelta);
        }
        return delta;
    }


    /**
     * TODO: Add a description of this method.
     *
     * @param find
     * @return
     */
    protected ResourceDelta delta(final Resource resource) {
        final ResourceDelta delta = new ResourceDelta();
        delta._id = resource.id().toString();
        delta._name = resource.name().toString();
        delta._title = resource.title();
        delta._tags = resource.tagString();
        final Template t = resource.template();
        delta._templateId = (null==t) ? null : t.id().toString();
        return delta;
    }

    /**
     * TODO: Add a description of this method.
     *
     * @param templates
     * @return
     */
    protected Collection<TemplateDelta> deltaTemplates(final List<Template> templates) {
        final Collection<TemplateDelta> mapped = new ArrayList<TemplateDelta>();
        for (final Template t : templates) {
            mapped.add(delta(t));
        }
        return mapped;
    }
}
