CREATE DATABASE youtube;

USE youtube;

CREATE TABLE IF NOT EXISTS record (
    id INT AUTO_INCREMENT PRIMARY KEY, thumbnail VARCHAR(255), title VARCHAR(255), channel VARCHAR(255), link VARCHAR(255), time_stamp VARCHAR(255)
);

DROP TABLE record;

INSERT INTO
    record (
        title, channel, description, videoId, imageUrl, publishTime
    )
VALUES (
        "mi casa", "elxokas", "ñaljdfsajfdñjasfd", "32323dsfs", "lasñkdjfñas", "ldfjaljdfl"
    );