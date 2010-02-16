alter table resource_users drop constraint FK_RESUSERS_USER_ID;
alter table resource_users drop constraint FK_RESUSERS_RESOURCE_ID;
alter table comments drop constraint FK_COMMENT_RESOURCE_ID;

drop table resource_users if exists;
drop table group_permissions if exists;
drop table groups if exists;
drop table comments if exists;
