alter table resource_roles drop foreign key FK_RESROLES_GROUP_ID;
alter table user_roles drop foreign key FK_USERROLES_GROUP_ID;
alter table resource_users drop foreign key FK_RESUSERS_USER_ID;
alter table resource_users drop foreign key FK_RESUSERS_RESOURCE_ID;
alter table comments drop foreign key FK_COMMENT_RESOURCE_ID;

drop table if exists resource_users;
drop table if exists group_permissions;
drop table if exists groups;
drop table if exists comments;
