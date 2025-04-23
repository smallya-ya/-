-- ----------------------------
-- Table structure for t_battle_team_config2
-- ----------------------------
CREATE TABLE `t_battle_team_config2`  (
 `id` bigint,
 `battle_base_config_id` bigint,
 `team` varchar(64),
 `num` int,
 `name` varchar(64),
 `primary_weapon` varchar(32),
 `secondary_weapon` varchar(32),
 `create_time` datetime,
 `update_time` datetime,
 PRIMARY KEY (`id`)
) ENGINE = InnoDB;