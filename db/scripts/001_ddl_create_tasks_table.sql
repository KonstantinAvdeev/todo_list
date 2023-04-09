CREATE TABLE tasks
(
    id          SERIAL PRIMARY KEY,
    name        varchar(255),
    description TEXT,
    created     TIMESTAMP,
    done        BOOLEAN
);