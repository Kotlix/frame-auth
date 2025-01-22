--liquibase formatted sql

--changeset Enzhine:1.0.0:2
create table confirm_user_verify (
    id bigserial primary key,
    secret varchar(32) not null,
    created_at timestamptz not null default now(),
    expires_at timestamptz not null,
    confirmed boolean not null default false,
    user_id bigint not null
);

create table confirm_profile_username (
    id bigserial primary key,
    secret varchar(32) not null,
    created_at timestamptz not null default now(),
    expires_at timestamptz not null,
    confirmed boolean not null default false,
    user_id bigint not null,
    new_username varchar(32) not null
);

create table confirm_profile_email (
    id bigserial primary key,
    secret varchar(32) not null,
    created_at timestamptz not null default now(),
    expires_at timestamptz not null,
    confirmed boolean not null default false,
    user_id bigint not null,
    new_email varchar(64) not null unique
);

create table confirm_user_password (
    id bigserial primary key,
    secret varchar(32) not null,
    created_at timestamptz not null default now(),
    expires_at timestamptz not null,
    confirmed boolean not null default false,
    user_id bigint not null,
    new_password char(64) not null
);

--rollback drop table if exists confirm_user_verify;
--rollback drop table if exists confirm_profile_username;
--rollback drop table if exists confirm_profile_email;
--rollback drop table if exists confirm_user_password;