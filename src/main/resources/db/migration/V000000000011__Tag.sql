CREATE TABLE tag (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`name` varchar(32) NOT NULL DEFAULT '',
	`label` varchar(64) NOT NULL DEFAULT '',
	PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `tag` (`id`, `name`, `label`)
VALUES
(1, 'Vegetarian', 'Vegetarian'),
(2, 'Bio', 'Bio'),
(3, 'Spicy', 'Spicy'),
(4, 'Vegan', 'Vegan'),
(5, 'GlutenFree', 'Gluten Free'),
(6, 'Fish', 'Fish'),
(7, 'Meat', 'Meat'),
(8, 'BakedFood', 'Baked Food');
