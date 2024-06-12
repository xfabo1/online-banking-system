CREATE TABLE scheduled_payment
(
    id                varchar(40) PRIMARY KEY,
    amount            numeric(38, 2) NOT NULL,
    day_of_week       int,
    day_of_month      int,
    day_of_year       int,
    valid_until       timestamp,
    withdraws_from_id varchar(40)    not null,
    deposits_to_id    varchar(40)    not null,

    constraint scheduled_payment_withdraws_from_FK foreign key (withdraws_from_id) references accounts (id),
    constraint scheduled_payment_deposits_to_FK foreign key (deposits_to_id) references accounts (id)
);

CREATE INDEX day_of_week_IDX on scheduled_payment (day_of_week);
CREATE INDEX day_of_month_IDX on scheduled_payment (day_of_month);
CREATE INDEX day_of_year_IDX on scheduled_payment (day_of_year);
CREATE INDEX withdraws_from_id_IDX on scheduled_payment (withdraws_from_id);
CREATE INDEX deposits_to_IDX on scheduled_payment (deposits_to_id);

ALTER TABLE transactions
    ADD COLUMN transaction_state varchar(255) NOT NULL;