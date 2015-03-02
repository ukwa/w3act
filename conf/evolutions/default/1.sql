
# --- !Ups

create table communication_log (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  user_id                   bigint,
  date                      text,
  ttype                     text,
  crawlPermission_id        bigint,
  notes                     text,
  updated_at                timestamp not null,
  constraint uq_communication_log_url unique (url),
  constraint pk_communication_log primary key (id))
;

create table contact_person (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  position                  text,
  phone                     text,
  email                     text,
  postal_address            text,
  web_form                  text,
  description               text,
  contact_organisation      text,
  default_contact           boolean,
  permission_checked        boolean,
  updated_at                timestamp not null,
  constraint uq_contact_person_url unique (url),
  constraint pk_contact_person primary key (id))
;

create table crawl_permission (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  target_id                 bigint,
  mailTemplate_id           bigint,
  contactPerson_id          bigint,
  name                      text,
  description               text,
  any_other_information     text,
  archivist_id              bigint,
  status                    text,
  license_id                bigint,
  token                     varchar(255),
  request_followup          boolean,
  number_requests           bigint,
  third_party_content       boolean,
  publish                   boolean,
  agree                     boolean,
  updated_at                timestamp not null,
  constraint uq_crawl_permission_url unique (url),
  constraint pk_crawl_permission primary key (id))
;

create table field_url (
  id                        bigint not null,
  url                       text,
  created_at                timestamp,
  target_id                 bigint,
  domain                    text,
  updated_at                timestamp not null,
  constraint pk_field_url primary key (id))
;

create table instance (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  title                     varchar(255),
  language                  varchar(255),
  revision                  text,
  edit_url                  varchar(255),
  qaissue_id                bigint,
  notes                     text,
  format                    varchar(255),
  qa_issue_category         varchar(255),
  qa_notes                  text,
  target_id                 bigint,
  author_id                 bigint,
  field_timestamp           timestamp,
  value                     text,
  summary                   text,
  field_date                timestamp,
  updated_at                timestamp not null,
  constraint uq_instance_url unique (url),
  constraint pk_instance primary key (id))
;

create table lookup_entry (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  ttype                     text,
  scopevalue                boolean,
  target_id                 bigint,
  updated_at                timestamp not null,
  constraint uq_lookup_entry_url unique (url),
  constraint pk_lookup_entry primary key (id))
;

create table mail_template (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  ttype                     text,
  subject                   text,
  from_email                text,
  place_holders             text,
  default_email             boolean,
  text                      text,
  updated_at                timestamp not null,
  constraint uq_mail_template_url unique (url),
  constraint pk_mail_template primary key (id))
;

create table nomination (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  title                     text,
  website_url               text,
  email                     text,
  tel                       text,
  address                   text,
  nominated_website_owner   boolean,
  justification             text,
  notes                     text,
  nomination_checked        boolean,
  nomination_date           timestamp,
  updated_at                timestamp not null,
  constraint uq_nomination_url unique (url),
  constraint pk_nomination primary key (id))
;

create table organisation (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  title                     varchar(255),
  edit_url                  varchar(255),
  summary                   varchar(255),
  author_id                 bigint,
  affiliation               varchar(255),
  revision                  text,
  updated_at                timestamp not null,
  constraint uq_organisation_url unique (url),
  constraint pk_organisation primary key (id))
;

create table permission (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  description               text,
  revision                  text,
  updated_at                timestamp not null,
  constraint uq_permission_url unique (url),
  constraint pk_permission primary key (id))
;

create table permission_refusal (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  date                      text,
  ttype                     text,
  reason                    text,
  updated_at                timestamp not null,
  constraint uq_permission_refusal_url unique (url),
  constraint pk_permission_refusal primary key (id))
;

create table role (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  description               text,
  revision                  text,
  updated_at                timestamp not null,
  constraint uq_role_url unique (url),
  constraint pk_role primary key (id))
;

