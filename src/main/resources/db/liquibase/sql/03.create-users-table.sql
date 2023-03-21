CREATE TABLE users (
     id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
     email varchar(255) unique NOT NULL,
     password varchar(255) NOT NULL
);