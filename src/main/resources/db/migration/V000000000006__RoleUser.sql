CREATE TABLE `role_user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `role_id` bigint NOT NULL,
    `user_id` bigint NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_roles` (`role_id`),
    KEY `fk_users` (`user_id`),
    CONSTRAINT `fk_role_user_role_id` FOREIGN KEY (`role_id`) REFERENCES role(`id`),
    CONSTRAINT `fk_role_user_user_id` FOREIGN KEY (`user_id`) REFERENCES users(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;