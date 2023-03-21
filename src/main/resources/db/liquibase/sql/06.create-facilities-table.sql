CREATE TABLE facilities (
     id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
     name varchar(255) NOT NULL,
     address_id int NOT NULL,
     space double NOT NULL,
     owner_id int NOT NULL,
     FOREIGN KEY (owner_id) REFERENCES users (id) ON UPDATE CASCADE
                                                  ON DELETE CASCADE,
     FOREIGN KEY (address_id)  REFERENCES addresses (id) ON UPDATE CASCADE
                                                       ON DELETE RESTRICT
);


CREATE INDEX `space_idx` ON `facilities` (`space`);