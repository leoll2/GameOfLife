CREATE DATABASE  IF NOT EXISTS `giocodellavita` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `giocodellavita`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: 127.0.0.1    Database: giocodellavita
-- ------------------------------------------------------
-- Server version	5.6.21

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
-- Table structure for table `celle`
--

DROP TABLE IF EXISTS `celle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `celle` (
  `nomeModello` varchar(45) NOT NULL,
  `numeroCella` int(11) NOT NULL,
  PRIMARY KEY (`nomeModello`,`numeroCella`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `celle`
--

LOCK TABLES `celle` WRITE;
/*!40000 ALTER TABLE `celle` DISABLE KEYS */;
INSERT INTO `celle` VALUES ('Bloom',82),('Bloom',97),('Bloom',111),('Bloom',113),('Bloom',126),('Bloom',128),('Bloom',142),('Glider',22),('Glider',43),('Glider',61),('Glider',62),('Glider',63),('Glider Gun',65),('Glider Gun',103),('Glider Gun',105),('Glider Gun',133),('Glider Gun',134),('Glider Gun',141),('Glider Gun',142),('Glider Gun',155),('Glider Gun',156),('Glider Gun',172),('Glider Gun',176),('Glider Gun',181),('Glider Gun',182),('Glider Gun',195),('Glider Gun',196),('Glider Gun',201),('Glider Gun',202),('Glider Gun',211),('Glider Gun',217),('Glider Gun',221),('Glider Gun',222),('Glider Gun',241),('Glider Gun',242),('Glider Gun',251),('Glider Gun',255),('Glider Gun',257),('Glider Gun',258),('Glider Gun',263),('Glider Gun',265),('Glider Gun',291),('Glider Gun',297),('Glider Gun',305),('Glider Gun',332),('Glider Gun',336),('Glider Gun',373),('Glider Gun',374),('Java',122),('Java',123),('Java',124),('Java',125),('Java',128),('Java',129),('Java',132),('Java',135),('Java',138),('Java',139),('Java',149),('Java',151),('Java',154),('Java',156),('Java',159),('Java',161),('Java',164),('Java',173),('Java',175),('Java',178),('Java',180),('Java',183),('Java',185),('Java',188),('Java',197),('Java',199),('Java',200),('Java',201),('Java',202),('Java',204),('Java',207),('Java',209),('Java',210),('Java',211),('Java',212),('Java',218),('Java',221),('Java',223),('Java',226),('Java',228),('Java',230),('Java',233),('Java',236),('Java',243),('Java',244),('Java',247),('Java',250),('Java',253),('Java',257),('Java',260),('Lamp',49),('Lamp',59),('Lamp',60),('Lamp',61),('Pulsar',38),('Pulsar',39),('Pulsar',40),('Pulsar',44),('Pulsar',45),('Pulsar',46),('Pulsar',70),('Pulsar',75),('Pulsar',77),('Pulsar',82),('Pulsar',87),('Pulsar',92),('Pulsar',94),('Pulsar',99),('Pulsar',104),('Pulsar',109),('Pulsar',111),('Pulsar',116),('Pulsar',123),('Pulsar',124),('Pulsar',125),('Pulsar',129),('Pulsar',130),('Pulsar',131),('Pulsar',157),('Pulsar',158),('Pulsar',159),('Pulsar',163),('Pulsar',164),('Pulsar',165),('Pulsar',172),('Pulsar',177),('Pulsar',179),('Pulsar',184),('Pulsar',189),('Pulsar',194),('Pulsar',196),('Pulsar',201),('Pulsar',206),('Pulsar',211),('Pulsar',213),('Pulsar',218),('Pulsar',242),('Pulsar',243),('Pulsar',244),('Pulsar',248),('Pulsar',249),('Pulsar',250);
/*!40000 ALTER TABLE `celle` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `modelli`
--

DROP TABLE IF EXISTS `modelli`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `modelli` (
  `nomeModello` varchar(45) NOT NULL,
  `nomeAutore` varchar(45) NOT NULL,
  `dataSalvataggio` datetime NOT NULL,
  `larghezza` int(11) NOT NULL,
  `altezza` int(11) NOT NULL,
  PRIMARY KEY (`nomeModello`),
  UNIQUE KEY `nomeModello_UNIQUE` (`nomeModello`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `modelli`
--

LOCK TABLES `modelli` WRITE;
/*!40000 ALTER TABLE `modelli` DISABLE KEYS */;
INSERT INTO `modelli` VALUES ('Bloom','Leonardo','2018-02-15 17:05:43',15,15),('Glider','Conway','1970-10-01 12:00:00',20,20),('Glider Gun','Gosper','1970-11-22 00:00:00',40,40),('Java','Gosling','2018-02-25 19:59:37',24,18),('Lamp','Leonardo','2018-02-14 23:58:29',11,11),('Pulsar','Leonardo','2018-02-15 17:44:22',17,17);
/*!40000 ALTER TABLE `modelli` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
