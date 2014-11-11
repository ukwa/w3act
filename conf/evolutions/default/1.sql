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
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
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
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkContactPerson primary key (id))
;

create table CrawlPermission (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  target_id                 bigint,
  mailTemplate_id           bigint,
  contactPerson_id          bigint,
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
  licenseDate              	timestamp NOT NULL,
  requestFollowup          	boolean,
  numberRequests           	bigint,
  thirdPartyContent       	boolean,
  publish                   boolean,
  agree                     boolean,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkCrawlPermission primary key (id))
;

create table Collection (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       varchar(255),
  value                     text,
  summary                   text,
  format                    varchar(255),
  vid                       bigint,
  isNew                    	boolean,
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
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkCollection primary key (id))
;

create table Flag (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  description               text,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkFlag primary key (id))
;

create table Instance (
  id 								bigint(20) NOT NULL AUTO_INCREMENT,
  url                       		varchar(255),
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
  fieldCrawlStartDate    			timestamp NOT NULL,
  fieldCrawlEndDate      			timestamp NOT NULL,
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
  fieldSubSubject          			text,
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
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pk_instance primary key (id))
;

create table LookupEntry (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  ttype                     text,
  scopeValue                boolean,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkLookupEntry primary key (id))
;

create table MailTemplate (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  ttype                     text,
  subject                   text,
  fromEmail                	text,
  placeHolders             	text,
  defaultEmail             	boolean,
  text                      text,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkMailTemplate primary key (id))
;

create table Nomination (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  title                     text,
  websiteUrl               	text,
  email                     text,
  tel                       text,
  address                   text,
  nominated_website_owner   boolean,
  justification             text,
  notes                     text,
  nominationDate           	timestamp not null,
  nominationChecked        	boolean,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pk_nomination primary key (id))
;

create table Organisation (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       varchar(255),
  value                     text,
  summary                   varchar(255),
  format                    varchar(255),
  fieldAbbreviation        	varchar(255),
  vid                       bigint,
  isNew                    	boolean,
  type                      varchar(255),
  title                     varchar(255),
  language                  varchar(255),
  editUrl                  	varchar(255),
  status                    bigint,
  promote                   bigint,
  sticky                    bigint,
  author                    varchar(255),
  log                       varchar(255),
  comment                   bigint,
  commentCount             	bigint,
  commentCountNew         	bigint,
  revision                  text,
  feedNid                  	bigint,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkOrganisation primary key (id))
;

create table Permission (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  description               text,
  revision                  text,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkPermission primary key (id))
;

create table PermissionRefusal (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  ttype                     text,
  reason                    text,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkPermissionRefusal primary key (id))
;

create table Role (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       TEXT,
  name                      TEXT,
  description               TEXT,
  revision                  TEXT,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkRole primary key (id))
;

create table Tag (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      text,
  description               text,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkTag primary key (id))
;

create table Target (
  id 									bigint(20) NOT NULL AUTO_INCREMENT,
  url                       			varchar(255),
  organisation_id           			bigint,
  value                     			text,
  summary                   			text,
  format                    			varchar(255),
  fieldScope               				varchar(255),
  fieldDepth               				varchar(255),
  fieldViaCorrespondence  				boolean,
  fieldUkPostal_address   				boolean,
  fieldUkHosting          				boolean,
  fieldNominatingOrganisation 			varchar(255),
  fieldCrawlFrequency     				varchar(255),
  fieldCrawlStartDate    				timestamp NOT NULL,
  fieldCrawlEndDate		      			timestamp NOT NULL,
  fieldUkDomain           				boolean,
  fieldCrawlPermission    				varchar(255),
  fieldSpecialDispensation 				boolean,
  fieldSpecialDispensationReason		text,
  fieldUkGeoIp           	 			boolean,
  fieldProfessionalJudgement 			boolean,
  vid                       			bigint,
  isNew				                    boolean,
  type                      			varchar(255),
  title                     			varchar(255),
  language                  			varchar(255),
  editUrl                  				varchar(255),
  status                    			bigint,
  promote                   			bigint,
  sticky                    			bigint,
  author                   				varchar(255),
  log                       			varchar(255),
  comment                   			bigint,
  commentCount             				bigint,
  commentCountNew         				bigint,
  feedNid                  				bigint,
  fieldLiveSiteStatus    				varchar(255),
  fieldWct_id				            bigint,
  fieldSpt_id              				bigint,
  legacySite_id            				bigint,
  fieldNoLdCriteriaMet					boolean,
  fieldKeySite            				boolean,
  fieldProfessionalJudgementExp 		text,
  fieldIgnoreRobotsTxt   				boolean,
  revision                  			text,
  active                    			boolean,
  whiteList				                varchar(255),
  blackList                				varchar(255),
  dateOfPublication       				timestamp NOT NULL,
  justification             			text,
  selectorNotes            				text,
  archivistNotes           				text,
  selectionType            				varchar(255),
  selector                  			varchar(255),
  flagNotes                				text,
  tabStatus                 			varchar(255),
  isInScopeUkRegistrationValue 			boolean,
  isInScopeDomainValue  				boolean,
  isUkHostingValue      				boolean,
  isInScopeIpValue      				boolean,
  isInScopeIpWithoutLicenseValue 		boolean,
  fieldUrl                 				text,
  domain                    			text,
  fieldDescription         				text,
  fieldUkPostalAddressUrl 				text,
  fieldSuggestedCollections 			text,
  fieldCollections         				text,
  fieldLicense             				text,
  fieldCollectionCategories 			text,
  fieldNotes               				text,
  fieldInstances           				text,
  fieldSubject             				text,
  fieldSubSubject          				text,
  keywords                  			text,
  tags                      			text,
  synonyms                  			text,
  originatingOrganisation  				text,
  flags                     			text,
  authors                   			text,
  fieldQaStatus           				text,
  qaStatus                 				text,
  qaIssueCategory         				text,
  qaNotes                  				text,
  qualityNotes             				text,
  createdAt                 			timestamp NOT NULL,
  updatedAt                 			timestamp NOT NULL,
  constraint pkTarget primary key (id))
