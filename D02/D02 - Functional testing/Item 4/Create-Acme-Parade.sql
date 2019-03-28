start transaction;

create database `Acme-Parade`;

use `Acme-Parade`;

create user 'acme-user'@'%' 
	identified by password '*4F10007AADA9EE3DBB2CC36575DFC6F4FDE27577';

create user 'acme-manager'@'%' 
	identified by password '*FDB8CD304EB2317D10C95D797A4BD7492560F55F';

grant select, insert, update, delete 
	on `Acme-Parade`.* to 'acme-user'@'%';

grant select, insert, update, delete, create, drop, references, index, alter, 
        create temporary tables, lock tables, create view, create routine, 
        alter routine, execute, trigger, show view
    on `Acme-Parade`.* to 'acme-manager'@'%';

-- MySQL dump 10.13  Distrib 5.5.29, for Win64 (x86)
--
-- Host: localhost    Database: Acme-Parade
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
  UNIQUE KEY `UK_jj3mmcc0vjobqibj67dvuwihk` (`email`),
  KEY `FK_7ohwsa2usmvu0yxb44je2lge` (`user_account`),
  CONSTRAINT `FK_7ohwsa2usmvu0yxb44je2lge` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `administrator`
--

LOCK TABLES `administrator` WRITE;
/*!40000 ALTER TABLE `administrator` DISABLE KEYS */;
INSERT INTO `administrator` VALUES (1067,0,'Calle Palmera 4','fruiz@gmail.com',NULL,'','Fernando','63018754','https://previews.123rf.com/images/jemastock/jemastock1705/jemastock170509790/78457359-cara-hombre-arte-pop-estilo-imagen-vector-ilustraci%C3%B3n.jpg',0.2,'Ruiz',1057),(1068,0,NULL,'system@',NULL,NULL,'System',NULL,NULL,NULL,'System',1058);
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
INSERT INTO `area` VALUES (1069,0,'area1','http://gl.maps-bangkok.com/img/0/bangkok-%C3%A1rea-mapa.jpg'),(1070,0,'area2','https://static.leonoticias.com/www/multimedia/201708/22/media/cortadas/mapacorres-kUUB-U40613444838HhE-624x385@Leonoticias.jpg');
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
  KEY `UK_l5g0jmje8o7jspo3o3krds6c0` (`actor`,`name`,`is_system_box`),
  KEY `UK_5e9rnbc6727egwfcsvbl1le8f` (`actor`,`parent`),
  KEY `FK_2byqkm71y34wbwpwr7s5m0enc` (`parent`),
  CONSTRAINT `FK_2byqkm71y34wbwpwr7s5m0enc` FOREIGN KEY (`parent`) REFERENCES `box` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `box`
--

