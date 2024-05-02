CREATE TABLE accounts
(
    id             VARCHAR(40) PRIMARY KEY,
    customer_id    VARCHAR(40) NOT NULL,
    currency_code  VARCHAR(10) NOT NULL
);

CREATE INDEX customer_id_index ON accounts(customer_id);

CREATE TABLE transactions
(
    id               varchar(40) PRIMARY KEY,
    conversion_rate  FLOAT(53)      NOT NULL,
    withdraws_from   VARCHAR(40)    NOT NULL,
    deposits_to      VARCHAR(40)    NOT NULL,
    withdrawn_amount numeric(38, 2) NOT NULL,
    deposited_amount numeric(38, 2) NOT NULL,
    note             VARCHAR(255)   NOT NULL,
    variable_symbol  VARCHAR(10)    NOT NULL,
    constraint fk_account_transaction_withdraws_from foreign key (withdraws_from) references accounts (id),
    constraint fk_account_transaction_deposits_to foreign key (deposits_to) references accounts (id)
);

CREATE INDEX withdraws_from_index ON transactions(withdraws_from);
CREATE INDEX deposits_to_index ON transactions(deposits_to);
