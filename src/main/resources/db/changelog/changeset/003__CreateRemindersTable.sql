CREATE TABLE reminders
(
    id          BIGINT        NOT NULL,
    title       VARCHAR(255)  NOT NULL,
    description VARCHAR(4096) NOT NULL,
    remind      TIMESTAMP     NOT NULL,
    user_id     BIGINT        NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE SEQUENCE reminder_id_seq START 1
    INCREMENT BY 10
    MINVALUE 1;
