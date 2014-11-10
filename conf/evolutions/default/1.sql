# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table CommunicationLog (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  curator                   text,
  ttype                     text,
  permission                text,
  notes                     text,
  createdAt                 timestamp not null,
  updatedAt               	timestamp not null,
  constraint pkCommunicationLog primary key (id))
;

create table ContactPerson (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  position                  text,
  phone                     text,
  email                     text,
  postalAddress            	text,
  webForm                  	text,
  description               text,
  contactOrganisation      	text,
  defaultContact           	boolean,
  permissionChecked        	boolean,
  createdAt                 timestamp not null,
  updatedAt               	timestamp not null,
  constraint pkContactPerson primary key (id))
;

create table CrawlPermission (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  target_id                 bigint,
  mailTemplate_id           bigint,
  contactPerson_id          bigint,
  url                       text,
  name                      text,
  target                    text,
  description               text,
  anyOtherInformation     	text,
  status                    text,
  contactPerson            	text,
  creatorUser              	text,
  assignedArchivist        	text,
  template                  text,
  license                   text,
  licenseDate              	timestamp not null,
  requestFollowup          	boolean,
  numberRequests           	bigint,
  thirdPartyContent       	boolean,
  publish                   boolean,
  agree                     boolean,
  createdAt                 timestamp not null,
  updatedAt               	timestamp not null,
  constraint pkCrawlPermission primary key (id))
;

create table Collection (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  value                     text,
  summary                   text,
  format                    varchar(255),
  vid                       bigint,
  isNew                    	boolean,
  type                      varchar(255),
  title                     varchar(255),
  language                  varchar(255),
  url                       varchar(255),
  edit_url                  varchar(255),
  status                    bigint,
  promote                   bigint,
  sticky                    bigint,
  author                    varchar(255),
  log                       varchar(255),
  comment                   bigint,
  commentCount             	bigint,
  commentCountNew         	bigint,
  revision                  varchar(255),
  feedNid                  	bigint,
  fieldOwner               	text,
  fieldDates               	text,
  publish                   boolean,
  fieldTargets             	text,
  fieldSubCollections     	text,
  fieldInstances           	text,
  weight                    bigint,
  nodeCount                	bigint,
  vocabulary                text,
  parent                    text,
  parentsAll               	text,
  createdAt                 timestamp not null,
  updatedAt                 timestamp not null,
  constraint pkCollection primary key (id))
;

create table Flag (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  description               text,
  createdAt                 timestamp not null,
  updatedAt                 timestamp not null,
  constraint pkFlag primary key (id))
;

create table Instance (
  id 								bigint(20) NOT NULL AUTO_INCREMENT,
  organisation_id           		bigint not null,
  value                     		text,
  summary                   		text,
  actUrl                   			varchar(255),
  wctUrl                   			varchar(255),
  format                    		varchar(255),
  fieldScope               			varchar(255),
  fieldDepth               			varchar(255),
  fieldViaCorrespondence  			boolean,
  fieldUkPostal_address   			boolean,
  fieldUkHosting          			boolean,
  fieldNominatingOrganisation 		varchar(255),
  fieldCrawlFrequency     			varchar(255),
  fieldCrawlStartDate    			varchar(255),
  fieldUkDomain           			boolean,
  fieldCrawlPermission    			varchar(255),
  fieldSpecialDispensation 			varchar(255),
  fieldUkGeoip            			boolean,
  fieldProfessionalJudgement 		boolean,
  vid                       		bigint,
  isNew                    			boolean,
  type                      		varchar(255),
  title                     		varchar(255),
  language                  		varchar(255),
  url                       		varchar(255),
  editUrl                  			varchar(255),
  status                    		bigint,
  promote                   		bigint,
  sticky                    		bigint,
  author                    		varchar(255),
  log                       		varchar(255),
  comment                   		bigint,
  commentCount             			bigint,
  commentCountNew         			bigint,
  feedNid                  			bigint,
  fieldCrawlEndDate      			varchar(255),
  fieldLiveSiteStatus    			varchar(255),
  fieldWct_id              			bigint,
  fieldSpt_id              			bigint,
  fieldNoldCriteriaMet  			boolean,
  fieldKeySite            			boolean,
  fieldProfessionalJudgementExp 	text,
  fieldIgnoreRobotsTxt   			boolean,
  revision                  		varchar(255),
  fieldQaIssues           			text,
  fieldTarget              			text,
  fieldDescriptionOfQaIssues 		text,
  fieldTimestamp           			timestamp NOT NULL,
  fieldPublished           			boolean,
  fieldToBePublished     			boolean,
  dateofpublication       			varchar(255),
  justification             		text,
  selectorNotes            			text,
  archivistNotes           			text,
  selectionType            			varchar(255),
  selector                  		varchar(255),
  legacySite_id            			bigint,
  whiteList                			varchar(255),
  blackList                			varchar(255),
  fieldUrl                 			text,
  fieldDescription         			text,
  fieldUkPostalAddressUrl 			text,
  fieldSuggestedCollections 		text,
  fieldCollections         			text,
  fieldLicense             			text,
  fieldCollectionCategories 		text,
  fieldNotes               			text,
  fieldInstances           			text,
  fieldSubject             			text,
  fieldSubsubject          			text,
  fieldQaStatus           			text,
  qaStatus                 			text,
  qaIssueCategory         			text,
  qaNotes                  			text,
  qualityNotes             			text,
  keywords                  		text,
  tags                      		text,
  synonyms                  		text,
  originatingOrganisation  			text,
  flags                     		text,
  authors                   		text,
  createdAt                 timestamp not null,
  updatedAt                 timestamp not null,
  constraint pk_instance primary key (id))
;

create table LookupEntry (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  ttype                     text,
  scopeValue                boolean,
  updatedAt               	timestamp not null,
  constraint pkLookupEntry primary key (id))
;

create table mail_template (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
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
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
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
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  value                     TEXT,
  summary                   varchar(255),
  format                    varchar(255),
  field_abbreviation        varchar(255),
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
  revision                  TEXT,
  feed_nid                  bigint,
  last_update               timestamp not null,
  constraint pk_organisation primary key (id))
;

create table permission (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  name                      TEXT,
  url                       TEXT,
  description               TEXT,
  revision                  TEXT,
  last_update               timestamp not null,
  constraint pk_permission primary key (id))
;

create table permission_refusal (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       TEXT,
  name                      TEXT,
  date                      TEXT,
  ttype                     TEXT,
  reason                    TEXT,
  last_update               timestamp not null,
  constraint pk_permission_refusal primary key (id))
;

create table role (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  name                      TEXT,
  url                       TEXT,
  description               TEXT,
  revision                  TEXT,
  last_update               timestamp not null,
  constraint pk_role primary key (id))
;

create table tag (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       TEXT,
  name                      TEXT,
  description               TEXT,
  last_update               timestamp not null,
  constraint pk_tag primary key (id))
;

create table target (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  id_organisation           bigint,
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
  tabstatus                 varchar(255),
  is_in_scope_uk_registration_value boolean,
  is_in_scope_domain_value  boolean,
  is_uk_hosting_value       boolean,
  is_in_scope_ip_value      boolean,
  is_in_scope_ip_without_license_value boolean,
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
  constraint pk_target primary key (id))
;

create table taxonomy (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
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
  publish                   boolean,
  parent                    TEXT,
  parents_all               TEXT,
  constraint pk_taxonomy primary key (id))
;

create table taxonomy_vocabulary (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  name                      varchar(255),
  machine_name              varchar(255),
  description               TEXT,
  term_count                bigint,
  constraint pk_taxonomy_vocabulary primary key (id))
;

create table creator (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       varchar(255),
  id_organisation           bigint,
  email                     varchar(255),
  name                      varchar(255),
  password                  varchar(255),
  field_affiliation         varchar(255),
  edit_url                  varchar(255),
  last_access               varchar(255),
  last_login                varchar(255),
  created                   varchar(255),
  status                    bigint,
  language                  varchar(255),
  feed_nid                  bigint,
  revision                  TEXT,
  last_update               timestamp not null,
  constraint pk_creator primary key (id))
;


create table collection_target (
  id_collection                  bigint not null,
  id_target                      bigint not null,
  constraint pk_collection_target primary key (id_collection, id_target))
;

create table collection_instance (
  id_collection                  bigint not null,
  id_instance                    bigint not null,
  constraint pk_collection_instance primary key (id_collection, id_instance))
;

create table flag_target (
  id_flag                        bigint not null,
  id_target                      bigint not null,
  constraint pk_flag_target primary key (id_flag, id_target))
;

create table flag_instance (
  id_flag                        bigint not null,
  id_instance                    bigint not null,
  constraint pk_flag_instance primary key (id_flag, id_instance))
;

create table permission_role (
  id_permission                  bigint not null,
  id_role                        bigint not null,
  constraint pk_permission_role primary key (id_permission, id_role))
;

create table role_user (
  id_role                        bigint not null,
  id_user                        bigint not null,
  constraint pk_role_user primary key (id_role, id_user))
;

