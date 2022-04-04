 CREATE TABLE offer_tag ( 
	`id` bigint NOT NULL AUTO_INCREMENT,
	`offer_id` bigint NOT NULL,
	`tag_id` bigint NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_offer_tag_offer_id` FOREIGN KEY (`offer_id`) REFERENCES offer(`id`),
	CONSTRAINT `fk_offer_tag_tag_id` FOREIGN KEY (`tag_id`) REFERENCES tag(`id`)
 );