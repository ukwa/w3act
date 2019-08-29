# --- !Ups

ALTER TABLE document ADD COLUMN id_primary_subject bigint references fast_subject (id);

# --- !Downs

ALTER TABLE document DROP COLUMN id_primary_subject;
