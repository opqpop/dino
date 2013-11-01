CREATE TABLE IF NOT EXISTS `elo` (
  `id`      BIGINT(20) NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT(20),
  `elo`     VARCHAR(15),
  `time`    TIMESTAMP DEFAULT 0,

  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `lol_users` (`id`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci;