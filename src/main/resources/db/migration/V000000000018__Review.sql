CREATE TABLE review (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`reviewer_id` bigint,
	`chef_id` bigint,
	`order_id` bigint,
	`offer_id` bigint NOT NULL,
	`rating` decimal(2,1) NOT NULL DEFAULT 0.0,
	`comment` varchar(512),
	`reviewer_first_name` varchar(32) NOT NULL,
	`reviewer_last_name` varchar(32) NOT NULL,
	`created_at` timestamp DEFAULT current_timestamp(),
	`updated_at` timestamp DEFAULT current_timestamp(),
	PRIMARY KEY (`id`),
	CONSTRAINT fk_review_reviewer_id FOREIGN KEY (`reviewer_id` ) REFERENCES users(`id`),
	CONSTRAINT fk_review_chef_id FOREIGN KEY (`chef_id` ) REFERENCES chef(`id`),
	CONSTRAINT fk_review_offer_id FOREIGN KEY (`offer_id` ) REFERENCES offer(`id`),
	CONSTRAINT fk_review_order_id FOREIGN KEY (`order_id` ) REFERENCES orders(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;