INSERT INTO account (id, account_number)
VALUES ('1', '1234567890');

INSERT INTO date (id, year, month, day, date)
VALUES ('1', 2021, 1, 1, '2021-01-01');
INSERT INTO date (id, year, month, day, date)
VALUES ('2', 2021, 1, 2, '2021-01-02');

INSERT INTO daily_transaction (id, total_withdrawal_transactions, total_deposit_transactions, total_transaction_amount,
                               total_withdrawal_amount, total_deposit_amount, average_withdrawal_amount,
                               average_deposit_amount, account_id, date_id)
VALUES ('1', 1, 1, 100.00, 50.00, 50.00, 50.00, 50.00, '1', '1');
INSERT INTO daily_transaction (id, total_withdrawal_transactions, total_deposit_transactions, total_transaction_amount,
                               total_withdrawal_amount, total_deposit_amount, average_withdrawal_amount,
                               average_deposit_amount, account_id, date_id)
VALUES ('2', 2, 2, 200.00, 100.00, 100.00, 50.00, 50.00, '1', '2');
