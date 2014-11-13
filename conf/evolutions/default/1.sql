# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table collection (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  value                     text,
  summary                   text,
  format                    varchar(255),
  vid                       bigint,
  is_new                    boolean,
  type                      varchar(255),
  title                     varchar(255),
  language                  varchar(255),
  edit_url                  varchar(255),
  status                    bigint,
  promote                   bigint,
  sticky                    bigint,
  author                    varchar(255),
  log                       varchar(255),
  comment                   bigint,
  comment_count             bigint,
  comment_count_new         bigint,
  revision                  varchar(255),
  feed_nid                  bigint,
  field_owner               text,
  field_dates               text,
  publish                   boolean,
  field_targets             text,
  field_sub_collections     text,
  field_instances           text,
  weight                    bigint,
  node_count                bigint,
  vocabulary                text,
  parent                    text,
  parents_all               text,
  updated_at                timestamp not null,
  constraint pk_collection primary key (id))
;

create table communication_log (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  curator                   text,
  date                      text,
  ttype                     text,
  permission                text,
  notes                     text,
  updated_at                timestamp not null,
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
  target                    text,
  description               text,
  any_other_information     text,
  status                    text,
  contact_person            text,
  creator_user              text,
  assigned_archivist        text,
  template                  text,
  license                   text,
  license_date              text,
  request_followup          boolean,
  number_requests           bigint,
  third_party_content       boolean,
  publish                   boolean,
  agree                     boolean,
  updated_at                timestamp not null,
  constraint pk_crawl_permission primary key (id))
;

create table flag (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  description               text,
  updated_at                timestamp not null,
  constraint pk_flag primary key (id))
;

create table instance (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  organisation_id           bigint,
  value                     text,
  summary                   text,
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
  field_crawl_start_date    timestamp,
  field_crawl_end_date      timestamp,
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
  edit_url                  varchar(255),
  status                    bigint,
  promote                   bigint,
  sticky                    bigint,
  author                    varchar(255),
  log                       varchar(255),
  comment                   bigint,
  comment_count             bigint,
  comment_count_new         bigint,
  feed_nid                  bigint,
  field_live_site_status    varchar(255),
  field_wct_id              bigint,
  field_spt_id              bigint,
  field_no_ld_criteria_met  boolean,
  field_key_site            boolean,
  field_professional_judgement_exp text,
  field_ignore_robots_txt   boolean,
  revision                  varchar(255),
  field_qa_issues           text,
  field_target              text,
  field_description_of_qa_issues text,
  field_timestamp           text,
  field_published           boolean,
  field_to_be_published     boolean,
  date_of_publication       varchar(255),
  justification             text,
  selector_notes            text,
  archivist_notes           text,
  selection_type            varchar(255),
  selector                  varchar(255),
  legacy_site_id            bigint,
  white_list                varchar(255),
  black_list                varchar(255),
  field_url                 text,
  field_description         text,
  field_uk_postal_address_url text,
  field_suggested_collections text,
  field_collections         text,
  field_license             text,
  field_collection_categories text,
  field_notes               text,
  field_instances           text,
  field_subject             text,
  field_sub_subject         text,
  field_qa_status           text,
  qa_status                 text,
  qa_issue_category         text,
  qa_notes                  text,
  quality_notes             text,
  keywords                  text,
  tags                      text,
  synonyms                  text,
  originating_organisation  text,
  flags                     text,
  authors                   TEXT,
  updated_at                timestamp not null,
  constraint pk_instance primary key (id))
;

create table lookup_entry (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  ttype                     text,
  scopevalue                boolean,
  updated_at                timestamp not null,
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
  nomination_date           timestamp,
  nomination_checked        boolean,
  updated_at                timestamp not null,
  constraint pk_nomination primary key (id))
;

create table organisation (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  value                     text,
  summary                   varchar(255),
  format                    varchar(255),
  abbreviation              varchar(255),
  vid                       bigint,
  is_new                    boolean,
  type                      varchar(255),
  title                     varchar(255),
  language                  varchar(255),
  edit_url                  varchar(255),
  status                    bigint,
  promote                   bigint,
  sticky                    bigint,
  author                    varchar(255),
  log                       varchar(255),
  comment                   bigint,
  comment_count             bigint,
  comment_count_new         bigint,
  revision                  text,
  feed_nid                  bigint,
  updated_at                timestamp not null,
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
  constraint pk_role primary key (id))
;

create table tag (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      text,
  description               text,
  updated_at                timestamp not null,
  constraint pk_tag primary key (id))
;

