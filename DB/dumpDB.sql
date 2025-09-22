-- MySQL dump 10.13  Distrib 9.3.0, for macos15 (arm64)
--
-- Host: 127.0.0.1    Database: mobishare
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ACTUATOR`
--

DROP TABLE IF EXISTS `ACTUATOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ACTUATOR` (
  `type` varchar(50) NOT NULL,
  `idDock` int unsigned NOT NULL,
  `value` varchar(50) NOT NULL,
  PRIMARY KEY (`type`,`idDock`),
  KEY `FK_ATTUATOR_2_DOCK` (`idDock`),
  CONSTRAINT `FK_ATTUATOR_1_TA` FOREIGN KEY (`type`) REFERENCES `ACTUATOR_TYPE` (`type`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_ATTUATOR_2_DOCK` FOREIGN KEY (`idDock`) REFERENCES `DOCK` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ACTUATOR`
--

LOCK TABLES `ACTUATOR` WRITE;
/*!40000 ALTER TABLE `ACTUATOR` DISABLE KEYS */;
INSERT INTO `ACTUATOR` VALUES ('Light',1,'green'),('Light',2,'green'),('Light',3,'red'),('Light',4,'off'),('Light',5,'off'),('Light',6,'off'),('Light',7,'off'),('Light',8,'off'),('Light',9,'off'),('Light',10,'off'),('Light',11,'off'),('Light',12,'off'),('Light',13,'off'),('Light',14,'off'),('Light',15,'off'),('Light',16,'off'),('Light',17,'off'),('Light',18,'off'),('Light',19,'off'),('Light',20,'off'),('Light',21,'green'),('Light',22,'green'),('Light',23,'green'),('Light',24,'off'),('Light',25,'off'),('Light',26,'off'),('Light',27,'off'),('Light',28,'off'),('Light',29,'off'),('Light',30,'off'),('Light',31,'off'),('Light',32,'off'),('Light',33,'off'),('Light',34,'off'),('Light',35,'off'),('Light',36,'off'),('Light',37,'off'),('Light',38,'off'),('Light',39,'off'),('Light',40,'off'),('Light',41,'green'),('Light',42,'green'),('Light',43,'off'),('Light',44,'off'),('Light',45,'off'),('Light',46,'off'),('Light',47,'off'),('Light',48,'off'),('Light',49,'off'),('Light',50,'off'),('Light',51,'off'),('Light',52,'off'),('Light',53,'off'),('Light',54,'off'),('Light',55,'off'),('Light',56,'off'),('Light',57,'off'),('Light',58,'off'),('Light',59,'off'),('Light',60,'off'),('Lock',1,'close'),('Lock',2,'close'),('Lock',3,'close'),('Lock',4,'open'),('Lock',5,'open'),('Lock',6,'open'),('Lock',7,'open'),('Lock',8,'open'),('Lock',9,'open'),('Lock',10,'open'),('Lock',11,'open'),('Lock',12,'open'),('Lock',13,'open'),('Lock',14,'open'),('Lock',15,'open'),('Lock',16,'open'),('Lock',17,'open'),('Lock',18,'open'),('Lock',19,'open'),('Lock',20,'open'),('Lock',21,'close'),('Lock',22,'close'),('Lock',23,'close'),('Lock',24,'open'),('Lock',25,'open'),('Lock',26,'open'),('Lock',27,'open'),('Lock',28,'open'),('Lock',29,'open'),('Lock',30,'open'),('Lock',31,'open'),('Lock',32,'open'),('Lock',33,'open'),('Lock',34,'open'),('Lock',35,'open'),('Lock',36,'open'),('Lock',37,'open'),('Lock',38,'open'),('Lock',39,'open'),('Lock',40,'open'),('Lock',41,'close'),('Lock',42,'close'),('Lock',43,'open'),('Lock',44,'open'),('Lock',45,'open'),('Lock',46,'open'),('Lock',47,'open'),('Lock',48,'open'),('Lock',49,'open'),('Lock',50,'open'),('Lock',51,'open'),('Lock',52,'open'),('Lock',53,'open'),('Lock',54,'open'),('Lock',55,'open'),('Lock',56,'open'),('Lock',57,'open'),('Lock',58,'open'),('Lock',59,'open'),('Lock',60,'open');
/*!40000 ALTER TABLE `ACTUATOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ACTUATOR_TYPE`
--

DROP TABLE IF EXISTS `ACTUATOR_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ACTUATOR_TYPE` (
  `type` varchar(50) NOT NULL,
  PRIMARY KEY (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ACTUATOR_TYPE`
