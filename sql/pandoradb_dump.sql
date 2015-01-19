-- MySQL dump 10.13  Distrib 5.6.16, for Win64 (x86_64)
--
-- Host: localhost    Database: pandoradb
-- ------------------------------------------------------
-- Server version	5.6.16

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
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `comment` (
  `COMMENT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTENT` varchar(255) NOT NULL,
  `DATE` datetime NOT NULL,
  `post_POST_ID` int(11) DEFAULT NULL,
  `user_USERNAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`COMMENT_ID`),
  UNIQUE KEY `COMMENT_ID` (`COMMENT_ID`),
  KEY `FK38A5EE5FEBA724CC` (`user_USERNAME`),
  KEY `FK38A5EE5F3DE04212` (`post_POST_ID`),
  CONSTRAINT `FK38A5EE5F3DE04212` FOREIGN KEY (`post_POST_ID`) REFERENCES `post` (`POST_ID`),
  CONSTRAINT `FK38A5EE5FEBA724CC` FOREIGN KEY (`user_USERNAME`) REFERENCES `user` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `document` (
  `DOCUMENT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `USERNAME` varchar(255) NOT NULL,
  `grupo_GROUP_ID` int(11) DEFAULT NULL,
  `subject_SUBJECT_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`DOCUMENT_ID`),
  UNIQUE KEY `DOCUMENT_ID` (`DOCUMENT_ID`),
  KEY `FK335CD11BFF4FDFD5` (`grupo_GROUP_ID`),
  KEY `FK335CD11B446C0192` (`subject_SUBJECT_ID`),
  CONSTRAINT `FK335CD11B446C0192` FOREIGN KEY (`subject_SUBJECT_ID`) REFERENCES `subject` (`SUBJECT_ID`),
  CONSTRAINT `FK335CD11BFF4FDFD5` FOREIGN KEY (`grupo_GROUP_ID`) REFERENCES `grupo` (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
/*!40000 ALTER TABLE `document` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grupo`
--

DROP TABLE IF EXISTS `grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `grupo` (
  `GROUP_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  `OWNER` varchar(255) NOT NULL,
  PRIMARY KEY (`GROUP_ID`),
  UNIQUE KEY `GROUP_ID` (`GROUP_ID`),
  UNIQUE KEY `NAME` (`NAME`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grupo`
--

LOCK TABLES `grupo` WRITE;
/*!40000 ALTER TABLE `grupo` DISABLE KEYS */;
INSERT INTO `grupo` VALUES (1,'EA Rules','user1'),(2,'PX - como mola el Pajek','user2');
/*!40000 ALTER TABLE `grupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `NOTIFICATION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `SEEN` char(1) NOT NULL,
  `TYPE` int(11) NOT NULL,
  `USERNAME` varchar(255) NOT NULL,
  `grupo_GROUP_ID` int(11) DEFAULT NULL,
  `subject_SUBJECT_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`NOTIFICATION_ID`),
  UNIQUE KEY `NOTIFICATION_ID` (`NOTIFICATION_ID`),
  KEY `FK237A88EBFF4FDFD5` (`grupo_GROUP_ID`),
  KEY `FK237A88EB446C0192` (`subject_SUBJECT_ID`),
  CONSTRAINT `FK237A88EB446C0192` FOREIGN KEY (`subject_SUBJECT_ID`) REFERENCES `subject` (`SUBJECT_ID`),
  CONSTRAINT `FK237A88EBFF4FDFD5` FOREIGN KEY (`grupo_GROUP_ID`) REFERENCES `grupo` (`GROUP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,'N',1,'user1',NULL,1),(2,'N',1,'user1',NULL,2),(3,'N',3,'user1',2,NULL);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post`
--

DROP TABLE IF EXISTS `post`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post` (
  `POST_ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONTENT` varchar(255) NOT NULL,
  `DATE` datetime DEFAULT NULL,
  `grupo_GROUP_ID` int(11) DEFAULT NULL,
  `subject_SUBJECT_ID` int(11) DEFAULT NULL,
  `user_USERNAME` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`POST_ID`),
  UNIQUE KEY `POST_ID` (`POST_ID`),
  KEY `FK3498A0FF4FDFD5` (`grupo_GROUP_ID`),
  KEY `FK3498A0EBA724CC` (`user_USERNAME`),
  KEY `FK3498A0446C0192` (`subject_SUBJECT_ID`),
  CONSTRAINT `FK3498A0446C0192` FOREIGN KEY (`subject_SUBJECT_ID`) REFERENCES `subject` (`SUBJECT_ID`),
  CONSTRAINT `FK3498A0EBA724CC` FOREIGN KEY (`user_USERNAME`) REFERENCES `user` (`USERNAME`),
  CONSTRAINT `FK3498A0FF4FDFD5` FOREIGN KEY (`grupo_GROUP_ID`) REFERENCES `grupo` (`GROUP_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post`
--

LOCK TABLES `post` WRITE;
/*!40000 ALTER TABLE `post` DISABLE KEYS */;
INSERT INTO `post` VALUES (1,'A alguien le falta un componente para el grupo?','2015-01-19 12:54:30',NULL,1,'user1'),(2,'Hoy hay clase?','2015-01-19 12:54:30',NULL,1,'user1'),(3,'Como nos organizamos?','2015-01-19 12:54:30',1,NULL,'user2');
/*!40000 ALTER TABLE `post` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subject`
--

DROP TABLE IF EXISTS `subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subject` (
  `SUBJECT_ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(255) NOT NULL,
  PRIMARY KEY (`SUBJECT_ID`),
  UNIQUE KEY `SUBJECT_ID` (`SUBJECT_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subject`
--

LOCK TABLES `subject` WRITE;
/*!40000 ALTER TABLE `subject` DISABLE KEYS */;
INSERT INTO `subject` VALUES (1,'Enginyeria d\'aplicacions'),(2,'Planificacio de xarxes'),(3,'Seguretat en xarxes'),(4,'Xarxes Locals, d\'Access i Metropolitanes'),(5,'Disseny d\'aplicacions'),(6,'Serveis Audivisuals sobre Internet'),(7,'Analisis i dimensionament de xarxes'),(8,'Xarxes de Transport');
/*!40000 ALTER TABLE `subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `USERNAME` varchar(255) NOT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `NAME` varchar(255) NOT NULL,
  `SURNAME` varchar(255) NOT NULL,
  `USERPASS` varchar(255) NOT NULL,
  PRIMARY KEY (`USERNAME`),
  UNIQUE KEY `USERNAME` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES ('twitter','twitter@twitter.es','twitter','twitter','twitter'),('user1','user1@ea.upc.es','Pepe','Garcia','user1'),('user2','user1@ea.upc.es','Jose','Perez','user2');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_grupo`
--

DROP TABLE IF EXISTS `user_grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_grupo` (
  `user_USERNAME` varchar(255) NOT NULL,
  `grupo_GROUP_ID` int(11) NOT NULL,
  KEY `FK72A916F5FF4FDFD5` (`grupo_GROUP_ID`),
  KEY `FK72A916F5EBA724CC` (`user_USERNAME`),
  CONSTRAINT `FK72A916F5EBA724CC` FOREIGN KEY (`user_USERNAME`) REFERENCES `user` (`USERNAME`),
  CONSTRAINT `FK72A916F5FF4FDFD5` FOREIGN KEY (`grupo_GROUP_ID`) REFERENCES `grupo` (`GROUP_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_grupo`
--

LOCK TABLES `user_grupo` WRITE;
/*!40000 ALTER TABLE `user_grupo` DISABLE KEYS */;
INSERT INTO `user_grupo` VALUES ('user1',1),('user2',2);
/*!40000 ALTER TABLE `user_grupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_subject`
--

DROP TABLE IF EXISTS `user_subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_subject` (
  `user_USERNAME` varchar(255) NOT NULL,
  `subject_SUBJECT_ID` int(11) NOT NULL,
  KEY `FKEB99ECB8EBA724CC` (`user_USERNAME`),
  KEY `FKEB99ECB8446C0192` (`subject_SUBJECT_ID`),
  CONSTRAINT `FKEB99ECB8446C0192` FOREIGN KEY (`subject_SUBJECT_ID`) REFERENCES `subject` (`SUBJECT_ID`),
  CONSTRAINT `FKEB99ECB8EBA724CC` FOREIGN KEY (`user_USERNAME`) REFERENCES `user` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_subject`
--

LOCK TABLES `user_subject` WRITE;
/*!40000 ALTER TABLE `user_subject` DISABLE KEYS */;
INSERT INTO `user_subject` VALUES ('user1',1),('user1',2),('user2',3);
/*!40000 ALTER TABLE `user_subject` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-01-19 12:55:10
