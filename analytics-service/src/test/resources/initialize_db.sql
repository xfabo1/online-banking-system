INSERT INTO account_dim (id, account_number)
VALUES ('1', '1234567890');

INSERT INTO date_dim (id, year_number, month_number, day_number, full_date)
VALUES ('1', 2021, 1, 1, '2021-01-01');
INSERT INTO date_dim (id, year_number, month_number, day_number, full_date)
VALUES ('2', 2021, 1, 2, '2021-01-02');
INSERT INTO date_dim (id, year_number, month_number, day_number, full_date)
VALUES ('3', 2021, 1, 3, '2021-01-03');

INSERT INTO daily_transaction (id, total_withdrawal_transactions, total_deposit_transactions, total_transaction_amount,
                               total_withdrawal_amount, total_deposit_amount, average_withdrawal_amount,
                               average_deposit_amount, account_id, date_id)
VALUES ('1', 5, 5, 30000, 20000, 10000, 4000, 2000, '1', '1');
INSERT INTO daily_transaction (id, total_withdrawal_transactions, total_deposit_transactions, total_transaction_amount,
                               total_withdrawal_amount, total_deposit_amount, average_withdrawal_amount,
                               average_deposit_amount, account_id, date_id)
VALUES ('2', 10, 10, 40000, 20000, 20000, 2000, 2000, '1', '2');

INSERT INTO daily_transaction (id, total_withdrawal_transactions, total_deposit_transactions, total_transaction_amount,
                               total_withdrawal_amount, total_deposit_amount, average_withdrawal_amount,
                               average_deposit_amount, account_id, date_id)
VALUES ('3', 4, 2, 10000, 8000, 2000, 2000, 1000, '1', '3');

