# --- !Ups

create table journal_title (
  id			bigint primary key,
  id_target		bigint references target (id),
  title			varchar(255) not null,
  issn			varchar(255),
  frequency		varchar(255),
  publisher_name	varchar(255) not null,
  language		varchar(255),
  priority_cataloguing	varchar(255),
  bl_collection_subset	varchar(255),
  subject		varchar(255)
);

create table document (
  id			bigint primary key,
  id_instance		bigint references instance (id),
  landing_page_url	varchar(255),
  document_url		varchar(255),
  title			TEXT not null,
  doi			varchar(255),
  publication_date	date,
  filename		varchar(255) not null,
  type			varchar(255)
);

create table book (
  id			bigint primary key,
  id_document		bigint references document (id),
  isbn			varchar(255),
  author		varchar(255),
  corporate_author	varchar(255),
  priority_cataloguing	boolean not null,
  series		varchar(255),
  publisher		varchar(255),
  edition		varchar(255),
  publication_year	integer
);

create table journal (
  id			bigint primary key,
  id_document		bigint references document (id),
  id_journal_title	bigint references journal_title (id),
  publication_year	integer not null,
  volume		varchar(255) not null,
  issue			varchar(255),
  author		varchar(255)
);

create sequence journal_title_seq;
create sequence document_seq;
create sequence book_seq;
create sequence journal_seq;

# --- !Downs

drop table if exists journal_title cascade;
drop table if exists document cascade;
drop table if exists book cascade;
drop table if exists journal cascade;

drop sequence if exists journal_title_seq;
drop sequence if exists document_seq;
drop sequence if exists book_seq;
drop sequence if exists journal_seq;