LOCK TABLES `box` WRITE;
/*!40000 ALTER TABLE `box` DISABLE KEYS */;
INSERT INTO `box` VALUES (1071,0,'','in box',1067,NULL),(1072,0,'','out box',1067,NULL),(1073,0,'','trash box',1067,NULL),(1074,0,'','spam box',1067,NULL),(1075,0,'','notification box',1067,NULL),(1076,0,'','in box',1068,NULL),(1077,0,'','out box',1068,NULL),(1078,0,'','trash box',1068,NULL),(1079,0,'','spam box',1068,NULL),(1080,0,'','notification box',1068,NULL),(1111,0,'','in box',1086,NULL),(1112,0,'','out box',1086,NULL),(1113,0,'','trash box',1086,NULL),(1114,0,'','spam box',1086,NULL),(1115,0,'','notification box',1086,NULL),(1116,0,'','in box',1087,NULL),(1117,0,'','out box',1087,NULL),(1118,0,'','trash box',1087,NULL),(1119,0,'','spam box',1087,NULL),(1120,0,'','notification box',1087,NULL),(1121,0,'','in box',1081,NULL),(1122,0,'','out box',1081,NULL),(1123,0,'','trash box',1081,NULL),(1124,0,'','spam box',1081,NULL),(1125,0,'','notification box',1081,NULL),(1126,0,'','in box',1082,NULL),(1127,0,'','out box',1082,NULL),(1128,0,'','trash box',1082,NULL),(1129,0,'','spam box',1082,NULL),(1130,0,'','notification box',1082,NULL),(1131,0,'','in box',1095,NULL),(1132,0,'','out box',1095,NULL),(1133,0,'','trash box',1095,NULL),(1134,0,'','spam box',1095,NULL),(1135,0,'','notification box',1095,NULL),(1136,0,'','in box',1096,NULL),(1137,0,'','out box',1096,NULL),(1138,0,'','trash box',1096,NULL),(1139,0,'','spam box',1096,NULL),(1140,0,'','notification box',1096,NULL),(1141,0,'','in box',1083,NULL),(1142,0,'','out box',1083,NULL),(1143,0,'','trash box',1083,NULL),(1144,0,'','spam box',1083,NULL),(1145,0,'','notification box',1083,NULL),(1146,0,'','in box',1084,NULL),(1147,0,'','out box',1084,NULL),(1148,0,'','trash box',1084,NULL),(1149,0,'','spam box',1084,NULL),(1150,0,'','notification box',1084,NULL);
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
  UNIQUE KEY `UK_frot1xp3o54kpoirio1wtaxgm` (`email`),
  KEY `FK_oku65kpdi3ro8ta0bmmxdkidt` (`area`),
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
INSERT INTO `brotherhood` VALUES (1081,0,'Calle María 2','hermandadmaquinavirtual@gmail.com',NULL,'Virtual','Hermandad de la santísima Máquina Virtual','632014700','https://t1.uc.ltmcdn.com/images/0/3/0/img_como_crear_una_maquina_virtual_en_virtualbox_23030_600.jpg',NULL,'Box',1061,'2007-09-20','https://cloud10.todocoleccion.online/antiguedades/tc/2014/04/27/13/43036847.jpg','Hermandad de la santísima',1069),(1082,0,'Calle Cristo 2','cristodemadera2@gmail.com',NULL,'Escalones','Hermandad del Cristo de la escalera','698523658','http://www.jesusnazarenoolias.es/hermandad/images/ESCUDO2015.png',NULL,'de Madera',1062,'2001-07-20','https://img.milanuncios.com/fg/2182/56/218256741_1.jpg','Hermandad del Cristo',1070);
/*!40000 ALTER TABLE `brotherhood` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chapter`
--

DROP TABLE IF EXISTS `chapter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `chapter` (
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
  `title` varchar(255) DEFAULT NULL,
  `area` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_kq7utiirwp7g4fyjok1pe4tp0` (`email`),
  KEY `FK_g1jjg80txjhuvgdkb84om9q9p` (`area`),
  KEY `FK_j0iwie78xmrf4kapbyfbgl8uo` (`user_account`),
  CONSTRAINT `FK_j0iwie78xmrf4kapbyfbgl8uo` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`),
  CONSTRAINT `FK_g1jjg80txjhuvgdkb84om9q9p` FOREIGN KEY (`area`) REFERENCES `area` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chapter`
--

LOCK TABLES `chapter` WRITE;
/*!40000 ALTER TABLE `chapter` DISABLE KEYS */;
INSERT INTO `chapter` VALUES (1083,0,'Calle Principal 2','alfonsorperez@gmail.com',NULL,'','Alfonso','+34 639852147','https://d500.epimg.net/cincodias/imagenes/2016/03/16/lifestyle/1458143779_942162_1458143814_noticia_normal.jpg',NULL,'Perez',1065,'Chapter 1',1069),(1084,0,'Calle Secundaria 2','carmenruiz@gmail.com',NULL,'','Carmen','+34 654987321','https://d500.epimg.net/cincodias/imagenes/2016/03/16/lifestyle/1458143779_942162_1458143814_noticia_normal.jpg',NULL,'Ruiz',1066,'Chapter 2',1070);
/*!40000 ALTER TABLE `chapter` ENABLE KEYS */;
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
  `credit_card_makes` varchar(255) DEFAULT NULL,
  `english_welcome_message` varchar(255) DEFAULT NULL,
  `fare` double NOT NULL,
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
  `vat_percentage` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customisation`
--

LOCK TABLES `customisation` WRITE;
/*!40000 ALTER TABLE `customisation` DISABLE KEYS */;
INSERT INTO `customisation` VALUES (1085,0,'https://i.ibb.co/Thcj1dW/acme-parade-1.jpg',2000,'+34','VISA,MCARD,AMEX,DINNERS,FLY','Welcome to Acme Madrugá! the site to organise your parades.',500,'es,en',10,'Acme Madrugá','not,bad,horrible,average,disaster,no,mal,mediocre,desastre','good,fantastic,excellent,great,amazing,terrific,beautiful,bien,fantastico,excelente,genial,increible,terrorifico,hermosos','HIGH,NEUTRAL,LOW',3,'sex,viagra,cialis,one million,you\'ve been selected,Nigeria,sexo,un millon,ha sido seleccionado','¡Bienvenidos a Acme Madrugá! Tu sitio para organizar desfiles. ',-0.5,1,0.21);
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
  KEY `UK_bhyp1fkadfvo3edsycf165fnc` (`brotherhood`,`drop_out_moment`,`registered_moment`),
  KEY `UK_s82o14lmc5yfn97gxyrscpro3` (`brotherhood`,`drop_out_moment`,`registered_moment`,`member`),
  KEY `UK_gmmuyl43b7j56q33iwfx3nud6` (`brotherhood`,`drop_out_moment`,`registered_moment`,`member`,`position`),
  KEY `UK_c4i979euif92mm9ywqoae513s` (`registered_moment`,`member`),
  KEY `UK_ltryfyssi2p7um0gdxrlp4i6k` (`registered_moment`,`drop_out_moment`,`member`),
  KEY `UK_ulnkeq0i3pkrdlogdr3u0bxp` (`drop_out_moment`,`registered_moment`),
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
INSERT INTO `finder` VALUES (1151,0,'area','','2020-10-05','2018-10-05','2019-01-01 10:10:00',1086),(1152,0,'','','2022-10-05','2005-12-14','2018-05-08 21:47:00',1087);
/*!40000 ALTER TABLE `finder` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finder_parades`
--

