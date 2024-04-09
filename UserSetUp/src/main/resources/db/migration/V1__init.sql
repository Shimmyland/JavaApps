create table user (
    id binary(16) primary key,
    email varchar(255) not null,
    name varchar(255) not null,
    password varchar(255) not null,
    token varchar(255) not null,
    username varchar(255) not null
);