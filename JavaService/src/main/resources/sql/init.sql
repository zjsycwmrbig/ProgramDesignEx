create database customer_service_system;
use customer_service_system;

drop table if exists dependency_map;
create table dependency_map(
    id int primary key ,
    name varchar(25) not null,
    description varchar(255) ,
    path varchar(255) not null,
    code varchar(255) not null,
    default_state int not null
)
# 转移边