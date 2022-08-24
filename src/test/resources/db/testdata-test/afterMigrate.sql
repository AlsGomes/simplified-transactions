set foreign_key_checks = 0;

truncate table user;
truncate table common_user;
truncate table shopkeeper;
truncate table wallet;
truncate table `transaction`;

set foreign_key_checks = 1;

insert into user(id, fullname, email, phone_number, pwd) values (1, 'MASTER_COMMON_USER', 'mastercommonuser@gmail.com', '+5511999999999', 'master_password');
insert into common_user(user_id, cpf) values (1, '99999999999');
insert into wallet(id, balance) values (1, 1000);

insert into user(id, fullname, email, phone_number, pwd) values (2, 'MASTER_SHOPKEEPER', 'mastershopkeeper@gmail.com', '+5511988888888', 'master_password');
insert into shopkeeper(user_id, cnpj) values (2, '88888888888888');
insert into wallet(id, balance) values (2, 1000);

insert into `transaction` (id, payer_id, payee_id, amount, date) values (1, 1, 2, 32.40, utc_timestamp);
insert into `transaction` (id, payer_id, payee_id, amount, date) values (2, 1, 2, 40.50, utc_timestamp);