create table target (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  title                     varchar(255),
  language                  varchar(255),
  revision                  text,
  edit_url                  varchar(255),
  qaissue_id                bigint,
  notes                     text,
  format                    varchar(255),
  originating_organisation  text,
  description               text,
  is_in_scope_ip            boolean,
  is_in_scope_ip_without_license boolean,
  active                    boolean,
  author_id                 bigint,
  flag_notes                text,
  value                     text,
  summary                   text,
  special_dispensation      boolean,
  special_dispensation_reason text,
  is_uk_hosting             boolean,
  is_top_level_domain       boolean,
  is_uk_registration        boolean,
  live_site_status          varchar(255),
  key_site                  boolean,
  wct_id                    bigint,
  spt_id                    bigint,
  keywords                  text,
  synonyms                  text,
  organisation_id           bigint,
  authors                   text,
  date_of_publication       timestamp,
  justification             text,
  selection_type            varchar(255),
  selector_notes            text,
  archivist_notes           text,
  legacy_site_id            bigint,
  uk_postal_address         boolean,
  uk_postal_address_url     text,
  via_correspondence        boolean,
  professional_judgement    boolean,
  professional_judgement_exp text,
  no_ld_criteria_met        boolean,
  scope                     varchar(255),
  depth                     varchar(255),
  ignore_robots_txt         boolean,
  crawl_frequency           varchar(255),
  crawl_start_date          timestamp,
  crawl_end_date            timestamp,
  white_list                varchar(255),
  black_list                varchar(255),
  license_status            varchar(255),
  updated_at                timestamp not null,
  constraint uq_target_url unique (url),
  constraint pk_target primary key (id))
;

create table taxonomy (
  ttype                     varchar(31) not null,
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      varchar(255),
  description               text,
  publish                   boolean,
  parents_all               text,
  revision                  text,
  taxonomyType_id           bigint,
  parent_id                 bigint,
  updated_at                timestamp not null,
  status                    varchar(255),
  constraint uq_taxonomy_url unique (url),
  constraint pk_taxonomy primary key (id))
;

create table taxonomy_type (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      varchar(255),
  machine_name              varchar(255),
  description               text,
  vid                       bigint,
  updated_at                timestamp not null,
  constraint uq_taxonomy_type_url unique (url),
  constraint pk_taxonomy_type primary key (id))
;

create table token (
  token                     varchar(255) not null,
  user_id                   bigint,
  type                      varchar(8),
  date_creation             timestamp,
  email                     varchar(255),
  constraint ck_token_type check (type in ('password','email')),
  constraint pk_token primary key (token))
;

create table creator (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  organisation_id           bigint,
  email                     varchar(255),
  password                  varchar(255),
  name                      varchar(255),
  affiliation               varchar(255),
  edit_url                  varchar(255),
  revision                  text,
  updated_at                timestamp not null,
  constraint uq_creator_url unique (url),
  constraint pk_creator primary key (id))
;


create table organisation_instance (
  organisation_id                bigint not null,
  instance_id                    bigint not null,
  constraint pk_organisation_instance primary key (organisation_id, instance_id))
;

create table permission_role (
  permission_id                  bigint not null,
  role_id                        bigint not null,
  constraint pk_permission_role primary key (permission_id, role_id))
;

create table role_user (
  role_id                        bigint not null,
  user_id                        bigint not null,
  constraint pk_role_user primary key (role_id, user_id))
;

create table license_target (
  target_id                      bigint not null,
  license_id                     bigint not null,
  constraint pk_license_target primary key (target_id, license_id))
;

create table subject_target (
  target_id                      bigint not null,
  subject_id                     bigint not null,
  constraint pk_subject_target primary key (target_id, subject_id))
;

create table collection_target (
  target_id                      bigint not null,
  collection_id                  bigint not null,
  constraint pk_collection_target primary key (target_id, collection_id))
;

create table tag_target (
  target_id                      bigint not null,
  tag_id                         bigint not null,
  constraint pk_tag_target primary key (target_id, tag_id))
;

create table flag_target (
  target_id                      bigint not null,
  flag_id                        bigint not null,
  constraint pk_flag_target primary key (target_id, flag_id))
;

create table taxonomy_user (
  taxonomy_id                    bigint not null,
  user_id                        bigint not null,
  constraint pk_taxonomy_user primary key (taxonomy_id, user_id))
;

create table taxonomy_parents_all (
  taxonomy_id                    bigint not null,
  parent_id                      bigint not null,
  constraint pk_taxonomy_parents_all primary key (taxonomy_id, parent_id))
;

create table flag_instance (
  flag_id                        bigint not null,
  instance_id                    bigint not null,
  constraint pk_flag_instance primary key (flag_id, instance_id))
;

create table tag_instance (
  tag_id                         bigint not null,
  instance_id                    bigint not null,
  constraint pk_tag_instance primary key (tag_id, instance_id))
