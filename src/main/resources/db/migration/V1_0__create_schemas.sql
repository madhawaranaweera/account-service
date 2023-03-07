create sequence account_id_seq as integer start with 111156711 increment by 1;

create table account
(
   id int
        constraint account_pkey
            primary key,
   account_number varchar(9) not null unique,
   account_name varchar(60) not null,
   account_type varchar(16) not null,
   balance_date date not null,
   currency varchar(3) not null,
   opening_available_balance decimal(20,4),
   user_id varchar(16) not null
);

create sequence transaction_id_seq as integer start with 1 increment by 1;

create table transactions
(
   id int
        constraint transaction_pkey
            primary key,
   account_id int not null,
   value_date date not null,
   currency varchar(3) not null,
   amount decimal(20,4) not null,
   cr_dr_indicator varchar(6) not null,
   transaction_narrative varchar(60),
   foreign key (account_id) references account(id)
)