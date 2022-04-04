CREATE TABLE allergen (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`name` varchar(32) NOT NULL DEFAULT '',
	`label` varchar(64) NOT NULL DEFAULT '',
	PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `allergen` (`id`, `name`, `label`)
VALUES
(1, 'Peanuts', 'Peanuts'),
(2, 'Seasame', 'Seasame'),
(3, 'Soybeans', 'Soybeans'),
(4, 'TreeNuts', 'Tree nuts'),
(5, 'Guten', 'Guten'),
(6, 'Crustaceans', 'Crustaceans'),
(7, 'Egg', 'Egg'),
(8, 'Lupin', 'Lupin'),
(9, 'Milk', 'Milk'),
(10, 'Molluscs', 'Molluscs'),
(11, 'Mustard', 'Mustard'),
(12, 'Sulphite', 'Sulphite');