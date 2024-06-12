INSERT INTO accounts(id, customer_id, currency_code)
VALUES ('1', 'customer-1', 'CZK'),
       ('2', 'customer-2', 'EUR'),
       ('3', 'customer-3', 'USD'),
       ('4', 'customer-4', 'CZK'),
       ('5', 'customer-5', 'EUR'),
       ('6', 'customer-6', 'EUR');


INSERT INTO transactions(id, conversion_rate, withdraws_from, deposits_to, withdrawn_amount, deposited_amount, note,
                         variable_symbol, transaction_state)
VALUES ('1', 0.04, '1', '2', 1000, 40, 'note-1', '00', 'SUCCESSFUL'),
       ('2', 25.0, '3', '1', 100, 2500, 'note-2', '11', 'SUCCESSFUL'),
       ('3', 25.0, '2', '4', 50, 1250, 'note-3', '22', 'SUCCESSFUL'),
       ('4', 1.0, '5', '3', 100, 100, 'note-4', '33', 'SUCCESSFUL'),
       ('5', 0.04, '4', '2', 200, 8, 'note-5', '44', 'SUCCESSFUL'),
       ('6', 1.0, '3', '5', 1200, 1200, 'note-6', '55', 'SUCCESSFUL'),
       ('7', 1.0, '1', '4', 700, 700, 'note-7', '66', 'SUCCESSFUL'),
       ('8', 1.0, '2', '3', 150, 150, 'note-8', '77', 'SUCCESSFUL'),
       ('9', 25, '5', '1', 400, 10000, 'note-9', '88', 'SUCCESSFUL');

