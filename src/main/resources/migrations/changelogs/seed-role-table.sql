--liquibase formatted sql
--changeset kferrer:5

INSERT INTO role(id,name) VALUES(1,'ROLE_ADMIN'),(2,'ROLE_USER'),(3,'ROLE_HR');