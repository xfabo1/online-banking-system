CREATE TABLE currency_dim
(
    id      varchar(40) PRIMARY KEY,
    symbol  varchar(10) NOT NULL UNIQUE,
);

ALTER TABLE daily_transaction
    ADD COLUMN currency_id varchar(40) NOT NULL;

ALTER TABLE daily_transaction
    ADD CONSTRAINT fk_currency_id FOREIGN KEY (currency_id) REFERENCES currency_dim (id);

CREATE INDEX currency_id_index ON daily_transaction (currency_id);