create table tag_target (
  id_tag                         bigint not null,
  id_target                      bigint not null,
  constraint pk_tag_target primary key (id_tag, id_target))
;

create table tag_instance (
  id_tag                         bigint not null,
  id_instance                    bigint not null,
  constraint pk_tag_instance primary key (id_tag, id_instance))
;

create table subject_target (
  id_taxonomy                    bigint not null,
  id_target                      bigint not null,
  constraint pk_subject_target primary key (id_taxonomy, id_target))
;

create table subject_instance (
  id_taxonomy                    bigint not null,
  id_instance                    bigint not null,
  constraint pk_subject_instance primary key (id_taxonomy, id_instance))
;

create table license_target (
  id_license                     bigint not null,
  id_target                      bigint not null,
  constraint pk_license_target primary key (id_license, id_target))
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

alter table crawl_permission add constraint fk_crawl_permission_target_to__1 foreign key (id_target) references target (ID);
create index ix_crawl_permission_target_to__1 on crawl_permission (id_target);
alter table crawl_permission add constraint fk_crawl_permission_mailtempla_2 foreign key (id_mailtemplate) references mail_template (id);
create index ix_crawl_permission_mailtempla_2 on crawl_permission (id_mailtemplate);
alter table crawl_permission add constraint fk_crawl_permission_contactper_3 foreign key (id_contactperson) references contact_person (id);
create index ix_crawl_permission_contactper_3 on crawl_permission (id_contactperson);
alter table instance add constraint fk_instance_organisation_to_in_4 foreign key (id_organisation) references organisation (nid);
create index ix_instance_organisation_to_in_4 on instance (id_organisation);
alter table target add constraint fk_target_organisation_to_targ_5 foreign key (id_organisation) references organisation (nid);
create index ix_target_organisation_to_targ_5 on target (id_organisation);
alter table creator add constraint fk_creator_organisation_6 foreign key (id_organisation) references organisation (nid);
create index ix_creator_organisation_6 on creator (id_organisation);



alter table collection_target add constraint fk_collection_target_dcollect_01 foreign key (id_collection) references dcollection (ID);

alter table collection_target add constraint fk_collection_target_target_02 foreign key (id_target) references target (ID);

alter table collection_instance add constraint fk_collection_instance_dcolle_01 foreign key (id_collection) references dcollection (ID);

alter table collection_instance add constraint fk_collection_instance_instan_02 foreign key (id_instance) references instance (ID);

alter table flag_target add constraint fk_flag_target_flag_01 foreign key (id_flag) references flag (ID);

alter table flag_target add constraint fk_flag_target_target_02 foreign key (id_target) references target (ID);

alter table flag_instance add constraint fk_flag_instance_flag_01 foreign key (id_flag) references flag (ID);

alter table flag_instance add constraint fk_flag_instance_instance_02 foreign key (id_instance) references instance (ID);

alter table permission_role add constraint fk_permission_role_permission_01 foreign key (id_permission) references permission (ID);

alter table permission_role add constraint fk_permission_role_role_02 foreign key (id_role) references role (ID);

alter table role_user add constraint fk_role_user_role_01 foreign key (id_role) references role (ID);

alter table role_user add constraint fk_role_user_creator_02 foreign key (id_user) references creator (ID);

alter table tag_target add constraint fk_tag_target_tag_01 foreign key (id_tag) references tag (ID);

alter table tag_target add constraint fk_tag_target_target_02 foreign key (id_target) references target (ID);

alter table tag_instance add constraint fk_tag_instance_tag_01 foreign key (id_tag) references tag (ID);

alter table tag_instance add constraint fk_tag_instance_instance_02 foreign key (id_instance) references instance (ID);

alter table subject_target add constraint fk_subject_target_taxonomy_01 foreign key (id_taxonomy) references taxonomy (ID);

alter table subject_target add constraint fk_subject_target_target_02 foreign key (id_target) references target (ID);

alter table subject_instance add constraint fk_subject_instance_taxonomy_01 foreign key (id_taxonomy) references taxonomy (ID);

alter table subject_instance add constraint fk_subject_instance_instance_02 foreign key (id_instance) references instance (ID);

alter table license_target add constraint fk_license_target_taxonomy_01 foreign key (id_license) references taxonomy (ID);

alter table license_target add constraint fk_license_target_target_02 foreign key (id_target) references target (ID);

# --- !Downs

drop table if exists communication_log cascade;

drop table if exists contact_person cascade;

drop table if exists crawl_permission cascade;

drop table if exists dcollection cascade;

drop table if exists collection_target cascade;

drop table if exists collection_instance cascade;

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

