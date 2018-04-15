CREATE DATABASE  IF NOT EXISTS `project` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `project`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: project
-- ------------------------------------------------------
-- Server version	5.7.21-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `adulto`
--

DROP TABLE IF EXISTS `adulto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `adulto` (
  `Nome` varchar(15) NOT NULL,
  `Cognome` varchar(25) NOT NULL,
  `CF` char(16) NOT NULL,
  `Mail` varchar(30) NOT NULL,
  `CAP` int(5) NOT NULL,
  `Provincia` char(2) NOT NULL,
  `Indirizzo` varchar(45) NOT NULL,
  `DataNascita` date NOT NULL,
  `CittaNascita` char(2) NOT NULL,
  `Tel` int(12) NOT NULL,
  `Pediatra` bit(1) NOT NULL,
  `Tutore` bit(1) NOT NULL,
  `Contatto` bit(1) NOT NULL,
  `Bambino_CodRif` int(6) unsigned NOT NULL,
  PRIMARY KEY (`CF`,`Bambino_CodRif`),
  KEY `fk_Adulto_Bambino1_idx` (`Bambino_CodRif`),
  CONSTRAINT `fk_Adulto_Bambino1` FOREIGN KEY (`Bambino_CodRif`) REFERENCES `bambino` (`CodRif`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adulto`
--