DROP TABLE IF EXISTS `finder_parades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `finder_parades` (
  `finder` int(11) NOT NULL,
  `parades` int(11) NOT NULL,
  KEY `FK_a7t9ojmirwd7ijfq42ffessn5` (`parades`),
  KEY `FK_8pff5xgqq7qfh2ciyx24p67sp` (`finder`),
  CONSTRAINT `FK_8pff5xgqq7qfh2ciyx24p67sp` FOREIGN KEY (`finder`) REFERENCES `finder` (`id`),
  CONSTRAINT `FK_a7t9ojmirwd7ijfq42ffessn5` FOREIGN KEY (`parades`) REFERENCES `parade` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finder_parades`
--

LOCK TABLES `finder_parades` WRITE;
/*!40000 ALTER TABLE `finder_parades` DISABLE KEYS */;
/*!40000 ALTER TABLE `finder_parades` ENABLE KEYS */;
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
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `brotherhood` int(11) NOT NULL,
  `inception_record` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_cqm844lynupi3gfm0yjcinrlt` (`brotherhood`),
  UNIQUE KEY `UK_ft70pl0noyt3c2bufgsyepbbe` (`inception_record`),
  CONSTRAINT `FK_ft70pl0noyt3c2bufgsyepbbe` FOREIGN KEY (`inception_record`) REFERENCES `inception_record` (`id`),
  CONSTRAINT `FK_cqm844lynupi3gfm0yjcinrlt` FOREIGN KEY (`brotherhood`) REFERENCES `brotherhood` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history_legal_records`
--

