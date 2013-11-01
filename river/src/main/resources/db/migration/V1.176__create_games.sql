CREATE TABLE IF NOT EXISTS `games` (
  `id`              BIGINT(20) NOT NULL,
  `type`            VARCHAR(31),
  `blue_player1_id` BIGINT(20),
  `blue_player2_id` BIGINT(20),
  `blue_player3_id` BIGINT(20),
  `blue_player4_id` BIGINT(20),
  `blue_player5_id` BIGINT(20),
  `red_player1_id`  BIGINT(20),
  `red_player2_id`  BIGINT(20),
  `red_player3_id`  BIGINT(20),
  `red_player4_id`  BIGINT(20),
  `red_player5_id`  BIGINT(20),
  `players_info`    MEDIUMTEXT,
  `won`             BOOLEAN,
  `start_time`      TIMESTAMP DEFAULT 0,
  `end_time`        TIMESTAMP DEFAULT 0,
  `length`          INT,
  `other`           MEDIUMTEXT,
  `filler1`         VARCHAR(127),
  `filler2`         VARCHAR(127),
  `filler3`         VARCHAR(127),
  `filler4`         VARCHAR(127),
  `filler5`         VARCHAR(127),
  `filler6`         VARCHAR(127),
  `filler7`         VARCHAR(127),
  `filler8`         VARCHAR(127),
  `filler9`         VARCHAR(127),
  `filler10`        VARCHAR(127),

  PRIMARY KEY (`id`),
  KEY `games_start_time` (`start_time`),
  KEY `games_end_time` (`end_time`)
)
  ENGINE =InnoDB
  DEFAULT CHARSET =utf8
  COLLATE =utf8_unicode_ci;