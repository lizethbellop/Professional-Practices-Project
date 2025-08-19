DROP DATABASE IF EXISTS professional_practices;

CREATE DATABASE IF NOT EXISTS professional_practices;
USE professional_practices;

CREATE TABLE UserAccount (
    idUser INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(30) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('STUDENT','TEACHER','COORDINATOR', 'EVALUATOR') NOT NULL
);

INSERT INTO useraccount (username,password,role) VALUES
('camila.ramos', '$2a$10$Kwby2hoSr7L5eki8V4VYbORmJCHnTlSughcstNK6WwHDgeC/ORbC6', 'STUDENT'),
('david.mendoza', '$2a$10$Tx8.ZhYfv2p8jZxhpoXoT.dCP9HdjTLV7cr0BmMxTRvj/z2/ZDxSS', 'STUDENT'),
('paola.sandoval', '$2a$10$e7hxNmVg3fw.ShXvZ.uHpu.AAtQ1xL6HyQX8wXJa9PFkEulYZeyKS', 'STUDENT'),
('mateo.carrillo', '$2a$10$/yAZt6TbdVC37fQlSKNVSetV6tqL2u.9AArQx7NJRqtLDR1ibLxly', 'STUDENT'),
('valeria.garcia', '$2a$10$w87M09uxOuqbT2DKRxezc.7zd3b4e8gmqd9yK2D9fKguImIg9ZpOO', 'STUDENT'),
('marco.reyes', '$2a$10$mUiCMBHRyrwSek3wLsnzrenSNfIdWKOxDAiLpf72sQBse0nImKBAa', 'TEACHER'),
('lucia.fuentes', '$2a$10$nkyJf4XTSC1/nvcw9GJ/NO.2Cgmjvzt38wkdgHuL05hoDXKV4mxqu', 'TEACHER'),
('ricardo.ortiz', '$2a$10$Fxq3vzQ0MB0isygzCDsbROiq11/R4hjSiZFWR.hm2oZ.yvy.0ynWW', 'EVALUATOR'),
('monica.silva', '$2a$10$bETF8Rh.C7MIs0FEqS3A7O6z9yFLk7gl2Vebk9qbMHVwfPeWcShQO', 'EVALUATOR'),
('andrea.lopez', '$2a$10$h2Byv/af.JsiH9WAbNkZRuh/td9k.2xi9K7KwyCrq5BIdPyp82MFq', 'COORDINATOR');

CREATE TABLE Student (
    idStudent INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(45) NOT NULL,
    lastNameFather VARCHAR(45) NOT NULL,
    lastNameMother VARCHAR(45),
    enrollment VARCHAR(45) NOT NULL,
    email VARCHAR(45),
    phone VARCHAR(10),
    credits INT,
    semester VARCHAR(45),
    isAssignedToProject TINYINT(1),
    projectSelection VARCHAR(100),
    grade DECIMAL(3,2),
    idUser INT NOT NULL UNIQUE,
    FOREIGN KEY (idUser) REFERENCES UserAccount(idUser)
);

INSERT INTO Student (firstName, lastNameFather, lastNameMother, enrollment, email, phone, credits, semester, isAssignedToProject, projectSelection, grade, idUser) VALUES
('Camila', 'Ramos', 'Luna', 'S21012345', 'camila.ramos@estudiantes.uv.mx', '2284567890', 312, '10°', 0, 'Frontend', NULL, 1),
('David', 'Mendoza', 'García', 'S21098765', 'david.mendoza@estudiantes.uv.mx', '2281234567', 321, '10°', 0, 'Backend', NULL, 2),
('Paola', 'Sandoval', 'Hernández', 'S21024680', 'paola.sandoval@estudiantes.uv.mx', '2292345678', 307, '10°', 0, 'Pruebas', NULL, 3),
('Mateo', 'Carrillo', 'Pérez', 'S21013579', 'mateo.carrillo@estudiantes.uv.mx', '2283456789', 319, '10°', 0, 'DevOps', NULL, 4),
('Valeria', 'García', 'Delgado', 'S21086420', 'valeria.garcia@estudiantes.uv.mx', '2294567891', 328, '10°', 0, 'Base de datos', NULL, 5);

CREATE TABLE Subject (
    idSubject INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    credits INT NOT NULL
);

INSERT INTO Subject (name, credits) VALUES
('Prácticas Profesionales', 14);

CREATE TABLE Term (
    idTerm INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50),
    startDate DATE,
    endDate DATE
);

INSERT INTO Term (name, startDate, endDate) VALUES
('Febrero 2025 - Julio 2025', '2025-02-01', '2025-07-31'),
('Agosto 2025 - Enero 2026', '2025-08-01', '2026-01-31');

