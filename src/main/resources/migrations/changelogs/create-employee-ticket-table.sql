-- liquibase formatted sql
--changeset kferrer:4
DROP TABLE IF EXISTS employee_ticket;
CREATE TABLE employee_ticket(
    employee_id INT NOT NULL,
    ticket_id INT NOT NULL,
    PRIMARY KEY(employee_id,ticket_id),
    FOREIGN KEY(employee_id) REFERENCES employee(id),
    FOREIGN KEY(ticket_id) REFERENCES ticket(ticket_number)
)