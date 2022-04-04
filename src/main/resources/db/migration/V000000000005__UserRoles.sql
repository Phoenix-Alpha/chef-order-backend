CREATE TABLE `role` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    `description` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

INSERT INTO `role` (`id`, `name`, `description`)
VALUES
(1, 'SUPER_ADMIN', 'Super Admin'),
(2, 'CHEF', 'Chef'),
(3, 'CUSTOMER', 'Customer');
