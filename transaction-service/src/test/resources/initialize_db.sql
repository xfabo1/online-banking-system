INSERT INTO accounts(id, customer_id, currency_code, account_number)
VALUES ('1', 'customer-1', 'CZK', 'account-1'),
       ('2', 'customer-2', 'EUR', 'account-2'),
       ('3', 'customer-3', 'USD', 'account-3'),
       ('4', 'customer-4', 'CZK', 'account-4'),
       ('5', 'customer-5', 'EUR', 'account-5'),
       ('6', 'customer-6', 'EUR', 'account-6');


INSERT INTO transactions(id, conversion_rate, withdraws_from, deposits_to, withdrawn_amount, deposited_amount, note,
                         variable_symbol, transaction_time, transaction_state)
VALUES ('1', 0.04, '1', '2', 1000, 40, 'note-1', '00',now(),  'SUCCESSFUL'),
       ('2', 25.0, '3', '1', 100, 2500, 'note-2', '11',now(),  'SUCCESSFUL'),
       ('3', 25.0, '2', '4', 50, 1250, 'note-3', '22',now(), 'SUCCESSFUL'),
       ('4', 1.0, '5', '3', 100, 100, 'note-4', '33',now(), 'SUCCESSFUL'),
       ('5', 0.04, '4', '2', 200, 8, 'note-5', '44',now(), 'SUCCESSFUL'),
       ('6', 1.0, '3', '5', 1200, 1200, 'note-6', '55',now(), 'SUCCESSFUL'),
       ('7', 1.0, '1', '4', 700, 700, 'note-7', '66',now(), 'SUCCESSFUL'),
       ('8', 1.0, '2', '3', 150, 150, 'note-8', '77',now(), 'SUCCESSFUL'),
       ('9', 25, '5', '1', 400, 10000, 'note-9', '88',now(), 'SUCCESSFUL');

