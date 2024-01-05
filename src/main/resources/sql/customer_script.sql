drop table ricomart.customer;

create table ricomart.customer(
customer_id SERIAL primary key,
first_name varchar(40) not null,
last_name  varchar(40) not null,
email varchar(60) not null,
phone varchar(20) not null,
address varchar(70) not null,
user_name varchar(30) not null,
passkey varchar(100) not null
);