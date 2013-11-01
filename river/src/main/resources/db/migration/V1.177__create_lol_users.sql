CREATE TABLE IF NOT EXISTS `lol_users` (
  `id`            BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name`          VARCHAR(127),
  `region`        VARCHAR(31),
  `streamer_name` VARCHAR(127),

  PRIMARY KEY (`id`),
  FOREIGN KEY (`streamer_name`) REFERENCES `streamers` (`name`),
  KEY (`name`, `region`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci;