--

LOCK TABLES `ACTUATOR_TYPE` WRITE;
/*!40000 ALTER TABLE `ACTUATOR_TYPE` DISABLE KEYS */;
INSERT INTO `ACTUATOR_TYPE` VALUES ('Light'),('Lock');
/*!40000 ALTER TABLE `ACTUATOR_TYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `COUPON`
--

DROP TABLE IF EXISTS `COUPON`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `COUPON` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `idCustomer` varchar(50) NOT NULL,
  `used` tinyint(1) NOT NULL,
  `expiration` date NOT NULL,
  `value` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_COUPON_1_CUSTOMER` (`idCustomer`),
  CONSTRAINT `FK_COUPON_1_CUSTOMER` FOREIGN KEY (`idCustomer`) REFERENCES `CUSTOMER` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `COUPON`
--

LOCK TABLES `COUPON` WRITE;
/*!40000 ALTER TABLE `COUPON` DISABLE KEYS */;
INSERT INTO `COUPON` VALUES (1,'teobarby',0,'2026-07-05',500);
/*!40000 ALTER TABLE `COUPON` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CUSTOMER`
--

DROP TABLE IF EXISTS `CUSTOMER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CUSTOMER` (
  `username` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL,
  `surname` varchar(50) NOT NULL,
  `cf` char(16) NOT NULL,
  `gender` enum('M','F','X') NOT NULL,
  `password` varchar(256) NOT NULL,
  `email` varchar(256) NOT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `UK_CLIENTE_1` (`username`),
  UNIQUE KEY `UK_CLIENTE_2` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CUSTOMER`
--

LOCK TABLES `CUSTOMER` WRITE;
/*!40000 ALTER TABLE `CUSTOMER` DISABLE KEYS */;
INSERT INTO `CUSTOMER` VALUES ('suspended','Marco','Valentini','MRVLN033LS89DK90','M','$2a$10$5RPvgLcWIypOsUlgCR2CLOXHydnSrRP4aoKKFYYCg..QIiQZjlGWG','marcovalentini@gmail.com'),('teobarby','Matteo','Barbieri','BRBMTT03S29L750F','M','$2a$10$5RPvgLcWIypOsUlgCR2CLOXHydnSrRP4aoKKFYYCg..QIiQZjlGWG','matteobarbieri50@gmail.com');
/*!40000 ALTER TABLE `CUSTOMER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `CUSTOMER_REPORT`
--

DROP TABLE IF EXISTS `CUSTOMER_REPORT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `CUSTOMER_REPORT` (
  `time` datetime NOT NULL,
  `idVehicle` int unsigned NOT NULL,
  `idCustomer` varchar(50) NOT NULL,
  `idReportType` int unsigned NOT NULL,
  `description` varchar(300) NOT NULL,
  `state` enum('SOLVED','PENDING') NOT NULL DEFAULT 'PENDING',
  PRIMARY KEY (`time`,`idCustomer`),
  KEY `FK_CR_1_VEHICLE` (`idVehicle`),
  KEY `FK_CR_2_CUSTOMER` (`idCustomer`),
  CONSTRAINT `FK_CR_1_VEHICLE` FOREIGN KEY (`idVehicle`) REFERENCES `VEHICLE` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `FK_CR_2_CUSTOMER` FOREIGN KEY (`idCustomer`) REFERENCES `CUSTOMER` (`username`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `CUSTOMER_REPORT`
--

LOCK TABLES `CUSTOMER_REPORT` WRITE;
/*!40000 ALTER TABLE `CUSTOMER_REPORT` DISABLE KEYS */;
INSERT INTO `CUSTOMER_REPORT` VALUES ('2025-07-05 12:09:47',3,'teobarby',1,'Purtroppo, devo segnalare un’esperienza estremamente negativa con questo veicolo. Fin dal momento in cui ho cercato di sbloccarlo, ho notato diversi problemi che hanno reso impossibile l’utilizzo.\n','PENDING');
/*!40000 ALTER TABLE `CUSTOMER_REPORT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DOCK`
--

DROP TABLE IF EXISTS `DOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `DOCK` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `number` int unsigned NOT NULL,
  `idPark` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_DOCK_1` (`number`,`idPark`),
  KEY `FK_DOCK_1_PARK` (`idPark`),
  CONSTRAINT `FK_DOCK_1_PARK` FOREIGN KEY (`idPark`) REFERENCES `PARK` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DOCK`
--

LOCK TABLES `DOCK` WRITE;
/*!40000 ALTER TABLE `DOCK` DISABLE KEYS */;
INSERT INTO `DOCK` VALUES (1,1,1),(21,1,2),(41,1,3),(2,2,1),(22,2,2),(42,2,3),(3,3,1),(23,3,2),(43,3,3),(4,4,1),(24,4,2),(44,4,3),(5,5,1),(25,5,2),(45,5,3),(6,6,1),(26,6,2),(46,6,3),(7,7,1),(27,7,2),(47,7,3),(8,8,1),(28,8,2),(48,8,3),(9,9,1),(29,9,2),(49,9,3),(10,10,1),(30,10,2),(50,10,3),(11,11,1),(31,11,2),(51,11,3),(12,12,1),(32,12,2),(52,12,3),(13,13,1),(33,13,2),(53,13,3),(14,14,1),(34,14,2),(54,14,3),(15,15,1),(35,15,2),(55,15,3),(16,16,1),(36,16,2),(56,16,3),(17,17,1),(37,17,2),(57,17,3),(18,18,1),(38,18,2),(58,18,3),(19,19,1),(39,19,2),(59,19,3),(20,20,1),(40,20,2),(60,20,3);
/*!40000 ALTER TABLE `DOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MAINTENANCE`
--

DROP TABLE IF EXISTS `MAINTENANCE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MAINTENANCE` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `idVehicle` int unsigned NOT NULL,
  `description` varchar(300) NOT NULL,
  `start` date NOT NULL,
  `end` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_MAINTAINANCE_1_VEHICLE` (`idVehicle`),
  CONSTRAINT `FK_MAINTAINANCE_1_VEHICLE` FOREIGN KEY (`idVehicle`) REFERENCES `VEHICLE` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MAINTENANCE`
--

LOCK TABLES `MAINTENANCE` WRITE;
/*!40000 ALTER TABLE `MAINTENANCE` DISABLE KEYS */;
INSERT INTO `MAINTENANCE` VALUES (1,9,'prova','2025-07-05','2025-07-05'),(2,9,'prova','2025-07-05',NULL);
/*!40000 ALTER TABLE `MAINTENANCE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MANAGER`
--

DROP TABLE IF EXISTS `MANAGER`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `MANAGER` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(256) NOT NULL,
  `password` varchar(256) NOT NULL,
  `isAdmin` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_MANAGER_1_EMAIL` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MANAGER`
--

LOCK TABLES `MANAGER` WRITE;
/*!40000 ALTER TABLE `MANAGER` DISABLE KEYS */;
INSERT INTO `MANAGER` VALUES (1,'manager','$2a$10$5RPvgLcWIypOsUlgCR2CLOXHydnSrRP4aoKKFYYCg..QIiQZjlGWG',0),(2,'manager2','$2a$10$5RPvgLcWIypOsUlgCR2CLOXHydnSrRP4aoKKFYYCg..QIiQZjlGWG',0),(3,'admin','$2a$10$5RPvgLcWIypOsUlgCR2CLOXHydnSrRP4aoKKFYYCg..QIiQZjlGWG',1);
/*!40000 ALTER TABLE `MANAGER` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PARK`
--

DROP TABLE IF EXISTS `PARK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `PARK` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `address` varchar(100) NOT NULL,
  `idManager` int unsigned NOT NULL,
  `latitude` float unsigned NOT NULL,
  `longitude` float unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_PARK_1_MANAGER` (`idManager`),
  CONSTRAINT `FK_PARK_1_MANAGER` FOREIGN KEY (`idManager`) REFERENCES `MANAGER` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PARK`
--

LOCK TABLES `PARK` WRITE;
/*!40000 ALTER TABLE `PARK` DISABLE KEYS */;
INSERT INTO `PARK` VALUES (1,'Via Giuseppina Dusnasi 1',1,45.3344,8.40283),(2,'Piazza Sant\'Eusebio 5',1,45.3307,8.42075),(3,'Piazza Roma',2,45.3294,8.417);
/*!40000 ALTER TABLE `PARK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RACE`
--

DROP TABLE IF EXISTS `RACE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RACE` (
  `start` timestamp NOT NULL,
  `idCustomer` varchar(50) NOT NULL,
  `idVehicle` int unsigned NOT NULL,
  `minDuration` int unsigned NOT NULL,
  PRIMARY KEY (`start`,`idCustomer`,`idVehicle`),
  KEY `FK_RACE_1_CUSTOMER` (`idCustomer`),
  KEY `FK_RACE_2_VEHICLE` (`idVehicle`),
  CONSTRAINT `FK_RACE_1_CUSTOMER` FOREIGN KEY (`idCustomer`) REFERENCES `CUSTOMER` (`username`) ON UPDATE CASCADE,
  CONSTRAINT `FK_RACE_2_VEHICLE` FOREIGN KEY (`idVehicle`) REFERENCES `VEHICLE` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RACE`
--

LOCK TABLES `RACE` WRITE;
/*!40000 ALTER TABLE `RACE` DISABLE KEYS */;
/*!40000 ALTER TABLE `RACE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `REPORT_TYPE`
--

DROP TABLE IF EXISTS `REPORT_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `REPORT_TYPE` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_RT_1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `REPORT_TYPE`
--

LOCK TABLES `REPORT_TYPE` WRITE;
/*!40000 ALTER TABLE `REPORT_TYPE` DISABLE KEYS */;
INSERT INTO `REPORT_TYPE` VALUES (1,'Generico');
/*!40000 ALTER TABLE `REPORT_TYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SENSOR`
--

DROP TABLE IF EXISTS `SENSOR`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SENSOR` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `idSensorType` int unsigned NOT NULL,
  `idVehicle` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_SENSOR_1` (`idSensorType`,`idVehicle`),
  KEY `FK_SENSOR_2_VEHICLE` (`idVehicle`),
  CONSTRAINT `FK_SENSOR_1_TS` FOREIGN KEY (`idSensorType`) REFERENCES `SENSOR_TYPE` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `FK_SENSOR_2_VEHICLE` FOREIGN KEY (`idVehicle`) REFERENCES `VEHICLE` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SENSOR`
--

LOCK TABLES `SENSOR` WRITE;
/*!40000 ALTER TABLE `SENSOR` DISABLE KEYS */;
INSERT INTO `SENSOR` VALUES (1,1,1),(4,1,2),(7,1,3),(10,1,4),(13,1,5),(16,1,6),(19,1,7),(22,1,8),(25,1,9),(2,2,1),(5,2,2),(8,2,3),(11,2,4),(14,2,5),(17,2,6),(20,2,7),(23,2,8),(26,2,9),(3,3,1),(6,3,2),(9,3,3),(12,3,4),(15,3,5),(18,3,6),(21,3,7),(24,3,8),(27,3,9);
/*!40000 ALTER TABLE `SENSOR` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SENSOR_REPORT`
--

DROP TABLE IF EXISTS `SENSOR_REPORT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SENSOR_REPORT` (
  `time` datetime NOT NULL,
  `idSensor` int unsigned NOT NULL,
  `value` float NOT NULL,
  `state` enum('S','P','D') NOT NULL DEFAULT 'P',
  PRIMARY KEY (`time`),
  KEY `FK_SR_1_SENSOR` (`idSensor`),
  CONSTRAINT `FK_SR_1_SENSOR` FOREIGN KEY (`idSensor`) REFERENCES `SENSOR` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SENSOR_REPORT`
--

LOCK TABLES `SENSOR_REPORT` WRITE;
/*!40000 ALTER TABLE `SENSOR_REPORT` DISABLE KEYS */;
/*!40000 ALTER TABLE `SENSOR_REPORT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SENSOR_TYPE`
--

DROP TABLE IF EXISTS `SENSOR_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SENSOR_TYPE` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(35) NOT NULL,
  `description` varchar(300) NOT NULL,
  `sendPeriodMin` int unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ST_1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SENSOR_TYPE`
--

LOCK TABLES `SENSOR_TYPE` WRITE;
/*!40000 ALTER TABLE `SENSOR_TYPE` DISABLE KEYS */;
INSERT INTO `SENSOR_TYPE` VALUES (1,'Battery','Battery sensor for the state of charge',1),(2,'Pressure','Tyre pressure sensor',10),(3,'Latitude','Sends the latitude of the vehicle',5),(4,'Longitude','Sends the longitude of the vehicle',5);
/*!40000 ALTER TABLE `SENSOR_TYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SUSPENSION`
--

DROP TABLE IF EXISTS `SUSPENSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SUSPENSION` (
  `idCustomer` varchar(50) NOT NULL,
  `start` datetime NOT NULL,
  `end` datetime DEFAULT NULL,
  `value` float NOT NULL,
  `isRejected` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idCustomer`,`start`),
  CONSTRAINT `FK_SUSPENSION_1_CUSTOMER` FOREIGN KEY (`idCustomer`) REFERENCES `CUSTOMER` (`username`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SUSPENSION`
--

LOCK TABLES `SUSPENSION` WRITE;
/*!40000 ALTER TABLE `SUSPENSION` DISABLE KEYS */;
INSERT INTO `SUSPENSION` VALUES ('suspended','2025-07-04 11:33:19','2027-07-05 11:33:28',5,NULL);
/*!40000 ALTER TABLE `SUSPENSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TRANSACTION`
--

DROP TABLE IF EXISTS `TRANSACTION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TRANSACTION` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `idCustomer` varchar(50) NOT NULL,
  `value` float NOT NULL,
  `time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_TRANSACTION_1_CUSTOMER` (`idCustomer`),
  CONSTRAINT `FK_TRANSACTION_1_CUSTOMER` FOREIGN KEY (`idCustomer`) REFERENCES `CUSTOMER` (`username`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TRANSACTION`
--

LOCK TABLES `TRANSACTION` WRITE;
/*!40000 ALTER TABLE `TRANSACTION` DISABLE KEYS */;
INSERT INTO `TRANSACTION` VALUES (1,'teobarby',25,'2025-07-05 09:20:27'),(2,'teobarby',5,'2025-07-05 09:21:24');
/*!40000 ALTER TABLE `TRANSACTION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `TYPE_VEHICLE`
--

DROP TABLE IF EXISTS `TYPE_VEHICLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `TYPE_VEHICLE` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(300) NOT NULL,
  `constantPrice` float NOT NULL,
  `minutePrice` float NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_TV_1` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `TYPE_VEHICLE`
--

LOCK TABLES `TYPE_VEHICLE` WRITE;
/*!40000 ALTER TABLE `TYPE_VEHICLE` DISABLE KEYS */;
INSERT INTO `TYPE_VEHICLE` VALUES (1,'Bici muscolare','Bicicletta muscolare',1,0.2),(2,'Bici elettrica','Bicicletta elettrica',1.5,0.5),(3,'Monopattino elettrico','Monopattino elettrico',2,0.35);
/*!40000 ALTER TABLE `TYPE_VEHICLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VALUE_RANGE`
--
DROP TABLE IF EXISTS `VALUE_RANGE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VALUE_RANGE` (
  `idVehicleType` int unsigned NOT NULL,
  `idSensorType` int unsigned NOT NULL,
  `min` int DEFAULT NULL,
  `max` int DEFAULT NULL,
  PRIMARY KEY (`idVehicleType`,`idSensorType`),
  KEY `FK_VR_2_SENSOR` (`idSensorType`),
  CONSTRAINT `FK_VR_1_VEHICLE` FOREIGN KEY (`idVehicleType`) REFERENCES `TYPE_VEHICLE` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_VR_2_SENSOR` FOREIGN KEY (`idSensorType`) REFERENCES `SENSOR_TYPE` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VALUE_RANGE`
--
LOCK TABLES `VALUE_RANGE` WRITE;
/*!40000 ALTER TABLE `VALUE_RANGE` DISABLE KEYS */;
INSERT INTO VALUE_RANGE (idVehicleType, idSensorType, min, max) VALUES
(1, 1, 10, 100),
(1, 2, 2, 4),
(1, 3, -90, 90),
(1, 4, -180, 180),
(2, 1, 10, 100),
(2, 2, 2, 4),
(2, 3, -90, 90),
(2, 4, -180, 180),
(3, 1, 10, 100),
(3, 2, 2, 4),
(3, 3, -90, 90),
(3, 4, -180, 180);
/*!40000 ALTER TABLE `VALUE_RANGE` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `VEHICLE`
--

DROP TABLE IF EXISTS `VEHICLE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VEHICLE` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `immissionDate` date NOT NULL,
  `dismissionDate` date DEFAULT NULL,
  `idVehicleType` int unsigned NOT NULL,
  `disabled` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_VEHICLE_1_VEHICLETYPE` (`idVehicleType`),
  CONSTRAINT `FK_VEHICLE_1_VEHICLETYPE` FOREIGN KEY (`idVehicleType`) REFERENCES `TYPE_VEHICLE` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VEHICLE`
--

LOCK TABLES `VEHICLE` WRITE;
/*!40000 ALTER TABLE `VEHICLE` DISABLE KEYS */;
INSERT INTO `VEHICLE` VALUES (1,'2025-07-05',NULL,1,0),(2,'2025-07-05',NULL,3,0),(3,'2025-07-05',NULL,2,1),(4,'2025-07-05',NULL,1,0),(5,'2025-07-05',NULL,2,0),(6,'2025-07-05',NULL,3,0),(7,'2025-07-05',NULL,1,0),(8,'2025-07-05',NULL,2,0),(9,'2025-07-05',NULL,3,1);
/*!40000 ALTER TABLE `VEHICLE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `VEHICLE_DOCK`
--

DROP TABLE IF EXISTS `VEHICLE_DOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `VEHICLE_DOCK` (
  `idVehicle` int unsigned NOT NULL,
  `idDock` int unsigned NOT NULL,
  PRIMARY KEY (`idVehicle`,`idDock`),
  UNIQUE KEY `UK_VE_1_VEHICLE` (`idVehicle`),
  UNIQUE KEY `UK_VE_2_DOCK` (`idDock`),
  CONSTRAINT `FK_VE_1_VEHICLE` FOREIGN KEY (`idVehicle`) REFERENCES `VEHICLE` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_VE_2_DOCK` FOREIGN KEY (`idDock`) REFERENCES `DOCK` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `VEHICLE_DOCK`
--

LOCK TABLES `VEHICLE_DOCK` WRITE;
/*!40000 ALTER TABLE `VEHICLE_DOCK` DISABLE KEYS */;
INSERT INTO `VEHICLE_DOCK` VALUES (1,1),(2,2),(3,3),(4,21),(5,22),(6,23),(7,41),(8,42);
/*!40000 ALTER TABLE `VEHICLE_DOCK` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-05 21:03:14