create table target (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  organisation_id           bigint,
  value                     text,
  summary                   text,
  format                    varchar(255),
  field_scope               varchar(255),
  field_depth               varchar(255),
  field_via_correspondence  boolean,
  field_uk_postal_address   boolean,
  field_uk_hosting          boolean,
  field_nominating_organisation varchar(255),
  field_crawl_frequency     varchar(255),
  field_crawl_start_date    timestamp,
  field_crawl_end_date      timestamp,
  field_uk_domain           boolean,
  field_crawl_permission    varchar(255),
  field_special_dispensation boolean,
  field_special_dispensation_reason text,
  field_uk_geo_ip           boolean,
  field_professional_judgement boolean,
  vid                       bigint,
  is_new                    boolean,
  type                      varchar(255),
  title                     varchar(255),
  language                  varchar(255),
  edit_url                  varchar(255),
  status                    bigint,
  promote                   bigint,
  sticky                    bigint,
  author                    varchar(255),
  log                       varchar(255),
  comment                   bigint,
  comment_count             bigint,
  comment_count_new         bigint,
  feed_nid                  bigint,
  field_live_site_status    varchar(255),
  field_wct_id              bigint,
  field_spt_id              bigint,
  legacy_site_id            bigint,
  field_no_ld_criteria_met  boolean,
  field_key_site            boolean,
  field_professional_judgement_exp text,
  field_ignore_robots_txt   boolean,
  revision                  text,
  active                    boolean,
  white_list                varchar(255),
  black_list                varchar(255),
  date_of_publication       timestamp,
  justification             text,
  selector_notes            text,
  archivist_notes           text,
  selection_type            varchar(255),
  selector                  varchar(255),
  flag_notes                text,
  tab_status                varchar(255),
  is_in_scope_uk_registration_value boolean,
  is_in_scope_domain_value  boolean,
  is_uk_hosting_value       boolean,
  is_in_scope_ip_value      boolean,
  is_in_scope_ip_without_license_value boolean,
  field_url                 text,
  domain                    text,
  field_description         text,
  field_uk_postal_address_url text,
  field_suggested_collections text,
  field_collections         text,
  field_license             text,
  field_collection_categories text,
  field_notes               text,
  field_instances           text,
  field_subject             text,
  field_sub_subject         text,
  keywords                  text,
  tags                      text,
  synonyms                  text,
  originating_organisation  text,
  flags                     text,
  authors                   text,
  field_qa_status           text,
  qa_status                 text,
  qa_issue_category         text,
  qa_notes                  text,
  quality_notes             text,
  updated_at                timestamp not null,
  constraint pk_target primary key (id))
;

create table taxonomy (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      varchar(255),
  ttype                     varchar(255),
  description               text,
  weight                    bigint,
  node_count                bigint,
  vocabulary                text,
  feed_nid                  bigint,
  field_owner               text,
  field_dates               text,
  field_publish             text,
  publish                   boolean,
  parent                    text,
  parents_all               text,
  updated_at                timestamp not null,
  constraint pk_taxonomy primary key (id))
;

create table taxonomy_vocabulary (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  name                      varchar(255),
  machine_name              varchar(255),
  description               text,
  term_count                bigint,
  updated_at                timestamp not null,
  constraint pk_taxonomy_vocabulary primary key (id))
;

create table creator (
  id                        bigint not null,
  url                       varchar(255),
  created_at                timestamp,
  organisation_id           bigint,
  email                     varchar(255),
  password                  varchar(255),
  name                      varchar(255),
  field_affiliation         varchar(255),
  edit_url                  varchar(255),
  last_access               varchar(255),
  last_login                varchar(255),
  status                    bigint,
  language                  varchar(255),
  feed_nid                  bigint,
  revision                  text,
  updated_at                timestamp not null,
  constraint pk_creator primary key (id))
;


create table collection_target (
  collection_id                  bigint not null,
  target_id                      bigint not null,
  constraint pk_collection_target primary key (collection_id, target_id))
;

create table collection_instance (
  collection_id                  bigint not null,
  instance_id                    bigint not null,
  constraint pk_collection_instance primary key (collection_id, instance_id))
;

create table flag_target (
  flag_id                        bigint not null,
  target_id                      bigint not null,
  constraint pk_flag_target primary key (flag_id, target_id))
;

create table flag_instance (
  flag_id                        bigint not null,
  instance_id                    bigint not null,
  constraint pk_flag_instance primary key (flag_id, instance_id))
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

create table tag_target (
  tag_id                         bigint not null,
  target_id                      bigint not null,
  constraint pk_tag_target primary key (tag_id, target_id))
;

create table tag_instance (
  tag_id                         bigint not null,
  instance_id                    bigint not null,
  constraint pk_tag_instance primary key (tag_id, instance_id))
;

create table subject_target (
  taxonomy_id                    bigint not null,
  target_id                      bigint not null,
  constraint pk_subject_target primary key (taxonomy_id, target_id))
;

create table subject_instance (
  taxonomy_id                    bigint not null,
  instance_id                    bigint not null,
  constraint pk_subject_instance primary key (taxonomy_id, instance_id))
;

create table license_target (
  license_id                     bigint not null,
  target_id                      bigint not null,
  constraint pk_license_target primary key (license_id, target_id))
