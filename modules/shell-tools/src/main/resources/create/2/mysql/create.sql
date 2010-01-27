UPDATE settings SET value='1' WHERE name='DATABASE_VERSION';

create table comments (id varchar(36) not null, vn bigint not null, timestamp datetime not null, body longtext not null, author varchar(1024), url varchar(1024), email varchar(512) not null, status varchar(255) not null, resource_id varchar(36) not null, primary key (id)) ENGINE=InnoDB;
create table groups (id varchar(36) not null, vn bigint not null, name varchar(255) not null unique, primary key (id)) ENGINE=InnoDB;
create table group_permissions (group_id varchar(36) not null, permission varchar(255) not null, primary key (group_id, permission)) ENGINE=InnoDB;
create table resource_users (resource_id varchar(36) not null, user_id varchar(36) not null, primary key (resource_id, user_id)) ENGINE=InnoDB;

alter table comments add constraint FK_COMMENT_RESOURCE_ID foreign key (resource_id) references resources (id);
alter table resource_users add constraint FK_RESUSERS_RESOURCE_ID foreign key (resource_id) references resources (id);
alter table resource_users add constraint FK_RESUSERS_USER_ID foreign key (user_id) references users (id);

insert into groups (id, vn, name) select UUID(), 0, ar.role from (select role from user_roles union select role from resource_roles) as ar;
insert into groups (id, vn, name) select UUID(), 0, 'ADMINISTRATOR';
insert into groups (id, vn, name) select UUID(), 0, 'SITE_BUILDER';
insert into groups (id, vn, name) select UUID(), 0, 'CONTENT_CREATOR';
insert into group_permissions (permission, group_id) values ('CONTENT_CREATOR', (select id from groups where name='CONTENT_CREATOR'));
insert into group_permissions (permission, group_id) values ('SITE_BUILDER', (select id from groups where name='SITE_BUILDER'));
insert into group_permissions (permission, group_id) values ('ADMINISTRATOR', (select id from groups where name='ADMINISTRATOR'));

alter table user_roles add group_id varchar(36);
update user_roles r set group_id=(SELECT g.id FROM groups g WHERE g.NAME=r.role);
ALTER TABLE user_roles MODIFY COLUMN group_id varchar(36) not null;
ALTER TABLE user_roles DROP PRIMARY KEY;
ALTER TABLE user_roles DROP COLUMN role;
ALTER TABLE user_roles ADD PRIMARY KEY (USER_ID, GROUP_ID);
alter table user_roles add constraint FK_USERROLES_GROUP_ID foreign key (group_id) references groups (id);

alter table resource_roles add group_id varchar(36);
update resource_roles r set group_id=(SELECT g.id FROM groups g WHERE g.NAME=r.role);
ALTER TABLE resource_roles MODIFY COLUMN group_id varchar(36) not null;
ALTER TABLE resource_roles DROP PRIMARY KEY;
ALTER TABLE resource_roles DROP COLUMN role;
ALTER TABLE resource_roles ADD PRIMARY KEY (resource_id, GROUP_ID);
alter table resource_roles add constraint FK_RESROLES_GROUP_ID foreign key (group_id) references groups (id);