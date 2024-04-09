create table weather (
    id binary(16) primary key,
    create_at timestamp not null,
    city varchar(255) not null,
    country varchar(255) not null,
    temperature double not null,
    weather varchar(255) not null
);
