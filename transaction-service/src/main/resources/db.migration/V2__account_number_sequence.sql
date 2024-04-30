ALTER TABLE accounts
    ALTER COLUMN account_number SET DATA TYPE numeric(38, 0);
CREATE SEQUENCE account_number_sequence START 1;