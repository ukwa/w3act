# --- !Ups

create table document (
  id                        bigint primary key,
  id_instance               bigint references instance (id),
  url                       varchar(255),
  title                     TEXT not null,
  doi                       varchar(255),
  publication_date          date
);

create table journal (
  title                     varchar(255) primary key,
  id_document               bigint references document (id),
  publication_year          integer,
  volume                    varchar(255) not null,
  issue                     varchar(255)
);

create sequence document_seq;

# --- !Downs

drop table if exists document cascade;

drop table if exists journal cascade;

drop sequence if exists document_seq;
