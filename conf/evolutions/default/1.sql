# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table communication_log (
  id                        bigint not null,
  url                       TEXT,
  name                      TEXT,
  curator                   TEXT,
  date                      TEXT,
  ttype                     TEXT,
  permission                TEXT,
  notes                     TEXT,
  last_update               timestamp not null,
  constraint pk_communication_log primary key (id))
;

create table contact_person (
  id                        bigint not null,
  url                       TEXT,
  name                      TEXT,
  position                  TEXT,
  phone                     TEXT,
  email                     TEXT,
  postal_address            TEXT,
  web_form                  TEXT,
  description               TEXT,
  contact_organisation      TEXT,
  default_contact           boolean,
  permission_checked        boolean,
  last_update               timestamp not null,
  constraint pk_contact_person primary key (id))
;

create table crawl_permission (
  id                        bigint not null,
  url                       TEXT,
  name                      TEXT,
  target                    TEXT,
  description               TEXT,
  any_other_information     TEXT,
  status                    TEXT,
  contact_person            TEXT,
  creator_user              TEXT,
  assigned_archivist        TEXT,
  template                  TEXT,
  license                   TEXT,
  license_date              TEXT,
  request_followup          boolean,
  number_requests           bigint,
  third_party_content       boolean,
  publish                   boolean,
  agree                     boolean,
  last_update               timestamp not null,
  constraint pk_crawl_permission primary key (id))
;

create table dcollection (
  nid                       bigint not null,
  value                     TEXT,
  summary                   TEXT,
  format                    varchar(255),
  vid                       bigint,
  is_new                    boolean,
  type                      varchar(255),
  title                     varchar(255),
  language                  varchar(255),
  url                       varchar(255),
  edit_url                  varchar(255),
  status                    bigint,
  promote                   bigint,
  sticky                    bigint,
  created                   varchar(255),
  changed                   varchar(255),
  author                    varchar(255),
  log                       varchar(255),
  comment                   bigint,
  comment_count             bigint,
  comment_count_new         bigint,
  revision                  varchar(255),
  feed_nid                  bigint,
  publish                   boolean,
  field_targets             TEXT,
  field_sub_collections     TEXT,
  field_instances           TEXT,
  weight                    bigint,
  node_count                bigint,
  vocabulary                TEXT,
  parent                    TEXT,
  parents_all               TEXT,
  constraint pk_dcollection primary key (nid))
;

create table flag (
  id                        bigint not null,
  url                       TEXT,
  name                      TEXT,
  description               TEXT,
  last_update               timestamp not null,
  constraint pk_flag primary key (id))
;

create table instance (
  nid                       bigint not null,
  value                     TEXT,
  summary                   TEXT,
  act_url                   varchar(255),
  wct_url                   varchar(255),
  format                    varchar(255),
  field_scope               varchar(255),
  field_depth               varchar(255),
  field_via_correspondence  boolean,
  field_uk_postal_address   boolean,
  field_uk_hosting          boolean,
  field_nominating_organisation varchar(255),
  field_crawl_frequency     varchar(255),
  field_crawl_start_date    varchar(255),
  field_uk_domain           boolean,
  field_crawl_permission    varchar(255),
  field_special_dispensation varchar(255),
  field_uk_geoip            boolean,
  field_professional_judgement boolean,
  vid                       bigint,
  is_new                    boolean,
  type                      varchar(255),
  title                     varchar(255),
  language                  varchar(255),
  url                       varchar(255),
  edit_url                  varchar(255),
  status                    bigint,
  promote                   bigint,
  sticky                    bigint,
  created                   varchar(255),
  changed                   varchar(255),
  author                    varchar(255),
  log                       varchar(255),
  comment                   bigint,
  comment_count             bigint,
  comment_count_new         bigint,
  feed_nid                  bigint,
  field_crawl_end_date      varchar(255),
  field_live_site_status    varchar(255),
  field_wct_id              bigint,
  field_spt_id              bigint,
  field_no_ld_criteria_met  boolean,
  field_key_site            boolean,
  field_professional_judgement_exp TEXT,
  field_ignore_robots_txt   boolean,
  revision                  varchar(255),
  field_qa_issues           TEXT,
  field_target              TEXT,
  field_description_of_qa_issues TEXT,
  field_timestamp           TEXT,
  field_published           boolean,
  field_to_be_published     boolean,
  date_of_publication       varchar(255),
  justification             TEXT,
  selector_notes            TEXT,
  archivist_notes           TEXT,
  selection_type            varchar(255),
  selector                  varchar(255),
  legacy_site_id            bigint,
  white_list                varchar(255),
  black_list                varchar(255),
  field_url                 TEXT,
  field_description         TEXT,
  field_uk_postal_address_url TEXT,
  field_suggested_collections TEXT,
  field_collections         TEXT,
  field_license             TEXT,
  field_collection_categories TEXT,
  field_notes               TEXT,
  field_instances           TEXT,
  field_subject             TEXT,
  field_subsubject          TEXT,
  field_qa_status           TEXT,
  qa_status                 TEXT,
  qa_issue_category         TEXT,
  qa_notes                  TEXT,
  quality_notes             TEXT,
  keywords                  TEXT,
  tags                      TEXT,
  synonyms                  TEXT,
  originating_organisation  TEXT,
  flags                     TEXT,
  authors                   TEXT,
  last_update               timestamp not null,
  constraint pk_instance primary key (nid))
