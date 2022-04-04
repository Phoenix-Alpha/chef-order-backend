 CREATE TABLE offer_cuisine ( 
	`id` bigint NOT NULL AUTO_INCREMENT,
	`offer_id` bigint NOT NULL,
	`cuisine_id` bigint NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_offer_cuisine_offer_id` FOREIGN KEY (`offer_id`) REFERENCES offer(`id`),
	CONSTRAINT `fk_offer_cuisine_cuisine_id` FOREIGN KEY (`cuisine_id`) REFERENCES cuisine(`id`)
 );