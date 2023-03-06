create table account
(
   account_number varchar(30) not null
        constraint account_pkey
           primary key,
   account_name varchar(60) not null,
   account_type varchar(16) not null,
   balance_date date not null,
   currency varchar(3) not null,
   opening_available_balance decimal(20,4),
   user_id varchar(16) not null
);

create sequence transaction_id_seq as integer start with 1 increment by 1;

create table account_transaction
(
   id int
        constraint transaction_pkey
            primary key,
   account_number varchar(30) not null,
   value_date date not null,
   currency varchar(3) not null,
   amount decimal(20,4) not null,
   cr_dr_indicator varchar(6) not null,
   transaction_narrative varchar(60),
   foreign key (account_number) references account(account_number)
)