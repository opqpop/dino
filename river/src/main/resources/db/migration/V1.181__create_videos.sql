CREATE TABLE IF NOT EXISTS `videos` (
  `id`            BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `streamer_name` VARCHAR(127) NOT NULL,
  `start_time`    TIMESTAMP DEFAULT 0,
  `end_time`      TIMESTAMP DEFAULT 0,
  `length`        INT,
  `views`         INT,
  `url`           VARCHAR(255),
  `state`         VARCHAR(31)  NOT NULL DEFAULT 'QUEUED',
  `queued_at`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `other`         MEDIUMTEXT,
  `filler1`       VARCHAR(127),
  `filler2`       VARCHAR(127),
  `filler3`       VARCHAR(127),
  `filler4`       VARCHAR(127),
  `filler5`       VARCHAR(127),
  `filler6`       VARCHAR(127),
  `filler7`       VARCHAR(127),
  `filler8`       VARCHAR(127),
  `filler9`       VARCHAR(127),
  `filler10`      VARCHAR(127),

  PRIMARY KEY (`id`),
  UNIQUE INDEX (`url`),
  FOREIGN KEY (`streamer_name`) REFERENCES `streamers` (`name`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci;