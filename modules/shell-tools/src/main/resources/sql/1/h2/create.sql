UPDATE settings SET value='1' WHERE name='DATABASE_VERSION';

create table comments (id varchar(36) not null, vn bigint not null, timestamp timestamp not null, body clob not null, author varchar(1024), url varchar(1024), email varchar(512) not null, status varchar(255) not null, resource_id varchar(36) not null, primary key (id));
create table groups (id varchar(36) not null, vn bigint not null, name varchar(255) not null unique, primary key (id));
create table group_permissions (group_id varchar(36) not null, permission varchar(255) not null, primary key (group_id, permission));
create table resource_users (resource_id varchar(36) not null, user_id varchar(36) not null, primary key (resource_id, user_id));

alter table comments add constraint FK_COMMENT_RESOURCE_ID foreign key (resource_id) references resources;
alter table resource_users add constraint FK_RESUSERS_RESOURCE_ID foreign key (resource_id) references resources;
alter table resource_users add constraint FK_RESUSERS_USER_ID foreign key (user_id) references users;

insert into groups (id, vn, name) select RANDOM_UUID(), 0, role from (select role from user_roles union select role from resource_roles);
insert into groups (id, vn, name) select RANDOM_UUID(), 0, 'ADMINISTRATOR';
insert into groups (id, vn, name) select RANDOM_UUID(), 0, 'SITE_BUILDER';
insert into groups (id, vn, name) select RANDOM_UUID(), 0, 'CONTENT_CREATOR';
insert into group_permissions (permission, group_id) values ('CONTENT_CREATOR', (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('SITE_BUILDER', (select id from groups where name='SITE_BUILDER'));
insert into group_permissions (permission, group_id) values ('ADMINISTRATOR', (select id from groups where name='ADMINISTRATOR'));

alter table user_roles add group_id varchar(36);
update user_roles r set group_id=(SELECT g.id FROM groups g WHERE g.NAME=r.role);
ALTER TABLE user_roles ALTER COLUMN group_id set not null;
alter table user_roles drop constraint FK_USERROLES_USER_ID;
ALTER TABLE user_roles DROP PRIMARY KEY;
ALTER TABLE user_roles DROP COLUMN role;
create primary key on user_roles (USER_ID, GROUP_ID);
alter table user_roles add constraint FK_USERROLES_USER_ID foreign key (user_id) references users;
alter table user_roles add constraint FK_USERROLES_GROUP_ID foreign key (group_id) references groups;

alter table resource_roles add group_id varchar(36);
update resource_roles r set group_id=(SELECT g.id FROM groups g WHERE g.NAME=r.role);
ALTER TABLE resource_roles ALTER COLUMN group_id set not null;
alter table RESOURCE_ROLES drop constraint FK_RESROLES_RESOURCE_ID;
ALTER TABLE RESOURCE_ROLES DROP PRIMARY KEY;
ALTER TABLE RESOURCE_ROLES DROP COLUMN role;
create primary key on RESOURCE_ROLES  (RESOURCE_ID, GROUP_ID);
alter table resource_roles add constraint FK_RESROLES_RESOURCE_ID foreign key (resource_id) references resources;
alter table resource_roles add constraint FK_RESROLES_GROUP_ID foreign key (group_id) references groups;