;

create table license_instance (
  instance_id                    bigint not null,
  license_id                     bigint not null,
  constraint pk_license_instance primary key (instance_id, license_id))
;
create sequence communication_log_seq;

create sequence contact_person_seq;

create sequence crawl_permission_seq;

create sequence field_url_seq;

create sequence instance_seq;

create sequence lookup_entry_seq;

create sequence mail_template_seq;

create sequence nomination_seq;

create sequence organisation_seq;

create sequence permission_seq;

create sequence permission_refusal_seq;

create sequence role_seq;

create sequence target_seq;

create sequence taxonomy_seq;

create sequence taxonomy_type_seq;

create sequence token_seq;

create sequence creator_seq;

alter table communication_log add constraint fk_communication_log_user_1 foreign key (user_id) references creator (id);
create index ix_communication_log_user_1 on communication_log (user_id);
alter table communication_log add constraint fk_communication_log_crawlPerm_2 foreign key (crawlPermission_id) references crawl_permission (id);
create index ix_communication_log_crawlPerm_2 on communication_log (crawlPermission_id);
alter table crawl_permission add constraint fk_crawl_permission_target_3 foreign key (target_id) references target (id);
create index ix_crawl_permission_target_3 on crawl_permission (target_id);
alter table crawl_permission add constraint fk_crawl_permission_mailTempla_4 foreign key (mailTemplate_id) references mail_template (id);
create index ix_crawl_permission_mailTempla_4 on crawl_permission (mailTemplate_id);
alter table crawl_permission add constraint fk_crawl_permission_contactPer_5 foreign key (contactPerson_id) references contact_person (id);
create index ix_crawl_permission_contactPer_5 on crawl_permission (contactPerson_id);
alter table crawl_permission add constraint fk_crawl_permission_user_6 foreign key (archivist_id) references creator (id);
create index ix_crawl_permission_user_6 on crawl_permission (archivist_id);
alter table crawl_permission add constraint fk_crawl_permission_license_7 foreign key (license_id) references taxonomy (id);
create index ix_crawl_permission_license_7 on crawl_permission (license_id);
alter table field_url add constraint fk_field_url_target_8 foreign key (target_id) references target (id);
create index ix_field_url_target_8 on field_url (target_id);
alter table instance add constraint fk_instance_qaIssue_9 foreign key (qaissue_id) references taxonomy (id);
create index ix_instance_qaIssue_9 on instance (qaissue_id);
alter table instance add constraint fk_instance_target_10 foreign key (target_id) references target (id);
create index ix_instance_target_10 on instance (target_id);
alter table instance add constraint fk_instance_authorUser_11 foreign key (author_id) references creator (id);
create index ix_instance_authorUser_11 on instance (author_id);
alter table lookup_entry add constraint fk_lookup_entry_target_12 foreign key (target_id) references target (id);
create index ix_lookup_entry_target_12 on lookup_entry (target_id);
alter table organisation add constraint fk_organisation_authorUser_13 foreign key (author_id) references creator (id);
create index ix_organisation_authorUser_13 on organisation (author_id);
alter table target add constraint fk_target_qaIssue_14 foreign key (qaissue_id) references taxonomy (id);
create index ix_target_qaIssue_14 on target (qaissue_id);
alter table target add constraint fk_target_authorUser_15 foreign key (author_id) references creator (id);
create index ix_target_authorUser_15 on target (author_id);
alter table target add constraint fk_target_organisation_16 foreign key (organisation_id) references organisation (id);
create index ix_target_organisation_16 on target (organisation_id);
alter table taxonomy add constraint fk_taxonomy_taxonomyType_17 foreign key (taxonomyType_id) references taxonomy_type (id);
create index ix_taxonomy_taxonomyType_17 on taxonomy (taxonomyType_id);
alter table taxonomy add constraint fk_taxonomy_parent_18 foreign key (parent_id) references taxonomy (id);
create index ix_taxonomy_parent_18 on taxonomy (parent_id);
alter table creator add constraint fk_creator_organisation_19 foreign key (organisation_id) references organisation (id);
create index ix_creator_organisation_19 on creator (organisation_id);



alter table organisation_instance add constraint fk_organisation_instance_orga_01 foreign key (organisation_id) references organisation (id);

alter table organisation_instance add constraint fk_organisation_instance_inst_02 foreign key (instance_id) references instance (id);

