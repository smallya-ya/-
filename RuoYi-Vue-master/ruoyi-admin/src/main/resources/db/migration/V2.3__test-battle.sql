ALTER TABLE `t_battle_base_config` ADD COLUMN `type` int(11) NULL DEFAULT 0 AFTER `mode`;
ALTER TABLE `t_battle_team_config2` ADD COLUMN `nums` varchar(4096) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL AFTER `team`;
alter table `t_battle_team_config2` drop column `num`;