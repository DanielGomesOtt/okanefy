CREATE TABLE transactions_payment_method (
  id bigint not null auto_increment,
  transaction_id bigint,
  payment_method_id bigint,
  status tinyint default 1,


  primary key(id),
  constraint fk_transactions_payment_method_id foreign key(payment_method_id) references payment_method(id),
  constraint fk_transactions_payment_method_transaction_id foreign key(transaction_id) references transactions(id)
);