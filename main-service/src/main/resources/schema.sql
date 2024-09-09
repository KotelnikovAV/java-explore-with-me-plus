drop table if exists users, categories, location;

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
);

create table if not exists location
(
    id   bigint generated always as identity primary key,
    lat  float not null,
    lon  float not null
);

create table if not exists events
(
    id                  bigint generated always as identity primary key,
    annotation          varchar,
    category_id         INTEGER REFERENCES categories (id) ON DELETE CASCADE ON UPDATE CASCADE,
    createdOn           TIMESTAMP NOT NULL,
    description         varchar NOT NULL,
    eventDate           TIMESTAMP NOT NULL,
    initiator_id        INTEGER REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    location_id         INTEGER REFERENCES location (id) ON DELETE CASCADE ON UPDATE CASCADE,
    paid                BOOLEAN NOT NULL,
    participantLimit    INTEGER NOT NULL,
    publishedOn         TIMESTAMP,
    requestModeration   BOOLEAN NOT NULL,
    state               varchar NOT NULL,
    title               varchar NOT NULL,
    confirmed_requests  INTEGER
);

create table if not exists requests
(
    id              bigint generated always as identity primary key,
    created         TIMESTAMP not null,
    event_id        INTEGER REFERENCES events (id) ON DELETE CASCADE ON UPDATE CASCADE,
    requester_id    INTEGER REFERENCES users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    status          varchar(20) NOT NULL
)