;

create table lookup_entry (
  id                        bigint not null,
  url                       TEXT,
  name                      TEXT,
  ttype                     TEXT,
  scopevalue                boolean,
  last_update               timestamp not null,
  constraint pk_lookup_entry primary key (id))
;

create table mail_template (
  id                        bigint not null,
  url                       TEXT,
  name                      TEXT,
  ttype                     TEXT,
  subject                   TEXT,
  from_email                TEXT,
  place_holders             TEXT,
  default_email             boolean,
  text                      TEXT,
  last_update               timestamp not null,
  constraint pk_mail_template primary key (id))
;

create table nomination (
  id                        bigint not null,
  url                       TEXT,
  name                      TEXT,
  title                     TEXT,
  website_url               TEXT,
  email                     TEXT,
  tel                       TEXT,
  address                   TEXT,
  nominated_website_owner   boolean,
  justification             TEXT,
  notes                     TEXT,
  nomination_date           varchar(255),
  nomination_checked        boolean,
  last_update               timestamp not null,
  constraint pk_nomination primary key (id))
;

create table organisation (
  nid                       bigint not null,
  title                     varchar(255) not null,
  value                     TEXT,
  summary                   varchar(255),
  format                    varchar(255),
  field_abbreviation        varchar(255),
  vid                       bigint,
  is_new                    boolean,
  type                      varchar(255),
  language                  varchar(255),
  url                       varchar(255),
  edit_url                  varchar(255),
  status                    bigint,
  promote                   bigint,
  sticky                    bigint,
  created                   varchar(255),
  changed                   varchar(255),
  author                    varchar(255),
  log                       varchar(255),
  comment                   bigint,
  comment_count             bigint,
  comment_count_new         bigint,
  revision                  TEXT,
  feed_nid                  bigint,
  last_update               timestamp not null)
;

create table permission (
  id                        bigint not null,
  name                      TEXT,
  url                       TEXT,
  description               TEXT,
  revision                  TEXT,
  last_update               timestamp not null,
  constraint pk_permission primary key (id))
;

create table permission_refusal (
  id                        bigint not null,
  url                       TEXT,
  name                      TEXT,
  date                      TEXT,
  ttype                     TEXT,
  reason                    TEXT,
  last_update               timestamp not null,
  constraint pk_permission_refusal primary key (id))
;

create table role (
  id                        bigint not null,
  name                      TEXT,
  url                       TEXT,
  permissions               TEXT,
  description               TEXT,
  revision                  TEXT,
  last_update               timestamp not null,
  constraint pk_role primary key (id))
;

create table tag (
  id                        bigint not null,
  url                       TEXT,
  name                      TEXT,
  description               TEXT,
  last_update               timestamp not null,
  constraint pk_tag primary key (id))
;

