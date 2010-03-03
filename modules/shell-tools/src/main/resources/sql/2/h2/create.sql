UPDATE settings SET value='2' WHERE name='DATABASE_VERSION';

delete from group_permissions where permission='CONTENT_CREATOR';
delete from group_permissions where permission='SITE_BUILDER';
delete from group_permissions where permission='ADMINISTRATOR';

insert into group_permissions (permission, group_id) values ('API_ACCESS',            (select id from groups where name='ADMINISTRATOR'));
insert into group_permissions (permission, group_id) values ('API_ACCESS',            (select id from groups where name='SITE_BUILDER'));
insert into group_permissions (permission, group_id) values ('API_ACCESS',            (select id from groups where name='CONTENT_CREATOR'));

insert into group_permissions (permission, group_id) values ('ACTION_EXECUTE',        (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('ACTION_LIST',           (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('ACTION_CANCEL',         (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('ACTION_CREATE',         (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('PAGE_UPDATE',           (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('PAGE_CREATE',           (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('ALIAS_READ',            (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('ALIAS_CREATE',          (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('ALIAS_UPDATE',          (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('COMMENT_DELETE',        (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('COMMENT_CREATE',        (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('COMMENT_UPDATE',        (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('COMMENT_READ',          (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('FILE_CREATE',           (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('FILE_UPDATE',           (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('FILE_READ',             (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('FOLDER_READ',           (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('FOLDER_CREATE',         (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('ROOT_CREATE',           (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('TEMPLATE_READ',         (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('SELF_UPDATE',           (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_MM',           (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_PUBLISH',      (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_UNPUBLISH',    (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_LOCK',         (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_UNLOCK',       (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_RENAME',       (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_MOVE',         (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_READ',         (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_UPDATE',       (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_DELETE',       (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('FOLDER_UPDATE',         (select id from groups where name='CONTENT_CREATOR'));

insert into group_permissions (permission, group_id) values ('TEMPLATE_CREATE',       (select id from groups where name='SITE_BUILDER'));
insert into group_permissions (permission, group_id) values ('TEMPLATE_UPDATE',       (select id from groups where name='SITE_BUILDER'));
insert into group_permissions (permission, group_id) values ('RESOURCE_CACHE_UPDATE', (select id from groups where name='SITE_BUILDER'));

insert into group_permissions (permission, group_id) values ('MIGRATE',               (select id from groups where name='ADMINISTRATOR'));
insert into group_permissions (permission, group_id) values ('GROUP_CREATE',          (select id from groups where name='ADMINISTRATOR'));
insert into group_permissions (permission, group_id) values ('GROUP_READ',            (select id from groups where name='ADMINISTRATOR'));
insert into group_permissions (permission, group_id) values ('GROUP_UPDATE',          (select id from groups where name='ADMINISTRATOR'));
insert into group_permissions (permission, group_id) values ('USER_CREATE',           (select id from groups where name='ADMINISTRATOR'));
insert into group_permissions (permission, group_id) values ('USER_UPDATE',           (select id from groups where name='ADMINISTRATOR'));
insert into group_permissions (permission, group_id) values ('USER_READ',             (select id from groups where name='ADMINISTRATOR'));
insert into group_permissions (permission, group_id) values ('SEARCH_CREATE',         (select id from groups where name='ADMINISTRATOR'));
insert into group_permissions (permission, group_id) values ('RESOURCE_ACL_UPDATE',   (select id from groups where name='ADMINISTRATOR'));