CREATE TABLE categories (
  id bigint not null auto_increment,
  name varchar(100) not null,
  type varchar(10) check (type in ('income', 'expense')) not null,
  status tinyint,
  user_id bigint not null,

  primary key(id),
  constraint fk_categories_users_id foreign key(user_id) references users(id)
);
