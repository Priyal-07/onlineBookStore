CREATE TABLE users (
username VARCHAR(255) PRIMARY KEY,
password VARCHAR(255),
email VARCHAR(255),
userRole VARCHAR(255)
);

CREATE TABLE books (
title VARCHAR(255),
author VARCHAR(255),
isbn VARCHAR(255) PRIMARY KEY,
price FLOAT,
description VARCHAR(2555), 
summary VARCHAR(10000000)
);

CREATE TABLE cart (
title VARCHAR(255),
author VARCHAR(255),
isbn VARCHAR(255),
price FLOAT
);
