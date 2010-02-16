alter table resource_roles drop constraint FK_RESROLES_GROUP_ID;
alter table user_roles drop constraint FK_USERROLES_GROUP_ID;
alter table comments drop constraint FK_COMMENT_RESOURCE_ID;

drop table resource_users;
drop table group_permissions;
drop table groups;
drop table comments;
