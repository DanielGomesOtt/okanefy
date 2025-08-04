CREATE TABLE users (
    id bigint not null auto_increment,
    name varchar(100),
    email varchar(100) unique,
    password varchar(500),
    status tinyint,

    primary key(id)
);