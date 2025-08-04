CREATE TABLE categories (
  id bigint not null auto_increment,
  name varchar(100) not null,
  type varchar(10) check (type in ('income', 'expense')) not null,
  status tinyint,

  primary key(id)
);
