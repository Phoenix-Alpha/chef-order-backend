 CREATE TABLE chef_cuisine ( 
	`id` bigint NOT NULL AUTO_INCREMENT,
	`chef_id` bigint NOT NULL,
	`cuisine_id` bigint NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_chef_cuisine_chef_id` FOREIGN KEY (`chef_id`) REFERENCES chef(`id`),
	CONSTRAINT `fk_chef_cuisine_cuisine_id` FOREIGN KEY (`cuisine_id`) REFERENCES cuisine(`id`)
 );