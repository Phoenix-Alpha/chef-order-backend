CREATE TABLE `country` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(32)  NOT NULL DEFAULT '',
  `name` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `phone_code` varchar(6) COLLATE utf8_unicode_ci DEFAULT NULL,
  `currency_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_country_currency` (`currency_id`),
  CONSTRAINT `fk_country_currency` FOREIGN KEY (`currency_id`) REFERENCES `currency` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

insert into country(id, code, name, phone_code, currency_id) values(default, 'FR', 'France', '33', (select id from currency where iso_code = 'EUR' limit 1)) on duplicate key update code = 'FR', name = 'France', phone_code = '33', currency_id = (select id from currency where iso_code = 'EUR' limit 1);
insert into country(id, code, name, phone_code, currency_id) values(default, 'DE', 'Germany', '49', (select id from currency where iso_code = 'EUR' limit 1)) on duplicate key update code = 'DE', name = 'Germany', phone_code = '49', currency_id = (select id from currency where iso_code = 'EUR' limit 1);
insert into country(id, code, name, phone_code, currency_id) values(default, 'NL', 'Netherlands', '31', (select id from currency where iso_code = 'EUR' limit 1)) on duplicate key update code = 'NL', name = 'Netherlands', phone_code = '31', currency_id = (select id from currency where iso_code = 'EUR' limit 1);
