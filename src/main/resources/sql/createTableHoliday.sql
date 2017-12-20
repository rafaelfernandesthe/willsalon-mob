CREATE TABLE IF NOT EXISTS `holiday` (
  `pk_id_holiday` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `initialDate` datetime NOT NULL,
  `finalDate` datetime NOT NULL,
  PRIMARY KEY (`pk_id_holiday`)
) ;