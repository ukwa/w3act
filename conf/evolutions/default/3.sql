# --- !Ups

alter table crawl_permission add granted_at timestamp;
alter table crawl_permission add requested_at timestamp;
alter table creator add last_login timestamp;

# --- !Downs

alter table crawl_permission drop granted_at;
alter table crawl_permission drop requested_at;
alter table creator drop last_login;
