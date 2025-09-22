SET default_storage_engine = InnoDB;
DROP DATABASE IF EXISTS mobishare;
CREATE DATABASE mobishare;

USE mobishare;

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE CUSTOMER
(
    username varchar(50)          not null,
    name     varchar(50)          not null,
    surname  varchar(50)          not null,
    cf       char(16)             not null,
    gender   enum ('M', 'F', 'X') not null,
    password varchar(256)         not null,
    email    varchar(256)         not null,
    PRIMARY KEY (username)
);

ALTER TABLE CUSTOMER
    ADD CONSTRAINT UK_CLIENTE_1 UNIQUE (username),
    ADD CONSTRAINT UK_CLIENTE_2 UNIQUE (email);

CREATE TABLE COUPON
(
    id         integer unsigned not null auto_increment,
    idCustomer varchar(50)      not null,
    used       boolean          not null,
    expiration date             not null,
    value      integer unsigned not null,
    PRIMARY KEY (id)
);

ALTER TABLE COUPON
    ADD CONSTRAINT FK_COUPON_1_CUSTOMER
        FOREIGN KEY (idCustomer) REFERENCES CUSTOMER (username)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

CREATE TABLE RACE
(
    start       timestamp        not null,
    idCustomer  varchar(50)      not null,
    idVehicle   integer unsigned not null,
    minDuration integer unsigned not null,
    PRIMARY KEY (start, idCustomer, idVehicle)
);
ALTER TABLE RACE
    ADD CONSTRAINT FK_RACE_1_CUSTOMER
        FOREIGN KEY (idCustomer) REFERENCES CUSTOMER (username)
            ON DELETE NO ACTION
            ON UPDATE CASCADE,
    ADD CONSTRAINT FK_RACE_2_VEHICLE
        FOREIGN KEY (idVehicle) REFERENCES VEHICLE (id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

CREATE TABLE VEHICLE
(
    id             integer unsigned not null auto_increment auto_increment,
    immissionDate  date             not null,
    dismissionDate date             null,
    idVehicleType  integer unsigned not null,
    disabled       boolean          not null DEFAULT false,
    PRIMARY KEY (id)
);

ALTER TABLE VEHICLE
    ADD CONSTRAINT FK_VEHICLE_1_VEHICLETYPE
        FOREIGN KEY (idVehicleType) REFERENCES TYPE_VEHICLE (id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

CREATE TABLE TYPE_VEHICLE
(
    id            integer unsigned not null auto_increment,
    name          varchar(50)      not null,
    description   varchar(300)     not null,
    constantPrice float            not null,
    minutePrice   float            not null,
    PRIMARY KEY (id)
);

ALTER TABLE TYPE_VEHICLE
    ADD CONSTRAINT UK_TV_1 UNIQUE (name);

CREATE TABLE MAINTENANCE
(
    id          integer unsigned not null auto_increment,
    idVehicle   integer unsigned not null,
    description varchar(300)     not null,
    start       date             not null,
    end         date             null,
    PRIMARY KEY (id)
);

ALTER TABLE MAINTENANCE
    ADD CONSTRAINT FK_MAINTAINANCE_1_VEHICLE
        FOREIGN KEY (idVehicle) REFERENCES VEHICLE (id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

CREATE TABLE SENSOR
(
    id           integer unsigned not null auto_increment,
    idSensorType integer unsigned not null,
    idVehicle    integer unsigned not null,
    PRIMARY KEY (id)
);

ALTER TABLE SENSOR
    ADD CONSTRAINT UK_SENSOR_1 UNIQUE (idSensorType, idVehicle),
    ADD CONSTRAINT FK_SENSOR_1_TS
        FOREIGN KEY (idSensorType) REFERENCES SENSOR_TYPE (id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE,
    ADD CONSTRAINT FK_SENSOR_2_VEHICLE
        FOREIGN KEY (idVehicle) REFERENCES VEHICLE (id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

CREATE TABLE SENSOR_TYPE
(
    id            integer unsigned not null auto_increment,
    name          varchar(35)      not null,
    description   varchar(300)     not null,
    sendPeriodMin integer unsigned not null,
    PRIMARY KEY (id)
);


ALTER TABLE SENSOR_TYPE
    ADD CONSTRAINT UK_ST_1 UNIQUE (name);

CREATE TABLE SENSOR_REPORT
(
    time     datetime                         not null,
    idSensor integer unsigned                 not null,
    value    float                            not null,
    state    enum ('S', 'P', 'D') default 'P' not null,
    PRIMARY KEY (time)
);

ALTER TABLE SENSOR_REPORT
    ADD CONSTRAINT FK_SR_1_SENSOR
        FOREIGN KEY (idSensor) REFERENCES SENSOR (id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

CREATE TABLE CUSTOMER_REPORT
(
    time         datetime                                     not null,
    idVehicle    integer unsigned                             not null,
    idCustomer   varchar(50)                                  not null,
    idReportType integer unsigned                             not null,
    description  varchar(300)                                 not null,
    state        enum ('SOLVED', 'PENDING') default 'PENDING' not null,
    PRIMARY KEY (time, idCustomer)
);

ALTER TABLE CUSTOMER_REPORT
    ADD CONSTRAINT FK_CR_1_VEHICLE
        FOREIGN KEY (idVehicle) REFERENCES VEHICLE (id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE,
    ADD CONSTRAINT FK_CR_2_CUSTOMER
        FOREIGN KEY (idCustomer) REFERENCES CUSTOMER (username)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

CREATE TABLE REPORT_TYPE
(
    id   integer unsigned not null auto_increment,
    name varchar(50)      not null,
    PRIMARY KEY (id)
);

ALTER TABLE REPORT_TYPE
    ADD CONSTRAINT UK_RT_1 UNIQUE (name);

CREATE TABLE PARK
(
    id        integer unsigned not null auto_increment,
    address   varchar(100)     not null,
    idManager integer unsigned not null,
    latitude  float unsigned   not null,
    longitude float unsigned   not null,
    PRIMARY KEY (id)
);

ALTER TABLE PARK
    ADD CONSTRAINT FK_PARK_1_MANAGER
        FOREIGN KEY (idManager) REFERENCES MANAGER (id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

CREATE TABLE DOCK
(
    id     integer unsigned not null auto_increment,
    number integer unsigned not null,
    idPark integer unsigned not null,
    PRIMARY KEY (id)
);

ALTER TABLE DOCK
    ADD CONSTRAINT FK_DOCK_1_PARK
        FOREIGN KEY (idPark) REFERENCES PARK (id)
            ON DELETE NO ACTION
            ON UPDATE CASCADE,
    ADD CONSTRAINT UK_DOCK_1
        UNIQUE (number, idPark);

CREATE TABLE ACTUATOR_TYPE
(
    type varchar(50) not null,
    PRIMARY KEY (type)
);

CREATE TABLE ACTUATOR
(
    type   varchar(50)      not null,
    idDock integer unsigned not null,
    value  varchar(50)      not null,
    PRIMARY KEY (type, idDock)
);

ALTER TABLE ACTUATOR
    ADD CONSTRAINT FK_ATTUATOR_1_TA
        FOREIGN KEY (type) REFERENCES ACTUATOR_TYPE (type)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    ADD CONSTRAINT FK_ATTUATOR_2_DOCK
        FOREIGN KEY (idDock) REFERENCES DOCK (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

CREATE TABLE TRANSACTION
(
    id         integer unsigned not null auto_increment,
    idCustomer varchar(50)      not null,
    value      float            not null,
    time       datetime         not null,
    PRIMARY KEY (id)
);

ALTER TABLE TRANSACTION
    ADD CONSTRAINT FK_TRANSACTION_1_CUSTOMER
        FOREIGN KEY (idCustomer) REFERENCES CUSTOMER (username)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

CREATE TABLE SUSPENSION
(
    idCustomer varchar(50) not null,
    start      datetime    not null,
    end        datetime    null,
    value      float       not null,
    isRejected boolean     null,
    PRIMARY KEY (idCustomer, start)
);

ALTER TABLE SUSPENSION
    ADD CONSTRAINT FK_SUSPENSION_1_CUSTOMER
        FOREIGN KEY (idCustomer) REFERENCES CUSTOMER (username)
            ON DELETE NO ACTION
            ON UPDATE CASCADE;

CREATE TABLE MANAGER
(
    id       integer unsigned not null auto_increment,
    email    varchar(256)     not null,
    password varchar(256)     not null,
    isAdmin  boolean          not null,
    PRIMARY KEY (id)
);

ALTER TABLE MANAGER
    ADD CONSTRAINT UK_MANAGER_1_EMAIL
        UNIQUE (email);

CREATE TABLE VEHICLE_DOCK
(
    idVehicle integer unsigned not null,
    idDock    integer unsigned not null,
    PRIMARY KEY (idVehicle, idDock)
);

ALTER TABLE VEHICLE_DOCK
    ADD CONSTRAINT FK_VE_1_VEHICLE
        FOREIGN KEY (idVehicle) REFERENCES VEHICLE (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    ADD CONSTRAINT FK_VE_2_DOCK
        FOREIGN KEY (idDock) REFERENCES DOCK (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    ADD CONSTRAINT UK_VE_1_VEHICLE
        UNIQUE (idVehicle),
    ADD CONSTRAINT UK_VE_2_DOCK
        UNIQUE (idDock);

CREATE TABLE VALUE_RANGE
(
    idVehicleType integer unsigned not null,
    idSensorType  integer unsigned not null,
    min           integer          null,
    max           integer          null,
    PRIMARY KEY (idVehicleType, idSensorType)
);

ALTER TABLE VALUE_RANGE
    ADD CONSTRAINT FK_VR_1_VEHICLE
        FOREIGN KEY (idVehicleType) REFERENCES TYPE_VEHICLE (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    ADD CONSTRAINT FK_VR_2_SENSOR
        FOREIGN KEY (idSensorType) REFERENCES SENSOR_TYPE (id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;


DROP USER IF EXISTS core_backend;
CREATE USER core_backend IDENTIFIED BY 'core_backend';
GRANT ALL PRIVILEGES ON mobishare.* TO core_backend;

SET FOREIGN_KEY_CHECKS = 1;
