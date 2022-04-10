CREATE TABLE IF NOT EXISTS address
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    latitude  DECIMAL(15, 7) NOT NULL,
    longitude DECIMAL(15, 7) NOT NULL
);

CREATE TABLE IF NOT EXISTS sport_type
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS location_type
(
    id    INT AUTO_INCREMENT PRIMARY KEY,
    name  VARCHAR(32) NOT NULL,
    place VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS location
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    name             VARCHAR(128) NOT NULL,
    address_id       INT UNIQUE,
    location_type_id INT          NOT NULL,
    web_site         VARCHAR(64),
    phone            VARCHAR(15) UNIQUE,
    admin_id         INT,

    CONSTRAINT fk_location_address
        FOREIGN KEY (address_id)
            REFERENCES address (id),

    CONSTRAINT fk_location_location_type
        FOREIGN KEY (location_type_id)
            REFERENCES location_type (id)
);

CREATE TABLE IF NOT EXISTS location_schedule
(
    id                 INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,
    day                VARCHAR(16) NOT NULL,
    start_working_time TIME        NOT NULL,
    end_working_time   TIME        NOT NULL,
    location_id        INT         NOT NULL,
    CONSTRAINT fk_location_schedule_location
        FOREIGN KEY (location_id)
            REFERENCES location (id)
);

CREATE TABLE IF NOT EXISTS coach
(
    id                 INT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
    rating             DOUBLE  NOT NULL,
    work_with_children BOOLEAN NOT NULL,
    location_id        INT     NOT NULL,
    user_id            INT     NOT NULL,

    CONSTRAINT fk_coach_location
        FOREIGN KEY (location_id)
            REFERENCES location (id)
);

CREATE TABLE IF NOT EXISTS comment
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    id_user       INT           NOT NULL,
    id_coach      INT           NOT NULL,
    creation_date TIMESTAMP     NOT NULL,
    comment       VARCHAR(2048) NOT NULL,
    rating        INT           NOT NULL,

    CONSTRAINT fk_comments_coach
        FOREIGN KEY (id_coach)
            REFERENCES coach (id)
);

ALTER TABLE comment
    ADD UNIQUE (id_user, id_coach);

CREATE TABLE IF NOT EXISTS location_sport_type
(
    location_id   INT NOT NULL,
    sport_type_id INT NOT NULL,
    PRIMARY KEY (location_id, sport_type_id),
    CONSTRAINT fk_location_sport_type_location
        FOREIGN KEY (location_id)
            REFERENCES location (id),
    CONSTRAINT fk_location_sport_type_sport_type
        FOREIGN KEY (sport_type_id)
            REFERENCES sport_type (id)
);

CREATE TABLE IF NOT EXISTS coach_sport_type
(
    coach_id      INT NOT NULL,
    sport_type_id INT NOT NULL,
    PRIMARY KEY (coach_id, sport_type_id),
    CONSTRAINT fk_coach_sport_type_coach
        FOREIGN KEY (coach_id)
            REFERENCES coach (id),
    CONSTRAINT fk_coach_sport_type_sport_type
        FOREIGN KEY (sport_type_id)
            REFERENCES sport_type (id)
);

CREATE TABLE IF NOT EXISTS photo
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    url         VARCHAR(2048),
    location_id INT,

    CONSTRAINT fk_photo_location
        FOREIGN KEY (location_id)
            REFERENCES location (id)
);

CREATE TABLE IF NOT EXISTS link
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    url      VARCHAR(2048),
    type     VARCHAR(20),
    coach_id INT,

    CONSTRAINT fk_link_coach
        FOREIGN KEY (coach_id)
            REFERENCES coach (id)
);

