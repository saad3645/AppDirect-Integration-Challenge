# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table companies (
  uuid                      varchar(255) not null,
  email                     varchar(255),
  name                      varchar(255),
  phone_number              varchar(255),
  website                   varchar(255),
  country                   varchar(255),
  constraint pk_companies primary key (uuid))
;

create table subscriptions (
  id                        varchar(255) not null,
  creator_uuid              varchar(255),
  company_uuid              varchar(255),
  edition                   varchar(255),
  status                    varchar(255),
  constraint pk_subscriptions primary key (id))
;

create table subscription_items (
  id                        bigint not null,
  subscription_id           varchar(255) not null,
  unit                      varchar(255),
  quantity                  integer,
  constraint pk_subscription_items primary key (id))
;

create table users (
  uuid                      varchar(255) not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  open_id                   varchar(255),
  constraint pk_users primary key (uuid))
;

create table user_attributes (
  id                        bigint not null,
  user_uuid                 varchar(255) not null,
  key                       varchar(255) not null,
  value                     varchar(255) not null,
  constraint pk_user_attributes primary key (id))
;

create sequence companies_seq;

create sequence subscriptions_seq;

create sequence subscription_items_seq;

create sequence users_seq;

create sequence user_attributes_seq;

alter table subscriptions add constraint fk_subscriptions_creator_1 foreign key (creator_uuid) references users (uuid) on delete restrict on update restrict;
create index ix_subscriptions_creator_1 on subscriptions (creator_uuid);
alter table subscriptions add constraint fk_subscriptions_company_2 foreign key (company_uuid) references companies (uuid) on delete restrict on update restrict;
create index ix_subscriptions_company_2 on subscriptions (company_uuid);
alter table subscription_items add constraint fk_subscription_items_subscrip_3 foreign key (subscription_id) references subscriptions (id) on delete restrict on update restrict;
create index ix_subscription_items_subscrip_3 on subscription_items (subscription_id);
alter table user_attributes add constraint fk_user_attributes_user_4 foreign key (user_uuid) references users (uuid) on delete restrict on update restrict;
create index ix_user_attributes_user_4 on user_attributes (user_uuid);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists companies;

drop table if exists subscriptions;

drop table if exists subscription_items;

drop table if exists users;

drop table if exists user_attributes;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists companies_seq;

drop sequence if exists subscriptions_seq;

drop sequence if exists subscription_items_seq;

drop sequence if exists users_seq;

drop sequence if exists user_attributes_seq;

