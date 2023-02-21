--liquibase formatted sql
--changeset kferrer:7

INSERT INTO ticket(ticket_number,title,description,severity,status) VALUES(1,'Server Error','Server Error','NORMAL','NEW'),
            (2,'Server Error','Server Error','NORMAL','NEW')