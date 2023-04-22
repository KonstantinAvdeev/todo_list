CREATE TABLE tasks
(
    id          SERIAL PRIMARY KEY,
    name        varchar(255) not null,
    description TEXT,
    created     TIMESTAMP,
    done        BOOLEAN
);