CREATE TABLE transactions (
    id varchar(40) PRIMARY KEY,
    conversion_rate DECIMAL(10,3) NOT NULL,
    withdraws_from VARCHAR(40) NOT NULL,
    deposits_to VARCHAR(40) NOT NULL,
    withdrawn_amount DECIMAL(10,3) NOT NULL,
    deposited_amount DECIMAL(10,3) NOT NULL,
    description VARCHAR(255) NOT NULL,
    variable_symbol VARCHAR(10) NOT NULL,
    state VARCHAR(10) NOT NULL,
    constraint fk_account_transaction_withdraws_from foreign key (withdraws_from) references accounts(id),
    constraint fk_account_transaction_deposits_to foreign key (deposits_to) references accounts(id)
);

CREATE TABLE accounts (
    id VARCHAR(40) PRIMARY KEY,
    customer_id VARCHAR(40) NOT NULL,
    currency_code VARCHAR(10) NOT NULL,
    account_number VARCHAR(40) NOT NULL,
    constraint customer_id_unique unique (customer_id),
    constraint account_number_unique unique (account_number)
);
