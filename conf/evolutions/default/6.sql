# --- !Ups

ALTER TABLE fast_subject_document ADD COLUMN position integer;

# --- !Downs

ALTER TABLE fast_subject_document DROP COLUMN position;
