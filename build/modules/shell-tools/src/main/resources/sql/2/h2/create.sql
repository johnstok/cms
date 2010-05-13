UPDATE settings SET value='2' WHERE name='DATABASE_VERSION';

ALTER TABLE resource_users ADD can_read bit DEFAULT 1 NOT NULL;
ALTER TABLE resource_users ADD can_write bit DEFAULT 1 NOT NULL;
ALTER TABLE resource_roles ADD can_read bit DEFAULT 1 NOT NULL;
ALTER TABLE resource_roles ADD can_write bit DEFAULT 1 NOT NULL;

ALTER TABLE folders DROP COLUMN sort_order;

INSERT INTO groups (id, vn, name) SELECT RANDOM_UUID(), 0, 'Site Reader';
INSERT INTO users (id, email, username, vn, hash, name) VALUES (RANDOM_UUID(), 'support@civicuk.com', 'anonymous', 0, X'00', 'Anonymous User');
INSERT INTO user_roles (user_id, group_id) VALUES ((SELECT id FROM users WHERE username='anonymous'), (SELECT id FROM groups WHERE name='Site Reader'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_READ', (SELECT id FROM groups WHERE name='Site Reader'));
INSERT INTO group_permissions (permission, group_id) VALUES ('ALIAS_READ', (SELECT id FROM groups WHERE name='Site Reader'));
INSERT INTO group_permissions (permission, group_id) VALUES ('COMMENT_READ', (SELECT id FROM groups WHERE name='Site Reader'));
INSERT INTO group_permissions (permission, group_id) VALUES ('FILE_READ', (SELECT id FROM groups WHERE name='Site Reader'));
INSERT INTO group_permissions (permission, group_id) VALUES ('FOLDER_READ', (SELECT id FROM groups WHERE name='Site Reader'));
INSERT INTO group_permissions (permission, group_id) VALUES ('TEMPLATE_READ', (SELECT id FROM groups WHERE name='Site Reader'));

DELETE FROM group_permissions WHERE permission='CONTENT_CREATOR';
DELETE FROM group_permissions WHERE permission='SITE_BUILDER';
DELETE FROM group_permissions WHERE permission='ADMINISTRATOR';

INSERT INTO group_permissions (permission, group_id) VALUES ('API_ACCESS',            (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('API_ACCESS',            (SELECT id FROM groups WHERE name='SITE_BUILDER'));
INSERT INTO group_permissions (permission, group_id) VALUES ('API_ACCESS',            (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));

INSERT INTO group_permissions (permission, group_id) VALUES ('ACTION_EXECUTE',        (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('ACTION_LIST',           (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('ACTION_CANCEL',         (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('ACTION_CREATE',         (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('PAGE_UPDATE',           (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('PAGE_CREATE',           (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('ALIAS_READ',            (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('ALIAS_CREATE',          (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('ALIAS_UPDATE',          (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('COMMENT_DELETE',        (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('COMMENT_CREATE',        (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('COMMENT_UPDATE',        (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('COMMENT_READ',          (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('FILE_CREATE',           (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('FILE_UPDATE',           (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('FILE_READ',             (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('FOLDER_READ',           (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('FOLDER_CREATE',         (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('ROOT_CREATE',           (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('TEMPLATE_READ',         (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('SELF_UPDATE',           (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_MM',           (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_PUBLISH',      (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_UNPUBLISH',    (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_LOCK',         (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_UNLOCK',       (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_RENAME',       (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_MOVE',         (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_READ',         (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_UPDATE',       (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_DELETE',       (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('FOLDER_UPDATE',         (SELECT id FROM groups WHERE name='CONTENT_CREATOR'));

INSERT INTO group_permissions (permission, group_id) VALUES ('TEMPLATE_CREATE',       (SELECT id FROM groups WHERE name='SITE_BUILDER'));
INSERT INTO group_permissions (permission, group_id) VALUES ('TEMPLATE_UPDATE',       (SELECT id FROM groups WHERE name='SITE_BUILDER'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_CACHE_UPDATE', (SELECT id FROM groups WHERE name='SITE_BUILDER'));

INSERT INTO group_permissions (permission, group_id) VALUES ('MIGRATE',               (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('GROUP_CREATE',          (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('GROUP_READ',            (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('GROUP_UPDATE',          (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('USER_CREATE',           (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('USER_UPDATE',           (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('USER_READ',             (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('SEARCH_CREATE',         (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('RESOURCE_ACL_UPDATE',   (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('SEARCH_SCHEDULE',       (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('SEARCH_REINDEX',        (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
INSERT INTO group_permissions (permission, group_id) VALUES ('ACTION_SCHEDULE',       (SELECT id FROM groups WHERE name='ADMINISTRATOR'));
