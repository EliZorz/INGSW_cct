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
  `Mail` varchar(45) NOT NULL,
  `CAP` int(5) NOT NULL,
  `Provincia` varchar(45) NOT NULL,
  `Indirizzo` varchar(45) NOT NULL,
  `DataNascita` date NOT NULL,
  `CittaNascita` char(25) NOT NULL,
  `Tel` varchar(25) NOT NULL,
  `Pediatra` tinyint(4) NOT NULL DEFAULT '0',
  `Tutore` tinyint(4) NOT NULL DEFAULT '0',
  `Contatto` tinyint(4) NOT NULL DEFAULT '0',
  `Bambino_CodRif` varchar(6) NOT NULL,
  PRIMARY KEY (`CF`,`Bambino_CodRif`),
  KEY `fk_Adulto_Bambino1_idx` (`Bambino_CodRif`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `adulto`
--

LOCK TABLES `adulto` WRITE;
/*!40000 ALTER TABLE `adulto` DISABLE KEYS */;
INSERT INTO `adulto` VALUES ('Ginevra','Berini','BRNLNR90D23R140O','berini@mailcct.com',26100,'CR','via Fratelli 8','1990-05-17','Verona','0564893652',1,0,1,'b6'),('Virginia','Mori','DMOLSE76P43R587E','elisa.domi@mailcct.com',26100,'CR','via Montichiari 89','1976-07-08','Mantova','6589475589',1,1,0,'b6'),('Eleonora','Federi','FDRLNR90T34R120O','federi@mailcct.com',24100,'CR','via Nobili 4','1990-09-06','Cremona','0256985472',1,0,0,'b3'),('Rita','Loserti','LSRRTI70P49D129E','rita.loserti@mailcct.com',20158,'CR','via Fermino 30 Milano','1983-05-06','Milano','6589654123',1,0,0,'b4'),('Rita','Loserti','LSRRTI70P49D129E','rita.loserti@mailcct.com',20158,'CR','via Fermino 30 Milano','1983-05-06','Milano','6589654123',1,0,1,'b5'),('MariaRosa','Perera','PRRLNE94P56D130S','elena@mailcct.com',26100,'CR','via Mondo 3','1994-08-11','Firenze','0532489656',1,0,0,'b6'),('Alessia','Perera','PRRLSS80S47F205O','perera.alessia@mailone.com',20140,'MI','Milano Bovisa','1980-11-07','Milano','3278599655',0,1,0,'b1'),('Amelia','Rossi','RSSMLA80P56F489O','amelia.rossi@mail.cct.com',26100,'CR','via Cali 34','1980-08-02','Cremona','0256986325',1,1,0,'b5'),('Daniele','Espera','SPRDNL80L02D150C','espera.daniele@mailone.com',20158,'MI','Milano Bovisa','1980-07-02','Milano','2569875441',0,1,1,'b2');
/*!40000 ALTER TABLE `adulto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bambino`
--

DROP TABLE IF EXISTS `bambino`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bambino` (
  `CodRif` varchar(6) NOT NULL,
  `Interni_CF` char(16) NOT NULL,
  PRIMARY KEY (`CodRif`,`Interni_CF`),
  UNIQUE KEY `CodRif_UNIQUE` (`CodRif`),
  KEY `fk_Bambino_Interni1_idx` (`Interni_CF`),
  CONSTRAINT `fk_Bambino_Interni1` FOREIGN KEY (`Interni_CF`) REFERENCES `interni` (`CF`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bambino`
--

LOCK TABLES `bambino` WRITE;
/*!40000 ALTER TABLE `bambino` DISABLE KEYS */;
INSERT INTO `bambino` VALUES ('b1','PRRLCU09H10F205M'),('b3','VRDNTS10P41D150R'),('b4','LAILEI10O45F130T'),('b5','RDOLAE34F45T578O'),('b6','MGGRSO10I09O987Y');
/*!40000 ALTER TABLE `bambino` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bus`
--

DROP TABLE IF EXISTS `bus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bus` (
  `Targa` varchar(7) NOT NULL,
  `capienza` int(70) NOT NULL,
  `Noleggio_PIVA` int(30) NOT NULL,
  PRIMARY KEY (`Targa`,`Noleggio_PIVA`),
  UNIQUE KEY `Targa_UNIQUE` (`Targa`),
  KEY `fk_bus_noleggio1_idx` (`Noleggio_PIVA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bus`
--

LOCK TABLES `bus` WRITE;
/*!40000 ALTER TABLE `bus` DISABLE KEYS */;
INSERT INTO `bus` VALUES ('AA456YY',2,123456789),('DD345RT',3,456231456),('FE432JH',2,456123789),('FF456TT',3,456231456),('HH456EY',4,123456789),('SS345RT',4,456123789);
/*!40000 ALTER TABLE `bus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dish_ingredients`
--

DROP TABLE IF EXISTS `dish_ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dish_ingredients` (
  `Nome_piatto` varchar(45) NOT NULL,
  `ingredients_ingredient` varchar(15) NOT NULL,
  PRIMARY KEY (`Nome_piatto`,`ingredients_ingredient`),
  KEY `fk_dish_ingredients_ingredients1_idx` (`ingredients_ingredient`),
  CONSTRAINT `fk_dish_ingredients_ingredients1` FOREIGN KEY (`ingredients_ingredient`) REFERENCES `ingredients` (`ingredient`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dish_ingredients`
--

LOCK TABLES `dish_ingredients` WRITE;
/*!40000 ALTER TABLE `dish_ingredients` DISABLE KEYS */;
INSERT INTO `dish_ingredients` VALUES ('banane','banane'),('Dessert strano','banane'),('Lasagne','carne'),('Pasta al ragù','carne'),('Senzapesce','carne'),('Side','carne'),('Torta fritta e salumi','carne'),('Boh','carote'),('carote','carote'),('Dessert strano','carote'),('Insalata e funghi','carote'),('Lasagne','carote'),('Torta di carote','carote'),('cocacola','cocacola'),('cococola','cocacola'),('Acqua','fragole'),('Entre','fragole'),('Entree','fragole'),('fragole','fragole'),('Senzapesce','fragole'),('Torta di fragole','fragole'),('Vico','fragole'),('Boh','funghi'),('Felicità','funghi'),('Funghetti','funghi'),('Insalata e funghi','funghi'),('pasta','funghi'),('Boh','insalata'),('carne','insalata'),('Felicità','insalata'),('insalata','insalata'),('Insalata e funghi','insalata'),('Lasagne','insalata'),('Dessert','Melone'),('Dessert strano','Melone'),('melone','Melone'),('Nuovo','Melone'),('YesMelone','Melone'),('Mirtilli','mirtilli'),('Pane e mirtilli','mirtilli'),('Torta ai mirtilli','mirtilli'),('Entre','pane'),('Entree','pane'),('Lasagne','pane'),('Pane','pane'),('Pane e mirtilli','pane'),('Pasta al ragù','pane'),('Senzapesce','pane'),('Torta ai mirtilli','pane'),('Torta di carote','pane'),('Torta di fragole','pane'),('Torta fritta e salumi','pane'),('Consommé','pesce'),('Felicità','pesce'),('Main','pesce'),('Pesce','pesce');
/*!40000 ALTER TABLE `dish_ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fornitore`
--

DROP TABLE IF EXISTS `fornitore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fornitore` (
  `PIVA` int(11) NOT NULL,
  `NomeAzienda` varchar(45) NOT NULL,
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
INSERT INTO `fornitore` VALUES (5632225,'Gente de zona',264852366,'gentedepais@mailcct.com','via Monfalcone 25',65422,'CU'),(123456789,'Banane',25698523,'banane@cct.com','via Bg 11',26100,'CR'),(123654789,'bd',2569822,'vgj',' bsuydfgw',2654,'cyhj'),(125698743,'cola',54566,'bkdfcuh','bisdufh',6548,'bujwg'),(152635645,'Pescado Fresco',37689562,'pescadofresco@mail.com','Mantova',46100,'MN'),(156870031,'Las Fresas de Milo',37265485,'fresasdemilo@mail.com','Cavatigozzi',26100,'CR'),(256332641,'Pane e panini Ferruccio',523826548,'panedani@mail.com','Monticelli',29010,'PC'),(256486523,'Solo Carne',1564856,'carnepesce@mail.com','Roma',118,'RO'),(256832647,'PatateFolli',583894548,'solopatate@mail.com','Monticelli',29010,'PC'),(564789321,'mirti',36543,'mirti@mailcct.com','jhfggrf',365,'bkj');
/*!40000 ALTER TABLE `fornitore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gita`
--

DROP TABLE IF EXISTS `gita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gita` (
  `Partenza` varchar(45) NOT NULL,
  `DataOraPar` date NOT NULL,
  `DataOraRit` date NOT NULL,
  `Alloggio` tinytext,
  `DataOraArr` date NOT NULL,
  `Destinazione` varchar(45) NOT NULL,
  `NumGita` varchar(8) NOT NULL,
  PRIMARY KEY (`NumGita`),
  UNIQUE KEY `NumGita_UNIQUE` (`NumGita`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gita`
--

LOCK TABLES `gita` WRITE;
/*!40000 ALTER TABLE `gita` DISABLE KEYS */;
INSERT INTO `gita` VALUES ('CR','2018-05-30','2018-05-30','','2018-05-30','CR','g1'),('CR','2018-05-29','2018-06-02','','2018-05-29','LK','g2');
/*!40000 ALTER TABLE `gita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gita_has_bus`
--

DROP TABLE IF EXISTS `gita_has_bus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gita_has_bus` (
  `gita_NumGita` varchar(8) NOT NULL,
  `bus_Targa` varchar(7) NOT NULL,
  PRIMARY KEY (`gita_NumGita`,`bus_Targa`),
  KEY `fk_gita_has_bus_bus1_idx` (`bus_Targa`),
  KEY `fk_gita_has_bus_gita1_idx` (`gita_NumGita`),
  CONSTRAINT `fk_gita_has_bus_bus1` FOREIGN KEY (`bus_Targa`) REFERENCES `bus` (`Targa`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_gita_has_bus_gita1` FOREIGN KEY (`gita_NumGita`) REFERENCES `gita` (`NumGita`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gita_has_bus`
--

LOCK TABLES `gita_has_bus` WRITE;
/*!40000 ALTER TABLE `gita_has_bus` DISABLE KEYS */;
INSERT INTO `gita_has_bus` VALUES ('g2','DD345RT'),('g1','HH456EY');
/*!40000 ALTER TABLE `gita_has_bus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ingredients`
--

DROP TABLE IF EXISTS `ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ingredients` (
  `ingredient` varchar(15) NOT NULL,
  `Fornitore_PIVA` int(11) NOT NULL,
  PRIMARY KEY (`ingredient`),
  KEY `fk_Ingredients_Fornitore1_idx` (`Fornitore_PIVA`),
  CONSTRAINT `fk_Ingredients_Fornitore1` FOREIGN KEY (`Fornitore_PIVA`) REFERENCES `fornitore` (`PIVA`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ingredients`
--

LOCK TABLES `ingredients` WRITE;
/*!40000 ALTER TABLE `ingredients` DISABLE KEYS */;
INSERT INTO `ingredients` VALUES ('Melone',5632225),('banane',123456789),('funghi',123654789),('insalata',123654789),('cocacola',125698743),('pesce',152635645),('carote',156870031),('fragole',156870031),('pane',256332641),('carne',256486523),('mirtilli',564789321);
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
  `DataNascita` date DEFAULT NULL,
  `CittaNascita` varchar(45) NOT NULL,
  `Residenza` varchar(45) NOT NULL,
  `Indirizzo` tinytext NOT NULL,
  `CAP` int(5) NOT NULL,
  `Provincia` varchar(45) NOT NULL,
  `Allergie` varchar(100) NOT NULL,
  PRIMARY KEY (`CF`,`Allergie`),
  UNIQUE KEY `CF_UNIQUE` (`CF`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interni`
--

LOCK TABLES `interni` WRITE;
/*!40000 ALTER TABLE `interni` DISABLE KEYS */;
INSERT INTO `interni` VALUES ('Eli','Alican','LAILEI10O45F130T','2010-05-10','Crema','Cremona','via Rita 5',26100,'CR','none'),('Rosa','Maggiore','MGGRSO10I09O987Y','2009-05-24','Chiuro','Cremona','via Buoso Dovara 4',26100,'CR','none'),('Perera','Luca','PRRLCU09H10F205M','2009-06-10','LK','Milano Bovisa','via Gramsci 10',20140,'MI','none'),('Ale','Rodi','RDOLAE34F45T578O','2009-10-16','Milano Cadorna','Cremona','via Volta 3',26100,'CR','none'),('Matteo','Rossi','RSSMTT32R45T465O','1973-05-10','Mantova','Cavatigozzi','via Giuseppina 4',26100,'CR','none'),('Virginia','Rossi','RSSVRG32R45T465O','1973-05-10','Mantova','Cavatigozzi','via Giuseppina 1',26100,'CR','none'),('Daniele','Viani','VNIDNL80G57F903I','1980-02-15','Cremona','Cremona','via Buoso 34',26100,'CR','none'),('Verdi','Anastasia','VRDNTS10P41D150R','2010-09-01','Genova','Cremona','via Garibaldi 57',26100,'CR','mirtilli, fragole, ');
/*!40000 ALTER TABLE `interni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interni_has_gita`
--

DROP TABLE IF EXISTS `interni_has_gita`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interni_has_gita` (
  `interni_CF` char(16) NOT NULL,
  `gita_NumGita` varchar(45) NOT NULL,
  `Partecipante_effettivo` tinyint(4) NOT NULL,
  PRIMARY KEY (`interni_CF`,`gita_NumGita`),
  KEY `fk_interni_has_gita_gita1_idx` (`gita_NumGita`),
  CONSTRAINT `fk_interni_has_gita_gita1` FOREIGN KEY (`gita_NumGita`) REFERENCES `gita` (`NumGita`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_interni_has_gita_interni1` FOREIGN KEY (`interni_CF`) REFERENCES `interni` (`CF`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interni_has_gita`
--

LOCK TABLES `interni_has_gita` WRITE;
/*!40000 ALTER TABLE `interni_has_gita` DISABLE KEYS */;
INSERT INTO `interni_has_gita` VALUES ('LAILEI10O45F130T','g1',1),('LAILEI10O45F130T','g2',0),('MGGRSO10I09O987Y','g2',1),('PRRLCU09H10F205M','g1',1),('RDOLAE34F45T578O','g2',1),('RSSMTT32R45T465O','g1',1),('RSSMTT32R45T465O','g2',0),('RSSVRG32R45T465O','g2',1),('VRDNTS10P41D150R','g1',1),('VRDNTS10P41D150R','g2',0);
/*!40000 ALTER TABLE `interni_has_gita` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interni_is_here`
--

DROP TABLE IF EXISTS `interni_is_here`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interni_is_here` (
  `interni_CF` char(16) NOT NULL,
  `gita_NumGita` varchar(8) NOT NULL,
  `bus_Targa` varchar(7) NOT NULL,
  `is_here` tinyint(4) NOT NULL,
  KEY `fk_interni_has_gita_has_bus_interni1_idx` (`interni_CF`),
  KEY `fk_interni_has_gita_has_bus_gita_has_bus1_idx` (`bus_Targa`),
  KEY `fk_interni_has_gita_gita1_idx` (`gita_NumGita`),
  KEY `fk_interni_is_here_gita_has_bus1_idx` (`bus_Targa`,`gita_NumGita`),
  CONSTRAINT `fk_interni_is_here_gita_has_bus1` FOREIGN KEY (`bus_Targa`, `gita_NumGita`) REFERENCES `gita_has_bus` (`bus_Targa`, `gita_NumGita`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_interni_is_here_interni_has_gita1` FOREIGN KEY (`interni_CF`) REFERENCES `interni_has_gita` (`interni_CF`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interni_is_here`
--

LOCK TABLES `interni_is_here` WRITE;
/*!40000 ALTER TABLE `interni_is_here` DISABLE KEYS */;
INSERT INTO `interni_is_here` VALUES ('PRRLCU09H10F205M','g1','HH456EY',1),('VRDNTS10P41D150R','g1','HH456EY',1),('RSSMTT32R45T465O','g1','HH456EY',1),('RDOLAE34F45T578O','g2','DD345RT',1),('MGGRSO10I09O987Y','g2','DD345RT',1),('RSSVRG32R45T465O','g2','DD345RT',1);
/*!40000 ALTER TABLE `interni_is_here` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `interno_has_allergia`
--

DROP TABLE IF EXISTS `interno_has_allergia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `interno_has_allergia` (
  `has_allergia` tinyint(4) NOT NULL DEFAULT '0',
  `interni_CF` char(16) NOT NULL,
  PRIMARY KEY (`has_allergia`,`interni_CF`),
  KEY `fk_interno_has_allergia_interni1_idx` (`interni_CF`),
  CONSTRAINT `fk_interno_has_allergia_interni1` FOREIGN KEY (`interni_CF`) REFERENCES `interni` (`CF`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `interno_has_allergia`
--

LOCK TABLES `interno_has_allergia` WRITE;
/*!40000 ALTER TABLE `interno_has_allergia` DISABLE KEYS */;
/*!40000 ALTER TABLE `interno_has_allergia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_base`
--

DROP TABLE IF EXISTS `menu_base`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_base` (
  `NumPiatti` int(11) NOT NULL,
  `entrees` text,
  `main_courses` text,
  `dessert` text,
  `side_dish` text,
  `drink` text,
  `date` date NOT NULL,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_base`
--

LOCK TABLES `menu_base` WRITE;
/*!40000 ALTER TABLE `menu_base` DISABLE KEYS */;
INSERT INTO `menu_base` VALUES (12,'Melone','Main','Pane e mirtilli','Carote','cocacola','2018-05-30'),(9,'Insalata e funghi','Main','Torta ai mirtilli','','cocacola','2018-06-08');
/*!40000 ALTER TABLE `menu_base` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_base_has_dish_ingredients`
--

DROP TABLE IF EXISTS `menu_base_has_dish_ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_base_has_dish_ingredients` (
  `menu_base_date` date NOT NULL,
  `dish_ingredients_Nome_piatto` varchar(45) NOT NULL,
  `dish_ingredients_ingredients_ingredient` varchar(15) NOT NULL,
  PRIMARY KEY (`menu_base_date`,`dish_ingredients_Nome_piatto`,`dish_ingredients_ingredients_ingredient`),
  KEY `fk_menu_base_has_dish_ingredients_dish_ingredients1_idx` (`dish_ingredients_Nome_piatto`,`dish_ingredients_ingredients_ingredient`),
  KEY `fk_menu_base_has_dish_ingredients_menu_base1_idx` (`menu_base_date`),
  CONSTRAINT `fk_menu_base_has_dish_ingredients_dish_ingredients1` FOREIGN KEY (`dish_ingredients_Nome_piatto`, `dish_ingredients_ingredients_ingredient`) REFERENCES `dish_ingredients` (`Nome_piatto`, `ingredients_ingredient`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_menu_base_has_dish_ingredients_menu_base1` FOREIGN KEY (`menu_base_date`) REFERENCES `menu_base` (`date`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_base_has_dish_ingredients`
--

LOCK TABLES `menu_base_has_dish_ingredients` WRITE;
/*!40000 ALTER TABLE `menu_base_has_dish_ingredients` DISABLE KEYS */;
INSERT INTO `menu_base_has_dish_ingredients` VALUES ('2018-05-30','Carote','carote'),('2018-05-30','cocacola','cocacola'),('2018-05-30','Main','pesce'),('2018-05-30','Melone','Melone'),('2018-05-30','Pane e mirtilli','mirtilli'),('2018-05-30','Pane e mirtilli','pane'),('2018-06-08','cocacola','cocacola'),('2018-06-08','Insalata e funghi','carote'),('2018-06-08','Insalata e funghi','funghi'),('2018-06-08','Insalata e funghi','insalata'),('2018-06-08','Main','pesce'),('2018-06-08','Torta ai mirtilli','mirtilli'),('2018-06-08','Torta ai mirtilli','pane');
/*!40000 ALTER TABLE `menu_base_has_dish_ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_special`
--

DROP TABLE IF EXISTS `menu_special`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_special` (
  `entrees` text,
  `main_courses` text,
  `dessert` text,
  `side_dish` text,
  `drink` text,
  `date` date NOT NULL,
  `interni_CF` char(16) NOT NULL,
  `interni_Allergie` varchar(100) NOT NULL,
  PRIMARY KEY (`date`,`interni_CF`,`interni_Allergie`),
  KEY `interni_idx` (`interni_CF`,`interni_Allergie`),
  CONSTRAINT `interni` FOREIGN KEY (`interni_CF`, `interni_Allergie`) REFERENCES `interni` (`CF`, `Allergie`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_special`
--

LOCK TABLES `menu_special` WRITE;
/*!40000 ALTER TABLE `menu_special` DISABLE KEYS */;
INSERT INTO `menu_special` VALUES ('Melone','Main','Lasagne','Carote','cocacola','2018-05-30','VRDNTS10P41D150R','mirtilli, fragole, '),('Insalata e funghi','Main','Dessert strano','','cocacola','2018-06-08','VRDNTS10P41D150R','mirtilli, fragole, ');
/*!40000 ALTER TABLE `menu_special` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_special_has_dish_ingredients`
--

DROP TABLE IF EXISTS `menu_special_has_dish_ingredients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `menu_special_has_dish_ingredients` (
  `menu_special_date` date NOT NULL,
  `menu_special_CF` char(16) NOT NULL,
  `menu_special_allergie` varchar(100) NOT NULL,
  `dish_ingredients_Nome_piatto` varchar(45) NOT NULL,
  `dish_ingredients_ingredients_ingredient` varchar(15) NOT NULL,
  PRIMARY KEY (`menu_special_date`,`menu_special_CF`,`menu_special_allergie`,`dish_ingredients_Nome_piatto`,`dish_ingredients_ingredients_ingredient`),
  KEY `fk_menu_special_has_dish_ingredients_dish_ingredients1_idx` (`dish_ingredients_Nome_piatto`,`dish_ingredients_ingredients_ingredient`),
  CONSTRAINT `fk_menu_special_has_dish_ingredients_dish_ingredients1` FOREIGN KEY (`dish_ingredients_Nome_piatto`, `dish_ingredients_ingredients_ingredient`) REFERENCES `dish_ingredients` (`Nome_piatto`, `ingredients_ingredient`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_menu_special_has_dish_ingredients_menu_special1` FOREIGN KEY (`menu_special_date`, `menu_special_CF`, `menu_special_allergie`) REFERENCES `menu_special` (`date`, `interni_CF`, `interni_Allergie`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_special_has_dish_ingredients`
--

LOCK TABLES `menu_special_has_dish_ingredients` WRITE;
/*!40000 ALTER TABLE `menu_special_has_dish_ingredients` DISABLE KEYS */;
INSERT INTO `menu_special_has_dish_ingredients` VALUES ('2018-05-30','VRDNTS10P41D150R','mirtilli, fragole, ','Carote','carote'),('2018-05-30','VRDNTS10P41D150R','mirtilli, fragole, ','cocacola','cocacola'),('2018-06-08','VRDNTS10P41D150R','mirtilli, fragole, ','cocacola','cocacola'),('2018-06-08','VRDNTS10P41D150R','mirtilli, fragole, ','Dessert strano','banane'),('2018-06-08','VRDNTS10P41D150R','mirtilli, fragole, ','Dessert strano','carote'),('2018-06-08','VRDNTS10P41D150R','mirtilli, fragole, ','Dessert strano','Melone'),('2018-06-08','VRDNTS10P41D150R','mirtilli, fragole, ','Insalata e funghi','carote'),('2018-06-08','VRDNTS10P41D150R','mirtilli, fragole, ','Insalata e funghi','funghi'),('2018-06-08','VRDNTS10P41D150R','mirtilli, fragole, ','Insalata e funghi','insalata'),('2018-05-30','VRDNTS10P41D150R','mirtilli, fragole, ','Lasagne','carne'),('2018-05-30','VRDNTS10P41D150R','mirtilli, fragole, ','Lasagne','carote'),('2018-05-30','VRDNTS10P41D150R','mirtilli, fragole, ','Lasagne','insalata'),('2018-05-30','VRDNTS10P41D150R','mirtilli, fragole, ','Lasagne','pane'),('2018-05-30','VRDNTS10P41D150R','mirtilli, fragole, ','Main','pesce'),('2018-06-08','VRDNTS10P41D150R','mirtilli, fragole, ','Main','pesce'),('2018-05-30','VRDNTS10P41D150R','mirtilli, fragole, ','Melone','Melone');
/*!40000 ALTER TABLE `menu_special_has_dish_ingredients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `noleggio`
--

DROP TABLE IF EXISTS `noleggio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `noleggio` (
  `PIVA` int(11) NOT NULL,
  `NomeAzienda` varchar(45) NOT NULL,
  `Tel` varchar(10) NOT NULL,
  `Mail` varchar(30) NOT NULL,
  `Indirizzo` varchar(45) NOT NULL,
  `CAP` int(5) NOT NULL,
  `Provincia` varchar(45) NOT NULL,
  PRIMARY KEY (`PIVA`),
  UNIQUE KEY `PIVA_UNIQUE` (`PIVA`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `noleggio`
--

LOCK TABLES `noleggio` WRITE;
/*!40000 ALTER TABLE `noleggio` DISABLE KEYS */;
INSERT INTO `noleggio` VALUES (123456789,'tonino','0213659844','toninobus@mailcct.com','via del Sale 14',26100,'CR'),(456123789,'busRomeoColosseo','0214563211','busRomeo@colosseo.com','pzza Roma 1',26100,'CR'),(456231456,'busdomodossola','0214563325','busdomodossola@mailcct.com','via Ghiribella 1',26511,'VCO');
/*!40000 ALTER TABLE `noleggio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personaleint`
--

DROP TABLE IF EXISTS `personaleint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `personaleint` (
  `Mail` tinytext NOT NULL,
  `CodID` varchar(6) NOT NULL,
  `Interni_CF` char(16) NOT NULL,
  PRIMARY KEY (`CodID`,`Interni_CF`),
  UNIQUE KEY `CodID_UNIQUE` (`CodID`),
  KEY `fk_PersonaleInt_Interni1_idx` (`Interni_CF`),
  CONSTRAINT `fk_PersonaleInt_Interni1` FOREIGN KEY (`Interni_CF`) REFERENCES `interni` (`CF`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personaleint`
--

LOCK TABLES `personaleint` WRITE;
/*!40000 ALTER TABLE `personaleint` DISABLE KEYS */;
INSERT INTO `personaleint` VALUES ('rossi.virginia@mailunica.com','s2','RSSVRG32R45T465O'),('rossi.matteo@mailunica.com','s3','RSSMTT32R45T465O'),('danieleviani@cctmail.com','s5','VNIDNL80G57F903I');
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

-- Dump completed on 2018-05-27  0:06:12
