# --- !Ups

ALTER TABLE id_fast_subject ADD COLUMN position;

# --- !Downs

ALTER TABLE id_fast_subject DROP COLUMN position;
