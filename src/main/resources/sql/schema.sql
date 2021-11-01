create schema if not exists `simple-shop`;
use `simple-shop`;

create table Product
(
    id bigint unsigned auto_increment
        primary key,
    name varchar(35) not null,
    createdAt timestamp not null,
    status enum('AVAILABLE', 'ON_SALE', 'NOT_AVAILABLE') default 'AVAILABLE' not null
);

create unique index Product_name_uindex
    on Product (name);

create table Subscriber
(
    id bigint unsigned auto_increment
        primary key,
    firstName varchar(35) not null,
    lastName varchar(35) not null,
    createdAt timestamp not null
);

create table SubscriberProduct
(
    id bigint unsigned auto_increment
        primary key,
    subscriberId bigint not null,
    productId bigint not null,
    createdAt timestamp not null
);




