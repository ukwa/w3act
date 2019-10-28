# --- !Ups

ALTER TABLE document ADD COLUMN id_primary_subject BIGINT;
ALTER TABLE document ADD CONSTRAINT fk_document_primary_subject FOREIGN KEY (id_primary_subject) REFERENCES fast_subject (id);

# --- !Downs

ALTER TABLE document DROP COLUMN id_primary_subject;
