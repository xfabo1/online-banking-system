DELETE
FROM account_dim ac
where ac.id = ac.id;

DELETE
FROM daily_transaction dt
where dt.id = dt.id;

DELETE
FROM date_dim d
where d.id = d.id;
