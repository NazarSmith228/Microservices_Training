DROP TABLE IF EXISTS user_role, role, criteria, article_tag, tag, article, sport_type, address, users, event, event_user;

CREATE TABLE IF NOT EXISTS address
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    latitude  DECIMAL(15, 6),
    longitude DECIMAL(15, 6)
);

CREATE TABLE IF NOT EXISTS sport_type
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(20),
    surname       VARCHAR(20),
    password      VARCHAR(256),
    email         VARCHAR(30) UNIQUE,
    phone         VARCHAR(11) UNIQUE,
    photo         VARCHAR(2048),
    date_of_birth DATE,
    address_id    INT,
    gender        VARCHAR(32),
    has_child     BOOLEAN   DEFAULT FALSE,
    creation_date TIMESTAMP,
    enabled       BOOLEAN   DEFAULT FALSE,
    auth_provider VARCHAR(32),
    provider_id   VARCHAR(64),
    online        BOOLEAN   DEFAULT FALSE,
    last_seen     TIMESTAMP DEFAULT '1970-01-01 00:00:01',

    CONSTRAINT fk_address
        FOREIGN KEY (address_id)
            REFERENCES address (id)
);

CREATE TABLE IF NOT EXISTS role
(
    id   INT         NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS user_role
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,

    CONSTRAINT fk_role_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    CONSTRAINT fk_role_id
        FOREIGN KEY (role_id)
            REFERENCES role (id)
);

CREATE TABLE IF NOT EXISTS user_stats
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    user_id       INT    NOT NULL,
    sport_type_id INT    NOT NULL,
    result_km     DOUBLE NOT NULL,
    result_hours  LONG   NOT NULL,
    location_id   INT,
    coach_id      INT,
    insert_date   DATE   NOT NULL,

    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT fk_sport_type
        FOREIGN KEY (sport_type_id)
            REFERENCES sport_type (id)
);

CREATE TABLE IF NOT EXISTS criteria
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    user_id          INT,
    sport_type_id    INT,
    maturity         VARCHAR(32),
    running_distance DOUBLE,
    gender           VARCHAR(32),
    activity_time    VARCHAR(32),

    CONSTRAINT fk_matching_list_sport_type
        FOREIGN KEY (sport_type_id)
            REFERENCES sport_type (id),

    CONSTRAINT fk_matching_list_user
        FOREIGN KEY (user_id)
            REFERENCES users (id) ON DELETE CASCADE
);

ALTER TABLE criteria
    ADD UNIQUE (user_id, sport_type_id, maturity, running_distance, gender, activity_time);

CREATE TABLE IF NOT EXISTS tag
(
    id   INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS article
(
    id             INT AUTO_INCREMENT PRIMARY KEY,
    author_name    VARCHAR(20)  NOT NULL,
    author_surname VARCHAR(20)  NOT NULL,
    topic          VARCHAR(100) NOT NULL UNIQUE,
    description    VARCHAR(255) NOT NULL,
    picture_url    VARCHAR(2048),
    creation_date  DATE         NOT NULL,
    content        CLOB         NOT NULL
);



CREATE TABLE IF NOT EXISTS article_comment
(
    id            INT AUTO_INCREMENT PRIMARY KEY,
    content       CLOB     NOT NULL,
    creation_date DATETIME NOT NULL,
    image         VARCHAR(2048),
    user_id       INT,
    article_id    INT,

    CONSTRAINT fk_article_comment_user
        FOREIGN KEY (user_id)
            REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT fk_article_comment_article
        FOREIGN KEY (article_id)
            REFERENCES article (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS estimation
(
    id         INT AUTO_INCREMENT PRIMARY KEY,
    value      INT NOT NULL,
    user_id    INT NOT NULL,
    comment_id INT NOT NULL,

    CONSTRAINT fk_estimation_user
        FOREIGN KEY (user_id)
            REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT fk_estimation_article_comment
        FOREIGN KEY (comment_id)
            REFERENCES article_comment (id) ON DELETE CASCADE,
);

CREATE TABLE IF NOT EXISTS article_comment_reply
(
    main_article_comment_id  INT NOT NULL,
    reply_article_comment_id INT NOT NULL,

    PRIMARY KEY (main_article_comment_id, reply_article_comment_id),

    CONSTRAINT fk_main_article_comment_id
        FOREIGN KEY (main_article_comment_id)
            REFERENCES article_comment (id) ON DELETE CASCADE,

    CONSTRAINT fk_reply_article_comment_id
        FOREIGN KEY (reply_article_comment_id)
            REFERENCES article_comment (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS article_tag
(
    article_id INT NOT NULL,
    tag_id     INT NOT NULL,

    PRIMARY KEY (article_id, tag_id),

    CONSTRAINT fk_article_article_tag
        FOREIGN KEY (article_id)
            REFERENCES article (id) ON DELETE CASCADE,

    CONSTRAINT fk_tag_article_tag
        FOREIGN KEY (tag_id)
            REFERENCES tag (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS form
(
    id            INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    location_name VARCHAR(128),
    latitude      DECIMAL(15, 6),
    longitude     DECIMAL(15, 6),
    role_id       INT NOT NULL,
    user_id       INT NOT NULL,
    location_id   INT,

    CONSTRAINT fk_form_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id),

    CONSTRAINT fk_form_role_id
        FOREIGN KEY (role_id)
            REFERENCES role (id)
);

CREATE TABLE IF NOT EXISTS chat
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS message
(
    id           INT      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    sender_id    INT      NOT NULL,
    chat_id      INT      NOT NULL,
    message      CLOB     NOT NULL,
    message_date DATETIME NOT NULL,

    CONSTRAINT fk_sender_message
        FOREIGN KEY (sender_id)
            REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT fk_chat_message
        FOREIGN KEY (chat_id)
            REFERENCES chat (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS user_chat
(
    user_id INT NOT NULL,
    chat_id INT NOT NULL,

    PRIMARY KEY (user_id, chat_id),

    CONSTRAINT fk_user_user_chat
        FOREIGN KEY (user_id)
            REFERENCES users (id) ON DELETE CASCADE,

    CONSTRAINT fk_chat_user_chat
        FOREIGN KEY (chat_id)
            REFERENCES chat (id)
);

CREATE TABLE IF NOT EXISTS event
(
    id           INT  NOT NULL AUTO_INCREMENT PRIMARY KEY,
    date_meeting DATE NOT NULL,
    time_meeting TIME NOT NULL,
    description  VARCHAR(2048),
    owner_id     INT  NOT NULL,
    location_id  INT,

    CONSTRAINT fk_users
        FOREIGN KEY (owner_id)
            REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS event_user
(
    event_id INT NOT NULL,
    user_id  INT NOT NULL,

    PRIMARY KEY (event_id, user_id),

    CONSTRAINT fk_event_event_user
        FOREIGN KEY (event_id)
            REFERENCES event (id) ON DELETE CASCADE,

    CONSTRAINT fk_user_event_user
        FOREIGN KEY (user_id)
            REFERENCES users (id) ON DELETE CASCADE
);
