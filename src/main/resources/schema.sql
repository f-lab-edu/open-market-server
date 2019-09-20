create table account (
    id bigint not null,
    city varchar(255),
    street varchar(255),
    zip_code varchar(255),
    created_at timestamp,
    email varchar(255),
    modified_at timestamp,
    nickname varchar(255),
    password varchar(255),
    phone varchar(255),
    status varchar(255),
    primary key (id));

create table account_account_role (
    account_id bigint not null,
     account_role varchar(255));
