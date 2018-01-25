-- phpMyAdmin SQL Dump
-- version 4.7.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Jan 25, 2018 at 02:30 AM
-- Server version: 5.7.19
-- PHP Version: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mobile`
--

-- --------------------------------------------------------

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
CREATE TABLE IF NOT EXISTS `client` (
  `sexOrientation` int(11) DEFAULT NULL,
  `pk_id_person` bigint(20) NOT NULL,
  PRIMARY KEY (`pk_id_person`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
CREATE TABLE IF NOT EXISTS `employee` (
  `cpf` varchar(255) NOT NULL,
  `rg` varchar(255) NOT NULL,
  `pk_id_person` bigint(20) NOT NULL,
  `meetByOrder` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`pk_id_person`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `holiday`
--

DROP TABLE IF EXISTS `holiday`;
CREATE TABLE IF NOT EXISTS `holiday` (
  `pk_id_holiday` bigint(20) NOT NULL AUTO_INCREMENT,
  `finalDate` datetime NOT NULL,
  `initialDate` datetime NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`pk_id_holiday`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
CREATE TABLE IF NOT EXISTS `person` (
  `pk_id_person` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `birthDate` datetime NOT NULL,
  `mail` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) NOT NULL,
  PRIMARY KEY (`pk_id_person`)
) ENGINE=InnoDB AUTO_INCREMENT=1525 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `procedure_e`
--

DROP TABLE IF EXISTS `procedure_e`;
CREATE TABLE IF NOT EXISTS `procedure_e` (
  `pk_id_procedure_e` bigint(20) NOT NULL AUTO_INCREMENT,
  `minutesPrevision` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `value` decimal(19,2) DEFAULT NULL,
  `active` bit(1) DEFAULT NULL,
  `administrative` bit(1) DEFAULT NULL,
  PRIMARY KEY (`pk_id_procedure_e`),
  UNIQUE KEY `UK_8iv6m220gu9yjkwhqgyswgdr8` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `scheduling`
--

DROP TABLE IF EXISTS `scheduling`;
CREATE TABLE IF NOT EXISTS `scheduling` (
  `pk_id_scheduling` bigint(20) NOT NULL AUTO_INCREMENT,
  `finished` tinyint(1) NOT NULL DEFAULT '0',
  `initialDate` datetime NOT NULL,
  `fk_id_client` bigint(20) NOT NULL,
  `fk_id_employee` bigint(20) NOT NULL,
  `wasRepetition` tinyint(1) NOT NULL DEFAULT '0',
  `history` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pk_id_scheduling`),
  KEY `FK_3rqaxs4ynjytor3al9rdvxgnu` (`fk_id_client`),
  KEY `FK_bfh8k81l1jrq1inbnk7j3pkwa` (`fk_id_employee`)
) ENGINE=InnoDB AUTO_INCREMENT=15645 DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `scheduling_procedure_e`
--

DROP TABLE IF EXISTS `scheduling_procedure_e`;
CREATE TABLE IF NOT EXISTS `scheduling_procedure_e` (
  `scheduling_pk_id_scheduling` bigint(20) NOT NULL,
  `procedureList_pk_id_procedure_e` bigint(20) NOT NULL,
  KEY `FK_5fegmvf37h8hrbtbbom6cjppw` (`procedureList_pk_id_procedure_e`),
  KEY `FK_1pw03jmhgdu3w4p33k7hlyrd8` (`scheduling_pk_id_scheduling`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `Users`;
CREATE TABLE IF NOT EXISTS `Users` (
  `UserID` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `active` tinyint(4) NOT NULL DEFAULT '1',
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `client`
--
ALTER TABLE `client`
  ADD CONSTRAINT `FK_ct0sox581dt6pm91ixj19clm8` FOREIGN KEY (`pk_id_person`) REFERENCES `person` (`pk_id_person`);

--
-- Constraints for table `employee`
--
ALTER TABLE `employee`
  ADD CONSTRAINT `FK_4ifa0p672isbobsuf5nuo20m4` FOREIGN KEY (`pk_id_person`) REFERENCES `person` (`pk_id_person`);

--
-- Constraints for table `scheduling`
--
ALTER TABLE `scheduling`
  ADD CONSTRAINT `FK_3rqaxs4ynjytor3al9rdvxgnu` FOREIGN KEY (`fk_id_client`) REFERENCES `client` (`pk_id_person`),
  ADD CONSTRAINT `FK_bfh8k81l1jrq1inbnk7j3pkwa` FOREIGN KEY (`fk_id_employee`) REFERENCES `employee` (`pk_id_person`);

--
-- Constraints for table `scheduling_procedure_e`
--
ALTER TABLE `scheduling_procedure_e`
  ADD CONSTRAINT `FK_1pw03jmhgdu3w4p33k7hlyrd8` FOREIGN KEY (`scheduling_pk_id_scheduling`) REFERENCES `scheduling` (`pk_id_scheduling`),
  ADD CONSTRAINT `FK_5fegmvf37h8hrbtbbom6cjppw` FOREIGN KEY (`procedureList_pk_id_procedure_e`) REFERENCES `procedure_e` (`pk_id_procedure_e`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

INSERT INTO `Users` (`UserID`, `name`, `password`, `role`, `username`, `active`) VALUES 
(1, 'admin', 'admin', 'ROLE_ADMIN', 'admin', 1),
(2, 'recepcao', 'recepcao', 'ROLE_USER', 'recepcao', 1);

INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (1, 59, 'Corte Masculino', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (2, 29, 'Barba', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (3, 29, 'Progressiva Masculina', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (4, 29, 'Alisamento Masculino', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (5, 29, 'Botox Masculino', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (6, 29, 'Pigmentação', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (7, 29, 'Tintura Masculina ', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (8, 29, 'Luzes Masculina', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (9, 29, 'Acabamento Penteado Masculino', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (10, 29, 'Máscara Black', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (11, 29, 'Sobrancelha design ', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (12, 29, 'Sobrancelha Henna', '0.00', '1', '0'
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (13, 59, 'Corte Feminino', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (14, 89, 'Corte Bordado Feminino', '0.00', '1', '0');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (15, 59, 'Escova Feminina', '0.00', '1', '0'););
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (16, 60, 'Indisponível 1', '0.00', '1', '1');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (17, 240, 'Indisponível 4', '0.00', '1', '1');
INSERT INTO `procedure_e` (`pk_id_procedure_e`, `minutesPrevision`, `name`, `value`, `active`, `administrative`) VALUES (18, 480, 'Indisponível 8', '0.00', '1', '1');

COMMIT;