;

create table Taxonomy (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      varchar(255),
  ttype                     varchar(255),
  description               text,
  weight                    bigint,
  nodeCount                	bigint,
  vocabulary                text,
  feedNid                  	bigint,
  fieldOwner               	text,
  fieldDates               	text,
  fieldPublish             	text,
  publish                   boolean,
  parent                    text,
  parentsAll               	text,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkTaxonomy primary key (id))
;

create table TaxonomyVocabulary (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       text,
  name                      varchar(255),
  machineName              	varchar(255),
  description               text,
  termCount               	bigint,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pkTaxonomyVocabulary primary key (id))
;

create table Creator (
  id 						bigint(20) NOT NULL AUTO_INCREMENT,
  url                       varchar(255),
  organisation_id           bigint,
  email                     varchar(255),
  name                      varchar(255),
  password                  varchar(255),
  fieldAaffiliation         varchar(255),
  editUrl                  	varchar(255),
  lastAccess               	varchar(255),
  lastLogin                	varchar(255),
  status                    bigint,
  language                  varchar(255),
  feedNid                  	bigint,
  revision                  text,
  createdAt                 timestamp NOT NULL,
  updatedAt                 timestamp NOT NULL,
  constraint pk_creator primary key (id))
;


create table CollectionTarget (
  collection_id				bigint not null,
  target_id              	bigint not null,
  constraint pkCollectionTarget primary key (collection_id, target_id))
;

create table CollectionInstance (
  collection_id				bigint not null,
  instance_id               bigint not null,
  constraint pkCollectionInstance primary key (collection_id, instance_id))
;

create table FlagTarget (
  flag_id					bigint not null,
  target_id					bigint not null,
  constraint pkFlagTarget primary key (flag_id, target_id))
;

create table FlagInstance (
  flag_id					bigint not null,
  instance_id               bigint not null,
  constraint pkFlagInstance primary key (flag_id, instance_id))
;

create table PermissionRole (
  permission_id				bigint not null,
  role_id                   bigint not null,
  constraint pkPermissionRole primary key (permission_id, role_id))
;

create table RoleUser (
  role_id					bigint not null,
  user_id                   bigint not null,
  constraint pkRoleUser primary key (role_id, user_id))
;

create table TagTarget (
  tag_id					bigint not null,
  target_id                 bigint not null,
  constraint pkTagTarget primary key (tag_id, target_id))
;

create table TagInstance (
  tag_id					bigint not null,
  instance_id				bigint not null,
  constraint pkTagInstance primary key (tag_id, instance_id))
;

create table SubjectTarget (
  taxonomy_id				bigint not null,
  target_id                 bigint not null,
  constraint pkSubjectTarget primary key (taxonomy_id, target_id))
;

create table SubjectInstance (
  taxonomy_id				bigint not null,
  instance_id				bigint not null,
  constraint pkSubjectInstance primary key (taxonomy_id, instance_id))
;

create table LicenseTarget (
  license_id				bigint not null,
  target_id					bigint not null,
  constraint pkLicenseTarget primary key (license_id, target_id))
;
create sequence communication_log_seq start with 1;

create sequence contact_person_seq start with 1;

create sequence crawl_permission_seq start with 1;

create sequence dcollection_seq start with 1;

create sequence flag_seq start with 1;

create sequence instance_seq start with 1;

create sequence lookup_entry_seq start with 1;

create sequence mail_template_seq start with 1;

create sequence nomination_seq start with 1;

create sequence organisation_seq start with 1;

create sequence permission_seq start with 1;

create sequence permission_refusal_seq start with 1;

create sequence role_seq start with 1;

create sequence tag_seq start with 1;

create sequence target_seq start with 1;

create sequence taxonomy_seq start with 1;

create sequence taxonomy_vocabulary_seq start with 1;

