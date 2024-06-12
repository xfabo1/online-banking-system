alter table transactions
    add column transaction_time timestamp not null default now();