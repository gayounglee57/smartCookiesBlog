DROP TABLE IF EXISTS cookies_comments;
DROP TABLE IF EXISTS cookies_articles;
DROP TABLE IF EXISTS cookies_users;

CREATE table cookies_users (
  uname VARCHAR(40) NOT NULL UNIQUE,
  fname VARCHAR(100),
  lname VARCHAR(100),
  email VARCHAR(255),
  dob DATE,
  country VARCHAR(50),
  description TEXT,
  avatar VARCHAR(100),
  PRIMARY KEY (uname)
);

CREATE table cookies_articles (
  articleID INT NOT NULL AUTO_INCREMENT,
  uname VARCHAR(40),
  date DATE,
  image VARCHAR(500),
  PRIMARY KEY (articleID),
  FOREIGN KEY (uname) REFERENCES cookies_users(uname)
);

CREATE TABLE cookies_comments (
  commentID INT AUTO_INCREMENT NOT NULL,
  articleID INT,
  uname VARCHAR(40),
  content VARCHAR(100) NOT NULL,
  date DATE,
  PRIMARY KEY (commentID),
  FOREIGN KEY (articleID) REFERENCES cookies_articles (articleID),
  FOREIGN KEY (uname) REFERENCES cookies_users (uname)
);
INSERT INTO cookies_users VALUES ('mmiz', 'Miki', 'Miz', 'mmiz318@aucklanduni.ac.nz', '1995-06-01', 'NZ', 'hello', NULL );