create sequence creator_seq start with 1;

alter table CrawlPermission add constraint fk_crawl_permission_target_to__1 foreign key (target_id) references target (id);
create index ix_crawl_permission_target_to__1 on CrawlPermission (target_id);

alter table CrawlPermission add constraint fk_crawl_permission_mailtempla_2 foreign key (mailtemplate_id) references MailTemplate (id);
create index ix_crawl_permission_mailtempla_2 on CrawlPermission (mailtemplate_id);

alter table CrawlPermission add constraint fk_crawl_permission_contactper_3 foreign key (contactperson_id) references ContactPerson (id);
create index ix_crawl_permission_contactper_3 on CrawlPermission (contactperson_id);

alter table Instance add constraint fk_instance_organisation_to_in_4 foreign key (organisation_id) references Organisation (id);
create index ix_instance_organisation_to_in_4 on Instance (organisation_id);

alter table Target add constraint fk_target_organisation_to_targ_5 foreign key (organisation_id) references Organisation (id);
create index ix_target_organisation_to_targ_5 on Target (organisation_id);

alter table Creator add constraint fk_creator_organisation_6 foreign key (organisation_id) references Organisation (id);
create index ix_creator_organisation_6 on Target (organisation_id);

alter table CollectionTarget add constraint fk_collection_target_dcollect_01 foreign key (collection_id) references Collection (id);
alter table CollectionTarget add constraint fk_collection_target_target_02 foreign key (target_id) references target (id);

alter table CollectionInstance add constraint fk_collection_instance_dcolle_01 foreign key (collection_id) references Collection (id);
alter table CollectionInstance add constraint fk_collection_instance_instan_02 foreign key (instance_id) references Instance (id);

alter table FlagTarget add constraint fk_flag_target_flag_01 foreign key (flag_id) references Flag (id);
alter table FlagTarget add constraint fk_flag_target_target_02 foreign key (target_id) references Target (id);

alter table FlagInstance add constraint fk_flag_instance_flag_01 foreign key (flag_id) references Flag (id);
alter table FlagInstance add constraint fk_flag_instance_instance_02 foreign key (instance_id) references Instance (id);

alter table PermissionRole add constraint fk_permission_role_permission_01 foreign key (permission_id) references permission (id);
alter table PermissionRole add constraint fk_permission_role_role_02 foreign key (role_id) references role (id);

alter table RoleUser add constraint fk_role_user_role_01 foreign key (role_id) references Role (id);
alter table RoleUser add constraint fk_role_user_creator_02 foreign key (user_id) references Creator (id);

alter table TagTarget add constraint fk_tag_target_tag_01 foreign key (tag_id) references Tag (id);
alter table TagTarget add constraint fk_tag_target_target_02 foreign key (target_id) references Target (id);

alter table TagInstance add constraint fk_tag_instance_tag_01 foreign key (tag_id) references Tag (ID);

alter table TagInstance add constraint fk_tag_instance_instance_02 foreign key (instance_id) references Instance (id);

alter table SubjectTarget add constraint fk_subject_target_taxonomy_01 foreign key (taxonomy_id) references Taxonomy (id);

alter table SubjectTarget add constraint fk_subject_target_target_02 foreign key (target_id) references Target (id);

alter table SubjectInstance add constraint fk_subject_instance_taxonomy_01 foreign key (taxonomy_id) references Taxonomy (id);

alter table SubjectTarget add constraint fk_subject_instance_instance_02 foreign key (instance_id) references Instance (id);

alter table LicenseTarget add constraint fk_license_target_taxonomy_01 foreign key (license_id) references Taxonomy (id);

alter table LicenseTarget add constraint fk_license_target_target_02 foreign key (target_id) references Target (id);

# --- !Downs

drop table if exists CommunicationLog cascade;

drop table if exists ContactPerson cascade;

drop table if exists CrawlPermission cascade;

drop table if exists Collection cascade;

drop table if exists CollectionTarget cascade;

drop table if exists CollectionInstance cascade;

drop table if exists Flag cascade;

drop table if exists FlagTarget cascade;

drop table if exists FlagInstance cascade;

drop table if exists Instance cascade;

drop table if exists SubjectInstance cascade;

drop table if exists TagInstance cascade;

drop table if exists LookupEntry cascade;

drop table if exists MailTemplate cascade;

drop table if exists Nomination cascade;

drop table if exists Organisation cascade;

drop table if exists Permission cascade;

drop table if exists PermissionRole cascade;

drop table if exists PermissionRefusal cascade;

drop table if exists Role cascade;

drop table if exists RoleUser cascade;

drop table if exists Tag cascade;

drop table if exists TagTarget cascade;

drop table if exists Target cascade;

drop table if exists SubjectTarget cascade;

drop table if exists Taxonomy cascade;

drop table if exists LicenseTarget cascade;

drop table if exists TaxonomyVocabulary cascade;

drop table if exists Creator cascade;


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

