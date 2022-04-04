 CREATE TABLE offer_allergen ( 
	`id` bigint NOT NULL AUTO_INCREMENT,
	`offer_id` bigint NOT NULL,
	`allergen_id` bigint NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_offer_allergen_offer_id` FOREIGN KEY (`offer_id`) REFERENCES offer(`id`),
	CONSTRAINT `fk_offer_allergen_allergen_id` FOREIGN KEY (`allergen_id`) REFERENCES allergen(`id`)
 );