create table target (
  nid                       bigint not null,
  value                     TEXT,
  summary                   TEXT,
  format                    varchar(255),
  field_scope               varchar(255),
  field_depth               varchar(255),
  field_via_correspondence  boolean,
  field_uk_postal_address   boolean,
  field_uk_hosting          boolean,
  field_nominating_organisation varchar(255),
  field_crawl_frequency     varchar(255),
  field_crawl_start_date    varchar(255),
  field_uk_domain           boolean,
  field_crawl_permission    varchar(255),
  field_special_dispensation boolean,
  field_special_dispensation_reaso TEXT,
  field_uk_geoip            boolean,
  field_professional_judgement boolean,
  vid                       bigint,
  is_new                    boolean,
  type                      varchar(255),
  title                     varchar(255),
  language                  varchar(255),
  url                       varchar(255),
  edit_url                  varchar(255),
  status                    bigint,
  promote                   bigint,
  sticky                    bigint,
  created                   varchar(255),
  changed                   varchar(255),
  author                    varchar(255),
  log                       varchar(255),
  comment                   bigint,
  comment_count             bigint,
  comment_count_new         bigint,
  feed_nid                  bigint,
  field_crawl_end_date      varchar(255),
  field_live_site_status    varchar(255),
  field_wct_id              bigint,
  field_spt_id              bigint,
  legacy_site_id            bigint,
  field_no_ld_criteria_met  boolean,
  field_key_site            boolean,
  field_professional_judgement_exp TEXT,
  field_ignore_robots_txt   boolean,
  revision                  TEXT,
  active                    boolean,
  white_list                varchar(255),
  black_list                varchar(255),
  date_of_publication       varchar(255),
  justification             TEXT,
  selector_notes            TEXT,
  archivist_notes           TEXT,
  selection_type            varchar(255),
  selector                  varchar(255),
  flag_notes                TEXT,
  field_url                 TEXT,
  domain                    TEXT,
  field_description         TEXT,
  field_uk_postal_address_url TEXT,
  field_suggested_collections TEXT,
  field_collections         TEXT,
  field_license             TEXT,
  field_collection_categories TEXT,
  field_notes               TEXT,
  field_instances           TEXT,
  field_subject             TEXT,
  field_subsubject          TEXT,
  keywords                  TEXT,
  tags                      TEXT,
  synonyms                  TEXT,
  originating_organisation  TEXT,
  flags                     TEXT,
  authors                   TEXT,
  field_qa_status           TEXT,
  qa_status                 TEXT,
  qa_issue_category         TEXT,
  qa_notes                  TEXT,
  quality_notes             TEXT,
  last_update               timestamp not null,
  constraint pk_target primary key (nid))
;

create table taxonomy (
  tid                       bigint not null,
  name                      varchar(255),
  ttype                     varchar(255),
  description               TEXT,
  weight                    bigint,
  node_count                bigint,
  url                       TEXT,
  vocabulary                TEXT,
  feed_nid                  bigint,
  field_owner               TEXT,
  field_dates               TEXT,
  field_publish             TEXT,
  parent                    TEXT,
  parents_all               TEXT,
  constraint pk_taxonomy primary key (tid))
;

create table taxonomy_vocabulary (
  vid                       bigint not null,
  name                      varchar(255),
  machine_name              varchar(255),
  description               TEXT,
  term_count                bigint,
  constraint pk_taxonomy_vocabulary primary key (vid))
;

create table creator (
  email                     varchar(255) not null,
  name                      varchar(255),
  password                  varchar(255),
  field_affiliation         varchar(255),
  uid                       bigint,
  url                       varchar(255),
  edit_url                  varchar(255),
  last_access               varchar(255),
  last_login                varchar(255),
  created                   varchar(255),
  status                    bigint,
  language                  varchar(255),
  feed_nid                  bigint,
  roles                     TEXT,
  revision                  TEXT,
  last_update               timestamp not null,
  constraint pk_creator primary key (email))
;

create sequence communication_log_seq;

create sequence contact_person_seq;

create sequence crawl_permission_seq;

create sequence dcollection_seq;

create sequence flag_seq;

create sequence instance_seq;

create sequence lookup_entry_seq;

create sequence mail_template_seq;

create sequence nomination_seq;

create sequence organisation_seq;

create sequence permission_seq;

create sequence permission_refusal_seq;

create sequence role_seq;

create sequence tag_seq;

create sequence target_seq;

create sequence taxonomy_seq;

create sequence taxonomy_vocabulary_seq;

create sequence creator_seq;




# --- !Downs

drop table if exists communication_log cascade;

drop table if exists contact_person cascade;

drop table if exists crawl_permission cascade;

drop table if exists dcollection cascade;

drop table if exists flag cascade;

drop table if exists instance cascade;

drop table if exists lookup_entry cascade;

drop table if exists mail_template cascade;

drop table if exists nomination cascade;

drop table if exists organisation cascade;

drop table if exists permission cascade;

drop table if exists permission_refusal cascade;

drop table if exists role cascade;

drop table if exists tag cascade;

drop table if exists target cascade;

drop table if exists taxonomy cascade;

drop table if exists taxonomy_vocabulary cascade;

drop table if exists creator cascade;

drop sequence if exists communication_log_seq;

drop sequence if exists contact_person_seq;

drop sequence if exists crawl_permission_seq;

drop sequence if exists dcollection_seq;

drop sequence if exists flag_seq;

drop sequence if exists instance_seq;

drop sequence if exists lookup_entry_seq;

drop sequence if exists mail_template_seq;

drop sequence if exists nomination_seq;

drop sequence if exists organisation_seq;

drop sequence if exists permission_seq;

drop sequence if exists permission_refusal_seq;

drop sequence if exists role_seq;

drop sequence if exists tag_seq;

drop sequence if exists target_seq;

drop sequence if exists taxonomy_seq;

drop sequence if exists taxonomy_vocabulary_seq;

drop sequence if exists creator_seq;

