CREATE TABLE transactions (
  id bigint not null auto_increment,
  initial_date date,
  end_date date,
  amount decimal(12, 2),
  description varchar(255),
  number_installments int,
  frequency varchar(100),
  status tinyint,
  user_id bigint,
  category_id bigint,


  primary key(id),
  constraint fk_transactions_users_id foreign key(user_id) references users(id),
  constraint fk_transactions_category_id foreign key(category_id) references categories(id)
);