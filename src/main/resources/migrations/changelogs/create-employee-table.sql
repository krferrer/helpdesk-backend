-- liquibase formatted sql
--changeset kferrer:2

DROP TABLE IF EXISTS employee;
CREATE TABLE employee(
    id SERIAL PRIMARY KEY,
    employee_number SERIAL,
    first_name VARCHAR(50),
    middle_name VARCHAR(50),
    last_name VARCHAR(50),
    department VARCHAR(50),
    username VARCHAR(50),
    password VARCHAR(250),
    role_id INT,
    FOREIGN KEY(role_id) REFERENCES role(id)
);