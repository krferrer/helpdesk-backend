--liquibase formatted sql
--changeset kferrer:6

INSERT INTO employee(id,employee_number,first_name,middle_name,last_name,department,username,password,role_id) VALUES
            (1,1,'kurt','buena','ferrer','ADMIN','makf13','$2y$10$EcBEp.Sn7ucJzF07aeLHAe3VRiiIIRfeJ3fwKT26Ck.3XHq2KXsBi',1),
            (2,2,'yoona','buena','permejo','ADMIN','yoona13','$2y$10$EcBEp.Sn7ucJzF07aeLHAe3VRiiIIRfeJ3fwKT26Ck.3XHq2KXsBi',2)