;
create sequence collection_seq;

create sequence communication_log_seq;

create sequence contact_person_seq;

create sequence crawl_permission_seq;

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

alter table crawl_permission add constraint fk_crawl_permission_targetToCr_1 foreign key (target_id) references target (id);
create index ix_crawl_permission_targetToCr_1 on crawl_permission (target_id);
alter table crawl_permission add constraint fk_crawl_permission_mailTempla_2 foreign key (mailTemplate_id) references mail_template (id);
create index ix_crawl_permission_mailTempla_2 on crawl_permission (mailTemplate_id);
alter table crawl_permission add constraint fk_crawl_permission_contactPer_3 foreign key (contactPerson_id) references contact_person (id);
create index ix_crawl_permission_contactPer_3 on crawl_permission (contactPerson_id);
alter table instance add constraint fk_instance_organisationToInst_4 foreign key (organisation_id) references organisation (id);
create index ix_instance_organisationToInst_4 on instance (organisation_id);
alter table target add constraint fk_target_organisationToTarget_5 foreign key (organisation_id) references organisation (id);
create index ix_target_organisationToTarget_5 on target (organisation_id);
alter table creator add constraint fk_creator_organisation_6 foreign key (organisation_id) references organisation (id);
create index ix_creator_organisation_6 on creator (organisation_id);



alter table collection_target add constraint fk_collection_target_collecti_01 foreign key (collection_id) references collection (id);

alter table collection_target add constraint fk_collection_target_target_02 foreign key (target_id) references target (id);

alter table collection_instance add constraint fk_collection_instance_collec_01 foreign key (collection_id) references collection (id);

alter table collection_instance add constraint fk_collection_instance_instan_02 foreign key (instance_id) references instance (id);

alter table flag_target add constraint fk_flag_target_flag_01 foreign key (flag_id) references flag (id);

alter table flag_target add constraint fk_flag_target_target_02 foreign key (target_id) references target (id);

alter table flag_instance add constraint fk_flag_instance_flag_01 foreign key (flag_id) references flag (id);

alter table flag_instance add constraint fk_flag_instance_instance_02 foreign key (instance_id) references instance (id);

alter table permission_role add constraint fk_permission_role_permission_01 foreign key (permission_id) references permission (id);

alter table permission_role add constraint fk_permission_role_role_02 foreign key (role_id) references role (id);

alter table role_user add constraint fk_role_user_role_01 foreign key (role_id) references role (id);

alter table role_user add constraint fk_role_user_creator_02 foreign key (user_id) references creator (id);

alter table tag_target add constraint fk_tag_target_tag_01 foreign key (tag_id) references tag (id);

alter table tag_target add constraint fk_tag_target_target_02 foreign key (target_id) references target (ID);

alter table tag_instance add constraint fk_tag_instance_tag_01 foreign key (tag_id) references tag (id);

alter table tag_instance add constraint fk_tag_instance_instance_02 foreign key (instance_id) references instance (id);

alter table subject_target add constraint fk_subject_target_taxonomy_01 foreign key (taxonomy_id) references taxonomy (id);

alter table subject_target add constraint fk_subject_target_target_02 foreign key (target_id) references target (ID);

alter table subject_instance add constraint fk_subject_instance_taxonomy_01 foreign key (taxonomy_id) references taxonomy (id);

alter table subject_instance add constraint fk_subject_instance_instance_02 foreign key (instance_id) references instance (id);

alter table license_target add constraint fk_license_target_taxonomy_01 foreign key (license_id) references taxonomy (id);

alter table license_target add constraint fk_license_target_target_02 foreign key (target_id) references target (id);

# --- !Downs

drop table if exists collection cascade;

drop table if exists collection_target cascade;

drop table if exists collection_instance cascade;

drop table if exists communication_log cascade;

drop table if exists contact_person cascade;

drop table if exists crawl_permission cascade;

drop table if exists flag cascade;

drop table if exists flag_target cascade;

drop table if exists flag_instance cascade;

drop table if exists instance cascade;

drop table if exists subject_instance cascade;

drop table if exists tag_instance cascade;

drop table if exists lookup_entry cascade;

drop table if exists mail_template cascade;

drop table if exists nomination cascade;

drop table if exists organisation cascade;

drop table if exists permission cascade;

drop table if exists permission_role cascade;

drop table if exists permission_refusal cascade;

drop table if exists role cascade;

drop table if exists role_user cascade;

drop table if exists tag cascade;

drop table if exists tag_target cascade;

drop table if exists target cascade;

drop table if exists subject_target cascade;

drop table if exists taxonomy cascade;

drop table if exists license_target cascade;

drop table if exists taxonomy_vocabulary cascade;

drop table if exists creator cascade;

drop sequence if exists collection_seq;

drop sequence if exists communication_log_seq;

drop sequence if exists contact_person_seq;

drop sequence if exists crawl_permission_seq;

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

