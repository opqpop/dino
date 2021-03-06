CREATE TABLE IF NOT EXISTS `clips` (
  `id`                          BIGINT(20) NOT NULL AUTO_INCREMENT,
  `video_id`                    BIGINT(20) NOT NULL,
  `game_id`                     BIGINT(20) NOT NULL,
  `game_type`                   VARCHAR(31),
  `streamer_name`               VARCHAR(127),
  `start_time`                  TIMESTAMP DEFAULT 0,
  `end_time`                    TIMESTAMP DEFAULT 0,
  `length`                      INT,
  `views`                       INT,
  `url`                         VARCHAR(255),
  `champion_played`             VARCHAR(31),
  `role_played`                 VARCHAR(15),
  `champion_faced`              VARCHAR(31),
  `lane_partner_champion`       VARCHAR(15),
  `enemy_lane_partner_champion` VARCHAR(15),
  `elo`                         VARCHAR(15),
  `other`                       MEDIUMTEXT,
  `filler1`                     VARCHAR(127),
  `filler2`                     VARCHAR(127),
  `filler3`                     VARCHAR(127),
  `filler4`                     VARCHAR(127),
  `filler5`                     VARCHAR(127),
  `filler6`                     VARCHAR(127),
  `filler7`                     VARCHAR(127),
  `filler8`                     VARCHAR(127),
  `filler9`                     VARCHAR(127),
  `filler10`                    VARCHAR(127),

  PRIMARY KEY (`id`),
  UNIQUE INDEX (`url`),
  FOREIGN KEY (`video_id`) REFERENCES `videos` (`id`),
  FOREIGN KEY (`game_id`) REFERENCES `games` (`id`),
  KEY (`streamer_name`),
  KEY (`start_time`),
  KEY (`end_time`),
  KEY (`length`),
  KEY (`views`),
  KEY (`champion_played`, `lane_partner_champion`),
  KEY (`champion_faced`, `enemy_lane_partner_champion`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci;