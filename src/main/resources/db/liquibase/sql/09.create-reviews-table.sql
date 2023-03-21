CREATE TABLE `reviews`(
id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
facility_id int NOT NULL,
renter_id int NOT NULL,
order_id int NOT NULL,
text_review varchar (3000) NOT NULL,
rating ENUM ('1', '2', '3', '4', '5'),
FOREIGN KEY (renter_id) REFERENCES users(id) ON UPDATE CASCADE,
FOREIGN KEY (facility_id) REFERENCES facilities(id) ON UPDATE CASCADE
                                                    ON DELETE CASCADE,
FOREIGN KEY (order_id) REFERENCES orders(id) ON UPDATE CASCADE
                                                    ON DELETE RESTRICT
);

CREATE INDEX `rating_idx` ON `reviews` (rating);
