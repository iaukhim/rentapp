CREATE TABLE `orders` (
id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
facility_id int NOT NULL,
renter_id int NOT NULL,
planned_date datetime NOT NULL,
creation_date datetime NOT NULL,
duration_in_days int NOT NULL DEFAULT 1,
FOREIGN KEY (renter_id) REFERENCES users(id) ON UPDATE CASCADE,
FOREIGN KEY (facility_id) REFERENCES facilities(id) ON UPDATE CASCADE
                                                    ON DELETE CASCADE
);


CREATE INDEX `planned_date_idx` ON `orders` (planned_date);

CREATE INDEX `duration_in_days_idx` ON `orders` (duration_in_days);