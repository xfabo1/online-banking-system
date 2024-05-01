ALTER TABLE accounts
    ALTER COLUMN account_number SET DATA TYPE numeric(38, 0);
CREATE SEQUENCE "account_number_sequence"
    MINVALUE 1
    MAXVALUE 999999999
    INCREMENT BY 1
    START WITH 1;
