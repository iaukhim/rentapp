CREATE TABLE users_roles(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON UPDATE CASCADE
                                               ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON UPDATE CASCADE
                                               ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);