DROP TABLE IF EXISTS daily_transaction;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS date;

CREATE TABLE account (
    id varchar(40) PRIMARY KEY,
    account_number varchar(40) NOT NULL
);

CREATE TABLE date (
    id varchar(40) PRIMARY KEY,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL,
    day INTEGER NOT NULL,
    date DATE NOT NULL
);


CREATE TABLE daily_transaction (
    id varchar(40) PRIMARY KEY,
    total_withdrawal_transactions INTEGER NOT NULL,
    total_deposit_transactions INTEGER NOT NULL,
    total_transaction_amount numeric (38, 2) NOT NULL,
    total_withdrawal_amount numeric (38, 2) NOT NULL,
    total_deposit_amount numeric (38, 2) NOT NULL,
    average_withdrawal_amount numeric (38, 2) NOT NULL,
    average_deposit_amount numeric (38, 2) NOT NULL,
    account_id varchar(40) NOT NULL,
    date_id varchar(40) NOT NULL,

    constraint fk_account_id foreign key (account_id) references account(id),
    constraint fk_date_id foreign key (date_id) references date(id)
);


INSERT INTO account (id, account_number) VALUES ('1', '1234567890');

INSERT INTO date (id, year, month, day, date) VALUES ('1', 2021, 1, 1, '2021-01-01');
INSERT INTO date (id, year, month, day, date) VALUES ('2', 2021, 1, 2, '2021-01-02');

INSERT INTO daily_transaction (id, total_withdrawal_transactions, total_deposit_transactions, total_transaction_amount, total_withdrawal_amount, total_deposit_amount, average_withdrawal_amount, average_deposit_amount, account_id, date_id) VALUES ('1', 1, 1, 100.00, 50.00, 50.00, 50.00, 50.00, '1', '1');
INSERT INTO daily_transaction (id, total_withdrawal_transactions, total_deposit_transactions, total_transaction_amount, total_withdrawal_amount, total_deposit_amount, average_withdrawal_amount, average_deposit_amount, account_id, date_id) VALUES ('2', 2, 2, 200.00, 100.00, 100.00, 50.00, 50.00, '1', '2');
