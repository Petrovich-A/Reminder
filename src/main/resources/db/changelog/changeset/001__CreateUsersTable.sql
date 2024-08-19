CREATE TABLE users
(
    id              BIGINT       NOT NULL,
    login           VARCHAR(50)  NOT NULL,
    password        VARCHAR(100) NOT NULL,
    email           VARCHAR(50)  NOT NULL,
    chat_id         VARCHAR(10)  NOT NULL,
    o_auth_provider VARCHAR(15)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE SEQUENCE user_id_seq START 1
    INCREMENT BY 10
    MINVALUE 1;
