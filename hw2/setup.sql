create database if not exists payment_service;
create database if not exists restaurant_service;
create user 'springuser'@'172.17.0.1' identified by 'ThePassword';
create user 'springuser'@'localhost' identified by 'ThePassword';
grant all on payment_service.* to 'springuser'@'172.17.0.1';
grant all on payment_service.* to 'springuser'@'localhost';
grant all on restaurant_service.* to 'springuser'@'172.17.0.1';
grant all on restaurant_service.* to 'springuser'@'localhost';
flush privileges;