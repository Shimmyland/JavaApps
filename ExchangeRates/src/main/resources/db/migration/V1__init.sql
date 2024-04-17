create table currency
(
    id     binary(16) primary key,
    code   varchar(255) not null,
    name   varchar(255) not null,
    symbol varchar(255) not null,
    type   varchar(255) not null
);

create table rate
(
    id               bigint primary key auto_increment,
    from_date        date     not null,
    saved_at         datetime not null,
    base_currency_id binary(16) not null,
    currency_id      binary(16) not null,
    price double not null,

    foreign key (base_currency_id) references currency (id),
    foreign key (currency_id) references currency (id)
);