alter table permission_role add constraint fk_permission_role_permission_01 foreign key (permission_id) references permission (id);

alter table permission_role add constraint fk_permission_role_role_02 foreign key (role_id) references role (id);

alter table role_user add constraint fk_role_user_role_01 foreign key (role_id) references role (id);

alter table role_user add constraint fk_role_user_creator_02 foreign key (user_id) references creator (id);

alter table license_target add constraint fk_license_target_target_01 foreign key (target_id) references target (id);

alter table license_target add constraint fk_license_target_taxonomy_02 foreign key (license_id) references taxonomy (id);

alter table subject_target add constraint fk_subject_target_target_01 foreign key (target_id) references target (id);

alter table subject_target add constraint fk_subject_target_taxonomy_02 foreign key (subject_id) references taxonomy (id);

alter table collection_target add constraint fk_collection_target_target_01 foreign key (target_id) references target (id);

alter table collection_target add constraint fk_collection_target_taxonomy_02 foreign key (collection_id) references taxonomy (id);

alter table tag_target add constraint fk_tag_target_target_01 foreign key (target_id) references target (id);

alter table tag_target add constraint fk_tag_target_taxonomy_02 foreign key (tag_id) references taxonomy (id);

alter table flag_target add constraint fk_flag_target_target_01 foreign key (target_id) references target (id);

alter table flag_target add constraint fk_flag_target_taxonomy_02 foreign key (flag_id) references taxonomy (id);

alter table taxonomy_user add constraint fk_taxonomy_user_taxonomy_01 foreign key (taxonomy_id) references taxonomy (id);

alter table taxonomy_user add constraint fk_taxonomy_user_creator_02 foreign key (user_id) references creator (id);

alter table taxonomy_parents_all add constraint fk_taxonomy_parents_all_taxon_01 foreign key (taxonomy_id) references taxonomy (id);

alter table taxonomy_parents_all add constraint fk_taxonomy_parents_all_taxon_02 foreign key (parent_id) references taxonomy (id);

alter table flag_instance add constraint fk_flag_instance_taxonomy_01 foreign key (flag_id) references taxonomy (id);

alter table flag_instance add constraint fk_flag_instance_instance_02 foreign key (instance_id) references instance (id);

alter table tag_instance add constraint fk_tag_instance_taxonomy_01 foreign key (tag_id) references taxonomy (id);

alter table tag_instance add constraint fk_tag_instance_instance_02 foreign key (instance_id) references instance (id);

alter table license_instance add constraint fk_license_instance_taxonomy_01 foreign key (instance_id) references taxonomy (id);

alter table license_instance add constraint fk_license_instance_instance_02 foreign key (license_id) references instance (id);

# --- !Downs

drop table if exists communication_log cascade;

drop table if exists contact_person cascade;

drop table if exists crawl_permission cascade;

drop table if exists field_url cascade;

drop table if exists instance cascade;

drop table if exists lookup_entry cascade;

drop table if exists mail_template cascade;

drop table if exists nomination cascade;

drop table if exists organisation cascade;

drop table if exists organisation_instance cascade;

drop table if exists permission cascade;

drop table if exists permission_role cascade;

drop table if exists permission_refusal cascade;

drop table if exists role cascade;

drop table if exists role_user cascade;

drop table if exists target cascade;

drop table if exists license_target cascade;

drop table if exists subject_target cascade;

drop table if exists collection_target cascade;

drop table if exists tag_target cascade;

drop table if exists flag_target cascade;

drop table if exists taxonomy cascade;

drop table if exists taxonomy_user cascade;

drop table if exists taxonomy_parents_all cascade;

drop table if exists taxonomy_type cascade;

drop table if exists token cascade;

drop table if exists creator cascade;

drop sequence if exists communication_log_seq;

drop sequence if exists contact_person_seq;

drop sequence if exists crawl_permission_seq;

drop sequence if exists field_url_seq;

drop sequence if exists instance_seq;

drop sequence if exists lookup_entry_seq;

drop sequence if exists mail_template_seq;

drop sequence if exists nomination_seq;

drop sequence if exists organisation_seq;

drop sequence if exists permission_seq;

drop sequence if exists permission_refusal_seq;

drop sequence if exists role_seq;

drop sequence if exists target_seq;

drop sequence if exists taxonomy_seq;

drop sequence if exists taxonomy_type_seq;

drop sequence if exists token_seq;

drop sequence if exists creator_seq;

