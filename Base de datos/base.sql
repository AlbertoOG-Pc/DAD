-- MySQL dump 10.13  Distrib 8.0.23, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: dad
-- ------------------------------------------------------
-- Server version	8.0.23

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_coordinates` int DEFAULT NULL,
  `maxPower` decimal(6,2) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_coordinates` (`id_coordinates`),
  CONSTRAINT `fk_coordinates` FOREIGN KEY (`id_coordinates`) REFERENCES `coordinates` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` VALUES (1,2,5.80),(2,1,4.00),(5,2,33.00),(7,1,14.14),(8,2,14.00),(9,2,14.00),(10,2,14.00),(11,2,4444.44);
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board_production`
--

DROP TABLE IF EXISTS `board_production`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_production` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_board` int NOT NULL,
  `positionServo` int NOT NULL,
  `date` datetime NOT NULL,
  `production` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_board_production` (`id_board`),
  CONSTRAINT `fk_board_production` FOREIGN KEY (`id_board`) REFERENCES `board` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board_production`
--

LOCK TABLES `board_production` WRITE;
/*!40000 ALTER TABLE `board_production` DISABLE KEYS */;
INSERT INTO `board_production` VALUES (1,2,442,'1212-12-12 12:12:12',20.227),(2,2,142,'1212-11-12 12:12:12',20.227);
/*!40000 ALTER TABLE `board_production` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `coordinates`
--

DROP TABLE IF EXISTS `coordinates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coordinates` (
  `id` int NOT NULL AUTO_INCREMENT,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coordinates`
--

LOCK TABLES `coordinates` WRITE;
/*!40000 ALTER TABLE `coordinates` DISABLE KEYS */;
INSERT INTO `coordinates` VALUES (1,25,25),(2,30,30),(3,35,35),(4,40,40),(5,20.2,20.888),(6,-3.5661,-1.25656);
/*!40000 ALTER TABLE `coordinates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_board` int NOT NULL,
  `date` datetime NOT NULL,
  `issue` longtext CHARACTER SET utf8 COLLATE utf8_spanish_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `pk_board` (`id_board`),
  CONSTRAINT `pk_board` FOREIGN KEY (`id_board`) REFERENCES `board` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
INSERT INTO `log` VALUES (1,1,'2021-04-30 18:00:00','Primer log'),(2,2,'2021-04-30 14:30:00','Segundo Log'),(3,1,'2021-04-30 14:30:00','Tercero Log');
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sunposition`
--

DROP TABLE IF EXISTS `sunposition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sunposition` (
  `id` int NOT NULL AUTO_INCREMENT,
  `id_coordinates` int NOT NULL,
  `date` datetime NOT NULL,
  `elevation` float NOT NULL,
  `azimut` float NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_coordinates` (`id_coordinates`),
  CONSTRAINT `fk_id_coordinates` FOREIGN KEY (`id_coordinates`) REFERENCES `coordinates` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sunposition`
--

LOCK TABLES `sunposition` WRITE;
/*!40000 ALTER TABLE `sunposition` DISABLE KEYS */;
INSERT INTO `sunposition` VALUES (1,2,'2020-04-30 17:00:00',25,30),(2,3,'2020-04-28 17:00:00',10,58),(3,1,'2020-04-20 12:00:00',40,15),(4,2,'1212-12-12 12:12:12',4.8,14),(5,4,'1212-12-12 12:12:12',4.8,14.7),(6,4,'1212-12-12 12:12:12',4.8,14.7),(7,4,'1212-12-12 12:12:12',4.8,14.7);
/*!40000 ALTER TABLE `sunposition` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-05-02 19:11:18
