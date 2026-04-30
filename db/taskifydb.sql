-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: taskifydb
-- ------------------------------------------------------
-- Server version	9.4.0

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
-- Table structure for table `attachment`
--

DROP TABLE IF EXISTS `attachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `attachment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `filename` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `card_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `card_id` (`card_id`),
  CONSTRAINT `attachment_ibfk_1` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attachment`
--

LOCK TABLES `attachment` WRITE;
/*!40000 ALTER TABLE `attachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `attachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board`
--

DROP TABLE IF EXISTS `board`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `workspace_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `workspace_id` (`workspace_id`),
  CONSTRAINT `board_ibfk_1` FOREIGN KEY (`workspace_id`) REFERENCES `workspace` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `board`
--

LOCK TABLES `board` WRITE;
/*!40000 ALTER TABLE `board` DISABLE KEYS */;
INSERT INTO `board` VALUES (1,'Board Dev','2026-03-26 08:11:42',1),(3,'Board 1',NULL,3),(4,'Board 2',NULL,3),(5,'123123123',NULL,4),(6,'Board 2',NULL,4);
/*!40000 ALTER TABLE `board` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `boardlist`
--

DROP TABLE IF EXISTS `boardlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `boardlist` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `position` int DEFAULT NULL,
  `board_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `board_id` (`board_id`),
  CONSTRAINT `boardlist_ibfk_1` FOREIGN KEY (`board_id`) REFERENCES `board` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boardlist`
--

LOCK TABLES `boardlist` WRITE;
/*!40000 ALTER TABLE `boardlist` DISABLE KEYS */;
INSERT INTO `boardlist` VALUES (1,'To do',1,1),(2,'Doing',2,1),(3,'Done',3,1),(4,'asd',1,3),(5,'asdasd',2,3),(6,'asdasd',3,3),(7,'asd',4,3),(8,'oke em',1,5),(9,'oekoekoek',2,5);
/*!40000 ALTER TABLE `boardlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card`
--

DROP TABLE IF EXISTS `card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` longtext,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `is_active` tinyint(1) DEFAULT '1',
  `due_date` datetime DEFAULT NULL,
  `reminder_date` datetime DEFAULT NULL,
  `position` int DEFAULT NULL,
  `list_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `list_id` (`list_id`),
  KEY `fk_card_user` (`user_id`),
  CONSTRAINT `card_ibfk_1` FOREIGN KEY (`list_id`) REFERENCES `boardlist` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_card_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card`
--

LOCK TABLES `card` WRITE;
/*!40000 ALTER TABLE `card` DISABLE KEYS */;
INSERT INTO `card` VALUES (3,'Task 3','Deploy server','2026-03-26 08:11:42',1,NULL,NULL,1,2,NULL),(4,'asdasd',NULL,NULL,NULL,NULL,NULL,0,7,NULL),(5,'ÁDASD','ĐÁÁDFASDFKHABFIAGFAGBEFLABEFIUBEOIFUBQOEIUBFOBDFLAKJDBFBFQIUFALKSJDFHBQUEHFOAIBJSDFLJQBEFWIEUĐÁÁDFASDFKHABFIAGFAGBEFLABEFIUBEOIFUBQOEIUBFOBDFLAKJDBFB','2026-04-27 23:22:51',1,'2026-04-01 00:00:00','2026-04-28 00:00:00',0,8,6),(6,'Thẻ 1',NULL,'2026-04-28 22:52:15',1,NULL,NULL,NULL,8,6),(7,'ád',NULL,'2026-04-28 22:52:38',1,NULL,NULL,NULL,8,6);
/*!40000 ALTER TABLE `card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `card_user`
--

DROP TABLE IF EXISTS `card_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `card_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `card_id` int NOT NULL,
  `user_id` int NOT NULL,
  `assigned_date` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `card_user_ibfk_1` (`card_id`),
  CONSTRAINT `card_user_ibfk_1` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`) ON DELETE CASCADE,
  CONSTRAINT `card_user_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card_user`
--

LOCK TABLES `card_user` WRITE;
/*!40000 ALTER TABLE `card_user` DISABLE KEYS */;
INSERT INTO `card_user` VALUES (1,3,1,'2026-03-26 08:11:42'),(3,3,4,'2026-04-01 22:35:51'),(4,5,6,'2026-04-28 22:19:38');
/*!40000 ALTER TABLE `card_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comment`
--

DROP TABLE IF EXISTS `comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `comment` tinytext NOT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `card_id` int DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_comment_card` (`card_id`),
  KEY `fk_comment_user` (`user_id`),
  CONSTRAINT `fk_comment_card` FOREIGN KEY (`card_id`) REFERENCES `card` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment`
--

LOCK TABLES `comment` WRITE;
/*!40000 ALTER TABLE `comment` DISABLE KEYS */;
INSERT INTO `comment` VALUES (4,'test comment 2','2026-04-05 10:49:30',3,4),(5,'ád','2026-04-28 22:49:27',5,6),(6,'ád','2026-04-28 22:49:29',5,6),(7,'ád','2026-04-28 22:49:32',5,6),(8,'ádasd','2026-04-28 22:49:45',5,6),(9,'ádasd','2026-04-28 22:49:46',5,6),(10,'ádasd','2026-04-28 22:49:47',5,6);
/*!40000 ALTER TABLE `comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `avatar` varchar(255) DEFAULT NULL,
  `role` varchar(5) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'canh','$2a$10$rT8r5PCtDgdN9.D4NQGa9eeuqu9v8X6Yqv7ruzmnK5Y5wpbgKh2.a','canh@gmail.com','2026-03-26 08:11:42',NULL,'USER'),(2,'admin','$2a$10$rT8r5PCtDgdN9.D4NQGa9eeuqu9v8X6Yqv7ruzmnK5Y5wpbgKh2.a','admin@gmail.com','2026-03-26 08:11:42',NULL,'ADMIN'),(4,'quyen','$2a$10$rT8r5PCtDgdN9.D4NQGa9eeuqu9v8X6Yqv7ruzmnK5Y5wpbgKh2.a','quyen@gmail.com','2026-03-27 22:01:05',NULL,'USER'),(5,'test','$2a$10$EUXzJwbXQh0yDkPD9auCDeqFqx08uWznTQIi4y6PZlIBdsf0EZNiq','test@gmail.com','2026-04-08 21:41:59',NULL,'USER'),(6,'cong','$2a$10$kFUgwC9je.Mx8uvkWf1b6uoLQFs/f/qMTKSp8XDpskMhOWRZD4nsq','congghoul435@gmail.com','2026-04-27 23:22:30',NULL,'USER');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_workspace`
--

DROP TABLE IF EXISTS `user_workspace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_workspace` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `workspace_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKm08gh5dm1n84gx081ryd6wh7y` (`user_id`),
  KEY `FKgbyqvx4cb94thslkrd3o41tth` (`workspace_id`),
  CONSTRAINT `FKgbyqvx4cb94thslkrd3o41tth` FOREIGN KEY (`workspace_id`) REFERENCES `workspace` (`id`),
  CONSTRAINT `FKm08gh5dm1n84gx081ryd6wh7y` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_workspace`
--

LOCK TABLES `user_workspace` WRITE;
/*!40000 ALTER TABLE `user_workspace` DISABLE KEYS */;
INSERT INTO `user_workspace` VALUES (1,1,1),(2,2,1),(4,1,2),(16,5,1),(17,1,3),(18,6,4);
/*!40000 ALTER TABLE `user_workspace` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workspace`
--

DROP TABLE IF EXISTS `workspace`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `workspace` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `owner_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `owner_id` (`owner_id`),
  CONSTRAINT `workspace_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workspace`
--

LOCK TABLES `workspace` WRITE;
/*!40000 ALTER TABLE `workspace` DISABLE KEYS */;
INSERT INTO `workspace` VALUES (1,'Workspace 1',4),(2,'Workspace 2',2),(3,'Workspace 1',1),(4,'Nguyễn Hữu Công',6);
/*!40000 ALTER TABLE `workspace` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-30 19:30:17
