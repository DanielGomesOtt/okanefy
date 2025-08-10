CREATE TABLE payment_method (
  id bigint not null auto_increment,
  name varchar(100) not null,
  is_installment boolean not null default false,
  status tinyint,
  user_id bigint not null,

  primary key(id),
  constraint fk_payment_method_users_id foreign key(user_id) references users(id)
);