CREATE TABLE SubjectGroup (
    idSubjectGroup INT AUTO_INCREMENT PRIMARY KEY,
    idTerm INT,
    idSubject INT,
    schedule VARCHAR(100),
    FOREIGN KEY (idTerm) REFERENCES Term(idTerm),
    FOREIGN KEY (idSubject) REFERENCES Subject(idSubject)
);

INSERT INTO SubjectGroup (idTerm, idSubject, schedule) VALUES
(1, 1, 'Lunes y Miércoles 09:00 - 11:00'),
(1, 1, 'Martes y Jueves 14:00 - 16:00');

CREATE TABLE StudentInGroup (
    idStudent INT NOT NULL,
    idSubjectGroup INT NOT NULL,
    PRIMARY KEY (idStudent, idSubjectGroup),
    FOREIGN KEY (idStudent) REFERENCES Student(idStudent),
    FOREIGN KEY (idSubjectGroup) REFERENCES SubjectGroup(idSubjectGroup)
);

INSERT INTO StudentInGroup (idStudent, idSubjectGroup) VALUES
(1, 1),
(2, 2),
(3, 1),
(4, 2),
(5, 1);

CREATE TABLE Academic (
    idAcademic INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastNameFather VARCHAR(50) NOT NULL,
    lastNameMother VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    status TINYINT(1),
    idUser INT NOT NULL UNIQUE,
    FOREIGN KEY (idUser) REFERENCES UserAccount(idUser)
);

INSERT INTO Academic (firstName, lastNameFather, lastNameMother, email, status, idUser) VALUES
('Marco', 'Reyes', 'Sánchez', 'marco.reyes@uv.mx', 1, 6),
('Lucía', 'Fuentes', 'Gómez', 'lucia.fuentes@uv.mx', 1, 7);

CREATE TABLE TeachingAssignment (
    idSubjectGroup INT NOT NULL,
    idAcademic INT NOT NULL,
    PRIMARY KEY (idSubjectGroup, idAcademic),
    FOREIGN KEY (idSubjectGroup) REFERENCES SubjectGroup(idSubjectGroup),
    FOREIGN KEY (idAcademic) REFERENCES Academic(idAcademic)
);

INSERT INTO TeachingAssignment (idSubjectGroup, idAcademic) VALUES
(1, 1),
(2, 2);

CREATE TABLE InitialDocument (
    idInitialDocument INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(65),
    date DATE,
    delivered TINYINT(1) NOT NULL DEFAULT 0,
    status ENUM('ENTREGADO', 'NO_ENTREGADO', 'EN_REVISION') NOT NULL,
    filePath VARCHAR(255),
    observations VARCHAR(200),
    grade DECIMAL(4,2)
);

INSERT INTO InitialDocument (name, date, delivered, status, filePath, observations, grade)
VALUES ('Cronograma_PayonAguilar.pdf', '2025-06-19', 1, 'ENTREGADO', 'docs/initial/Cronograma_PayonAguilar.pdf', 'Revisado', 10.00);

CREATE TABLE ReportDocument (
    idReportDocument INT AUTO_INCREMENT PRIMARY KEY,
    reportedHours INT NOT NULL,
    date DATE NOT NULL,
    grade DECIMAL(4,2) NOT NULL,
    name VARCHAR(65),
    delivered TINYINT(1) NOT NULL DEFAULT 0,
    status ENUM('ENTREGADO', 'NO_ENTREGADO', 'EN_REVISION') NOT NULL,
    filePath VARCHAR(255)
);

INSERT INTO `reportdocument` VALUES (1,20,'2025-06-19',0.00,'admins.pdf',1,'ENTREGADO','docs\\reports\\0_3_admins.pdf');

CREATE TABLE FinalDocument (
    idFinalDocument INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(65) NOT NULL,
    date DATE,
    delivered TINYINT(1) NOT NULL DEFAULT 0,
    status ENUM('ENTREGADO', 'NO_ENTREGADO', 'EN_REVISION') NOT NULL,
    filePath VARCHAR(255),
    observations VARCHAR(200),
    grade DECIMAL(4,2)
);

CREATE TABLE LinkedOrganization (
    idLinkedOrganization INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(45),
    isActive TINYINT(1),
    address VARCHAR(70),
    phone VARCHAR(10)
);

INSERT INTO LinkedOrganization (name, isActive, address, phone) VALUES
('Innovatech Solutions', 1, 'Av. Tecnología #123, Xalapa, Ver.', '2281234567'),
('Veracruz Code Hub', 1, 'Calle del Software #45, Boca del Río, Ver.', '2299876543'),
('Sierra Verde Sustentable', 1, 'Carretera Antigua Km 7, Coatepec, Ver.', '2287654321'),
('Clinisoft Médica', 1, 'Blvd. Salud #220, Xalapa, Ver.', '2283456789'),
('Red de Datos MX', 1, 'Parque Industrial Sur, Nave 3, Xalapa, Ver.', '2291234567'),
('Proyectos Ágiles S.A.', 1, 'Calle Scrum #13, Orizaba, Ver.', '2724567890'),
('Lógica Creativa', 1, 'Centro Histórico, Calle Ideas #101, Córdoba, Ver.', '2712345678');

