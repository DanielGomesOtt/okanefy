CREATE TABLE recovery_code (
    id bigint not null auto_increment,
    email varchar(100),
    code varchar(200),
    expiration_date DATETIME,

    primary key(id)
);