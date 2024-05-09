DROP TABLE Portfolio;
DROP TABLE Users;

CREATE TABLE Users (
    email VARCHAR(255) NOT NULL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    balance DECIMAL(10, 2) DEFAULT 50000.00  -- Default balance set to 50000
);


CREATE TABLE Portfolio (
    trade_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    ticker VARCHAR(10),
    numStock INT,
    price DECIMAL(10, 2),
    FOREIGN KEY (email) REFERENCES Users(email)
);
