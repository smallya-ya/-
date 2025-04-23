drop table if exists `t_battle_aowei_map`;
CREATE TABLE `t_battle_aowei_map`  (
   `id` bigint,
   `lng` varchar(100),
   `lat` varchar(100),
   `zoom` varchar(15),
   `type` int(6) DEFAULT 2,
   `left_top_lng` varchar(100),
   `left_top_lat` varchar(100),
   `right_down_lng` varchar(100),
   `right_down_lat` varchar(100),
   `create_time` datetime,
   `update_time` datetime,
   `remark` varchar(255),
   `name` varchar(20),
   `path` varchar(1024) COMMENT '地图路径',
   PRIMARY KEY (`id`)
) ENGINE = InnoDB comment '奥维地图';

drop table if exists `t_battle_aowei_base_config`;
CREATE TABLE `t_battle_aowei_base_config`  (
     `id` bigint,
     `name` varchar(64),
     `map_id` bigint,
     `mode` int,
     `create_time` datetime,
     `update_time` datetime,
     PRIMARY KEY (`id`)
) ENGINE = InnoDB;

drop table if exists `t_battle_aowei_team_config`;
CREATE TABLE t_battle_aowei_team_config  (
     `id` bigint,
     `battle_base_config_id` bigint,
     `team` varchar(64),
     `start_num` int,
     `end_num` int,
     `primary_weapon` varchar(32),
     `secondary_weapon` varchar(32),
     `create_time` datetime,
     `update_time` datetime,
     PRIMARY KEY (`id`)
) ENGINE = InnoDB;

alter table `t_battle_record_detail` add COLUMN `wgs84_lat` decimal(10,6);
alter table `t_battle_record_detail` add COLUMN `wgs84_lng` decimal(10,6);