DROP TABLE IF EXISTS `history_legal_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history_legal_records` (
  `history` int(11) NOT NULL,
  `legal_records` int(11) NOT NULL,
  UNIQUE KEY `UK_4cwwxrddivsxn1pdy0vhb1ywj` (`legal_records`),
  KEY `FK_6q06x59ns3cq8hmomnvifnrl` (`history`),
  CONSTRAINT `FK_6q06x59ns3cq8hmomnvifnrl` FOREIGN KEY (`history`) REFERENCES `history` (`id`),
  CONSTRAINT `FK_4cwwxrddivsxn1pdy0vhb1ywj` FOREIGN KEY (`legal_records`) REFERENCES `legal_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history_legal_records`
--

LOCK TABLES `history_legal_records` WRITE;
/*!40000 ALTER TABLE `history_legal_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `history_legal_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history_link_records`
--

DROP TABLE IF EXISTS `history_link_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history_link_records` (
  `history` int(11) NOT NULL,
  `link_records` int(11) NOT NULL,
  UNIQUE KEY `UK_bg9ynbbfa8vmxt2d8o0ih813c` (`link_records`),
  KEY `FK_i0x3qbnon99dx62swyxjpscdi` (`history`),
  CONSTRAINT `FK_i0x3qbnon99dx62swyxjpscdi` FOREIGN KEY (`history`) REFERENCES `history` (`id`),
  CONSTRAINT `FK_bg9ynbbfa8vmxt2d8o0ih813c` FOREIGN KEY (`link_records`) REFERENCES `link_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history_link_records`
--

LOCK TABLES `history_link_records` WRITE;
/*!40000 ALTER TABLE `history_link_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `history_link_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history_miscellaneous_records`
--

DROP TABLE IF EXISTS `history_miscellaneous_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history_miscellaneous_records` (
  `history` int(11) NOT NULL,
  `miscellaneous_records` int(11) NOT NULL,
  UNIQUE KEY `UK_jen0lw0bk1yyhve3wcsgypaym` (`miscellaneous_records`),
  KEY `FK_qlwmifke4ewmebi6xck9lwbft` (`history`),
  CONSTRAINT `FK_qlwmifke4ewmebi6xck9lwbft` FOREIGN KEY (`history`) REFERENCES `history` (`id`),
  CONSTRAINT `FK_jen0lw0bk1yyhve3wcsgypaym` FOREIGN KEY (`miscellaneous_records`) REFERENCES `miscellaneous_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history_miscellaneous_records`
--

LOCK TABLES `history_miscellaneous_records` WRITE;
/*!40000 ALTER TABLE `history_miscellaneous_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `history_miscellaneous_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history_period_records`
--

DROP TABLE IF EXISTS `history_period_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `history_period_records` (
  `history` int(11) NOT NULL,
  `period_records` int(11) NOT NULL,
  UNIQUE KEY `UK_1butqdd9tg7u8ts0qq1gbsk4g` (`period_records`),
  KEY `FK_2n92fjhwuxbhgbrv5g4in69n2` (`history`),
  CONSTRAINT `FK_2n92fjhwuxbhgbrv5g4in69n2` FOREIGN KEY (`history`) REFERENCES `history` (`id`),
  CONSTRAINT `FK_1butqdd9tg7u8ts0qq1gbsk4g` FOREIGN KEY (`period_records`) REFERENCES `period_record` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history_period_records`
--

LOCK TABLES `history_period_records` WRITE;
/*!40000 ALTER TABLE `history_period_records` DISABLE KEYS */;
/*!40000 ALTER TABLE `history_period_records` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inception_record`
--

DROP TABLE IF EXISTS `inception_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inception_record` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `photos` longtext,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inception_record`
--

LOCK TABLES `inception_record` WRITE;
/*!40000 ALTER TABLE `inception_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `inception_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `legal_record`
--

DROP TABLE IF EXISTS `legal_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `legal_record` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `laws` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `vat_number` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `legal_record`
--

LOCK TABLES `legal_record` WRITE;
/*!40000 ALTER TABLE `legal_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `legal_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `link_record`
--

DROP TABLE IF EXISTS `link_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `link_record` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `brotherhood` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_m9iy5nonuvly36efe468wojq1` (`brotherhood`),
  CONSTRAINT `FK_m9iy5nonuvly36efe468wojq1` FOREIGN KEY (`brotherhood`) REFERENCES `brotherhood` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `link_record`
--

LOCK TABLES `link_record` WRITE;
/*!40000 ALTER TABLE `link_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `link_record` ENABLE KEYS */;
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
  UNIQUE KEY `UK_mbmcqelty0fbrvxp1q58dn57t` (`email`),
  KEY `FK_porqrqrfw70onhs6pelgepxhu` (`user_account`),
  CONSTRAINT `FK_porqrqrfw70onhs6pelgepxhu` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1086,0,'Calle Sexta 6','joseantonioferreras@gmail.com',NULL,'','José Antonio','632014785','https://deca.upc.edu/ca/persones/pdi-template-shared/profile/@@images/bac720ab-ea23-4696-9bdd-15f87f706868.png',NULL,'Ferreras',1059),(1087,0,'Calle Niebla 6','mariafernanda@gmail.com',NULL,'','María Fernanda','698745214','https://t1.uc.ltmcdn.com/images/8/5/3/img_como_usar_el_aceite_de_arbol_de_te_en_la_cara_43358_600.jpg',NULL,'Lobato',1060);
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
  KEY `UK_iwf26op1bvdfl6ubuvhbbp49p` (`sender`,`is_spam`)
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
-- Table structure for table `miscellaneous_record`
--

