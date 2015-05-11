# --- !Ups

create table watched_target (
  id			bigint primary key,
  id_target		bigint not null references target (id),
  document_url_scheme	varchar(255),
  wayback_timestamp	char(14),
  login_page_url	varchar(255),
  logout_url		varchar(255),
  secret_id		integer
);

create table fast_subject (
  id			bigint primary key,
  fast_id		char(11) unique not null,
  name			varchar(255) not null
);

create table fast_subject_watched_target (
  id_fast_subject	bigint references fast_subject (id),
  id_watched_target	bigint references watched_target (id),
  primary key (id_fast_subject, id_watched_target)
);

create table journal_title (
  id			bigint primary key,
  id_watched_target	bigint not null references watched_target (id),
  title			varchar(255) not null,
  issn			varchar(255),
  frequency		varchar(255),
  publisher_name	varchar(255) not null,
  language		varchar(255)
);

create table bl_collection_subset (
  id			bigint primary key,
  title			varchar(255) unique not null,
  active		boolean
);

create table bl_collection_subset_journal_title (
  id_bl_collection_subset	bigint references bl_collection_subset (id),
  id_journal_title		bigint references journal_title (id),
  primary key (id_bl_collection_subset, id_journal_title)
);

create table fast_subject_journal_title (
  id_fast_subject	bigint references fast_subject (id),
  id_journal_title	bigint references journal_title (id),
  primary key (id_fast_subject, id_journal_title)
);

create table document (
  id			bigint primary key,
  id_watched_target	bigint not null references watched_target (id),
  wayback_timestamp	varchar(255),
  status		integer,
  current_status_set	timestamp(3),
  landing_page_url	varchar(255),
  document_url		varchar(255),
  sha256hash		varchar(255),
  ctp_hash		varchar(255),
  title			text not null,
  doi			varchar(255),
  ark			varchar(255),
  publication_date	date,
  publication_year	integer,
  filename		varchar(255) not null,
  size			bigint,
  priority_cataloguing	boolean not null,
  type			varchar(255),
  author1fn		varchar(255),
  author1ln		varchar(255),
  author2fn		varchar(255),
  author2ln		varchar(255),
  author3fn		varchar(255),
  author3ln		varchar(255)
);

create table fast_subject_document (
  id_fast_subject	bigint references fast_subject (id),
  id_document		bigint references document (id),
  primary key (id_fast_subject, id_document)
);

create table assignable_ark (
  ark			varchar(255) primary key
);

create table portal (
  id			bigint primary key,
  title			varchar(255) unique not null,
  active		boolean
);

create table portal_document (
  id_portal		bigint references portal (id),
  id_document		bigint references document (id),
  primary key (id_portal, id_document)
);

create table portal_license (
  id_portal		bigint references portal (id),
  id_taxonomy		bigint references taxonomy (id),
  primary key (id_portal, id_taxonomy)
);

create table book (
  id			bigint primary key,
  id_document		bigint unique not null references document (id),
  isbn			varchar(255),
  corporate_author	varchar(255),
  series		varchar(255),
  series_number		varchar(255),
  publisher		varchar(255),
  edition		varchar(255)
);

create table bl_collection_subset_book (
  id_bl_collection_subset	bigint references bl_collection_subset (id),
  id_book			bigint references book (id),
  primary key (id_bl_collection_subset, id_book)
);

create table journal (
  id			bigint primary key,
  id_document		bigint unique not null references document (id),
  id_journal_title	bigint references journal_title (id),
  volume		varchar(255) not null,
  issue			varchar(255)
);

create table alert (
  id			bigint primary key,
  id_creator		bigint not null references creator (id),
  text			text not null,
  created_at		timestamp(3) not null,
  read			boolean not null
);

alter table creator add ddhapt_user boolean not null default false;

create sequence watched_target_seq;
create sequence fast_subject_seq;
create sequence journal_title_seq;
create sequence bl_collection_subset_seq;
create sequence document_seq;
create sequence portal_seq;
create sequence book_seq;
create sequence journal_seq;
create sequence alert_seq;

# --- !Downs

drop table if exists watched_target cascade;
drop table if exists fast_subject cascade;
drop table if exists fast_subject_watched_target cascade;
drop table if exists journal_title cascade;
drop table if exists bl_collection_subset cascade;
drop table if exists bl_collection_subset_journal_title cascade;
drop table if exists fast_subject_journal_title cascade;
drop table if exists document cascade;
drop table if exists fast_subject_document cascade;
drop table if exists assignable_ark cascade;
drop table if exists portal cascade;
drop table if exists portal_document cascade;
drop table if exists portal_license cascade;
drop table if exists book cascade;
drop table if exists bl_collection_subset_book cascade;
drop table if exists journal cascade;
drop table if exists alert cascade;

alter table creator drop ddhapt_user;

drop sequence if exists watched_target_seq;
drop sequence if exists fast_subject_seq;
drop sequence if exists journal_title_seq;
drop sequence if exists bl_collection_subset_seq;
drop sequence if exists document_seq;
drop sequence if exists portal_seq;
drop sequence if exists book_seq;
drop sequence if exists journal_seq;
drop sequence if exists alert_seq;
