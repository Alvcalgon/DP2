start transaction;

create database `Acme-Madruga`;

use `Acme-Madruga`;

create user 'acme-user'@'%' 
	identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';

create user 'acme-manager'@'%' 
	identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';

grant select, insert, update, delete 
	on `Acme-Madruga`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-Madruga`.* to 'acme-manager'@'%';
	
-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Acme-Madruga
-- ------------------------------------------------------
-- Server version	5.5.29

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
-- Table structure for table `administrator`
--

DROP TABLE IF EXISTS `administrator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `administrator` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_spammer` bit(1) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `score` double DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `user_account` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_7ohwsa2usmvu0yxb44je2lge` (`user_account`),
  CONSTRAINT `FK_7ohwsa2usmvu0yxb44je2lge` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (443,0,'Calle Palmera 4','fruiz@gmail.com',NULL,'','Fernando','63018754','https://previews.123rf.com/images/jemastock/jemastock1705/jemastock170509790/78457359-cara-hombre-arte-pop-estilo-imagen-vector-ilustraci%C3%B3n.jpg',0.2,'Ruiz',437),(444,0,NULL,'system@',NULL,NULL,'System',NULL,NULL,NULL,'System',442);
/*!40000 ALTER TABLE `administrator` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `area` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pictures` longtext,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_226rm1fd8fl8ewh0a7n1k8f94` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area` DISABLE KEYS */;
INSERT INTO `area` VALUES (445,0,'area1','http://gl.maps-bangkok.com/img/0/bangkok-%C3%A1rea-mapa.jpg'),(446,0,'area2','https://static.leonoticias.com/www/multimedia/201708/22/media/cortadas/mapacorres-kUUB-U40613444838HhE-624x385@Leonoticias.jpg');
/*!40000 ALTER TABLE `area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `box`
--

DROP TABLE IF EXISTS `box`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `box` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `is_system_box` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `actor` int(11) NOT NULL,
  `parent` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_r97hft7mf057u395h2vpulrno` (`name`,`parent`),
  KEY `UK_jobphec7hdlro6l8c5c1l7wcj` (`name`,`is_system_box`),
  KEY `FK_2byqkm71y34wbwpwr7s5m0enc` (`parent`),
  CONSTRAINT `FK_2byqkm71y34wbwpwr7s5m0enc` FOREIGN KEY (`parent`) REFERENCES `box` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `box`
--

LOCK TABLES `box` WRITE;
/*!40000 ALTER TABLE `box` DISABLE KEYS */;
INSERT INTO `box` VALUES (447,0,'','in box',443,NULL),(448,0,'','out box',443,NULL),(449,0,'','trash box',443,NULL),(450,0,'','spam box',443,NULL),(451,0,'','notification box',443,NULL),(452,0,'','in box',444,NULL),(453,0,'','out box',444,NULL),(454,0,'','trash box',444,NULL),(455,0,'','spam box',444,NULL),(456,0,'','notification box',444,NULL),(483,0,'','in box',460,NULL),(484,0,'','out box',460,NULL),(485,0,'','trash box',460,NULL),(486,0,'','spam box',460,NULL),(487,0,'','notification box',460,NULL),(488,0,'','in box',461,NULL),(489,0,'','out box',461,NULL),(490,0,'','trash box',461,NULL),(491,0,'','spam box',461,NULL),(492,0,'','notification box',461,NULL),(493,0,'','in box',457,NULL),(494,0,'','out box',457,NULL),(495,0,'','trash box',457,NULL),(496,0,'','spam box',457,NULL),(497,0,'','notification box',457,NULL),(498,0,'','in box',458,NULL),(499,0,'','out box',458,NULL),(500,0,'','trash box',458,NULL),(501,0,'','spam box',458,NULL),(502,0,'','notification box',458,NULL);
/*!40000 ALTER TABLE `box` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `box_messages`
--

DROP TABLE IF EXISTS `box_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `box_messages` (
  `box` int(11) NOT NULL,
  `messages` int(11) NOT NULL,
  KEY `FK_acfjrqu1jeixjmv14c0386o0s` (`messages`),
  KEY `FK_e6boieojekgfg919on0dci4na` (`box`),
  CONSTRAINT `FK_e6boieojekgfg919on0dci4na` FOREIGN KEY (`box`) REFERENCES `box` (`id`),
  CONSTRAINT `FK_acfjrqu1jeixjmv14c0386o0s` FOREIGN KEY (`messages`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `box_messages`
--

LOCK TABLES `box_messages` WRITE;
/*!40000 ALTER TABLE `box_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `box_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `brotherhood`
--

DROP TABLE IF EXISTS `brotherhood`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `brotherhood` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_spammer` bit(1) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `score` double DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `user_account` int(11) DEFAULT NULL,
  `establishment_date` date DEFAULT NULL,
  `pictures` longtext,
  `title` varchar(255) DEFAULT NULL,
  `area` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_oku65kpdi3ro8ta0bmmxdkidt` (`area`),
  KEY `FK_j7wkl7fdmmjo3c5wa21wo8nl` (`user_account`),
  CONSTRAINT `FK_j7wkl7fdmmjo3c5wa21wo8nl` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`),
  CONSTRAINT `FK_oku65kpdi3ro8ta0bmmxdkidt` FOREIGN KEY (`area`) REFERENCES `area` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `brotherhood`
--

LOCK TABLES `brotherhood` WRITE;
/*!40000 ALTER TABLE `brotherhood` DISABLE KEYS */;
INSERT INTO `brotherhood` VALUES (457,0,'Calle María 2','hermandadmaquinavirtual@gmail.com',NULL,'Virtual','Hermandad de la santísima Máquina Virtual','632014700','https://t1.uc.ltmcdn.com/images/0/3/0/img_como_crear_una_maquina_virtual_en_virtualbox_23030_600.jpg',NULL,'Box',440,'2007-09-20','https://cloud10.todocoleccion.online/antiguedades/tc/2014/04/27/13/43036847.jpg','Hermandad de la santísima',445),(458,0,'Calle Cristo 2','cristodemadera2@gmail.com',NULL,'Escalones','Hermandad del Cristo de la escalera','698523658','http://www.jesusnazarenoolias.es/hermandad/images/ESCUDO2015.png',NULL,'de Madera',441,'2001-07-20','https://img.milanuncios.com/fg/2182/56/218256741_1.jpg','Hermandad del Cristo',446);
/*!40000 ALTER TABLE `brotherhood` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customisation`
--

DROP TABLE IF EXISTS `customisation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customisation` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `column_limit` int(11) NOT NULL,
  `country_code` varchar(255) DEFAULT NULL,
  `english_welcome_message` varchar(255) DEFAULT NULL,
  `languages` varchar(255) DEFAULT NULL,
  `max_number_results` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `negative_words` varchar(255) DEFAULT NULL,
  `positive_words` varchar(255) DEFAULT NULL,
  `priorities` varchar(255) DEFAULT NULL,
  `row_limit` int(11) NOT NULL,
  `spam_words` varchar(255) DEFAULT NULL,
  `spanish_welcome_message` varchar(255) DEFAULT NULL,
  `threshold_score` double NOT NULL,
  `time_cached_results` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customisation`
--

LOCK TABLES `customisation` WRITE;
/*!40000 ALTER TABLE `customisation` DISABLE KEYS */;
INSERT INTO `customisation` VALUES (459,0,'https://image.ibb.co/iuaDgV/Untitled.png',2000,'+34','Welcome to Acme Madrugá! the site to organise your processions.','es,en',10,'Acme Madrugá','not,bad,horrible,average,disaster,no,mal,mediocre,desastre','good,fantastic,excellent,great,amazing,terrific,beautiful,bien,fantastico,excelente,genial,increible,terrorifico,hermosos','HIGH,NEUTRAL,LOW',3,'sex,viagra,cialis,one million,you\'ve been selected,Nigeria,sexo,un millon,ha sido seleccionado','¡Bienvenidos a Acme Madrugá! Tu sitio para organizar procesiones. ',-0.5,1);
/*!40000 ALTER TABLE `customisation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `enrolment`
--

DROP TABLE IF EXISTS `enrolment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `enrolment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `drop_out_moment` datetime DEFAULT NULL,
  `registered_moment` datetime DEFAULT NULL,
  `brotherhood` int(11) NOT NULL,
  `member` int(11) NOT NULL,
  `position` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_dfi3shy61xnydrrqq4kx3wd8n` (`drop_out_moment`,`registered_moment`,`position`),
  KEY `FK_kacft8i7jufll7t0nyk68cptn` (`brotherhood`),
  KEY `FK_o5re2u23cjomuht1q0fjmu09u` (`member`),
  KEY `FK_aopae51comq4kt7iadag2ygye` (`position`),
  CONSTRAINT `FK_aopae51comq4kt7iadag2ygye` FOREIGN KEY (`position`) REFERENCES `position` (`id`),
  CONSTRAINT `FK_kacft8i7jufll7t0nyk68cptn` FOREIGN KEY (`brotherhood`) REFERENCES `brotherhood` (`id`),
  CONSTRAINT `FK_o5re2u23cjomuht1q0fjmu09u` FOREIGN KEY (`member`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `enrolment`
--

LOCK TABLES `enrolment` WRITE;
/*!40000 ALTER TABLE `enrolment` DISABLE KEYS */;
/*!40000 ALTER TABLE `enrolment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finder`
--

DROP TABLE IF EXISTS `finder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `finder` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `area` varchar(255) DEFAULT NULL,
  `keyword` varchar(255) DEFAULT NULL,
  `maximum_date` date DEFAULT NULL,
  `minimum_date` date DEFAULT NULL,
  `updated_moment` datetime DEFAULT NULL,
  `member` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_469ycdp9tjhdqpue5lq91e8a9` (`member`),
  KEY `UK_bp5qpknid4jxrhov476xfdg24` (`keyword`,`area`,`minimum_date`,`maximum_date`),
  CONSTRAINT `FK_469ycdp9tjhdqpue5lq91e8a9` FOREIGN KEY (`member`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finder`
--

LOCK TABLES `finder` WRITE;
/*!40000 ALTER TABLE `finder` DISABLE KEYS */;
INSERT INTO `finder` VALUES (503,0,'area','','2020-10-05','2018-10-05','2019-01-01 10:10:00',460),(504,0,'','','2022-10-05','2005-12-14','2018-05-08 21:47:00',461);
/*!40000 ALTER TABLE `finder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finder_processions`
--

DROP TABLE IF EXISTS `finder_processions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `finder_processions` (
  `finder` int(11) NOT NULL,
  `processions` int(11) NOT NULL,
  KEY `FK_6p1eb8rm7luhusax3fd8uksw1` (`processions`),
  KEY `FK_1cueuewr7dicti6yua7dv4b6c` (`finder`),
  CONSTRAINT `FK_1cueuewr7dicti6yua7dv4b6c` FOREIGN KEY (`finder`) REFERENCES `finder` (`id`),
  CONSTRAINT `FK_6p1eb8rm7luhusax3fd8uksw1` FOREIGN KEY (`processions`) REFERENCES `procession` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finder_processions`
--

LOCK TABLES `finder_processions` WRITE;
/*!40000 ALTER TABLE `finder_processions` DISABLE KEYS */;
/*!40000 ALTER TABLE `finder_processions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `float`
--

DROP TABLE IF EXISTS `float`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `float` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `pictures` longtext,
  `title` varchar(255) DEFAULT NULL,
  `brotherhood` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_bmjnirgvwerdv604sfiusq45v` (`brotherhood`),
  CONSTRAINT `FK_bmjnirgvwerdv604sfiusq45v` FOREIGN KEY (`brotherhood`) REFERENCES `brotherhood` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `float`
--

LOCK TABLES `float` WRITE;
/*!40000 ALTER TABLE `float` DISABLE KEYS */;
/*!40000 ALTER TABLE `float` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hibernate_sequences`
--

DROP TABLE IF EXISTS `hibernate_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hibernate_sequences` (
  `sequence_name` varchar(255) DEFAULT NULL,
  `sequence_next_hi_value` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hibernate_sequences`
--

LOCK TABLES `hibernate_sequences` WRITE;
/*!40000 ALTER TABLE `hibernate_sequences` DISABLE KEYS */;
INSERT INTO `hibernate_sequences` VALUES ('domain_entity',1);
/*!40000 ALTER TABLE `hibernate_sequences` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `member` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `address` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `is_spammer` bit(1) DEFAULT NULL,
  `middle_name` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `photo` varchar(255) DEFAULT NULL,
  `score` double DEFAULT NULL,
  `surname` varchar(255) DEFAULT NULL,
  `user_account` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_porqrqrfw70onhs6pelgepxhu` (`user_account`),
  CONSTRAINT `FK_porqrqrfw70onhs6pelgepxhu` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (460,0,'Calle Sexta 6','joseantonioferreras@gmail.com',NULL,'','José Antonio','632014785','https://deca.upc.edu/ca/persones/pdi-template-shared/profile/@@images/bac720ab-ea23-4696-9bdd-15f87f706868.png',NULL,'Ferreras',438),(461,0,'Calle Niebla 6','mariafernanda@gmail.com',NULL,'','María Fernanda','698745214','https://t1.uc.ltmcdn.com/images/8/5/3/img_como_usar_el_aceite_de_arbol_de_te_en_la_cara_43358_600.jpg',NULL,'Lobato',439);
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `body` longtext,
  `is_spam` bit(1) NOT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `sent_moment` datetime DEFAULT NULL,
  `subject` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `sender` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_505vfvkjii4d93ypcv03urcd5` (`is_spam`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message_recipients`
--

DROP TABLE IF EXISTS `message_recipients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_recipients` (
  `message` int(11) NOT NULL,
  `recipients` int(11) NOT NULL,
  KEY `FK_1odmg2n3n487tvhuxx5oyyya2` (`message`),
  CONSTRAINT `FK_1odmg2n3n487tvhuxx5oyyya2` FOREIGN KEY (`message`) REFERENCES `message` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message_recipients`
--

LOCK TABLES `message_recipients` WRITE;
/*!40000 ALTER TABLE `message_recipients` DISABLE KEYS */;
/*!40000 ALTER TABLE `message_recipients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position`
--

DROP TABLE IF EXISTS `position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `position` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position`
--

LOCK TABLES `position` WRITE;
/*!40000 ALTER TABLE `position` DISABLE KEYS */;
INSERT INTO `position` VALUES (462,0),(463,0),(464,0),(465,0),(466,0),(467,0),(468,0);
/*!40000 ALTER TABLE `position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `position_translation_positions`
--

DROP TABLE IF EXISTS `position_translation_positions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `position_translation_positions` (
  `position` int(11) NOT NULL,
  `translation_positions` int(11) NOT NULL,
  UNIQUE KEY `UK_eqv8k12ma1f4ncn0fqemyih6w` (`translation_positions`),
  KEY `FK_nu330iad1yvj20qmnlg9h0xwe` (`position`),
  CONSTRAINT `FK_nu330iad1yvj20qmnlg9h0xwe` FOREIGN KEY (`position`) REFERENCES `position` (`id`),
  CONSTRAINT `FK_eqv8k12ma1f4ncn0fqemyih6w` FOREIGN KEY (`translation_positions`) REFERENCES `translation_position` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `position_translation_positions`
--

LOCK TABLES `position_translation_positions` WRITE;
/*!40000 ALTER TABLE `position_translation_positions` DISABLE KEYS */;
INSERT INTO `position_translation_positions` VALUES (462,469),(462,470),(463,471),(463,472),(464,473),(464,474),(465,475),(465,476),(466,477),(466,478),(467,479),(467,480),(468,481),(468,482);
/*!40000 ALTER TABLE `position_translation_positions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `procession`
--

DROP TABLE IF EXISTS `procession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `procession` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_final_mode` bit(1) NOT NULL,
  `matriz_procession` longblob,
  `moment` datetime DEFAULT NULL,
  `ticker` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sc8hr69nciikog00mms5616f8` (`ticker`),
  KEY `UK_p6xu93dmdn7jntgwpsjpkp56n` (`is_final_mode`,`ticker`,`title`,`description`,`moment`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `procession`
--

LOCK TABLES `procession` WRITE;
/*!40000 ALTER TABLE `procession` DISABLE KEYS */;
/*!40000 ALTER TABLE `procession` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `procession_floats`
--

DROP TABLE IF EXISTS `procession_floats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `procession_floats` (
  `procession` int(11) NOT NULL,
  `floats` int(11) NOT NULL,
  KEY `FK_h2i386rvrj0r7gc5bicglm0ab` (`floats`),
  KEY `FK_3kw2qfesi92aqi13ubidr6ffv` (`procession`),
  CONSTRAINT `FK_3kw2qfesi92aqi13ubidr6ffv` FOREIGN KEY (`procession`) REFERENCES `procession` (`id`),
  CONSTRAINT `FK_h2i386rvrj0r7gc5bicglm0ab` FOREIGN KEY (`floats`) REFERENCES `float` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `procession_floats`
--

LOCK TABLES `procession_floats` WRITE;
/*!40000 ALTER TABLE `procession_floats` DISABLE KEYS */;
/*!40000 ALTER TABLE `procession_floats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request`
--

DROP TABLE IF EXISTS `request`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `request` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `column_procession` int(11) DEFAULT NULL,
  `reason_why` varchar(255) DEFAULT NULL,
  `row_procession` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `member` int(11) NOT NULL,
  `procession` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_klo693510csw16kolut5uiklv` (`procession`,`member`),
  KEY `UK_gtsnidrsylpi5idb2jkk9w9x` (`status`),
  KEY `FK_hgv8wexlup4hjaqo4ki13th8v` (`member`),
  CONSTRAINT `FK_pihoapjtqc0dtknukqggpxmq6` FOREIGN KEY (`procession`) REFERENCES `procession` (`id`),
  CONSTRAINT `FK_hgv8wexlup4hjaqo4ki13th8v` FOREIGN KEY (`member`) REFERENCES `member` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request`
--

LOCK TABLES `request` WRITE;
/*!40000 ALTER TABLE `request` DISABLE KEYS */;
/*!40000 ALTER TABLE `request` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `social_profile`
--

DROP TABLE IF EXISTS `social_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `social_profile` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `link_profile` varchar(255) DEFAULT NULL,
  `nick` varchar(255) DEFAULT NULL,
  `social_network` varchar(255) DEFAULT NULL,
  `actor` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nedqor7tomp44srq0vbui1h6b` (`link_profile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `social_profile`
--

LOCK TABLES `social_profile` WRITE;
/*!40000 ALTER TABLE `social_profile` DISABLE KEYS */;
/*!40000 ALTER TABLE `social_profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `translation_position`
--

DROP TABLE IF EXISTS `translation_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `translation_position` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `language` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_abyod07xqbu9s88dqmbuwr7jr` (`name`,`language`),
  KEY `UK_7ll8r8494wfaw6knh3dcsqdqq` (`language`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `translation_position`
--

LOCK TABLES `translation_position` WRITE;
/*!40000 ALTER TABLE `translation_position` DISABLE KEYS */;
INSERT INTO `translation_position` VALUES (469,0,'en','President'),(470,0,'es','Presidente'),(471,0,'en','Vice President'),(472,0,'es','Vicepresidente'),(473,0,'en','Secretary'),(474,0,'es','Secretario'),(475,0,'en','Treasurer'),(476,0,'es','Tesorero'),(477,0,'en','Historian'),(478,0,'es','Historiador'),(479,0,'en','Fundraiser'),(480,0,'es','Promotor'),(481,0,'en','Officer'),(482,0,'es','Vocal');
/*!40000 ALTER TABLE `translation_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account`
--

DROP TABLE IF EXISTS `user_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `is_banned` bit(1) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_castjbvpeeus0r8lbpehiu0e4` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account`
--

LOCK TABLES `user_account` WRITE;
/*!40000 ALTER TABLE `user_account` DISABLE KEYS */;
INSERT INTO `user_account` VALUES (437,0,'\0','e00cf25ad42683b3df678c61f42c6bda','admin1'),(438,0,'\0','c7764cfed23c5ca3bb393308a0da2306','member1'),(439,0,'\0','88ed421f060aadcacbd63f28d889797f','member2'),(440,0,'\0','479e3180a45b21ea8e88beb0c45aa8ed','brotherhood1'),(441,0,'\0','88f1b810c40cd63107fb758fef4d77db','brotherhood2'),(442,0,'\0','54b53072540eeeb8f8e9343e71f28176','system');
/*!40000 ALTER TABLE `user_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_account_authorities`
--

DROP TABLE IF EXISTS `user_account_authorities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_account_authorities` (
  `user_account` int(11) NOT NULL,
  `authority` varchar(255) DEFAULT NULL,
  KEY `FK_pao8cwh93fpccb0bx6ilq6gsl` (`user_account`),
  CONSTRAINT `FK_pao8cwh93fpccb0bx6ilq6gsl` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_account_authorities`
--

LOCK TABLES `user_account_authorities` WRITE;
/*!40000 ALTER TABLE `user_account_authorities` DISABLE KEYS */;
INSERT INTO `user_account_authorities` VALUES (437,'ADMIN'),(438,'MEMBER'),(439,'MEMBER'),(440,'BROTHERHOOD'),(441,'BROTHERHOOD'),(442,'ADMIN');
/*!40000 ALTER TABLE `user_account_authorities` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-06 19:40:53

commit;