CREATE TABLE ProjectManager (
    idProjectManager INT AUTO_INCREMENT PRIMARY KEY,
    idLinkedOrganization INT,
    firstName VARCHAR(45) NOT NULL,
    lastNameFather VARCHAR(45) NOT NULL,
    lastNameMother VARCHAR(45),
    position VARCHAR(50),
    email VARCHAR(60),
    phone VARCHAR(10) NOT NULL,
    FOREIGN KEY (idLinkedOrganization) REFERENCES LinkedOrganization(idLinkedOrganization)
);

INSERT INTO ProjectManager (idLinkedOrganization, firstName, lastNameFather, lastNameMother, position, email, phone) VALUES
(1, 'Mariana', 'López', 'Ramírez', 'Líder de Proyectos', 'mariana.lopez@innovatech.mx', '2288765432'),
(2, 'Carlos', 'Gutiérrez', 'Morales', 'Coordinador Técnico', 'carlos.gutierrez@codehub.mx', '2297654321');

CREATE TABLE Coordinator (
    idCoordinator INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(45) NOT NULL,
    lastNameFather VARCHAR(45) NOT NULL,
    lastNameMother VARCHAR(45),
    email VARCHAR(60),
    idUser INT NOT NULL UNIQUE,
    FOREIGN KEY (idUser) REFERENCES UserAccount(idUser)
);

INSERT INTO Coordinator (firstName, lastNameFather, lastNameMother, email, idUser) VALUES
('Andrea', 'López', 'Martínez', 'andrea.lopez@uv.mx', 10);

CREATE TABLE Record (
    idRecord INT AUTO_INCREMENT PRIMARY KEY,
    idStudent INT NOT NULL,
    idSubjectGroup INT NOT NULL,
    hoursCount INT NULL,
    presentationPath VARCHAR(100) NULL,
    idTerm INT,
    idProject int DEFAULT NULL, -- Will add FK later
    FOREIGN KEY (idStudent) REFERENCES Student(idStudent),
    FOREIGN KEY (idSubjectGroup) REFERENCES SubjectGroup(idSubjectGroup),
    FOREIGN KEY (idTerm) REFERENCES Term(idTerm)
);

INSERT INTO Record (idStudent, idSubjectGroup, hoursCount, presentationPath, idTerm) VALUES
(1, 1, NULL, NULL, 1),
(2, 2, NULL, NULL, 1),
(3, 1, NULL, NULL, 1),
(4, 2, NULL, NULL, 1),
(5, 1, NULL, NULL, 1);

CREATE TABLE Project (
    idProject INT AUTO_INCREMENT PRIMARY KEY,
    idRecord INT, -- Will add FK later
    idProjectManager INT,
    idLinkedOrganization INT,
    idCoordinator INT,
    name VARCHAR(50) NOT NULL,
    department VARCHAR(30),
    description VARCHAR(200),
    methodology VARCHAR(45),
    availability INT,
    FOREIGN KEY (idProjectManager) REFERENCES ProjectManager(idProjectManager),
    FOREIGN KEY (idLinkedOrganization) REFERENCES LinkedOrganization(idLinkedOrganization),
    FOREIGN KEY (idCoordinator) REFERENCES Coordinator(idCoordinator)
);

ALTER TABLE Project
ADD CONSTRAINT fk_project_record FOREIGN KEY (idRecord) REFERENCES Record(idRecord);


ALTER TABLE Record
ADD CONSTRAINT fk_record_project FOREIGN KEY (idProject) REFERENCES Project(idProject);

INSERT INTO Project (idRecord, idProjectManager, idLinkedOrganization, idCoordinator, name, department, description, methodology, availability) VALUES
(1, 1, 1, 1, 'Desarrollo Frontend UX', 'Tecnologías Web', 'Proyecto enfocado en mejorar la experiencia de usuario de una aplicación financiera.', 'Scrum', 1),
(2, 2, 2, 1, 'Sistema de Monitoreo IoT', 'Ingeniería de Software', 'Implementación de un sistema IoT para monitoreo ambiental en tiempo real.', 'Kanban', 1);

