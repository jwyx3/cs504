create database if not exists searchads;
create user 'user'@'172.17.0.1' identified by 'user2017';
create user 'user'@'localhost' identified by 'user2017';
grant all on searchads.* to 'user'@'172.17.0.1';
grant all on searchads.* to 'user'@'localhost';
create table if not exists searchads.ad (
    adId int not null primary key,
    campaignId int not null,
    keywords text,
    bidPrice double default 0.0,
    price double not null,
    thumbnail varchar(255) not null,
    description text,
    brand varchar(255) not null,
    detailUrl varchar(255) not null,
    `category` varchar(255),
    title varchar(255) not null
) ENGINE=InnoDB;
create table if not exists searchads.campaign (
    campaignId int not null primary key,
    budget double default 0.0
) ENGINE=InnoDB;
flush privileges;