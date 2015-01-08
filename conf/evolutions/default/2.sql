# --- !Ups

create table watched_target (
  id			bigint primary key,
  id_target		bigint references target (id),
  id_creator		bigint references creator (id),
  document_url_scheme	varchar(255)
);

create table journal_title (
  id			bigint primary key,
  id_watched_target	bigint references watched_target (id),
  title			varchar(255) not null,
  issn			varchar(255),
  frequency		varchar(255),
  publisher_name	varchar(255) not null,
  language		varchar(255),
  bl_collection_subset	varchar(255)
);

create table subject_journal_title (
  id_taxonomy		bigint references taxonomy (id),
  id_journal_title	bigint references journal_title (id),
  primary key (id_taxonomy, id_journal_title)
);

create table document (
  id			bigint primary key,
  id_instance		bigint references instance (id),
  id_watched_target	bigint references watched_target (id),
  submitted		boolean not null,
  landing_page_url	varchar(255),
  document_url		varchar(255),
  title			TEXT not null,
  doi			varchar(255),
  publication_date	date,
  publication_year	integer,
  filename		varchar(255) not null,
  priority_cataloguing	boolean not null,
  type			varchar(255),
  author1fn		varchar(255),
  author1ln		varchar(255),
  author2fn		varchar(255),
  author2ln		varchar(255),
  author3fn		varchar(255),
  author3ln		varchar(255)
);

create table portal (
  id			bigint primary key,
  title			varchar(255) unique not null
);

create table portal_document (
  id_portal		bigint references portal (id),
  id_document		bigint references document (id),
  primary key (id_portal, id_document)
);

create table book (
  id			bigint primary key,
  id_document		bigint references document (id),
  isbn			varchar(255),
  corporate_author	varchar(255),
  series		varchar(255),
  publisher		varchar(255),
  edition		varchar(255)
);

create table journal (
  id			bigint primary key,
  id_document		bigint references document (id),
  id_journal_title	bigint references journal_title (id),
  volume		varchar(255) not null,
  issue			varchar(255)
);

create sequence watched_target_seq;
create sequence journal_title_seq;
create sequence document_seq;
create sequence portal_seq;
create sequence book_seq;
create sequence journal_seq;

# --- !Downs

drop table if exists watched_target cascade;
drop table if exists journal_title cascade;
drop table if exists subject_journal_title cascade;
drop table if exists document cascade;
drop table if exists portal cascade;
drop table if exists portal_document cascade;
drop table if exists book cascade;
drop table if exists journal cascade;

drop sequence if exists watched_target_seq;
drop sequence if exists journal_title_seq;
drop sequence if exists document_seq;
drop sequence if exists portal_seq;
drop sequence if exists book_seq;
drop sequence if exists journal_seq;