DROP TABLE IF EXISTS `miscellaneous_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `miscellaneous_record` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `miscellaneous_record`
--

LOCK TABLES `miscellaneous_record` WRITE;
/*!40000 ALTER TABLE `miscellaneous_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `miscellaneous_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parade`
--

DROP TABLE IF EXISTS `parade`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parade` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `is_final_mode` bit(1) NOT NULL,
  `matriz_parade` longblob,
  `moment` datetime DEFAULT NULL,
  `reason_why` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `ticker` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_5yuq215wrvxtjvi0gjynv32g7` (`ticker`),
  KEY `UK_rh69qehoqh2s3yh4mhjrjsvex` (`moment`),
  KEY `UK_guj5l37vpjajg10iwoc8nsgcf` (`is_final_mode`),
  KEY `UK_9njgxbw8nwct7hwyhjkafgk0j` (`status`),
  KEY `UK_mhwre7xdfrbg85khnxp9nhr2l` (`is_final_mode`,`status`),
  KEY `UK_fhc5iwoc32xd6ql86xxhc4g3m` (`description`,`is_final_mode`,`moment`,`status`,`ticker`,`title`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parade`
--

LOCK TABLES `parade` WRITE;
/*!40000 ALTER TABLE `parade` DISABLE KEYS */;
/*!40000 ALTER TABLE `parade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parade_floats`
--

DROP TABLE IF EXISTS `parade_floats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parade_floats` (
  `parade` int(11) NOT NULL,
  `floats` int(11) NOT NULL,
  KEY `FK_nmgwe8ou0qop1ocyigwv0a2xq` (`floats`),
  KEY `FK_cct51fcgo0xvooajhnwu0txl4` (`parade`),
  CONSTRAINT `FK_cct51fcgo0xvooajhnwu0txl4` FOREIGN KEY (`parade`) REFERENCES `parade` (`id`),
  CONSTRAINT `FK_nmgwe8ou0qop1ocyigwv0a2xq` FOREIGN KEY (`floats`) REFERENCES `float` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parade_floats`
--

LOCK TABLES `parade_floats` WRITE;
/*!40000 ALTER TABLE `parade_floats` DISABLE KEYS */;
/*!40000 ALTER TABLE `parade_floats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parade_segments`
--

DROP TABLE IF EXISTS `parade_segments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `parade_segments` (
  `parade` int(11) NOT NULL,
  `segments` int(11) NOT NULL,
  UNIQUE KEY `UK_eg9uttr6lao2q7opct8ul68lb` (`segments`),
  KEY `FK_auqm7ipebqt3u6e4j6sw668wo` (`parade`),
  CONSTRAINT `FK_auqm7ipebqt3u6e4j6sw668wo` FOREIGN KEY (`parade`) REFERENCES `parade` (`id`),
  CONSTRAINT `FK_eg9uttr6lao2q7opct8ul68lb` FOREIGN KEY (`segments`) REFERENCES `segment` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parade_segments`
--

LOCK TABLES `parade_segments` WRITE;
/*!40000 ALTER TABLE `parade_segments` DISABLE KEYS */;
/*!40000 ALTER TABLE `parade_segments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `period_record`
--

DROP TABLE IF EXISTS `period_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `period_record` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `end_year` int(11) NOT NULL,
  `photos` longtext,
  `start_year` int(11) NOT NULL,
  `text` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `period_record`
--

LOCK TABLES `period_record` WRITE;
/*!40000 ALTER TABLE `period_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `period_record` ENABLE KEYS */;
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
INSERT INTO `position` VALUES (1088,0),(1089,0),(1090,0),(1091,0),(1092,0),(1093,0),(1094,0);
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
INSERT INTO `position_translation_positions` VALUES (1088,1097),(1088,1098),(1089,1099),(1089,1100),(1090,1101),(1090,1102),(1091,1103),(1091,1104),(1092,1105),(1092,1106),(1093,1107),(1093,1108),(1094,1109),(1094,1110);
/*!40000 ALTER TABLE `position_translation_positions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proclaim`
--

DROP TABLE IF EXISTS `proclaim`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `proclaim` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `published_moment` datetime DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `chapter` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_1k8gj5092fv7q0e4ikqlc30g` (`chapter`),
  CONSTRAINT `FK_1k8gj5092fv7q0e4ikqlc30g` FOREIGN KEY (`chapter`) REFERENCES `chapter` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proclaim`
--

LOCK TABLES `proclaim` WRITE;
/*!40000 ALTER TABLE `proclaim` DISABLE KEYS */;
/*!40000 ALTER TABLE `proclaim` ENABLE KEYS */;
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
  `column_parade` int(11) DEFAULT NULL,
  `reason_why` varchar(255) DEFAULT NULL,
  `row_parade` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `member` int(11) NOT NULL,
  `parade` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_muu5p2p5evwro8md796evdpe8` (`parade`,`member`),
  KEY `UK_gtsnidrsylpi5idb2jkk9w9x` (`status`),
  KEY `UK_lkybq682iuwvk2ov22k2sn8n0` (`member`,`status`),
  CONSTRAINT `FK_69jhubt9asf03c38l84e802le` FOREIGN KEY (`parade`) REFERENCES `parade` (`id`),
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
-- Table structure for table `segment`
--

DROP TABLE IF EXISTS `segment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `segment` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `destination_latitude` double DEFAULT NULL,
  `destination_longitude` double DEFAULT NULL,
  `origin_latitude` double DEFAULT NULL,
  `origin_longitude` double DEFAULT NULL,
  `reaching_destination` datetime DEFAULT NULL,
  `reaching_origin` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `segment`
--

LOCK TABLES `segment` WRITE;
/*!40000 ALTER TABLE `segment` DISABLE KEYS */;
/*!40000 ALTER TABLE `segment` ENABLE KEYS */;
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
-- Table structure for table `sponsor`
--

DROP TABLE IF EXISTS `sponsor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsor` (
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
  UNIQUE KEY `UK_7inh7wiji1x5vpu5u3vh0funf` (`email`),
  KEY `FK_du2w5ldt8rvlvxvtr7vyxk7g3` (`user_account`),
  CONSTRAINT `FK_du2w5ldt8rvlvxvtr7vyxk7g3` FOREIGN KEY (`user_account`) REFERENCES `user_account` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sponsor`
--

LOCK TABLES `sponsor` WRITE;
/*!40000 ALTER TABLE `sponsor` DISABLE KEYS */;
INSERT INTO `sponsor` VALUES (1095,0,'Calle Feria 2','lobatojuan@gmail.com',NULL,'','Juan','+34 623587154','https://d500.epimg.net/cincodias/imagenes/2016/03/16/lifestyle/1458143779_942162_1458143814_noticia_normal.jpg',NULL,'Lobato',1063),(1096,0,'Calle Peatonal 2','martarod@gmail.com',NULL,'','Marta','+34 692157361','https://d500.epimg.net/cincodias/imagenes/2016/03/16/lifestyle/1458143779_942162_1458143814_noticia_normal.jpg',NULL,'Rodriguez',1064);
/*!40000 ALTER TABLE `sponsor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sponsorship`
--

DROP TABLE IF EXISTS `sponsorship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sponsorship` (
  `id` int(11) NOT NULL,
  `version` int(11) NOT NULL,
  `banner` varchar(255) DEFAULT NULL,
  `cvv_code` int(11) NOT NULL,
  `expiration_month` varchar(255) DEFAULT NULL,
  `expiration_year` varchar(255) DEFAULT NULL,
  `holder` varchar(255) DEFAULT NULL,
  `make` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `targeturl` varchar(255) DEFAULT NULL,
  `parade` int(11) NOT NULL,
  `sponsor` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `UK_rdgevus8541g8anhsm55hqr6j` (`is_active`),
  KEY `UK_gaeyw4uxi1oo57eeon7fvyi94` (`sponsor`,`is_active`),
  KEY `UK_jffqfok7ibns1pqx76jjbbs7s` (`parade`,`is_active`),
  CONSTRAINT `FK_huglhkud0ihqdljyou4eshra6` FOREIGN KEY (`sponsor`) REFERENCES `sponsor` (`id`),
  CONSTRAINT `FK_4ngakrolee2xy93wibkm3tr52` FOREIGN KEY (`parade`) REFERENCES `parade` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sponsorship`
--

LOCK TABLES `sponsorship` WRITE;
/*!40000 ALTER TABLE `sponsorship` DISABLE KEYS */;
/*!40000 ALTER TABLE `sponsorship` ENABLE KEYS */;
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
INSERT INTO `translation_position` VALUES (1097,0,'en','President'),(1098,0,'es','Presidente'),(1099,0,'en','Vice President'),(1100,0,'es','Vicepresidente'),(1101,0,'en','Secretary'),(1102,0,'es','Secretario'),(1103,0,'en','Treasurer'),(1104,0,'es','Tesorero'),(1105,0,'en','Historian'),(1106,0,'es','Historiador'),(1107,0,'en','Fundraiser'),(1108,0,'es','Promotor'),(1109,0,'en','Officer'),(1110,0,'es','Vocal');
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
INSERT INTO `user_account` VALUES (1057,0,'\0','e00cf25ad42683b3df678c61f42c6bda','admin1'),(1058,0,'\0','54b53072540eeeb8f8e9343e71f28176','system'),(1059,0,'\0','c7764cfed23c5ca3bb393308a0da2306','member1'),(1060,0,'\0','88ed421f060aadcacbd63f28d889797f','member2'),(1061,0,'\0','479e3180a45b21ea8e88beb0c45aa8ed','brotherhood1'),(1062,0,'\0','88f1b810c40cd63107fb758fef4d77db','brotherhood2'),(1063,0,'\0','42c63ad66d4dc07ed17753772bef96d6','sponsor1'),(1064,0,'\0','3dc67f80a03324e01b1640f45d107485','sponsor2'),(1065,0,'\0','178494d73a41f7d68e80f685324d3662','chapter1'),(1066,0,'\0','07a29f2348d728fad16c5260823f93ef','chapter2');
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
INSERT INTO `user_account_authorities` VALUES (1057,'ADMIN'),(1058,'ADMIN'),(1059,'MEMBER'),(1060,'MEMBER'),(1061,'BROTHERHOOD'),(1062,'BROTHERHOOD'),(1063,'SPONSOR'),(1064,'SPONSOR'),(1065,'CHAPTER'),(1066,'CHAPTER');
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

-- Dump completed on 2019-03-26 11:29:06

commit;