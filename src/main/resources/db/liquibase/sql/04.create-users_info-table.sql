CREATE TABLE users_info(
     id int,
     address_id int,
     phone_number varchar(20) unique,
     firstname varchar(255),
     lastname varchar(255),
     status boolean NOT NULL,
     joindate date NOT NULL,
     FOREIGN KEY (address_id) REFERENCES addresses(id) ON UPDATE CASCADE
                                                       ON DELETE NO ACTION,
     FOREIGN KEY (id) REFERENCES users(id) ON UPDATE CASCADE
                                           ON DELETE CASCADE,
     PRIMARY KEY (id)
     )