--liquibase formatted sql

--changeset Enzhine:1.0.0:1
create table user_auth (
    id bigserial primary key,
    login varchar(32) not null unique,
    password char(64) not null,
    banned boolean not null default false,
    verified boolean not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table user_profile (
    username varchar(32) not null,
    email varchar(64) not null unique,
    user_auth_id bigint unique references user_auth (id),
    updated_at timestamptz not null default now()
);

create table token (
    content varchar not null,
    expires_at timestamptz not null,
    user_auth_id bigint unique references user_auth (id)
);

--rollback drop table if exists user_auth;
--rollback drop table if exists user_profile;
--rollback drop table if exists token;