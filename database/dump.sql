-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: login
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
  `CF` int(16) NOT NULL,
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
  `Interni_CF` int(16) NOT NULL,
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
  `Prodotto` varchar(45) NOT NULL,
  `Tel` int(11) NOT NULL,
  `Mail` varchar(30) NOT NULL,
  `Indirizzo` varchar(45) NOT NULL,
  `CAP` int(5) NOT NULL,
  `Provincia` varchar(45) NOT NULL,
  `Menù_Numero` int(11) NOT NULL,
  PRIMARY KEY (`PIVA`,`Menù_Numero`),
  UNIQUE KEY `PIVA_UNIQUE` (`PIVA`),
  KEY `fk_Fornitore_Menù1_idx` (`Menù_Numero`),
  CONSTRAINT `fk_Fornitore_Menù1` FOREIGN KEY (`Menù_Numero`) REFERENCES `menù` (`Numero`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
  `Gita_Interni_CF` int(16) NOT NULL,
  `Interni_CF` int(16) NOT NULL,
  `PresenteEff` bit(1) NOT NULL,
  `Bus_Targa` int(7) NOT NULL,
  `Bus_Fornitore_PIVA` int(30) NOT NULL,
  PRIMARY KEY (`Gita_NumeroGita`,`Gita_Interni_CF`,`Interni_CF`,`Bus_Targa`,`Bus_Fornitore_PIVA`),
  KEY `fk_Gita_has_Interni_Interni1_idx` (`Interni_CF`),
  KEY `fk_Gita_has_Interni_Gita1_idx` (`Gita_NumeroGita`,`Gita_Interni_CF`),
  KEY `fk_Gita_has_Participants_Bus1_idx` (`Bus_Targa`,`Bus_Fornitore_PIVA`),
  CONSTRAINT `fk_Gita_has_Interni_Gita1` FOREIGN KEY (`Gita_NumeroGita`) REFERENCES `gita` (`NumeroGita`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Gita_has_Interni_Interni1` FOREIGN KEY (`Interni_CF`) REFERENCES `interni` (`CF`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Gita_has_Participants_Bus1` FOREIGN KEY (`Bus_Targa`, `Bus_Fornitore_PIVA`) REFERENCES `bus` (`Targa`, `Fornitore_PIVA`) ON DELETE NO ACTION ON UPDATE NO ACTION
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
-- Table structure for table `has_allergia`
--

DROP TABLE IF EXISTS `has_allergia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `has_allergia` (
  `Menù_Numero` int(11) NOT NULL,
  `Interni_CF` int(16) NOT NULL,
  `Has_Allergia` int(11) NOT NULL,
  `Allergene` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`Menù_Numero`,`Interni_CF`,`Has_Allergia`),
  KEY `fk_Menù_has_Interni_Interni1_idx` (`Interni_CF`),
  KEY `fk_Menù_has_Interni_Menù1_idx` (`Menù_Numero`),
  CONSTRAINT `fk_Menù_has_Interni_Interni1` FOREIGN KEY (`Interni_CF`) REFERENCES `interni` (`CF`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Menù_has_Interni_Menù1` FOREIGN KEY (`Menù_Numero`) REFERENCES `menù` (`Numero`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `has_allergia`
--

LOCK TABLES `has_allergia` WRITE;
/*!40000 ALTER TABLE `has_allergia` DISABLE KEYS */;
/*!40000 ALTER TABLE `has_allergia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interni`
--

DROP TABLE IF EXISTS `interni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interni` (
  `Nome` varchar(15) NOT NULL,
  `Cognome` varchar(15) NOT NULL,
  `CF` int(16) NOT NULL,
  `DataNascita` date NOT NULL,
  `CittaNascita` varchar(45) NOT NULL,
  `CAP` int(5) NOT NULL,
  `Indirizzo` tinytext NOT NULL,
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
/*!40000 ALTER TABLE `interni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menù`
--

DROP TABLE IF EXISTS `menù`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menù` (
  `Numero` int(11) NOT NULL,
  `Primo` tinytext,
  `Ingredienti` tinytext NOT NULL,
  `Secondo` tinytext,
  `Contorno` tinytext,
  `Dolce` tinytext,
  `Bevanda` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`Numero`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menù`
--

LOCK TABLES `menù` WRITE;
/*!40000 ALTER TABLE `menù` DISABLE KEYS */;
/*!40000 ALTER TABLE `menù` ENABLE KEYS */;
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
  `Interni_CF` int(16) NOT NULL,
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
/*!40000 ALTER TABLE `personaleint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userin`
--

DROP TABLE IF EXISTS `userin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userin` (
  `Username` varchar(15) NOT NULL,
  `Password` varchar(12) NOT NULL,
  PRIMARY KEY (`Username`,`Password`),
  UNIQUE KEY `Password_UNIQUE` (`Password`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userin`
--

LOCK TABLES `userin` WRITE;
/*!40000 ALTER TABLE `userin` DISABLE KEYS */;
INSERT INTO `userin` VALUES ('eli','eli123'),('ludo','ludo123'),('robin','rob123'),('robin','robin123'),('ron','ron123');
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

-- Dump completed on 2018-03-16 11:24:07
