CREATE DATABASE youtube;

USE youtube;

CREATE TABLE IF NOT EXISTS record (
    id INT AUTO_INCREMENT PRIMARY KEY, title VARCHAR(255), channel VARCHAR(255), description VARCHAR(255), videoId VARCHAR(255), imageUrl VARCHAR(255), publishTime VARCHAR(255)
);

DROP TABLE record;

INSERT INTO record (title, channel, description, videoId, imageUrl, publishTime) VALUES  ("mi casa", "elxokas", "ñaljdfsajfdñjasfd", "32323dsfs", "lasñkdjfñas", "ldfjaljdfl");