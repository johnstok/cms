UPDATE settings SET value='1' WHERE name='DATABASE_VERSION';

create table comments (id varchar2(36 char) not null, vn number(19,0) not null, timestamp timestamp not null, body clob not null, author varchar(1024 char), url varchar(1024 char), email varchar(512 char) not null, status varchar(255 char) not null, resource_id varchar(36 char) not null, primary key (id));
create table groups (id varchar2(36 char) not null, vn number(19,0) not null, name varchar2(255 char) not null unique, primary key (id));
create table group_permissions (group_id varchar2(36 char) not null, permission varchar2(255 char) not null, primary key (group_id, permission));
create table resource_users (resource_id varchar2(36 char) not null, user_id varchar2(36 char) not null, primary key (resource_id, user_id));
CREATE OR REPLACE FUNCTION uuid RETURN varchar2 IS guid varchar2(32) := lower(''||sys_guid()); BEGIN return SUBSTR(guid, 1, 8)||'-'||SUBSTR(guid, 9, 4)||'-'||SUBSTR(guid, 13, 4)||'-'||SUBSTR(guid, 17, 4)||'-'||SUBSTR(guid, 21, 12); END;;

alter table comments add constraint FK_COMMENT_RESOURCE_ID foreign key (resource_id) references resources;
alter table resource_users add constraint FK_RESUSERS_RESOURCE_ID foreign key (resource_id) references resources;
alter table resource_users add constraint FK_RESUSERS_USER_ID foreign key (user_id) references users;

insert into groups (id, vn, name) select uuid(), 0, role from (select role from user_roles union select role from resource_roles);
insert into groups (id, vn, name) values (uuid(), 0, 'ADMINISTRATOR');
insert into groups (id, vn, name) values (uuid(), 0, 'SITE_BUILDER');
insert into groups (id, vn, name) values (uuid(), 0, 'CONTENT_CREATOR');
insert into group_permissions (permission, group_id) values ('CONTENT_CREATOR', (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('SITE_BUILDER', (select id from groups where name='SITE_BUILDER'));
insert into group_permissions (permission, group_id) values ('ADMINISTRATOR', (select id from groups where name='ADMINISTRATOR'));

alter table user_roles add group_id varchar2(36 char);
update user_roles r set group_id=(SELECT g.id FROM groups g WHERE g.NAME=r.role);
ALTER TABLE user_roles modify group_id not null;
ALTER TABLE user_roles DROP COLUMN role cascade constraints;
ALTER TABLE user_roles add CONSTRAINT user_roles_pk PRIMARY KEY (USER_ID, GROUP_ID);
alter table user_roles add constraint FK_USERROLES_GROUP_ID foreign key (group_id) references groups;

alter table resource_roles add group_id varchar2(36 char);
update resource_roles r set group_id=(SELECT g.id FROM groups g WHERE g.NAME=r.role);
ALTER TABLE resource_roles modify group_id not null;
ALTER TABLE RESOURCE_ROLES DROP COLUMN role cascade constraints;
alter table resource_roles add CONSTRAINT resource_roles_pk PRIMARY KEY (RESOURCE_ID, GROUP_ID);
alter table resource_roles add constraint FK_RESROLES_GROUP_ID foreign key (group_id) references groups;
