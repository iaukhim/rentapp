CREATE TABLE `countries`(
    id INT PRIMARY KEY AUTO_INCREMENT,
    code varchar(10) NOT NULL,
    name varchar(255) NOT NULL
);

ALTER TABLE `countries`
ADD CONSTRAINT UNIQUE_CODE
UNIQUE (code);

ALTER TABLE `countries`
ADD CONSTRAINT UNIQUE_NAME
UNIQUE (name);

CREATE TABLE `cities`(
    id INT PRIMARY KEY  AUTO_INCREMENT,
    country_id int NOT NULL,
    name varchar(255) NOT NULL
);

ALTER TABLE `cities`
ADD
FOREIGN KEY (country_id) REFERENCES countries(id) ON UPDATE CASCADE
                                                  ON DELETE RESTRICT;

ALTER TABLE `cities`
ADD CONSTRAINT UNIQUE_CITY_IN_COUNTRY
UNIQUE (country_id, name);

CREATE TABLE `addresses`(
     id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
     city_id int NOT NULL,
     district varchar(255),
     street_name varchar(255),
     street_number varchar(255) NOT NULL,
     FOREIGN KEY (city_id) REFERENCES cities(id) ON UPDATE CASCADE
                                                 ON DELETE RESTRICT
);