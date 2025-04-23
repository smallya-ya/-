CREATE TABLE `t_rongcloud_token`  (
  `id` bigint,
  `num` int,
  `user_id` varchar(64),
  `token` varchar(512),
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB;