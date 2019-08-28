# --- !Ups

CREATE TABLE fast_subject_by_priority (
  id				bigint primary key,
  id_document		bigint not null references document (id),
  id_fast_subject	bigint not null references fast_subject (id),
  priority			integer
);
CREATE SEQUENCE fast_subject_by_priority_seq;

# --- !Downs

DROP TABLE IF EXISTS fast_subject_by_priority_seq;
DROP SEQUENCE IF EXISTS fast_subject_by_priority_seq;
