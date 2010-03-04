UPDATE settings SET value='2' WHERE name='DATABASE_VERSION';

DELETE FROM group_permissions WHERE permission='CONTENT_CREATOR';
DELETE FROM group_permissions WHERE permission='SITE_BUILDER';
DELETE FROM group_permissions WHERE permission='ADMINISTRATOR';

INSERT INTO group_permissions (permission, group_id) SELECT 'API_ACCESS',            id FROM groups WHERE name='ADMINISTRATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'API_ACCESS',            id FROM groups WHERE name='SITE_BUILDER';
INSERT INTO group_permissions (permission, group_id) SELECT 'API_ACCESS',            id FROM groups WHERE name='CONTENT_CREATOR';

INSERT INTO group_permissions (permission, group_id) SELECT 'ACTION_EXECUTE',        id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'ACTION_LIST',           id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'ACTION_CANCEL',         id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'ACTION_CREATE',         id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'PAGE_UPDATE',           id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'PAGE_CREATE',           id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'ALIAS_READ',            id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'ALIAS_CREATE',          id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'ALIAS_UPDATE',          id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'COMMENT_DELETE',        id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'COMMENT_CREATE',        id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'COMMENT_UPDATE',        id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'COMMENT_READ',          id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'FILE_CREATE',           id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'FILE_UPDATE',           id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'FILE_READ',             id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'FOLDER_READ',           id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'FOLDER_CREATE',         id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'ROOT_CREATE',           id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'TEMPLATE_READ',         id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'SELF_UPDATE',           id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_MM',           id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_PUBLISH',      id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_UNPUBLISH',    id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_LOCK',         id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_UNLOCK',       id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_RENAME',       id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_MOVE',         id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_READ',         id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_UPDATE',       id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_DELETE',       id FROM groups WHERE name='CONTENT_CREATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'FOLDER_UPDATE',         id FROM groups WHERE name='CONTENT_CREATOR';

INSERT INTO group_permissions (permission, group_id) SELECT 'TEMPLATE_CREATE',       id FROM groups WHERE name='SITE_BUILDER';
INSERT INTO group_permissions (permission, group_id) SELECT 'TEMPLATE_UPDATE',       id FROM groups WHERE name='SITE_BUILDER';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_CACHE_UPDATE', id FROM groups WHERE name='SITE_BUILDER';

INSERT INTO group_permissions (permission, group_id) SELECT 'MIGRATE',               id FROM groups WHERE name='ADMINISTRATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'GROUP_CREATE',          id FROM groups WHERE name='ADMINISTRATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'GROUP_READ',            id FROM groups WHERE name='ADMINISTRATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'GROUP_UPDATE',          id FROM groups WHERE name='ADMINISTRATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'USER_CREATE',           id FROM groups WHERE name='ADMINISTRATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'USER_UPDATE',           id FROM groups WHERE name='ADMINISTRATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'USER_READ',             id FROM groups WHERE name='ADMINISTRATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'SEARCH_CREATE',         id FROM groups WHERE name='ADMINISTRATOR';
INSERT INTO group_permissions (permission, group_id) SELECT 'RESOURCE_ACL_UPDATE',   id FROM groups WHERE name='ADMINISTRATOR';
