-- liquibase formatted sql
--changeset kferrer:1

DROP TABLE IF EXISTS role;

CREATE TABLE role(
    id SERIAL PRIMARY KEY,
    name VARCHAR(50)
);