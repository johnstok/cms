UPDATE settings SET value='1' WHERE name='DATABASE_VERSION';

create table comments (id varchar(36) not null, vn numeric(19,0) not null, timestamp datetime not null, body text not null, author varchar(1024), url varchar(1024), email varchar(512) not null, status varchar(255) not null, resource_id varchar(36) not null, primary key (id));
create table groups (id varchar(36) not null, vn numeric(19,0) not null, name varchar(255) not null unique, primary key (id));
create table group_permissions (group_id varchar(36) not null, permission varchar(255) not null, primary key (group_id, permission));
create table resource_users (resource_id varchar(36) not null, user_id varchar(36) not null, primary key (resource_id, user_id));

alter table comments add constraint FK_COMMENT_RESOURCE_ID foreign key (resource_id) references resources;
alter table resource_users add constraint FK_RESUSERS_RESOURCE_ID foreign key (resource_id) references resources;
alter table resource_users add constraint FK_RESUSERS_USER_ID foreign key (user_id) references users;

insert into groups (id, vn, name) select lower(NEWID()), 0, ar.role from (select role from user_roles union select role from resource_roles) as ar;
insert into groups (id, vn, name) select lower(NEWID()), 0, 'ADMINISTRATOR';
insert into groups (id, vn, name) select lower(NEWID()), 0, 'SITE_BUILDER';
insert into groups (id, vn, name) select lower(NEWID()), 0, 'CONTENT_CREATOR';
insert into group_permissions (permission, group_id) select 'CONTENT_CREATOR', id from groups where name='CONTENT_CREATOR';
insert into group_permissions (permission, group_id) select 'SITE_BUILDER', id from groups where name='SITE_BUILDER';
insert into group_permissions (permission, group_id) select 'ADMINISTRATOR', id from groups where name='ADMINISTRATOR';

alter table user_roles add group_id varchar(36);
update user_roles set group_id=(SELECT g.id FROM groups g WHERE g.NAME=role);
ALTER TABLE user_roles ALTER COLUMN group_id varchar(36) not null;
DECLARE @VALUE VARCHAR(52); SET @VALUE=(select name from sysobjects where xtype='PK' and parent_obj=object_id('user_roles')); EXECUTE ('ALTER TABLE user_roles DROP CONSTRAINT '+@VALUE);
ALTER TABLE user_roles DROP COLUMN role;
ALTER TABLE user_roles ADD CONSTRAINT pk_user_roles PRIMARY KEY (user_id, group_id);
alter table user_roles add constraint FK_USERROLES_GROUP_ID foreign key (group_id) references groups;

alter table resource_roles add group_id varchar(36);
update resource_roles set group_id=(SELECT g.id FROM groups g WHERE g.NAME=role);
ALTER TABLE resource_roles ALTER COLUMN group_id varchar(36) not null;
DECLARE @VALUE VARCHAR(52); SET @VALUE=(select name from sysobjects where xtype='PK' and parent_obj=object_id('resource_roles')); EXECUTE ('ALTER TABLE resource_roles DROP CONSTRAINT '+@VALUE);
ALTER TABLE resource_roles DROP COLUMN role;
ALTER TABLE resource_roles ADD CONSTRAINT pk_resource_roles PRIMARY KEY (resource_id, group_id);
alter table resource_roles add constraint FK_RESROLES_GROUP_ID foreign key (group_id) references groups;
