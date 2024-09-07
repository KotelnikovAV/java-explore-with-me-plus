drop table if exists users, categories;

create table if not exists users
(
    id    bigint generated always as identity primary key,
    email varchar(255) not null unique,
    name  varchar(255) not null
);

create table if not exists categories
(
    id   bigint generated always as identity primary key,
    name varchar(100) not null
)
