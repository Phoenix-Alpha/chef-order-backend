CREATE TABLE wallet (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`user_id` bigint NOT NULL,
	`status` varchar(32) NOT NULL DEFAULT 'DISABLED',
	`created_at` timestamp DEFAULT current_timestamp(),
	`updated_at` timestamp DEFAULT current_timestamp(),
	`balance` decimal(2,1) NOT NULL DEFAULT 0.0,
	`hold` decimal(2,1) NOT NULL DEFAULT 0.0,
	`wallet_type` varchar(32) NOT NULL DEFAULT 'CUSTOMER',
	`stripe_account_id` varchar(64) NOT NULL,
	PRIMARY KEY (`id`),
	CONSTRAINT `fk_wallet_user_id` FOREIGN KEY (`user_id`) REFERENCES users(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;