CREATE TABLE Delivery (
    idDelivery INT AUTO_INCREMENT PRIMARY KEY,
    idRecord INT NULL,
    name VARCHAR(100),
    description TEXT,
    startDate DATETIME,
    endDate DATETIME,
    deliveryType ENUM('INITIAL DOCUMENT','FINAL DOCUMENT','REPORT') NOT NULL,
    idInitialDocument INT,
    idFinalDocument INT,
    idReportDocument INT,
    FOREIGN KEY (idRecord) REFERENCES Record(idRecord),
    FOREIGN KEY (idInitialDocument) REFERENCES InitialDocument(idInitialDocument),
    FOREIGN KEY (idFinalDocument) REFERENCES FinalDocument(idFinalDocument),
    FOREIGN KEY (idReportDocument) REFERENCES ReportDocument(idReportDocument)
);

INSERT INTO `delivery` VALUES
(1,NULL,'Constancia de seguro facultativo',NULL,'2025-06-10 00:00:00','2025-06-15 23:59:59','INITIAL DOCUMENT',NULL,NULL,NULL),
(2,NULL,'Horario',NULL,'2025-06-10 00:00:00','2025-06-24 23:59:59','INITIAL DOCUMENT',NULL,NULL,NULL),
(3,1,'Reporte Mensual','test','2025-06-19 18:46:31','2025-06-28 00:00:00','REPORT',NULL,NULL,1),
(4,1,'Cronograma','test','2025-06-19 18:48:53','2025-06-28 00:00:00','INITIAL DOCUMENT',1,NULL,NULL);

CREATE TABLE StudentDelivery (
    idStudentDelivery INT AUTO_INCREMENT PRIMARY KEY,
    idStudent INT NOT NULL,
    idDelivery INT NOT NULL,
    FOREIGN KEY (idStudent) REFERENCES Student(idStudent),
    FOREIGN KEY (idDelivery) REFERENCES Delivery(idDelivery)
);

CREATE TABLE Evaluator (
    idEvaluator INT AUTO_INCREMENT PRIMARY KEY,
    idUser INT NOT NULL,
    FOREIGN KEY (idUser) REFERENCES UserAccount(idUser)
);

INSERT INTO Evaluator (idUser) VALUES (8), (9);

CREATE TABLE PresentationEvaluation (
    idEvaluation INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(65),
    date DATE,
    grade DECIMAL(4,2),
    observations TEXT,
    idRecord INT,
    idEvaluator INT,
    FOREIGN KEY (idEvaluator) REFERENCES Evaluator(idEvaluator),
    FOREIGN KEY (idRecord) REFERENCES Record(idRecord)
);

CREATE TABLE EvaluationCriteria (
    idCriteria INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(100),
    value DECIMAL(4,2)
);

INSERT INTO evaluationcriteria (description, value) VALUES
('Presentó con claridad los objetivos del proyecto o área donde colabora.', 10.00),
('Describió las actividades realizadas hasta la fecha con orden y coherencia.', 10.00),
('Explicó su rol dentro del equipo o empresa con claridad.', 10.00),
('Mostró evidencias de su trabajo (capturas, entregables, documentos, etc.).', 10.00),
('Reflexionó sobre lo aprendido hasta el momento.', 10.00),
('Identificó los retos que ha enfrentado y cómo los abordó.', 10.00),
('Demostró claridad al explicar términos técnicos relacionados con su rol.', 10.00),
('Mostró una actitud profesional durante su exposición.', 10.00),
('Proyectó próximos pasos o metas para el resto de las prácticas.', 10.00),
('Mantuvo buena expresión oral, contacto visual y lenguaje adecuado.', 10.00);

CREATE TABLE PresentationEvaluationCriteria (
    idEvaluation INT,
    idCriteria INT,
    criteriaMet BOOLEAN,
    valueEarned DECIMAL(4,2),
    PRIMARY KEY (idEvaluation, idCriteria),
    FOREIGN KEY (idEvaluation) REFERENCES PresentationEvaluation(idEvaluation),
    FOREIGN KEY (idCriteria) REFERENCES EvaluationCriteria(idCriteria)
);

DELIMITER $$

CREATE TRIGGER after_record_update_student
AFTER UPDATE ON Record
FOR EACH ROW
BEGIN
    IF NEW.idProject IS NOT NULL AND OLD.idProject IS NULL THEN
        UPDATE Student s
        SET s.isAssignedToProject = 1
        WHERE s.idStudent = NEW.idStudent;

        UPDATE Project p
        SET p.availability = p.availability - 1
        WHERE p.idProject = NEW.idProject;
    ELSEIF NEW.idProject IS NULL AND OLD.idProject IS NOT NULL THEN
        UPDATE Student s
        SET s.isAssignedToProject = 0
        WHERE s.idStudent = NEW.idStudent;

        UPDATE Project p
        SET p.availability = p.availability + 1
        WHERE p.idProject = OLD.idProject;
    END IF;
END$$

DELIMITER ;

CREATE USER 'pp_admin'@'localhost' IDENTIFIED BY 'Ppsyst3m!';
GRANT ALL PRIVILEGES ON professional_practices.* TO 'pp_admin'@'localhost';
FLUSH PRIVILEGES;