CREATE TABLE `role` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    `description` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `role` (`id`, `name`, `description`)
VALUES
(1, 'SUPER_ADMIN', 'Super Admin'),
(2, 'CHEF', 'Chef');
(3, 'CUSTOMER', 'Customer');

CREATE TABLE `role_user` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `role_id` int(11) NOT NULL,
    `user_id` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_roles` (`role_id`),
    KEY `fk_users` (`user_id`),
    CONSTRAINT `fk_role_user_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
    CONSTRAINT `fk_users` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

insert into role_user(id, role_id, user_id)
values(DEFAULT, 1, LAST_INSERT_ID());