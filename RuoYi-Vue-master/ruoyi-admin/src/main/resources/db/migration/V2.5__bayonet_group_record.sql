drop table if exists `t_bayonet_battle_group_record`;
CREATE TABLE `t_bayonet_battle_group_record`  (
  `id` bigint,
  `battle_id` bigint,
  `memberId1` bigint,
  `memberId2` bigint,
  `winner` bigint,
  `create_time` datetime,
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB comment '刺杀组演习记录';

alter table `t_bayonet_battle_achievement` add column `battle_group_record_id` bigint comment '刺杀组演习记录id';


