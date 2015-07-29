# --- !Ups

alter table crawl_permission add granted_at timestamp without time zone;
alter table crawl_permission add requested_at timestamp without time zone;

# --- !Downs

alter table crawl_permission drop granted_at;
alter table crawl_permission drop requested_at;
