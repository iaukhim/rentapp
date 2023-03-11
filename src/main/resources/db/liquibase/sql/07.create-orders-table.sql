CREATE TABLE `orders` (
id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
facility_id int NOT NULL,
renter_id int NOT NULL,
planned_date datetime NOT NULL,
creation_date datetime NOT NULL,
FOREIGN KEY (renter_id) REFERENCES users(id) ON UPDATE CASCADE,
FOREIGN KEY (facility_id) REFERENCES facilities(id) ON UPDATE CASCADE
                                                    ON DELETE CASCADE
);