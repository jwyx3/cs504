create database if not exists db_example;
create user 'springuser'@'172.17.0.1' identified by 'ThePassword';
create user 'springuser'@'localhost' identified by 'ThePassword';
grant all on db_example.* to 'springuser'@'172.17.0.1';
grant all on db_example.* to 'springuser'@'localhost';
flush privileges;
