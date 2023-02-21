-- liquibase formatted sql
--changeset kferrer:3
DROP TABLE IF EXISTS ticket;
CREATE TABLE ticket(
    ticket_number SERIAL PRIMARY KEY,
    title VARCHAR(255),
    description TEXT,
    severity VARCHAR(50),
    status VARCHAR(50),
    assignee INT,
    FOREIGN KEY (assignee) REFERENCES employee(id)
);