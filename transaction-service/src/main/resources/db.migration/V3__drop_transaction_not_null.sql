ALTER TABLE transactions
    ALTER COLUMN deposited_amount drop not null;
ALTER TABLE transactions
    ALTER COLUMN conversion_rate drop not null;
ALTER TABLE transactions
    ALTER COLUMN note drop not null;
ALTER TABLE transactions
    ALTER COLUMN variable_symbol drop not null;