CREATE table account(
    accountId varchar(255) PRIMARY KEY,
    password varchar(255) not null
);


CREATE TABLE url (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      accountId varchar(255) not null ,
                      url VARCHAR(255) NOT NULL,
                      shortened_url VARCHAR(100) NOT NULL UNIQUE ,
                      FOREIGN KEY (accountId) REFERENCES account(accountId)
);