LOCK TABLES `adulto` WRITE;
/*!40000 ALTER TABLE `adulto` DISABLE KEYS */;
/*!40000 ALTER TABLE `adulto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bambino`
--

DROP TABLE IF EXISTS `bambino`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bambino` (
  `CodRif` int(6) unsigned NOT NULL,
  `Interni_CF` char(16) NOT NULL,
  PRIMARY KEY (`CodRif`,`Interni_CF`),
  UNIQUE KEY `CodRif_UNIQUE` (`CodRif`),
  KEY `fk_Bambino_Interni1_idx` (`Interni_CF`),
  CONSTRAINT `fk_Bambino_Interni1` FOREIGN KEY (`Interni_CF`) REFERENCES `interni` (`CF`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bambino`
--

LOCK TABLES `bambino` WRITE;
/*!40000 ALTER TABLE `bambino` DISABLE KEYS */;
INSERT INTO `bambino` VALUES (1,'PRRLCU09H10F205M'),(2,'PRRVGN10P56F205T');
/*!40000 ALTER TABLE `bambino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus`
--

DROP TABLE IF EXISTS `bus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bus` (
  `Targa` int(7) NOT NULL,
  `capienza` int(70) NOT NULL,
  `Fornitore_PIVA` int(30) NOT NULL,
  `Gita_NumeroGita` int(11) NOT NULL,
  `Gita_Interni_CF` int(16) NOT NULL,
  PRIMARY KEY (`Targa`,`Fornitore_PIVA`),
  UNIQUE KEY `Targa_UNIQUE` (`Targa`),
  KEY `fk_Bus_Fornitore1_idx` (`Fornitore_PIVA`),
  KEY `fk_Bus_Gita1_idx` (`Gita_NumeroGita`,`Gita_Interni_CF`),
  CONSTRAINT `fk_Bus_Fornitore1` FOREIGN KEY (`Fornitore_PIVA`) REFERENCES `fornitore` (`PIVA`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Bus_Gita1` FOREIGN KEY (`Gita_NumeroGita`) REFERENCES `gita` (`NumeroGita`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus`
--

LOCK TABLES `bus` WRITE;
/*!40000 ALTER TABLE `bus` DISABLE KEYS */;
/*!40000 ALTER TABLE `bus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fornitore`
--

DROP TABLE IF EXISTS `fornitore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fornitore` (
  `PIVA` int(30) NOT NULL,
  `NomeAz` varchar(45) NOT NULL,
  `Tel` int(11) NOT NULL,
  `Mail` varchar(30) NOT NULL,
  `Indirizzo` varchar(45) NOT NULL,
  `CAP` int(5) NOT NULL,
  `Provincia` varchar(45) NOT NULL,
  PRIMARY KEY (`PIVA`),
  UNIQUE KEY `PIVA_UNIQUE` (`PIVA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fornitore`
--

LOCK TABLES `fornitore` WRITE;
/*!40000 ALTER TABLE `fornitore` DISABLE KEYS */;
/*!40000 ALTER TABLE `fornitore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gita`
--

DROP TABLE IF EXISTS `gita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gita` (
  `Luogo` tinytext NOT NULL,
  `DataOraPar` datetime NOT NULL,
  `DataOraRit` datetime NOT NULL,
  `NumeroGita` int(11) NOT NULL,
  PRIMARY KEY (`NumeroGita`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gita`
--

LOCK TABLES `gita` WRITE;
/*!40000 ALTER TABLE `gita` DISABLE KEYS */;
/*!40000 ALTER TABLE `gita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gita_has_participants`
--

DROP TABLE IF EXISTS `gita_has_participants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gita_has_participants` (
  `Gita_NumeroGita` int(11) NOT NULL,
  `PresenteEff` bit(1) NOT NULL,
  `Bus_Targa` int(7) NOT NULL,
  `Bus_Fornitore_PIVA` int(30) NOT NULL,
  `Interni_CF` char(16) NOT NULL,
  PRIMARY KEY (`Gita_NumeroGita`,`Bus_Targa`,`Bus_Fornitore_PIVA`,`Interni_CF`),
  KEY `fk_Gita_has_Interni_Gita1_idx` (`Gita_NumeroGita`),
  KEY `fk_Gita_has_Participants_Bus1_idx` (`Bus_Targa`,`Bus_Fornitore_PIVA`),
  KEY `fk_Gita_has_Participants_Interni1_idx` (`Interni_CF`),
  CONSTRAINT `fk_Gita_has_Interni_Gita1` FOREIGN KEY (`Gita_NumeroGita`) REFERENCES `gita` (`NumeroGita`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Gita_has_Participants_Bus1` FOREIGN KEY (`Bus_Targa`, `Bus_Fornitore_PIVA`) REFERENCES `bus` (`Targa`, `Fornitore_PIVA`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Gita_has_Participants_Interni1` FOREIGN KEY (`Interni_CF`) REFERENCES `interni` (`CF`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gita_has_participants`
--

LOCK TABLES `gita_has_participants` WRITE;
/*!40000 ALTER TABLE `gita_has_participants` DISABLE KEYS */;
/*!40000 ALTER TABLE `gita_has_participants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredients`
--

DROP TABLE IF EXISTS `ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ingredients` (
  `ingredient` varchar(15) NOT NULL,
  `Fornitore_PIVA` int(30) NOT NULL,
  PRIMARY KEY (`ingredient`),
  UNIQUE KEY `ingredient_UNIQUE` (`ingredient`),
  KEY `fk_Ingredients_Fornitore1_idx` (`Fornitore_PIVA`),
  CONSTRAINT `fk_Ingredients_Fornitore1` FOREIGN KEY (`Fornitore_PIVA`) REFERENCES `fornitore` (`PIVA`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredients`
--

LOCK TABLES `ingredients` WRITE;
/*!40000 ALTER TABLE `ingredients` DISABLE KEYS */;
/*!40000 ALTER TABLE `ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interni`
--

DROP TABLE IF EXISTS `interni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interni` (
  `Cognome` varchar(15) NOT NULL,
  `Nome` varchar(15) NOT NULL,
  `CF` char(16) NOT NULL,
  `DataNascita` date NOT NULL,
  `CittaNascita` varchar(45) NOT NULL,
  `Residenza` varchar(45) NOT NULL,
  `Indirizzo` tinytext NOT NULL,
  `CAP` int(5) NOT NULL,
  `Provincia` char(2) NOT NULL,
  PRIMARY KEY (`CF`),
  UNIQUE KEY `CF_UNIQUE` (`CF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interni`
--

LOCK TABLES `interni` WRITE;
/*!40000 ALTER TABLE `interni` DISABLE KEYS */;
INSERT INTO `interni` VALUES ('Fieri','Mariapia','FRIMRP68C47D612B','1968-03-07','Firenze','Cremona','corso Garibaldi 51',26100,'CR'),('Perera','Luca','PRRLCU09H10F205M','2009-06-10','Milano','Milano Bovisa','via Gramsci 10',20140,'MI'),('Perera','Virginia','PRRVGN10P56F205T','2010-09-16','Milano','Milano Bovisa','via Gramsci 10',20140,'MI');
/*!40000 ALTER TABLE `interni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_base`
--

DROP TABLE IF EXISTS `menu_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_base` (
  `NumPiatti` int(11) NOT NULL,
  `entrees` tinytext,
  `main_courses` tinytext,
  `dessert` tinytext,
  `contorno` tinytext,
  PRIMARY KEY (`NumPiatti`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_base`
--

LOCK TABLES `menu_base` WRITE;
/*!40000 ALTER TABLE `menu_base` DISABLE KEYS */;
/*!40000 ALTER TABLE `menu_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_base_has_ingredients`
--

DROP TABLE IF EXISTS `menu_base_has_ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_base_has_ingredients` (
  `menu_base_NumPiatti` int(11) NOT NULL,
  `Ingredients_ingredient` varchar(15) NOT NULL,
  PRIMARY KEY (`menu_base_NumPiatti`,`Ingredients_ingredient`),
  KEY `fk_menu_base_has_Ingredients_Ingredients1_idx` (`Ingredients_ingredient`),
  KEY `fk_menu_base_has_Ingredients_menu_base1_idx` (`menu_base_NumPiatti`),
  CONSTRAINT `fk_menu_base_has_Ingredients_Ingredients1` FOREIGN KEY (`Ingredients_ingredient`) REFERENCES `ingredients` (`ingredient`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_menu_base_has_Ingredients_menu_base1` FOREIGN KEY (`menu_base_NumPiatti`) REFERENCES `menu_base` (`NumPiatti`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_base_has_ingredients`
--

LOCK TABLES `menu_base_has_ingredients` WRITE;
/*!40000 ALTER TABLE `menu_base_has_ingredients` DISABLE KEYS */;
/*!40000 ALTER TABLE `menu_base_has_ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_special`
--

DROP TABLE IF EXISTS `menu_special`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_special` (
  `NumPiatti` int(11) NOT NULL,
  `entrees` tinytext NOT NULL,
  `main_courses` tinytext NOT NULL,
  `dessert` tinytext NOT NULL,
  `contorno` tinytext NOT NULL,
  PRIMARY KEY (`NumPiatti`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_special`
--

LOCK TABLES `menu_special` WRITE;
/*!40000 ALTER TABLE `menu_special` DISABLE KEYS */;
/*!40000 ALTER TABLE `menu_special` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_special_has_ingredients`
--

DROP TABLE IF EXISTS `menu_special_has_ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_special_has_ingredients` (
  `menu_special_NumPiatti` int(11) NOT NULL,
  `Ingredients_ingredient` varchar(15) NOT NULL,
  PRIMARY KEY (`menu_special_NumPiatti`,`Ingredients_ingredient`),
  KEY `fk_menu_special_has_Ingredients_Ingredients1_idx` (`Ingredients_ingredient`),
  KEY `fk_menu_special_has_Ingredients_menu_special1_idx` (`menu_special_NumPiatti`),
  CONSTRAINT `fk_menu_special_has_Ingredients_Ingredients1` FOREIGN KEY (`Ingredients_ingredient`) REFERENCES `ingredients` (`ingredient`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_menu_special_has_Ingredients_menu_special1` FOREIGN KEY (`menu_special_NumPiatti`) REFERENCES `menu_special` (`NumPiatti`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_special_has_ingredients`
--

LOCK TABLES `menu_special_has_ingredients` WRITE;
/*!40000 ALTER TABLE `menu_special_has_ingredients` DISABLE KEYS */;
/*!40000 ALTER TABLE `menu_special_has_ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personaleint`
--

DROP TABLE IF EXISTS `personaleint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personaleint` (
  `Mail` tinytext NOT NULL,
  `CodID` int(4) NOT NULL,
  `Interni_CF` char(16) NOT NULL,
  PRIMARY KEY (`CodID`,`Interni_CF`),
  UNIQUE KEY `CodID_UNIQUE` (`CodID`),
  KEY `fk_PersonaleInt_Interni1_idx` (`Interni_CF`),
  CONSTRAINT `fk_PersonaleInt_Interni1` FOREIGN KEY (`Interni_CF`) REFERENCES `interni` (`CF`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personaleint`
--

LOCK TABLES `personaleint` WRITE;
/*!40000 ALTER TABLE `personaleint` DISABLE KEYS */;
INSERT INTO `personaleint` VALUES ('fieri.mariapia@cct.mailunion.com',56140,'FRIMRP68C47D612B');
/*!40000 ALTER TABLE `personaleint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userin`
--

DROP TABLE IF EXISTS `userin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userin` (
  `username` varchar(15) NOT NULL,
  `password` varchar(15) NOT NULL,
  PRIMARY KEY (`username`,`password`),
  UNIQUE KEY `userincol_UNIQUE` (`password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userin`
--

LOCK TABLES `userin` WRITE;
/*!40000 ALTER TABLE `userin` DISABLE KEYS */;
INSERT INTO `userin` VALUES ('eli','eli123'),('ludo','ludo123'),('ron','ron123'),('ron','ronald123');
/*!40000 ALTER TABLE `userin` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-04-15 14:12:32
