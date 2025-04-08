-- EJECUTAR PRIMERO ESTO
CREATE DATABASE sistema_cds;
USE sistema_cds;

-- UNA VEZ EJECUTADAS LAS TABLAS, EJECUTAR TODAS LAS INSERCIONES
INSERT INTO phase(name) VALUES('Inicio');
INSERT INTO phase(name) VALUES('Planeacion');
INSERT INTO phase(name) VALUES('Ejecucion');
INSERT INTO phase(name) VALUES('Control');
INSERT INTO phase(name) VALUES('Cierre');

INSERT INTO rol(name) VALUES('ROLE_MASTER'); -- MASTER
INSERT INTO rol(name) VALUES('ROLE_RAPE'); -- Responsables de proyectos
INSERT INTO rol(name) VALUES('ROLE_RD'); -- Responsables de desarrollo
INSERT INTO rol(name) VALUES('ROLE_AP'); -- Analistas programadores

INSERT INTO employee(name, surname, lastname, email, password, username, status, id_rol) VALUES('Alexandro', 'Ceron', 'Aviles', 'alex@gmail.com', 'hola123', 'AleXD', 1, 1);
INSERT INTO employee(name, surname, lastname, email, password, username, status, id_rol) VALUES('Loreley', 'Carrilo', 'Juarez', 'loreley@gmail.com', 'hola123', 'LoreUwU', 1, 2);
INSERT INTO employee(name, surname, lastname, email, password, username, status, id_rol) VALUES('Rodolfo', 'Flores', 'Vergara', 'rodolfo@gmail.com', 'hola123', 'RodoXD', 1, 3);
INSERT INTO employee(name, surname, lastname, email, password, username, status, id_rol) VALUES('David', 'Linares', 'Noriega', 'david@gmail.com', 'hola123', 'DavidXD', 1, 4);
INSERT INTO employee(name, surname, lastname, email, password, username, status, id_rol) VALUES('Christhopher', 'Alarcon', 'Mendoza', 'chris@gmail.com', 'hola123', 'chrisXD', 1, 4);
INSERT INTO employee(name, surname, lastname, email, password, username, status, id_rol) VALUES('Jannethe', 'Millan', 'Millan', 'jannethe@gmail.com', 'hola123', 'JannetheXD', 1, 4);
INSERT INTO employee(name, surname, lastname, email, password, username, status, id_rol) VALUES('Nathan', 'Sanchez', '', 'nathan@gmail.com', 'hola123', 'NathanXD', 1, 4);
INSERT INTO employee(name, surname, lastname, email, password, username, status, id_rol) VALUES('Gael', 'Castañeda', 'Beltran', 'gael@gmail.com', 'hola123', 'GaelXD', 1, 4);






