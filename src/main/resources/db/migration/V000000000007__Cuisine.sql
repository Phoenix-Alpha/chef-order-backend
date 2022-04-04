CREATE TABLE cuisine (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`name` varchar(32) NOT NULL DEFAULT '',
	PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `cuisine` (`id`, `name`)
VALUES
(1, 'Indian'),
(2, 'Moroccan');
