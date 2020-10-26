CREATE TABLE administrator (
    uid VARCHAR(30) PRIMARY KEY,
    password VARCHAR(30) NOT NULL
);

CREATE TABLE users (
    email VARCHAR(60) NOT NULL,
    username VARCHAR(20) NOT NULL,
    name VARCHAR(20) NOT NULL,
    surname VARCHAR(20) NOT NULL,
    password VARCHAR(30) NOT NULL,
    is_confirmed BOOLEAN DEFAULT False NOT NULL,
    code VARCHAR(30),
    PRIMARY KEY (email, username)
);

CREATE TABLE game (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    max_players INT CONSTRAINT players_constraint CHECK ( max_players > 2 AND max_players < 7 ) NOT NULL,
    status VARCHAR(7) CONSTRAINT status_constraint CHECK ( status IN ('open', 'playing', 'closed') ) NOT NULL,
    date DATE NOT NULL
);

CREATE TABLE enter (
    id_game INT,
    email_user VARCHAR(60),
    username_user VARCHAR(20),
    session_number INT CONSTRAINT session_number_constraint CHECK ( session_number > 0 ),
    PRIMARY KEY (id_game, email_user, username_user, session_number),
    FOREIGN KEY (id_game) REFERENCES game(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (email_user, username_user) REFERENCES users(email, username)
);

CREATE TABLE discover (
    word VARCHAR(16),
    id_game INT,
    email_user VARCHAR(60),
    username_user VARCHAR(20),
    session_number_enter INT,
    n_requests INT CONSTRAINT n_requests_constrains CHECK ( n_requests >= 0 ) DEFAULT 0 NOT NULL,
    score INT CONSTRAINT score_constraint CHECK ( score >= 0 ) NOT NULL,
    is_valid BOOLEAN NOT NULL,
    PRIMARY KEY (id_game, email_user, username_user, session_number_enter, word),
    FOREIGN KEY (id_game, email_user, username_user, session_number_enter) REFERENCES enter(id_game, email_user, username_user, session_number) ON DELETE CASCADE ON UPDATE CASCADE
);
